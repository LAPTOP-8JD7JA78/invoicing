package com.smartech.invoicingprod.integration.json.IncotermByRest;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "FreightTermsCode",
    "ShippingCarrier",
    "CustomerPONumber",
    "totals"
})
public class Item {
	@JsonProperty("FreightTermsCode")
    private String FreightTermsCode;
	
	@JsonProperty("ShippingCarrier")
    private String ShippingCarrier;
	 
	@JsonProperty("CustomerPONumber")
    private String CustomerPONumber;
	
	@JsonProperty("totals") 
	private List<totals> totals = null;
	
	@JsonProperty("FreightTermsCode")
	public String getFreightTermsCode() {
		return FreightTermsCode;
	}

	@JsonProperty("FreightTermsCode")
	public void setFreightTermsCode(String freightTermsCode) {
		FreightTermsCode = freightTermsCode;
	}
	
	@JsonProperty("ShippingCarrier")
	public String getShippingCarrier() {
		return ShippingCarrier;
	}

	@JsonProperty("ShippingCarrier")
	public void setShippingCarrier(String shippingCarrier) {
		ShippingCarrier = shippingCarrier;
	}
	
	@JsonProperty("CustomerPONumber")
	public String getCustomerPONumber() {
		return CustomerPONumber;
	}

	@JsonProperty("CustomerPONumber")
	public void setCustomerPONumber(String customerPONumber) {
		CustomerPONumber = customerPONumber;
	}

	@JsonProperty("totals") 
	public List<totals> getTotals() {
		return totals;
	}

	@JsonProperty("totals") 
	public void setTotals(List<totals> totals) {
		this.totals = totals;
	}
	
	
}
