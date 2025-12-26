package com.smartech.invoicingprod.integration.json.standardReceiptsForAdvPay;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "ReceiptNumber",
    "BusinessUnit",
    "Amount",
    "Currency",
    "ConversionRate",
    "CustomerName",
    "TaxpayerIdentificationNumber",
    "UnappliedAmount",
    "standardReceiptGdf"
})

public class Item {
	 	@JsonProperty("ReceiptNumber")
	    private String ReceiptNumber;
	 	
	    @JsonProperty("BusinessUnit")
	    private String BusinessUnit;
	    
	 	@JsonProperty("Amount")
	    private String Amount;
	 	
	    @JsonProperty("Currency")
	    private String Currency;
	    
	 	@JsonProperty("ConversionRate")
	    private String ConversionRate;
	 	
	    @JsonProperty("CustomerName")
	    private String CustomerName;
	    
	 	@JsonProperty("TaxpayerIdentificationNumber")
	    private String TaxpayerIdentificationNumber;
	 	
	    @JsonProperty("UnappliedAmount")
	    private String UnappliedAmount;
	 	
	    @JsonProperty("standardReceiptGdf")
	    private List<StandardReceiptGdf> standardReceiptGdf = null;

	    @JsonProperty("ReceiptNumber")
		public String getReceiptNumber() {
			return ReceiptNumber;
		}

	    @JsonProperty("ReceiptNumber")
		public void setReceiptNumber(String receiptNumber) {
			ReceiptNumber = receiptNumber;
		}

	    @JsonProperty("BusinessUnit")
		public String getBusinessUnit() {
			return BusinessUnit;
		}

	    @JsonProperty("BusinessUnit")
		public void setBusinessUnit(String businessUnit) {
			BusinessUnit = businessUnit;
		}

	    @JsonProperty("Amount")
		public String getAmount() {
			return Amount;
		}

	    @JsonProperty("Amount")
		public void setAmount(String amount) {
			Amount = amount;
		}

	    @JsonProperty("Currency")
		public String getCurrency() {
			return Currency;
		}

	    @JsonProperty("Currency")
		public void setCurrency(String currency) {
			Currency = currency;
		}

	    @JsonProperty("ConversionRate")
		public String getConversionRate() {
			return ConversionRate;
		}

	    @JsonProperty("ConversionRate")
		public void setConversionRate(String conversionRate) {
			ConversionRate = conversionRate;
		}

	    @JsonProperty("CustomerName")
		public String getCustomerName() {
			return CustomerName;
		}

	    @JsonProperty("CustomerName")
		public void setCustomerName(String customerName) {
			CustomerName = customerName;
		}

	    @JsonProperty("TaxpayerIdentificationNumber")
		public String getTaxpayerIdentificationNumber() {
			return TaxpayerIdentificationNumber;
		}

	    @JsonProperty("TaxpayerIdentificationNumber")
		public void setTaxpayerIdentificationNumber(String taxpayerIdentificationNumber) {
			TaxpayerIdentificationNumber = taxpayerIdentificationNumber;
		}

	    @JsonProperty("UnappliedAmount")
		public String getUnappliedAmount() {
			return UnappliedAmount;
		}

	    @JsonProperty("UnappliedAmount")
		public void setUnappliedAmount(String unappliedAmount) {
			UnappliedAmount = unappliedAmount;
		}

	    @JsonProperty("standardReceiptGdf")
		public List<StandardReceiptGdf> getStandardReceiptGdf() {
			return standardReceiptGdf;
		}

	    @JsonProperty("standardReceiptGdf")
		public void setStandardReceiptGdf(List<StandardReceiptGdf> standardReceiptGdf) {
			this.standardReceiptGdf = standardReceiptGdf;
		}
      
	    
}

