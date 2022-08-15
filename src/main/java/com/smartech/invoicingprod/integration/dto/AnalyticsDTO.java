package com.smartech.invoicingprod.integration.dto;

public class AnalyticsDTO {
	private String Ar_Report_date;
	private String itemId;
	private String orgCode;
	private String salesOrder;
	private String receiptNumber;
	private String receiptId;
	private String customerName;
	private String transactionNumber;

	public String getAr_Report_date() {
		return Ar_Report_date;
	}

	public void setAr_Report_date(String ar_Report_date) {
		Ar_Report_date = ar_Report_date;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getSalesOrder() {
		return salesOrder;
	}

	public void setSalesOrder(String salesOrder) {
		this.salesOrder = salesOrder;
	}

	public String getReceiptNumber() {
		return receiptNumber;
	}

	public void setReceiptNumber(String receiptNumber) {
		this.receiptNumber = receiptNumber;
	}

	public String getReceiptId() {
		return receiptId;
	}

	public void setReceiptId(String receiptId) {
		this.receiptId = receiptId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}
	
}
