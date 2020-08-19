package com.example.demo.controller;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.demo.entity.BrokerEntity;
import com.example.demo.entity.TransactionDetails;
import com.example.demo.repository.TransactionRepo;
import com.example.demo.service.TransactionService;

@RestController(value = "/transactions")
public class TransactionController {
	Logger log = LoggerFactory.getLogger(TransactionController.class);
	@Autowired
	private TransactionService transService;
	

	
	  @GetMapping("/getAllTransactions")
	  public List<BrokerEntity>getAllTransactions() 
	  { 
		 return transService.getAllTransactions();
	  }
	
	// input userid, stock id, number of stocks
	// return HTTP ok if true.... or some error msg if not ok

	@PostMapping("/newTransaction")
	public ResponseEntity<?> createNewTransaction(@RequestBody TransactionDetails transDetails) 
	{
	try{

		 transService.buyStocks(transDetails);
		 return new ResponseEntity<HttpStatus>(HttpStatus.OK);
	}
	catch(Exception e)
	{
			String msg = e.getMessage();
			log.info("GOT AN exception and the message is"+msg);
			log.error(msg, e);
			
			return new ResponseEntity<String>(msg, HttpStatus.BAD_REQUEST);

	}

	
}
}
