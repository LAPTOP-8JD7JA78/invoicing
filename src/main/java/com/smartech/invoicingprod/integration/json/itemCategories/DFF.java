package com.smartech.invoicingprod.integration.json.itemCategories;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "tipoProducto",
    "yearModel"
})
public class DFF {
	@JsonProperty("tipoProducto")
    private double tipoProducto;

	@JsonProperty("yearModel")
    private String yearModel;
	
	@JsonProperty("tipoProducto")
	public double getTipoProducto() {
		return tipoProducto;
	}

	@JsonProperty("tipoProducto")
	public void setTipoProducto(double tipoProducto) {
		this.tipoProducto = tipoProducto;
	}
	
	@JsonProperty("yearModel")
	public String getYearModel() {
		return yearModel;
	}

	@JsonProperty("yearModel")
	public void setYearModel(String yearModel) {
		this.yearModel = yearModel;
	}
}
