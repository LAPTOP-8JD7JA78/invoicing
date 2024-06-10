package com.smartech.invoicingprod.dto;

import java.util.List;

public class receivablesInvoiceGdf {

	private int CustomerTrxId;
	private String ExcludeFromNetting;
	private String DeliveryDateforTaxPointDate;
	private String __FLEX_Context;
	private String __FLEX_Context_DisplayValue;
	private String CFDIUniqueIdentifier;
	private String CFDCBBSerialNumber;
	private String CFDCBBInvoiceNumber;
	private List<links> links;
	public int getCustomerTrxId() {
		return CustomerTrxId;
	}
	public void setCustomerTrxId(int customerTrxId) {
		CustomerTrxId = customerTrxId;
	}
	public String getExcludeFromNetting() {
		return ExcludeFromNetting;
	}
	public void setExcludeFromNetting(String excludeFromNetting) {
		ExcludeFromNetting = excludeFromNetting;
	}
	public String getDeliveryDateforTaxPointDate() {
		return DeliveryDateforTaxPointDate;
	}
	public void setDeliveryDateforTaxPointDate(String deliveryDateforTaxPointDate) {
		DeliveryDateforTaxPointDate = deliveryDateforTaxPointDate;
	}
	public String get__FLEX_Context() {
		return __FLEX_Context;
	}
	public void set__FLEX_Context(String __FLEX_Context) {
		this.__FLEX_Context = __FLEX_Context;
	}
	public String get__FLEX_Context_DisplayValue() {
		return __FLEX_Context_DisplayValue;
	}
	public void set__FLEX_Context_DisplayValue(String __FLEX_Context_DisplayValue) {
		this.__FLEX_Context_DisplayValue = __FLEX_Context_DisplayValue;
	}
	public String getCFDIUniqueIdentifier() {
		return CFDIUniqueIdentifier;
	}
	public void setCFDIUniqueIdentifier(String cFDIUniqueIdentifier) {
		CFDIUniqueIdentifier = cFDIUniqueIdentifier;
	}
	public String getCFDCBBSerialNumber() {
		return CFDCBBSerialNumber;
	}
	public void setCFDCBBSerialNumber(String cFDCBBSerialNumber) {
		CFDCBBSerialNumber = cFDCBBSerialNumber;
	}
	public String getCFDCBBInvoiceNumber() {
		return CFDCBBInvoiceNumber;
	}
	public void setCFDCBBInvoiceNumber(String cFDCBBInvoiceNumber) {
		CFDCBBInvoiceNumber = cFDCBBInvoiceNumber;
	}
	public List<links> getLinks() {
		return links;
	}
	public void setLinks(List<links> links) {
		this.links = links;
	}
	
}
