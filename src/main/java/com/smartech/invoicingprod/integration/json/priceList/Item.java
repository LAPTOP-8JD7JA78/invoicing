package com.smartech.invoicingprod.integration.json.priceList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "PriceListName",
    "PriceListId",
    "CurrencyCode"
})
public class Item {
	 	@JsonProperty("PriceListName")
	    private String PriceListName;
	    @JsonProperty("PriceListId")
	    private String PriceListId;
	    @JsonProperty("CurrencyCode")
	    private String CurrencyCode;
	    
	    @JsonProperty("PriceListName")
		public String getPriceListName() {
			return PriceListName;
		}
	    
	    @JsonProperty("PriceListName")
		public void setPriceListName(String priceListName) {
			PriceListName = priceListName;
		}
	    
	    @JsonProperty("PriceListId")
		public String getPriceListId() {
			return PriceListId;
		}
		
		@JsonProperty("PriceListId")
		public void setPriceListId(String priceListId) {
			PriceListId = priceListId;
		}
		
		@JsonProperty("CurrencyCode")
		public String getCurrencyCode() {
			return CurrencyCode;
		}
		 
		@JsonProperty("CurrencyCode")
		public void setCurrencyCode(String currencyCode) {
			CurrencyCode = currencyCode;
		} 
	    
	    
	    
}

