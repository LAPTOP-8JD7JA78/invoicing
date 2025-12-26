package com.smartech.invoicingprod.integration.json.standardReceiptsForAdvPay;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "digitalTaxReceiptUsingInternet"
})
public class StandardReceiptGdf {
	@JsonProperty("digitalTaxReceiptUsingInternet")
    private String digitalTaxReceiptUsingInternet;

	public String getDigitalTaxReceiptUsingInternet() {
		return digitalTaxReceiptUsingInternet;
	}

	public void setDigitalTaxReceiptUsingInternet(String digitalTaxReceiptUsingInternet) {
		this.digitalTaxReceiptUsingInternet = digitalTaxReceiptUsingInternet;
	}
	
	
}
