package com.smartech.invoicingprod.integration.json.priceListByItem;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "BasePrice"
})
public class Charge {
	@JsonProperty("BasePrice")
    private String BasePrice;

	public String getBasePrice() {
		return BasePrice;
	}

	public void setBasePrice(String basePrice) {
		BasePrice = basePrice;
	}
	
}
