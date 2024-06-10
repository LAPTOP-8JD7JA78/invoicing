package com.smartech.invoicingprod.dto;

import java.util.List;

public class receivablesInvoiceLines {

	private int CustomerTransactionLineId;
	private int LineNumber;
	private String Description; 
	private double Quantity;
	private double UnitSellingPrice;
	private String TaxClassificationCode;
	private String SalesOrder; 
	private String AccountingRuleDuration;
	private String RuleEndDate;
	private String RuleStartDate;
	private String AccountingRule;
	private String Warehouse;
	private String MemoLine;
	private String UnitOfMeasure;
	private String ItemNumber;
	private String AllocatedFreightAmount;
	private String AssessableValue;
	private String SalesOrderDate;
	private double LineAmount; 
	private String CreatedBy;
	private String CreationDate;
	private String LastUpdatedBy;
	private String LastUpdateDate;
	private String TransacationBusinessCategory;
	private String UserDefinedFiscalClassification;
	private String ProductFiscalClassification;
	private String ProductCategory;
	private String ProductType;
	private String LineIntendedUse;
	private String LineAmountIncludesTax;
	private String TaxInvoiceDate;
	private String TaxInvoiceNumber;
	private String TaxExemptionCertificateNumber;
	private String TaxExemptionHandling;
	private String TaxExemptionReason;
	private List<receivablesInvoiceLineTaxLines> receivablesInvoiceLineTaxLines;
	private List<links> links;
	public int getCustomerTransactionLineId() {
		return CustomerTransactionLineId;
	}
	public void setCustomerTransactionLineId(int customerTransactionLineId) {
		CustomerTransactionLineId = customerTransactionLineId;
	}
	public int getLineNumber() {
		return LineNumber;
	}
	public void setLineNumber(int lineNumber) {
		LineNumber = lineNumber;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
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
	public String getSalesOrder() {
		return SalesOrder;
	}
	public void setSalesOrder(String salesOrder) {
		SalesOrder = salesOrder;
	}
	public String getAccountingRuleDuration() {
		return AccountingRuleDuration;
	}
	public void setAccountingRuleDuration(String accountingRuleDuration) {
		AccountingRuleDuration = accountingRuleDuration;
	}
	public String getRuleEndDate() {
		return RuleEndDate;
	}
	public void setRuleEndDate(String ruleEndDate) {
		RuleEndDate = ruleEndDate;
	}
	public String getRuleStartDate() {
		return RuleStartDate;
	}
	public void setRuleStartDate(String ruleStartDate) {
		RuleStartDate = ruleStartDate;
	}
	public String getAccountingRule() {
		return AccountingRule;
	}
	public void setAccountingRule(String accountingRule) {
		AccountingRule = accountingRule;
	}
	public String getWarehouse() {
		return Warehouse;
	}
	public void setWarehouse(String warehouse) {
		Warehouse = warehouse;
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
	public String getItemNumber() {
		return ItemNumber;
	}
	public void setItemNumber(String itemNumber) {
		ItemNumber = itemNumber;
	}
	public String getAllocatedFreightAmount() {
		return AllocatedFreightAmount;
	}
	public void setAllocatedFreightAmount(String allocatedFreightAmount) {
		AllocatedFreightAmount = allocatedFreightAmount;
	}
	public String getAssessableValue() {
		return AssessableValue;
	}
	public void setAssessableValue(String assessableValue) {
		AssessableValue = assessableValue;
	}
	public String getSalesOrderDate() {
		return SalesOrderDate;
	}
	public void setSalesOrderDate(String salesOrderDate) {
		SalesOrderDate = salesOrderDate;
	}
	public double getLineAmount() {
		return LineAmount;
	}
	public void setLineAmount(double lineAmount) {
		LineAmount = lineAmount;
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
	public String getTransacationBusinessCategory() {
		return TransacationBusinessCategory;
	}
	public void setTransacationBusinessCategory(String transacationBusinessCategory) {
		TransacationBusinessCategory = transacationBusinessCategory;
	}
	public String getUserDefinedFiscalClassification() {
		return UserDefinedFiscalClassification;
	}
	public void setUserDefinedFiscalClassification(String userDefinedFiscalClassification) {
		UserDefinedFiscalClassification = userDefinedFiscalClassification;
	}
	public String getProductFiscalClassification() {
		return ProductFiscalClassification;
	}
	public void setProductFiscalClassification(String productFiscalClassification) {
		ProductFiscalClassification = productFiscalClassification;
	}
	public String getProductCategory() {
		return ProductCategory;
	}
	public void setProductCategory(String productCategory) {
		ProductCategory = productCategory;
	}
	public String getProductType() {
		return ProductType;
	}
	public void setProductType(String productType) {
		ProductType = productType;
	}
	public String getLineIntendedUse() {
		return LineIntendedUse;
	}
	public void setLineIntendedUse(String lineIntendedUse) {
		LineIntendedUse = lineIntendedUse;
	}
	public String getLineAmountIncludesTax() {
		return LineAmountIncludesTax;
	}
	public void setLineAmountIncludesTax(String lineAmountIncludesTax) {
		LineAmountIncludesTax = lineAmountIncludesTax;
	}
	public String getTaxInvoiceDate() {
		return TaxInvoiceDate;
	}
	public void setTaxInvoiceDate(String taxInvoiceDate) {
		TaxInvoiceDate = taxInvoiceDate;
	}
	public String getTaxInvoiceNumber() {
		return TaxInvoiceNumber;
	}
	public void setTaxInvoiceNumber(String taxInvoiceNumber) {
		TaxInvoiceNumber = taxInvoiceNumber;
	}
	public String getTaxExemptionCertificateNumber() {
		return TaxExemptionCertificateNumber;
	}
	public void setTaxExemptionCertificateNumber(String taxExemptionCertificateNumber) {
		TaxExemptionCertificateNumber = taxExemptionCertificateNumber;
	}
	public String getTaxExemptionHandling() {
		return TaxExemptionHandling;
	}
	public void setTaxExemptionHandling(String taxExemptionHandling) {
		TaxExemptionHandling = taxExemptionHandling;
	}
	public String getTaxExemptionReason() {
		return TaxExemptionReason;
	}
	public void setTaxExemptionReason(String taxExemptionReason) {
		TaxExemptionReason = taxExemptionReason;
	}
	public List<receivablesInvoiceLineTaxLines> getReceivablesInvoiceLineTaxLines() {
		return receivablesInvoiceLineTaxLines;
	}
	public void setReceivablesInvoiceLineTaxLines(List<receivablesInvoiceLineTaxLines> receivablesInvoiceLineTaxLines) {
		this.receivablesInvoiceLineTaxLines = receivablesInvoiceLineTaxLines;
	}
	public List<links> getLinks() {
		return links;
	}
	public void setLinks(List<links> links) {
		this.links = links;
	}
	
	
}
