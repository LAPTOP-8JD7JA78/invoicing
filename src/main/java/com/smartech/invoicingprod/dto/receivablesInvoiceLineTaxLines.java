package com.smartech.invoicingprod.dto;

import java.util.List;

public class receivablesInvoiceLineTaxLines {

	private int CustomerTransactionLineId;
	private String CreatedBy;
	private String CreationDate;
	private String LastUpdatedBy;
	private String LastUpdateDate;
	private String TaxJurisdictionCode;
	private String TaxRate;
	private String TaxRateCode;
	private String TaxRegimeCode;
	private String TaxStatusCode;
	private String Tax;
	private double TaxAmount;
	private String TaxableAmount;
	private String TaxPointBasis;
	private String TaxPointDate;
	private String TaxLineNumber;
	private String PlaceOfSupply;
	private String TaxInclusiveIndicator;
	private List<links> links;
	public int getCustomerTransactionLineId() {
		return CustomerTransactionLineId;
	}
	public void setCustomerTransactionLineId(int customerTransactionLineId) {
		CustomerTransactionLineId = customerTransactionLineId;
	}
	public String getCreatedBy() {
		return CreatedBy;
	}
	public void setCreatedBy(String createdBy) {
		CreatedBy = createdBy;
	}
	public String getCreationDate() {
		return CreationDate;
	}
	public void setCreationDate(String creationDate) {
		CreationDate = creationDate;
	}
	public String getLastUpdatedBy() {
		return LastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		LastUpdatedBy = lastUpdatedBy;
	}
	public String getLastUpdateDate() {
		return LastUpdateDate;
	}
	public void setLastUpdateDate(String lastUpdateDate) {
		LastUpdateDate = lastUpdateDate;
	}
	public String getTaxJurisdictionCode() {
		return TaxJurisdictionCode;
	}
	public void setTaxJurisdictionCode(String taxJurisdictionCode) {
		TaxJurisdictionCode = taxJurisdictionCode;
	}
	public String getTaxRate() {
		return TaxRate;
	}
	public void setTaxRate(String taxRate) {
		TaxRate = taxRate;
	}
	public String getTaxRateCode() {
		return TaxRateCode;
	}
	public void setTaxRateCode(String taxRateCode) {
		TaxRateCode = taxRateCode;
	}
	public String getTaxRegimeCode() {
		return TaxRegimeCode;
	}
	public void setTaxRegimeCode(String taxRegimeCode) {
		TaxRegimeCode = taxRegimeCode;
	}
	public String getTaxStatusCode() {
		return TaxStatusCode;
	}
	public void setTaxStatusCode(String taxStatusCode) {
		TaxStatusCode = taxStatusCode;
	}
	public String getTax() {
		return Tax;
	}
	public void setTax(String tax) {
		Tax = tax;
	}
	public double getTaxAmount() {
		return TaxAmount;
	}
	public void setTaxAmount(double taxAmount) {
		TaxAmount = taxAmount;
	}
	public String getTaxableAmount() {
		return TaxableAmount;
	}
	public void setTaxableAmount(String taxableAmount) {
		TaxableAmount = taxableAmount;
	}
	public String getTaxPointBasis() {
		return TaxPointBasis;
	}
	public void setTaxPointBasis(String taxPointBasis) {
		TaxPointBasis = taxPointBasis;
	}
	public String getTaxPointDate() {
		return TaxPointDate;
	}
	public void setTaxPointDate(String taxPointDate) {
		TaxPointDate = taxPointDate;
	}
	public String getTaxLineNumber() {
		return TaxLineNumber;
	}
	public void setTaxLineNumber(String taxLineNumber) {
		TaxLineNumber = taxLineNumber;
	}
	public String getPlaceOfSupply() {
		return PlaceOfSupply;
	}
	public void setPlaceOfSupply(String placeOfSupply) {
		PlaceOfSupply = placeOfSupply;
	}
	public String getTaxInclusiveIndicator() {
		return TaxInclusiveIndicator;
	}
	public void setTaxInclusiveIndicator(String taxInclusiveIndicator) {
		TaxInclusiveIndicator = taxInclusiveIndicator;
	}
	public List<links> getLinks() {
		return links;
	}
	public void setLinks(List<links> links) {
		this.links = links;
	}
	
}
