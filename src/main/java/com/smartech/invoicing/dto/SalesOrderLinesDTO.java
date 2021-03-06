package com.smartech.invoicing.dto;

import java.util.List;

public class SalesOrderLinesDTO {
	private String sourceTransactionLineNumber;
	private String orderedQuantity;
	private String orderedUOMCode;
	private String orderedUOM;
	private String productNumber;
	private String productDescription;
	private String productIdentifier;
	private String taxClassificationCode;
	private String statusCode;
	private String additionalInformation;
	private String freightTermsCode;
	private String shippingMethod;
	private String SourceTransactionLineIdentifier;
	private String InventoryOrganizationName;
	private List<SalesLineLotSerDTO> lotSerials;
	
	public String getSourceTransactionLineNumber() {
		return sourceTransactionLineNumber;
	}
	public void setSourceTransactionLineNumber(String sourceTransactionLineNumber) {
		this.sourceTransactionLineNumber = sourceTransactionLineNumber;
	}
	public String getOrderedQuantity() {
		return orderedQuantity;
	}
	public void setOrderedQuantity(String orderedQuantity) {
		this.orderedQuantity = orderedQuantity;
	}
	public String getOrderedUOMCode() {
		return orderedUOMCode;
	}
	public void setOrderedUOMCode(String orderedUOMCode) {
		this.orderedUOMCode = orderedUOMCode;
	}
	public String getOrderedUOM() {
		return orderedUOM;
	}
	public void setOrderedUOM(String orderedUOM) {
		this.orderedUOM = orderedUOM;
	}
	public String getProductNumber() {
		return productNumber;
	}
	public void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
	}
	public String getProductDescription() {
		return productDescription;
	}
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	public String getTaxClassificationCode() {
		return taxClassificationCode;
	}
	public void setTaxClassificationCode(String taxClassificationCode) {
		this.taxClassificationCode = taxClassificationCode;
	}
	public List<SalesLineLotSerDTO> getLotSerials() {
		return lotSerials;
	}
	public void setLotSerials(List<SalesLineLotSerDTO> lotSerials) {
		this.lotSerials = lotSerials;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getAdditionalInformation() {
		return additionalInformation;
	}
	public void setAdditionalInformation(String additionalInformation) {
		this.additionalInformation = additionalInformation;
	}
	public String getFreightTermsCode() {
		return freightTermsCode;
	}
	public void setFreightTermsCode(String freightTermsCode) {
		this.freightTermsCode = freightTermsCode;
	}
	public String getShippingMethod() {
		return shippingMethod;
	}
	public void setShippingMethod(String shippingMethod) {
		this.shippingMethod = shippingMethod;
	}
	public String getProductIdentifier() {
		return productIdentifier;
	}
	public void setProductIdentifier(String productIdentifier) {
		this.productIdentifier = productIdentifier;
	}
	public String getSourceTransactionLineIdentifier() {
		return SourceTransactionLineIdentifier;
	}
	public void setSourceTransactionLineIdentifier(String sourceTransactionLineIdentifier) {
		SourceTransactionLineIdentifier = sourceTransactionLineIdentifier;
	}
	public String getInventoryOrganizationName() {
		return InventoryOrganizationName;
	}
	public void setInventoryOrganizationName(String inventoryOrganizationName) {
		InventoryOrganizationName = inventoryOrganizationName;
	}
	
}
