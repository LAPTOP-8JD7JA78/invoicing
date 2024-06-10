package com.smartech.invoicingprod.dto;

public class ReceivablesInvoiceLineTaxLineDTO {

	private double TaxAmount;
    private String TaxRegimeCode;
    private String TaxRateCode;
	public double getTaxAmount() {
		return TaxAmount;
	}
	public void setTaxAmount(double taxAmount) {
		TaxAmount = taxAmount;
	}
	public String getTaxRegimeCode() {
		return TaxRegimeCode;
	}
	public void setTaxRegimeCode(String taxRegimeCode) {
		TaxRegimeCode = taxRegimeCode;
	}
	public String getTaxRateCode() {
		return TaxRateCode;
	}
	public void setTaxRateCode(String taxRateCode) {
		TaxRateCode = taxRateCode;
	}
	
}
