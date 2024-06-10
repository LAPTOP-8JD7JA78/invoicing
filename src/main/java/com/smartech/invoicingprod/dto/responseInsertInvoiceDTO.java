package com.smartech.invoicingprod.dto;

import java.util.List;

public class responseInsertInvoiceDTO {

	private String CustomerTransactionId;
	private String TransactionNumber;
	private String InvoiceStatus;
//	private String DueDate;
//	private double ConversionDate;
//	private double ConversionRate;
//	private String InvoiceCurrencyCode;
//	private String SpecialInstructions;
//	private String CrossReference;
//	private String DocumentNumber;
//	private String TransactionDate;
//	private String TransactionType;
//	private String TransactionSource;
//	private String BillToCustomerNumber;
//	private String BillToSite;
//	private String Comments;
//	private String InternalNotes;
//	private String PaymentTerms;
//	private String LegalEntityIdentifier;
//	private String ConversionRateType;
//	private String PurchaseOrder;
//	private String PurchaseOrderDate;
//	private String PurchaseOrderRevision;
//	private String FirstPartyRegistrationNumber;
//	private String ThirdPartyRegistrationNumber;
//	private String InvoicingRule;
//	private String ShipToCustomerName;
//	private String ShipToCustomerNumber;
//	private String BillingDate;
//	private String BillToPartyId;
//	private String BusinessUnit;
//	private String AccountingDate;
//	private String ShipToSite;
//	private String PayingCustomerName;
//	private String PayingCustomerSite;
//	private String BillToCustomerName;
//	private String FreightAmount;
//	private String Carrier;
//	private String ShipDate;
//	private String ShippingReference;
//	private String BillToContact;
//	private String ShipToContact;
//	private String PrintOption;
//	private String CreatedBy;
//	private String CreationDate;
//	private String LastUpdatedBy;
//	private String LastUpdateDate;
//	private String PayingCustomerAccount;
//	private String SoldToPartyNumber;
//	private String RemitToAddress;
//	private String DefaultTaxationCountry;
//	private double EnteredAmount;
//	private double InvoiceBalanceAmount;
//	private String Prepayment;
//	private String Intercompany;
//	private String DocumentFiscalClassification;
//	private String BankAccountNumber;
//	private String CreditCardAuthorizationRequestIdentifier;
//	private String CreditCardExpirationDate;
//	private String CreditCardIssuerCode;
//	private String CreditCardTokenNumber;
//	private String CreditCardVoiceAuthorizationCode;
//	private String CreditCardErrorCode;
//	private String CreditCardErrorText;
//	private String CardHolderLastName;
//	private String CardHolderFirstName;
//	private String ReceiptMethod;
//	private String SalesPersonNumber;
//	private String StructuredPaymentReference;
//	private String InvoicePrinted;
//	private String LastPrintDate;
//	private String OriginalPrintDate;
//	private String DeliveryMethod;
//	private String Email;
//	private String AllowCompletion;
//	private String ControlCompletionReason;
//	private List<receivablesInvoiceLines> receivablesInvoiceLines;
//	private List<links> links;
//	private List<receivablesInvoiceGdf> receivablesInvoiceGdf;
//	private List<receivablesInvoiceDistributions> receivablesInvoiceDistributions;
	public String getCustomerTransactionId() {
		return CustomerTransactionId;
	}
	public void setCustomerTransactionId(String customerTransactionId) {
		CustomerTransactionId = customerTransactionId;
	}
	public String getTransactionNumber() {
		return TransactionNumber;
	}
	public void setTransactionNumber(String transactionNumber) {
		TransactionNumber = transactionNumber;
	}
	public String getInvoiceStatus() {
		return InvoiceStatus;
	}
	public void setInvoiceStatus(String invoiceStatus) {
		InvoiceStatus = invoiceStatus;
	}
	
	
//	public String getDueDate() {
//		return DueDate;
//	}
//	public void setDueDate(String dueDate) {
//		DueDate = dueDate;
//	}
//	public double getConversionDate() {
//		return ConversionDate;
//	}
//	public void setConversionDate(double conversionDate) {
//		ConversionDate = conversionDate;
//	}
//	public double getConversionRate() {
//		return ConversionRate;
//	}
//	public void setConversionRate(double conversionRate) {
//		ConversionRate = conversionRate;
//	}
//	public String getInvoiceCurrencyCode() {
//		return InvoiceCurrencyCode;
//	}
//	public void setInvoiceCurrencyCode(String invoiceCurrencyCode) {
//		InvoiceCurrencyCode = invoiceCurrencyCode;
//	}
//	public String getSpecialInstructions() {
//		return SpecialInstructions;
//	}
//	public void setSpecialInstructions(String specialInstructions) {
//		SpecialInstructions = specialInstructions;
//	}
//	public String getCrossReference() {
//		return CrossReference;
//	}
//	public void setCrossReference(String crossReference) {
//		CrossReference = crossReference;
//	}
//	public String getDocumentNumber() {
//		return DocumentNumber;
//	}
//	public void setDocumentNumber(String documentNumber) {
//		DocumentNumber = documentNumber;
//	}
//	public String getTransactionDate() {
//		return TransactionDate;
//	}
//	public void setTransactionDate(String transactionDate) {
//		TransactionDate = transactionDate;
//	}
//	public String getTransactionType() {
//		return TransactionType;
//	}
//	public void setTransactionType(String transactionType) {
//		TransactionType = transactionType;
//	}
//	public String getTransactionSource() {
//		return TransactionSource;
//	}
//	public void setTransactionSource(String transactionSource) {
//		TransactionSource = transactionSource;
//	}
//	public String getBillToCustomerNumber() {
//		return BillToCustomerNumber;
//	}
//	public void setBillToCustomerNumber(String billToCustomerNumber) {
//		BillToCustomerNumber = billToCustomerNumber;
//	}
//	public String getBillToSite() {
//		return BillToSite;
//	}
//	public void setBillToSite(String billToSite) {
//		BillToSite = billToSite;
//	}
//	public String getComments() {
//		return Comments;
//	}
//	public void setComments(String comments) {
//		Comments = comments;
//	}
//	public String getInternalNotes() {
//		return InternalNotes;
//	}
//	public void setInternalNotes(String internalNotes) {
//		InternalNotes = internalNotes;
//	}
//	public String getPaymentTerms() {
//		return PaymentTerms;
//	}
//	public void setPaymentTerms(String paymentTerms) {
//		PaymentTerms = paymentTerms;
//	}
//	public String getLegalEntityIdentifier() {
//		return LegalEntityIdentifier;
//	}
//	public void setLegalEntityIdentifier(String legalEntityIdentifier) {
//		LegalEntityIdentifier = legalEntityIdentifier;
//	}
//	public String getConversionRateType() {
//		return ConversionRateType;
//	}
//	public void setConversionRateType(String conversionRateType) {
//		ConversionRateType = conversionRateType;
//	}
//	public String getPurchaseOrder() {
//		return PurchaseOrder;
//	}
//	public void setPurchaseOrder(String purchaseOrder) {
//		PurchaseOrder = purchaseOrder;
//	}
//	public String getPurchaseOrderDate() {
//		return PurchaseOrderDate;
//	}
//	public void setPurchaseOrderDate(String purchaseOrderDate) {
//		PurchaseOrderDate = purchaseOrderDate;
//	}
//	public String getPurchaseOrderRevision() {
//		return PurchaseOrderRevision;
//	}
//	public void setPurchaseOrderRevision(String purchaseOrderRevision) {
//		PurchaseOrderRevision = purchaseOrderRevision;
//	}
//	public String getFirstPartyRegistrationNumber() {
//		return FirstPartyRegistrationNumber;
//	}
//	public void setFirstPartyRegistrationNumber(String firstPartyRegistrationNumber) {
//		FirstPartyRegistrationNumber = firstPartyRegistrationNumber;
//	}
//	public String getThirdPartyRegistrationNumber() {
//		return ThirdPartyRegistrationNumber;
//	}
//	public void setThirdPartyRegistrationNumber(String thirdPartyRegistrationNumber) {
//		ThirdPartyRegistrationNumber = thirdPartyRegistrationNumber;
//	}
//	public String getInvoicingRule() {
//		return InvoicingRule;
//	}
//	public void setInvoicingRule(String invoicingRule) {
//		InvoicingRule = invoicingRule;
//	}
//	public String getShipToCustomerName() {
//		return ShipToCustomerName;
//	}
//	public void setShipToCustomerName(String shipToCustomerName) {
//		ShipToCustomerName = shipToCustomerName;
//	}
//	public String getShipToCustomerNumber() {
//		return ShipToCustomerNumber;
//	}
//	public void setShipToCustomerNumber(String shipToCustomerNumber) {
//		ShipToCustomerNumber = shipToCustomerNumber;
//	}
//	public String getBillingDate() {
//		return BillingDate;
//	}
//	public void setBillingDate(String billingDate) {
//		BillingDate = billingDate;
//	}
//	public String getBillToPartyId() {
//		return BillToPartyId;
//	}
//	public void setBillToPartyId(String billToPartyId) {
//		BillToPartyId = billToPartyId;
//	}
//	public String getBusinessUnit() {
//		return BusinessUnit;
//	}
//	public void setBusinessUnit(String businessUnit) {
//		BusinessUnit = businessUnit;
//	}
//	public String getAccountingDate() {
//		return AccountingDate;
//	}
//	public void setAccountingDate(String accountingDate) {
//		AccountingDate = accountingDate;
//	}
//	public String getShipToSite() {
//		return ShipToSite;
//	}
//	public void setShipToSite(String shipToSite) {
//		ShipToSite = shipToSite;
//	}
//	public String getPayingCustomerName() {
//		return PayingCustomerName;
//	}
//	public void setPayingCustomerName(String payingCustomerName) {
//		PayingCustomerName = payingCustomerName;
//	}
//	public String getPayingCustomerSite() {
//		return PayingCustomerSite;
//	}
//	public void setPayingCustomerSite(String payingCustomerSite) {
//		PayingCustomerSite = payingCustomerSite;
//	}
//	public String getBillToCustomerName() {
//		return BillToCustomerName;
//	}
//	public void setBillToCustomerName(String billToCustomerName) {
//		BillToCustomerName = billToCustomerName;
//	}
//	public String getFreightAmount() {
//		return FreightAmount;
//	}
//	public void setFreightAmount(String freightAmount) {
//		FreightAmount = freightAmount;
//	}
//	public String getCarrier() {
//		return Carrier;
//	}
//	public void setCarrier(String carrier) {
//		Carrier = carrier;
//	}
//	public String getShipDate() {
//		return ShipDate;
//	}
//	public void setShipDate(String shipDate) {
//		ShipDate = shipDate;
//	}
//	public String getShippingReference() {
//		return ShippingReference;
//	}
//	public void setShippingReference(String shippingReference) {
//		ShippingReference = shippingReference;
//	}
//	public String getBillToContact() {
//		return BillToContact;
//	}
//	public void setBillToContact(String billToContact) {
//		BillToContact = billToContact;
//	}
//	public String getShipToContact() {
//		return ShipToContact;
//	}
//	public void setShipToContact(String shipToContact) {
//		ShipToContact = shipToContact;
//	}
//	public String getPrintOption() {
//		return PrintOption;
//	}
//	public void setPrintOption(String printOption) {
//		PrintOption = printOption;
//	}
//	public String getCreatedBy() {
//		return CreatedBy;
//	}
//	public void setCreatedBy(String createdBy) {
//		CreatedBy = createdBy;
//	}
//	public String getCreationDate() {
//		return CreationDate;
//	}
//	public void setCreationDate(String creationDate) {
//		CreationDate = creationDate;
//	}
//	public String getLastUpdatedBy() {
//		return LastUpdatedBy;
//	}
//	public void setLastUpdatedBy(String lastUpdatedBy) {
//		LastUpdatedBy = lastUpdatedBy;
//	}
//	public String getLastUpdateDate() {
//		return LastUpdateDate;
//	}
//	public void setLastUpdateDate(String lastUpdateDate) {
//		LastUpdateDate = lastUpdateDate;
//	}
//	public String getPayingCustomerAccount() {
//		return PayingCustomerAccount;
//	}
//	public void setPayingCustomerAccount(String payingCustomerAccount) {
//		PayingCustomerAccount = payingCustomerAccount;
//	}
//	public String getSoldToPartyNumber() {
//		return SoldToPartyNumber;
//	}
//	public void setSoldToPartyNumber(String soldToPartyNumber) {
//		SoldToPartyNumber = soldToPartyNumber;
//	}
//	public String getRemitToAddress() {
//		return RemitToAddress;
//	}
//	public void setRemitToAddress(String remitToAddress) {
//		RemitToAddress = remitToAddress;
//	}
//	public String getDefaultTaxationCountry() {
//		return DefaultTaxationCountry;
//	}
//	public void setDefaultTaxationCountry(String defaultTaxationCountry) {
//		DefaultTaxationCountry = defaultTaxationCountry;
//	}
//	public double getEnteredAmount() {
//		return EnteredAmount;
//	}
//	public void setEnteredAmount(double enteredAmount) {
//		EnteredAmount = enteredAmount;
//	}
//	public double getInvoiceBalanceAmount() {
//		return InvoiceBalanceAmount;
//	}
//	public void setInvoiceBalanceAmount(double invoiceBalanceAmount) {
//		InvoiceBalanceAmount = invoiceBalanceAmount;
//	}
//	public String getPrepayment() {
//		return Prepayment;
//	}
//	public void setPrepayment(String prepayment) {
//		Prepayment = prepayment;
//	}
//	public String getIntercompany() {
//		return Intercompany;
//	}
//	public void setIntercompany(String intercompany) {
//		Intercompany = intercompany;
//	}
//	public String getDocumentFiscalClassification() {
//		return DocumentFiscalClassification;
//	}
//	public void setDocumentFiscalClassification(String documentFiscalClassification) {
//		DocumentFiscalClassification = documentFiscalClassification;
//	}
//	public String getBankAccountNumber() {
//		return BankAccountNumber;
//	}
//	public void setBankAccountNumber(String bankAccountNumber) {
//		BankAccountNumber = bankAccountNumber;
//	}
//	public String getCreditCardAuthorizationRequestIdentifier() {
//		return CreditCardAuthorizationRequestIdentifier;
//	}
//	public void setCreditCardAuthorizationRequestIdentifier(String creditCardAuthorizationRequestIdentifier) {
//		CreditCardAuthorizationRequestIdentifier = creditCardAuthorizationRequestIdentifier;
//	}
//	public String getCreditCardExpirationDate() {
//		return CreditCardExpirationDate;
//	}
//	public void setCreditCardExpirationDate(String creditCardExpirationDate) {
//		CreditCardExpirationDate = creditCardExpirationDate;
//	}
//	public String getCreditCardIssuerCode() {
//		return CreditCardIssuerCode;
//	}
//	public void setCreditCardIssuerCode(String creditCardIssuerCode) {
//		CreditCardIssuerCode = creditCardIssuerCode;
//	}
//	public String getCreditCardTokenNumber() {
//		return CreditCardTokenNumber;
//	}
//	public void setCreditCardTokenNumber(String creditCardTokenNumber) {
//		CreditCardTokenNumber = creditCardTokenNumber;
//	}
//	public String getCreditCardVoiceAuthorizationCode() {
//		return CreditCardVoiceAuthorizationCode;
//	}
//	public void setCreditCardVoiceAuthorizationCode(String creditCardVoiceAuthorizationCode) {
//		CreditCardVoiceAuthorizationCode = creditCardVoiceAuthorizationCode;
//	}
//	public String getCreditCardErrorCode() {
//		return CreditCardErrorCode;
//	}
//	public void setCreditCardErrorCode(String creditCardErrorCode) {
//		CreditCardErrorCode = creditCardErrorCode;
//	}
//	public String getCreditCardErrorText() {
//		return CreditCardErrorText;
//	}
//	public void setCreditCardErrorText(String creditCardErrorText) {
//		CreditCardErrorText = creditCardErrorText;
//	}
//	public String getCardHolderLastName() {
//		return CardHolderLastName;
//	}
//	public void setCardHolderLastName(String cardHolderLastName) {
//		CardHolderLastName = cardHolderLastName;
//	}
//	public String getCardHolderFirstName() {
//		return CardHolderFirstName;
//	}
//	public void setCardHolderFirstName(String cardHolderFirstName) {
//		CardHolderFirstName = cardHolderFirstName;
//	}
//	public String getReceiptMethod() {
//		return ReceiptMethod;
//	}
//	public void setReceiptMethod(String receiptMethod) {
//		ReceiptMethod = receiptMethod;
//	}
//	public String getSalesPersonNumber() {
//		return SalesPersonNumber;
//	}
//	public void setSalesPersonNumber(String salesPersonNumber) {
//		SalesPersonNumber = salesPersonNumber;
//	}
//	public String getStructuredPaymentReference() {
//		return StructuredPaymentReference;
//	}
//	public void setStructuredPaymentReference(String structuredPaymentReference) {
//		StructuredPaymentReference = structuredPaymentReference;
//	}
//	public String getInvoicePrinted() {
//		return InvoicePrinted;
//	}
//	public void setInvoicePrinted(String invoicePrinted) {
//		InvoicePrinted = invoicePrinted;
//	}
//	public String getLastPrintDate() {
//		return LastPrintDate;
//	}
//	public void setLastPrintDate(String lastPrintDate) {
//		LastPrintDate = lastPrintDate;
//	}
//	public String getOriginalPrintDate() {
//		return OriginalPrintDate;
//	}
//	public void setOriginalPrintDate(String originalPrintDate) {
//		OriginalPrintDate = originalPrintDate;
//	}
//	public String getDeliveryMethod() {
//		return DeliveryMethod;
//	}
//	public void setDeliveryMethod(String deliveryMethod) {
//		DeliveryMethod = deliveryMethod;
//	}
//	public String getEmail() {
//		return Email;
//	}
//	public void setEmail(String email) {
//		Email = email;
//	}
//	public String getAllowCompletion() {
//		return AllowCompletion;
//	}
//	public void setAllowCompletion(String allowCompletion) {
//		AllowCompletion = allowCompletion;
//	}
//	public String getControlCompletionReason() {
//		return ControlCompletionReason;
//	}
//	public void setControlCompletionReason(String controlCompletionReason) {
//		ControlCompletionReason = controlCompletionReason;
//	}
	
}
