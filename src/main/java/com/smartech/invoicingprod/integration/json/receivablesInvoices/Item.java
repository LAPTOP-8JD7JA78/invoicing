package com.smartech.invoicingprod.integration.json.receivablesInvoices;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "InvoiceCurrencyCode",
    "TransactionNumber",
    "TransactionType",
    "ConversionRateType",
    "EnteredAmount",
    "InvoiceBalanceAmount",
    "BillToCustomerName",
    "BillToCustomerNumber",
    "BillToContact"
})
public class Item {
	 	@JsonProperty("InvoiceCurrencyCode")
	    private String InvoiceCurrencyCode;
	    @JsonProperty("TransactionNumber")
	    private String TransactionNumber;
	    @JsonProperty("TransactionType")
	    private String TransactionType;
	    @JsonProperty("ConversionRateType")
	    private String ConversionRateType;
	    @JsonProperty("EnteredAmount")
	    private float EnteredAmount;
	    @JsonProperty("InvoiceBalanceAmount")
	    private float InvoiceBalanceAmount;
	    @JsonProperty("BillToCustomerName")
	    private String BillToCustomerName;
	    
	    @JsonProperty("BillToCustomerNumber")
	    private String BillToCustomerNumber;
	    
	    @JsonProperty("BillToContact")
	    private String BillToContact;	    
	    
	    @JsonProperty("InvoiceCurrencyCode")
		public String getInvoiceCurrencyCode() {
			return InvoiceCurrencyCode;
		}
	    @JsonProperty("InvoiceCurrencyCode")
		public void setInvoiceCurrencyCode(String invoiceCurrencyCode) {
			InvoiceCurrencyCode = invoiceCurrencyCode;
		}
	    
	    @JsonProperty("TransactionNumber")
		public String getTransactionNumber() {
			return TransactionNumber;
		}
	    @JsonProperty("TransactionNumber")
		public void setTransactionNumber(String transactionNumber) {
			TransactionNumber = transactionNumber;
		}
	    
	    @JsonProperty("TransactionType")
		public String getTransactionType() {
			return TransactionType;
		}
	    @JsonProperty("TransactionType")
		public void setTransactionType(String transactionType) {
			TransactionType = transactionType;
		}
	    
	    @JsonProperty("ConversionRateType")
		public String getConversionRateType() {
			return ConversionRateType;
		}
	    @JsonProperty("ConversionRateType")
		public void setConversionRateType(String conversionRateType) {
			ConversionRateType = conversionRateType;
		}
	    
	    @JsonProperty("EnteredAmount")
		public float getEnteredAmount() {
			return EnteredAmount;
		}
	    @JsonProperty("EnteredAmount")
		public void setEnteredAmount(float enteredAmount) {
			EnteredAmount = enteredAmount;
		}
	    
	    @JsonProperty("InvoiceBalanceAmount")
		public float getInvoiceBalanceAmount() {
			return InvoiceBalanceAmount;
		}
	    @JsonProperty("InvoiceBalanceAmount")
		public void setInvoiceBalanceAmount(float invoiceBalanceAmount) {
			InvoiceBalanceAmount = invoiceBalanceAmount;
		}
	    @JsonProperty("BillToCustomerName")
		public String getBillToCustomerName() {
			return BillToCustomerName;
		}
	    @JsonProperty("BillToCustomerName")
		public void setBillToCustomerName(String billToCustomerName) {
			BillToCustomerName = billToCustomerName;
		}
	    @JsonProperty("BillToCustomerNumber")
		public String getBillToCustomerNumber() {
			return BillToCustomerNumber;
		}
	    @JsonProperty("BillToCustomerNumber")
		public void setBillToCustomerNumber(String billToCustomerNumber) {
			BillToCustomerNumber = billToCustomerNumber;
		}
	    @JsonProperty("BillToContact")
		public String getBillToContact() {
			return BillToContact;
		}
	    @JsonProperty("BillToContact")
		public void setBillToContact(String billToContact) {
			BillToContact = billToContact;
		}
	    
	    
}

