package com.smartech.invoicing.dto;

public class SalesLineLotSerDTO {
	private String lotNumber;
	private String serialNumberFrom;
	private String serialNumberTo;
	
	public String getLotNumber() {
		return lotNumber;
	}
	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}
	public String getSerialNumberFrom() {
		return serialNumberFrom;
	}
	public void setSerialNumberFrom(String serialNumberFrom) {
		this.serialNumberFrom = serialNumberFrom;
	}
	public String getSerialNumberTo() {
		return serialNumberTo;
	}
	public void setSerialNumberTo(String serialNumberTo) {
		this.serialNumberTo = serialNumberTo;
	}
}
