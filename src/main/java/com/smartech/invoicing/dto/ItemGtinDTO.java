package com.smartech.invoicing.dto;

public class ItemGtinDTO {

	public String organizationCode;
	public String itemNumber;
	public String tradingPartnerName;
	public String tradingPartnerNumber;
	public String uomCodeValue;
	public String gtin;
	public String itemDescription;
	
	public String getOrganizationCode() {
		return organizationCode;
	}
	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}
	public String getItemNumber() {
		return itemNumber;
	}
	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}
	public String getTradingPartnerName() {
		return tradingPartnerName;
	}
	public void setTradingPartnerName(String tradingPartnerName) {
		this.tradingPartnerName = tradingPartnerName;
	}
	public String getTradingPartnerNumber() {
		return tradingPartnerNumber;
	}
	public void setTradingPartnerNumber(String tradingPartnerNumber) {
		this.tradingPartnerNumber = tradingPartnerNumber;
	}
	public String getUomCodeValue() {
		return uomCodeValue;
	}
	public void setUomCodeValue(String uomCodeValue) {
		this.uomCodeValue = uomCodeValue;
	}
	public String getGtin() {
		return gtin;
	}
	public void setGtin(String gtin) {
		this.gtin = gtin;
	}
	public String getItemDescription() {
		return itemDescription;
	}
	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}
	
}
