package com.smartech.invoicingprod.dto;

import java.util.List;

public class ReceivablesInvoiceLineDTO {

	private String Description;
    private int LineNumber;
    private double Quantity;
    private double UnitSellingPrice;
    private String TaxClassificationCode;
    private String MemoLine;
    private String UnitOfMeasure;
    private double LineAmount;
    private String LineAmountIncludesTax;
    private String TaxExemptionHandling;
    private List<ReceivablesInvoiceLineTaxLineDTO> receivablesInvoiceLineTaxLines;
	
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public int getLineNumber() {
		return LineNumber;
	}
	public void setLineNumber(int lineNumber) {
		LineNumber = lineNumber;
	}
	public double getQuantity() {
		return Quantity;
	}
	public void setQuantity(double quantity) {
		Quantity = quantity;
	}
	public double getUnitSellingPrice() {
		return UnitSellingPrice;
	}
	public void setUnitSellingPrice(double unitSellingPrice) {
		UnitSellingPrice = unitSellingPrice;
	}
	public String getTaxClassificationCode() {
		return TaxClassificationCode;
	}
	public void setTaxClassificationCode(String taxClassificationCode) {
		TaxClassificationCode = taxClassificationCode;
	}
	public String getMemoLine() {
		return MemoLine;
	}
	public void setMemoLine(String memoLine) {
		MemoLine = memoLine;
	}
	public String getUnitOfMeasure() {
		return UnitOfMeasure;
	}
	public void setUnitOfMeasure(String unitOfMeasure) {
		UnitOfMeasure = unitOfMeasure;
	}
	public double getLineAmount() {
		return LineAmount;
	}
	public void setLineAmount(double lineAmount) {
		LineAmount = lineAmount;
	}
	public String getLineAmountIncludesTax() {
		return LineAmountIncludesTax;
	}
	public void setLineAmountIncludesTax(String lineAmountIncludesTax) {
		LineAmountIncludesTax = lineAmountIncludesTax;
	}
	public String getTaxExemptionHandling() {
		return TaxExemptionHandling;
	}
	public void setTaxExemptionHandling(String taxExemptionHandling) {
		TaxExemptionHandling = taxExemptionHandling;
	}
	public List<ReceivablesInvoiceLineTaxLineDTO> getReceivablesInvoiceLineTaxLines() {
		return receivablesInvoiceLineTaxLines;
	}
	public void setReceivablesInvoiceLineTaxLines(List<ReceivablesInvoiceLineTaxLineDTO> receivablesInvoiceLineTaxLines) {
		this.receivablesInvoiceLineTaxLines = receivablesInvoiceLineTaxLines;
	}
    
}
