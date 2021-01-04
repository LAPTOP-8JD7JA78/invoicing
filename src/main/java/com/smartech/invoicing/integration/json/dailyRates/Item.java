package com.smartech.invoicing.integration.json.dailyRates;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "FromCurrency",
    "ToCurrency",
    "UserConversionType",
    "ConversionDate",
    "ConversionRate"
})
public class Item {
	 	@JsonProperty("FromCurrency")
	    private String FromCurrency;
	    @JsonProperty("ToCurrency")
	    private String ToCurrency;
	    @JsonProperty("UserConversionType")
	    private String UserConversionType;
	    @JsonProperty("ConversionDate")
	    private String ConversionDate;
	    @JsonProperty("ConversionRate")
	    private float ConversionRate;
	    
	    @JsonProperty("FromCurrency")
		public String getFromCurrency() {
			return FromCurrency;
		}
	    
	    @JsonProperty("FromCurrency")
		public void setFromCurrency(String fromCurrency) {
			FromCurrency = fromCurrency;
		}
	    
	    @JsonProperty("ToCurrency")
		public String getToCurrency() {
			return ToCurrency;
		}
	    
	    @JsonProperty("ToCurrency")
		public void setToCurrency(String toCurrency) {
			ToCurrency = toCurrency;
		}
	    
	    @JsonProperty("UserConversionType")
		public String getUserConversionType() {
			return UserConversionType;
		}
	    
	    @JsonProperty("UserConversionType")
		public void setUserConversionType(String userConversionType) {
			UserConversionType = userConversionType;
		}
	    
	    @JsonProperty("ConversionDate")
		public String getConversionDate() {
			return ConversionDate;
		}
	    
	    @JsonProperty("ConversionDate")
		public void setConversionDate(String conversionDate) {
			ConversionDate = conversionDate;
		}
	    
	    @JsonProperty("ConversionRate")
		public float getConversionRate() {
			return ConversionRate;
		}
	    
	    @JsonProperty("ConversionRate")
		public void setConversionRate(float conversionRate) {
			ConversionRate = conversionRate;
		}
	    
	    
}
