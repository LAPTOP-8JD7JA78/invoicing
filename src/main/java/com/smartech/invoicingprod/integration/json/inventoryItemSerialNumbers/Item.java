package com.smartech.invoicingprod.integration.json.inventoryItemSerialNumbers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "SupplierSerialNumber"
})
public class Item {
	@JsonProperty("SupplierSerialNumber")
    private String SupplierSerialNumber;

	@JsonProperty("SupplierSerialNumber")
	public String getSupplierSerialNumber() {
		return SupplierSerialNumber;
	}

	@JsonProperty("SupplierSerialNumber")
	public void setSupplierSerialNumber(String supplierSerialNumber) {
		SupplierSerialNumber = supplierSerialNumber;
	}
	
}
