package com.example.demo.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.stereotype.Component;

@Component
public class TransactionDetails {
	
	private Long id;
	private int userId;
	private String stockSymbol;
	private int noOfStocks;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getStockSymbol() {
		return stockSymbol;
	}
	public void setStockSymbol(String stockSymbol) {
		this.stockSymbol = stockSymbol;
	}
	public int getNoOfStocks() {
		return noOfStocks;
	}
	public void setNoOfStocks(int noOfStocks) {
		this.noOfStocks = noOfStocks;
	}
	public TransactionDetails(Long id, int userId, String stockSymbol, int noOfStocks) {
		super();
		this.id = id;
		this.userId = userId;
		this.stockSymbol = stockSymbol;
		this.noOfStocks = noOfStocks;
	}
	@Override
	public String toString() {
		return "TransactionDetails [id=" + id + ", userId=" + userId + ", stockSymbol=" + stockSymbol + ", noOfStocks="
				+ noOfStocks + "]";
	}
	public TransactionDetails() {
	}
	
	
	
	
}
