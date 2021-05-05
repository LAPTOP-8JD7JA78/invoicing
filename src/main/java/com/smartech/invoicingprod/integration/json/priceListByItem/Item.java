package com.smartech.invoicingprod.integration.json.priceListByItem;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "ItemName",
    "charges"
})
public class Item {
	@JsonProperty("ItemName")
    private String ItemName;
    @JsonProperty("charges")
    private List<Charge> charges = null;
    
	@JsonProperty("ItemName")
	public String getItemName() {
		return ItemName;
	}
	
	@JsonProperty("ItemName")
	public void setItemName(String itemName) {
		ItemName = itemName;
	}
	
	@JsonProperty("charges")
	public List<Charge> getCharges() {
		return charges;
	}
	
	@JsonProperty("charges")
	public void setCharges(List<Charge> charges) {
		this.charges = charges;
	}
    
   
}
