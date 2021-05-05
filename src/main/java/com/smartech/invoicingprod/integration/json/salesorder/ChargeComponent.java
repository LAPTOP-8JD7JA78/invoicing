
package com.smartech.invoicingprod.integration.json.salesorder;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "PriceElementCode",
    "PriceElement",
    "HeaderCurrencyUnitPrice",
    "HeaderCurrencyExtendedAmount",
    "PercentOfComparisonElement"
})
public class ChargeComponent {

    @JsonProperty("PriceElementCode")
    private String priceElementCode;
    @JsonProperty("PriceElement")
    private String priceElement;
    @JsonProperty("HeaderCurrencyUnitPrice")
    private Double headerCurrencyUnitPrice;
    @JsonProperty("HeaderCurrencyExtendedAmount")
    private Double headerCurrencyExtendedAmount;
    @JsonProperty("PercentOfComparisonElement")
    private Double percentOfComparisonElement;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("PriceElementCode")
    public String getPriceElementCode() {
        return priceElementCode;
    }

    @JsonProperty("PriceElementCode")
    public void setPriceElementCode(String priceElementCode) {
        this.priceElementCode = priceElementCode;
    }

    @JsonProperty("PriceElement")
    public String getPriceElement() {
        return priceElement;
    }

    @JsonProperty("PriceElement")
    public void setPriceElement(String priceElement) {
        this.priceElement = priceElement;
    }

    @JsonProperty("HeaderCurrencyUnitPrice")
    public Double getHeaderCurrencyUnitPrice() {
        return headerCurrencyUnitPrice;
    }

    @JsonProperty("HeaderCurrencyUnitPrice")
    public void setHeaderCurrencyUnitPrice(Double headerCurrencyUnitPrice) {
        this.headerCurrencyUnitPrice = headerCurrencyUnitPrice;
    }

    @JsonProperty("HeaderCurrencyExtendedAmount")
    public Double getHeaderCurrencyExtendedAmount() {
        return headerCurrencyExtendedAmount;
    }

    @JsonProperty("HeaderCurrencyExtendedAmount")
    public void setHeaderCurrencyExtendedAmount(Double headerCurrencyExtendedAmount) {
        this.headerCurrencyExtendedAmount = headerCurrencyExtendedAmount;
    }

    @JsonProperty("PercentOfComparisonElement")
    public Double getPercentOfComparisonElement() {
        return percentOfComparisonElement;
    }

    @JsonProperty("PercentOfComparisonElement")
    public void setPercentOfComparisonElement(Double percentOfComparisonElement) {
        this.percentOfComparisonElement = percentOfComparisonElement;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
