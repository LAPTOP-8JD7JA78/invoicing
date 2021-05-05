package com.smartech.invoicingprod.integration.json.unitCost;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "CostElement",
    "UnitCostAverage"
})
public class CostDetails {
	@JsonProperty("CostElement")
    private String CostElement;
	
    @JsonProperty("UnitCostAverage")
    private double UnitCostAverage;

    @JsonProperty("CostElement")
	public String getCostElement() {
		return CostElement;
	}

    @JsonProperty("CostElement")
	public void setCostElement(String costElement) {
		CostElement = costElement;
	}

    @JsonProperty("UnitCostAverage")
	public double getUnitCostAverage() {
		return UnitCostAverage;
	}

    @JsonProperty("UnitCostAverage")
	public void setUnitCostAverage(double unitCostAverage) {
		UnitCostAverage = unitCostAverage;
	}

    
    
}
