package com.smartech.invoicingprod.dto;

public class WarrantyDataProcessDTO {

	int missing_invoice_id;
	String customerName;
	String folio;
	String productType;
	public int getMissing_invoice_id() {
		return missing_invoice_id;
	}
	public void setMissing_invoice_id(int missing_invoice_id) {
		this.missing_invoice_id = missing_invoice_id;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getFolio() {
		return folio;
	}
	public void setFolio(String folio) {
		this.folio = folio;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	
	
}
