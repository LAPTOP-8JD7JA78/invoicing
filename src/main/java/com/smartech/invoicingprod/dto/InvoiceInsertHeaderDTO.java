package com.smartech.invoicingprod.dto;

import java.util.List;

public class InvoiceInsertHeaderDTO {

	private String DueDate;
    private String InvoiceCurrencyCode;
    private String ConversionDate;
    private String TransactionDate;
    private String TransactionType;
    private String TransactionSource;
    private String BillToCustomerNumber;
    private String PaymentTerms;
    private String LegalEntityIdentifier;
    private String ShipToCustomerName;
    private String ShipToCustomerNumber;
    private String BusinessUnit;
    private String AccountingDate;
    private String ShipToSite;
    private String PayingCustomerName;
    private String BillToCustomerName;
    private String PayingCustomerAccount;
    private String SoldToPartyNumber;
    private List<ReceivablesInvoiceLineDTO> receivablesInvoiceLines;
    private List<ReceivablesInvoiceGdfDTO> receivablesInvoiceGdf;
    private List<ReceivablesInvoiceDistributionDTO> receivablesInvoiceDistributions;
    
	public String getDueDate() {
		return DueDate;
	}
	public void setDueDate(String dueDate) {
		DueDate = dueDate;
	}
	public String getInvoiceCurrencyCode() {
		return InvoiceCurrencyCode;
	}
	public void setInvoiceCurrencyCode(String invoiceCurrencyCode) {
		InvoiceCurrencyCode = invoiceCurrencyCode;
	}
	public String getTransactionDate() {
		return TransactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		TransactionDate = transactionDate;
	}
	public String getTransactionType() {
		return TransactionType;
	}
	public void setTransactionType(String transactionType) {
		TransactionType = transactionType;
	}
	public String getTransactionSource() {
		return TransactionSource;
	}
	public void setTransactionSource(String transactionSource) {
		TransactionSource = transactionSource;
	}
	public String getBillToCustomerNumber() {
		return BillToCustomerNumber;
	}
	public void setBillToCustomerNumber(String billToCustomerNumber) {
		BillToCustomerNumber = billToCustomerNumber;
	}
	public String getPaymentTerms() {
		return PaymentTerms;
	}
	public void setPaymentTerms(String paymentTerms) {
		PaymentTerms = paymentTerms;
	}
	public String getLegalEntityIdentifier() {
		return LegalEntityIdentifier;
	}
	public void setLegalEntityIdentifier(String legalEntityIdentifier) {
		LegalEntityIdentifier = legalEntityIdentifier;
	}
	public String getShipToCustomerName() {
		return ShipToCustomerName;
	}
	public void setShipToCustomerName(String shipToCustomerName) {
		ShipToCustomerName = shipToCustomerName;
	}
	public String getShipToCustomerNumber() {
		return ShipToCustomerNumber;
	}
	public void setShipToCustomerNumber(String shipToCustomerNumber) {
		ShipToCustomerNumber = shipToCustomerNumber;
	}
	public String getBusinessUnit() {
		return BusinessUnit;
	}
	public void setBusinessUnit(String businessUnit) {
		BusinessUnit = businessUnit;
	}
	public String getAccountingDate() {
		return AccountingDate;
	}
	public void setAccountingDate(String accountingDate) {
		AccountingDate = accountingDate;
	}
	public String getShipToSite() {
		return ShipToSite;
	}
	public void setShipToSite(String shipToSite) {
		ShipToSite = shipToSite;
	}
	public String getPayingCustomerName() {
		return PayingCustomerName;
	}
	public void setPayingCustomerName(String payingCustomerName) {
		PayingCustomerName = payingCustomerName;
	}
	public String getBillToCustomerName() {
		return BillToCustomerName;
	}
	public void setBillToCustomerName(String billToCustomerName) {
		BillToCustomerName = billToCustomerName;
	}
	public String getPayingCustomerAccount() {
		return PayingCustomerAccount;
	}
	public void setPayingCustomerAccount(String payingCustomerAccount) {
		PayingCustomerAccount = payingCustomerAccount;
	}
	public String getSoldToPartyNumber() {
		return SoldToPartyNumber;
	}
	public void setSoldToPartyNumber(String soldToPartyNumber) {
		SoldToPartyNumber = soldToPartyNumber;
	}
	public List<ReceivablesInvoiceLineDTO> getReceivablesInvoiceLines() {
		return receivablesInvoiceLines;
	}
	public void setReceivablesInvoiceLines(List<ReceivablesInvoiceLineDTO> receivablesInvoiceLines) {
		this.receivablesInvoiceLines = receivablesInvoiceLines;
	}
	public List<ReceivablesInvoiceGdfDTO> getReceivablesInvoiceGdf() {
		return receivablesInvoiceGdf;
	}
	public void setReceivablesInvoiceGdf(List<ReceivablesInvoiceGdfDTO> receivablesInvoiceGdf) {
		this.receivablesInvoiceGdf = receivablesInvoiceGdf;
	}
	public List<ReceivablesInvoiceDistributionDTO> getReceivablesInvoiceDistributions() {
		return receivablesInvoiceDistributions;
	}
	public void setReceivablesInvoiceDistributions(
			List<ReceivablesInvoiceDistributionDTO> receivablesInvoiceDistributions) {
		this.receivablesInvoiceDistributions = receivablesInvoiceDistributions;
	}
	public String getConversionDate() {
		return ConversionDate;
	}
	public void setConversionDate(String conversionDate) {
		ConversionDate = conversionDate;
	}
    
    
}
