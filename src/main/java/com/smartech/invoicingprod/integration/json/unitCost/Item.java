package com.smartech.invoicingprod.integration.json.unitCost;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "ItemNumber",
    "costDetails"
})
public class Item {
	 	@JsonProperty("ItemNumber")
	    private String ItemNumber;
	    @JsonProperty("costDetails")
	    private List<CostDetails> costDetails = null;

	    @JsonProperty("ItemNumber")
	    public String getItemNumber() {
			return ItemNumber;
		}
	    
	    @JsonProperty("ItemNumber")
		public void setItemNumber(String itemNumber) {
			ItemNumber = itemNumber;
		}
	    
	    @JsonProperty("costDetails")
		public List<CostDetails> getCostDetails() {
			return costDetails;
		}
	    
	    @JsonProperty("costDetails")
		public void setCostDetails(List<CostDetails> costDetails) {
			this.costDetails = costDetails;
		}
	      
	    
}
