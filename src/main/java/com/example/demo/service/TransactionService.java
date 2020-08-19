package com.example.demo.service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.demo.entity.BrokerEntity;
import com.example.demo.entity.TransactionDetails;
import com.example.demo.repository.TransactionRepo;

@Component
public class TransactionService {
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	private TransactionRepo trnRepo;
	
	
	 public List<BrokerEntity>getAllTransactions() 
	 {
		  return trnRepo.findAll();
	 }
	
	public String buyStocks(TransactionDetails transDetails) throws Exception {
		// check for user(no user found exception)
		// get user current balance
		// check for stock(no stock exception)
		// get the stock
		// check for the no of stocks(less stocks return limited stocks exception)
		// separate only the stock price into a variable
		// math calculation of no of stocks multiplied by current stock price
		// check for calculated price in the user account
		// if so deduct the balance and update user account
		// reduce the no of that stocks after purchase
		try {
			Long UserBalance = 0L;
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			HttpEntity<String> entity = new HttpEntity<String>(headers);

			ResponseEntity<Long> response = restTemplate.exchange(
					"http://localhost:9090/UserAcoounts/getUsersBalanceById/" + transDetails.getUserId(), HttpMethod.GET, entity,
					Long.class);
			if (response.getStatusCode() == HttpStatus.OK) {
				UserBalance = response.getBody();
			} else {
				throw new Exception("Invalid UserID");
			}

			ResponseEntity<String> tradeResponse = restTemplate.exchange("http://localhost:8080/trades/getStockFinanceBySymbol/"+transDetails.getStockSymbol(), HttpMethod.GET, entity, String.class);
			if (tradeResponse.getStatusCode() == HttpStatus.OK) {
				String outputStock = tradeResponse.getBody();
				String[] splited = outputStock.split(",");
				Double price = Double.parseDouble(splited[0]);
				Integer availableStocks = Integer.parseInt(splited[1]);
				if (transDetails.getNoOfStocks() > availableStocks) {
					throw new Exception("Limted Stock availabilty");
				}
				Double requiredmoney = price * transDetails.getNoOfStocks();
				if (UserBalance >= requiredmoney) {
					// Double newUserBalance = UserBalance-requiredmoney;
					restTemplate.exchange("http://localhost:9090/UserAcoounts/deductBalance/" + transDetails.getUserId()
							+ "/" + requiredmoney.longValue(), HttpMethod.PUT, entity, String.class);
					
					BrokerEntity transObject = new BrokerEntity();
					transObject.setStockSymbol(transDetails.getStockSymbol());
					transObject.setUserId(transDetails.getUserId());
					transObject.setTransactionDate(new Timestamp(new Date().getTime()));
					transObject.setTransactionStatus("Success");
					trnRepo.save(transObject);
					return "success";

				} else {
					throw new Exception("InSuffiecient funds in the accoutn");
				}

			} else {
				throw new Exception("No Stock Found");
			}
		} catch (Exception e) {
			BrokerEntity transObject = new BrokerEntity();
			transObject.setStockSymbol(transDetails.getStockSymbol());
			transObject.setUserId(transDetails.getUserId());
			transObject.setTransactionDate(new Timestamp(new Date().getTime()));
			transObject.setTransactionStatus("Failed with Reason"+e.getMessage());
			trnRepo.save(transObject);
			throw e;
		}
	}


}
