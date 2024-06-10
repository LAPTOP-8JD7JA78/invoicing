package com.smartech.invoicingprod.dto;

import java.util.List;

public class receivablesInvoiceDistributions {

	private int DistributionId;
	private int InvoiceLineNumber;
	private int DetailedTaxLineNumber;
	private String AccountClass; 
	private String AccountCombination; 
	private double Amount; 
	private double AccountedAmount; 
	private double Percent; 
	private String Comments; 
	private String CreatedBy;
	private String CreationDate;
	private String LastUpdateDate;
	private String LastUpdatedBy;
	private List<links> links;
	public int getDistributionId() {
		return DistributionId;
	}
	public void setDistributionId(int distributionId) {
		DistributionId = distributionId;
	}
	public int getInvoiceLineNumber() {
		return InvoiceLineNumber;
	}
	public void setInvoiceLineNumber(int invoiceLineNumber) {
		InvoiceLineNumber = invoiceLineNumber;
	}
	public int getDetailedTaxLineNumber() {
		return DetailedTaxLineNumber;
	}
	public void setDetailedTaxLineNumber(int detailedTaxLineNumber) {
		DetailedTaxLineNumber = detailedTaxLineNumber;
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
	public double getAmount() {
		return Amount;
	}
	public void setAmount(double amount) {
		Amount = amount;
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
	public String getComments() {
		return Comments;
	}
	public void setComments(String comments) {
		Comments = comments;
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
	public String getLastUpdateDate() {
		return LastUpdateDate;
	}
	public void setLastUpdateDate(String lastUpdateDate) {
		LastUpdateDate = lastUpdateDate;
	}
	public String getLastUpdatedBy() {
		return LastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		LastUpdatedBy = lastUpdatedBy;
	}
	public List<links> getLinks() {
		return links;
	}
	public void setLinks(List<links> links) {
		this.links = links;
	}
	
}
