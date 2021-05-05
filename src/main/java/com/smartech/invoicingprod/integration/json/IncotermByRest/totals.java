package com.smartech.invoicingprod.integration.json.IncotermByRest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "TotalCode",
    "TotalAmount"
})
public class totals {
	@JsonProperty("TotalCode")
    private String TotalCode;
	
    @JsonProperty("TotalAmount")
    private double TotalAmount;

    @JsonProperty("TotalCode")
	public String getTotalCode() {
		return TotalCode;
	}

    @JsonProperty("TotalCode")
	public void setTotalCode(String totalCode) {
		TotalCode = totalCode;
	}

    @JsonProperty("TotalAmount")
	public double getTotalAmount() {
		return TotalAmount;
	}

    @JsonProperty("TotalAmount")
	public void setTotalAmount(double totalAmount) {
		TotalAmount = totalAmount;
	}
    
    
}
