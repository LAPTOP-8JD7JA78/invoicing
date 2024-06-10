package com.smartech.invoicingprod.dto;

public class ReceivablesInvoiceDistributionDTO {

	private String InvoiceLineNumber;
    private String AccountClass;
    private String AccountCombination;
    private double AccountedAmount;
    private double Percent;
    //private int detailedTaxLineNumber;
	public String getInvoiceLineNumber() {
		return InvoiceLineNumber;
	}
	public void setInvoiceLineNumber(String invoiceLineNumber) {
		InvoiceLineNumber = invoiceLineNumber;
	}
	public String getAccountClass() {
		return AccountClass;
	}
	public void setAccountClass(String accountClass) {
		AccountClass = accountClass;
	}
	public String getAccountCombination() {
		return AccountCombination;
	}
	public void setAccountCombination(String accountCombination) {
		AccountCombination = accountCombination;
	}
	public double getAccountedAmount() {
		return AccountedAmount;
	}
	public void setAccountedAmount(double accountedAmount) {
		AccountedAmount = accountedAmount;
	}
	public double getPercent() {
		return Percent;
	}
	public void setPercent(double percent) {
		Percent = percent;
	}
	
	/*public int getDetailedTaxLineNumber() {
		return detailedTaxLineNumber;
	}
	public void setDetailedTaxLineNumber(int detailedTaxLineNumber) {
		this.detailedTaxLineNumber = detailedTaxLineNumber;
	}*/
    
}
