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
@JsonPropertyOrder({ "ItemSerialNumberFrom", "ItemSerialNumberTo", "LotNumber" })
public class LotSerials {

	@JsonProperty("ItemSerialNumberFrom")
	private String itemSerialNumberFrom;
	@JsonProperty("ItemSerialNumberTo")
	private String itemSerialNumberTo;
	@JsonProperty("LotNumber")
	private String lotNumber;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("ItemSerialNumberFrom")
	public String getItemSerialNumberFrom() {
		return itemSerialNumberFrom;
	}

	@JsonProperty("ItemSerialNumberFrom")
	public void setItemSerialNumberFrom(String itemSerialNumberFrom) {
		this.itemSerialNumberFrom = itemSerialNumberFrom;
	}

	@JsonProperty("ItemSerialNumberTo")
	public String getItemSerialNumberTo() {
		return itemSerialNumberTo;
	}

	@JsonProperty("ItemSerialNumberTo")
	public void setItemSerialNumberTo(String itemSerialNumberTo) {
		this.itemSerialNumberTo = itemSerialNumberTo;
	}

	@JsonProperty("LotNumber")
	public String getLotNumber() {
		return lotNumber;
	}

	@JsonProperty("LotNumber")
	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
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