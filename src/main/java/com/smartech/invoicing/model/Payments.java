package com.smartech.invoicing.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity(name="Payments")
@Table(name="payments")
public class Payments implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
	
	@Column(name = "serial", nullable = true)
    private String serial;
	
	@Column(name = "folio", nullable = true)
    private String folio;
	
	@Column(name = "creationDate", nullable = true)
	@DateTimeFormat(iso = ISO.DATE_TIME)
    private String creationDate;
	
	@Column(name = "updateDate", nullable = true)
	@DateTimeFormat(iso = ISO.DATE_TIME)
    private String updateDate;
	
	@OneToOne
	Company company;
	
	@OneToOne
	Branch branch;
	
	@Column(name = "postalCode", nullable = true)
    private String postalCode;
	
	@Column(name = "relationType", nullable = true)
    private String relationType;
	
	@Column(name = "uuidReference", nullable = true)
    private String uuidReference;
	
	@Column(name = "country", nullable = true)
    private String country;
	
	@Column(name = "customerEmail", nullable = true)
    private String customerEmail;
	
	@Column(name = "customerName", nullable = true)
    private String customerName;
	
	@Column(name = "taxIdentifier", nullable = true)
    private String taxIdentifier;
	
	@Column(name = "partyNumber", nullable = true)
    private String partyNumber;
	
	@Column(name = "paymentForm", nullable = true)
    private String paymentForm;
	
	@Column(name = "currency", nullable = true)
    private String currency;
	
	@Column(name = "exchangeRate", nullable = true)
    private String exchangeRate;
	
	@Column(name = "transactionReference", nullable = true)
    private String transactionReference;
	
	@Column(name = "bankReference", nullable = true)
    private String bankReference;
	
	@Column(name = "acountBankTaxIdentifier", nullable = true)
    private String acountBankTaxIdentifier;
	
	@Column(name = "payerAccount", nullable = true)
    private String payerAccount;
	
	@Column(name = "beneficiaryAccount", nullable = true)
    private String beneficiaryAccount;
	
	@Column(name = "benBankAccTaxIden", nullable = true)
    private String benBankAccTaxIden;
	
	@Column(name = "paymentMethod", nullable = true)
    private String paymentMethod;
	
	@Column(name = "previousBalanceAmount", nullable = true)
    private String previousBalanceAmount;
	
	@Column(name = "paymentAmount", nullable = true)
    private String paymentAmount;
	
	@Column(name = "remainingBalanceAmount", nullable = true)
    private String remainingBalanceAmount;
	
	@Column(name = "receiptNumber", nullable = true)
    private String receiptNumber;
	
	@Column(name = "receiptId", nullable = true)
    private String receiptId;
	
	@Column(name = "paymentNumber", nullable = true)
    private String paymentNumber;
	
	@Column(name = "paymentStatus", nullable = true)
    private String paymentStatus;
	
	@Column(name = "UUID", nullable = true)
    private String UUID;
	
	@Column(name = "paymentType", nullable = true)
	private String paymentType;
	
	@Column(name = "paymentError", nullable = true)
	private String paymentError;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getRelationType() {
		return relationType;
	}

	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}

	public String getUuidReference() {
		return uuidReference;
	}

	public void setUuidReference(String uuidReference) {
		this.uuidReference = uuidReference;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getTaxIdentifier() {
		return taxIdentifier;
	}

	public void setTaxIdentifier(String taxIdentifier) {
		this.taxIdentifier = taxIdentifier;
	}

	public String getPartyNumber() {
		return partyNumber;
	}

	public void setPartyNumber(String partyNumber) {
		this.partyNumber = partyNumber;
	}

	public String getPaymentForm() {
		return paymentForm;
	}

	public void setPaymentForm(String paymentForm) {
		this.paymentForm = paymentForm;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(String exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getTransactionReference() {
		return transactionReference;
	}

	public void setTransactionReference(String transactionReference) {
		this.transactionReference = transactionReference;
	}

	public String getBankReference() {
		return bankReference;
	}

	public void setBankReference(String bankReference) {
		this.bankReference = bankReference;
	}

	public String getAcountBankTaxIdentifier() {
		return acountBankTaxIdentifier;
	}

	public void setAcountBankTaxIdentifier(String acountBankTaxIdentifier) {
		this.acountBankTaxIdentifier = acountBankTaxIdentifier;
	}

	public String getPayerAccount() {
		return payerAccount;
	}

	public void setPayerAccount(String payerAccount) {
		this.payerAccount = payerAccount;
	}

	public String getBeneficiaryAccount() {
		return beneficiaryAccount;
	}

	public void setBeneficiaryAccount(String beneficiaryAccount) {
		this.beneficiaryAccount = beneficiaryAccount;
	}

	public String getBenBankAccTaxIden() {
		return benBankAccTaxIden;
	}

	public void setBenBankAccTaxIden(String benBankAccTaxIden) {
		this.benBankAccTaxIden = benBankAccTaxIden;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getPreviousBalanceAmount() {
		return previousBalanceAmount;
	}

	public void setPreviousBalanceAmount(String previousBalanceAmount) {
		this.previousBalanceAmount = previousBalanceAmount;
	}

	public String getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(String paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getRemainingBalanceAmount() {
		return remainingBalanceAmount;
	}

	public void setRemainingBalanceAmount(String remainingBalanceAmount) {
		this.remainingBalanceAmount = remainingBalanceAmount;
	}

	public String getReceiptNumber() {
		return receiptNumber;
	}

	public void setReceiptNumber(String receiptNumber) {
		this.receiptNumber = receiptNumber;
	}

	public String getReceiptId() {
		return receiptId;
	}

	public void setReceiptId(String receiptId) {
		this.receiptId = receiptId;
	}

	public String getPaymentNumber() {
		return paymentNumber;
	}

	public void setPaymentNumber(String paymentNumber) {
		this.paymentNumber = paymentNumber;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getUUID() {
		return UUID;
	}

	public void setUUID(String uUID) {
		UUID = uUID;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getPaymentError() {
		return paymentError;
	}

	public void setPaymentError(String paymentError) {
		this.paymentError = paymentError;
	}
	
}
