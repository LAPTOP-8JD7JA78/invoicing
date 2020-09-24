package com.smartech.invoicing.dto;

public class InvoicesByReportsDTO {
	private String transactionNumber;
	private String bussinessUnitName;
	
	public String getTransactionNumber() {
		return transactionNumber;
	}
	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}
	public String getBussinessUnitName() {
		return bussinessUnitName;
	}
	public void setBussinessUnitName(String bussinessUnitName) {
		this.bussinessUnitName = bussinessUnitName;
	}
	
}
