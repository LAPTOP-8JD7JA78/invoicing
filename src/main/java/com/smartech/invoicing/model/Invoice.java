package com.smartech.invoicing.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity(name="Invoice")
@Table(name="invoice")
public class Invoice implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column(name = "UUID", nullable=true)
	private String UUID;
	
	@Column(name = "UUIDReference", nullable=true)
	private String UUIDReference;
	
	@Column(name = "customerAddress1", nullable=true)
	private String customerAddress1;
	
	@Column(name = "customerAddress2", nullable=true)
	private String customerAddress2;
	
	@Column(name = "customerCity", nullable=true)
	private String customerCity;
	
	@Column(name = "customerState", nullable=true)
	private String customerState;
	
	@Column(name = "customerCountry", nullable=true)
	private String customerCountry;
	
	@Column(name = "createdBy", nullable=true)
	private String createdBy;
	
	@Column(name = "creationDate", nullable = true)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date creationDate;
	
	@Column(name = "customerName", nullable=true)
	private String customerName;
	
	@Column(name = "customerTaxIdentifier", nullable=true)
	private String customerTaxIdentifier;
	
	@Column(name = "customerEmail", nullable=true)
	private String customerEmail;
	
	@Column(name = "customerZip", nullable=true)
	private String customerZip;
	
	@Column(name = "folio", nullable=true)
	private String folio;
	
	@Column(name = "invoiceTotal", nullable=true)
	private double invoiceTotal;
	
	@Column(name = "invoiceSubTotal", nullable=true)
	private double invoiceSubTotal;
	
	@Column(name = "invoiceDiscount", nullable=true)
	private double invoiceDiscount;
	
	@Column(name = "invoiceTaxAmount", nullable=true)
	private double invoiceTaxAmount;
	
	@Column(name = "isInvoice", nullable=true)
	private boolean isInvoice;
	
	@Column(name = "serial", nullable=true)
	private String serial;
	
	@Column(name = "updatedBy", nullable=true)
	private String updatedBy;
	
	@Column(name = "updatedDate", nullable = true)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date updatedDate;
	
	@Column(name = "paymentType", nullable=true)
	private String paymentType;
	
	@Column(name = "paymentMethod", nullable=true)
	private String paymentMethod;
	
	@Column(name = "CFDIUse", nullable=true)
	private String CFDIUse;
	
	@Lob
	@Column(name = "errorMsg", nullable=true)
	private String errorMsg;
	
	@Column(name = "invoiceReferenceTransactionNumber", nullable=true)
	private String invoiceReferenceTransactionNumber;
	
	@OneToOne
	private Branch branch;
	
	@OneToOne
	private Company company;

	@Column(name = "certComplement", nullable=true)
	private String certComplement;
	
	@Column(name = "status", nullable=false)
	private String status;
	
	@Column(name = "orderType", nullable=false)
	private String orderType;
	
	@Column(name = "orderSource", nullable=false)
	private String orderSource;
		
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private Set<InvoiceDetails> invoiceDetails;
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private Set<Payments> payments;
	
	@Column(name = "setName", nullable=false)
	private String setName;
	
	@Column(name = "fromSalesOrder", nullable=true)
	private String fromSalesOrder;
	
	@Column(name = "paymentTerms", nullable=true)
	private String paymentTerms;
	
	@Column(name = "invoiceCurrency", nullable=true)
	private String invoiceCurrency;
	
	@Column(name = "invoiceExchangeRate", nullable=true)
	private double invoiceExchangeRate;
	
	@Column(name = "invoiceType", nullable=true)
	private String invoiceType;
	
	@Column(name = "invoiceRelationType", nullable=true)
	private String invoiceRelationType;
	
	@Column(name = "shipToName", nullable=true)
	private String shipToName;
	
	@Column(name = "shipToaddress", nullable=true)
	private String shipToaddress;
	
	@Column(name = "shipToCity", nullable=true)
	private String shipToCity;
	
	@Column(name = "shipToCountry", nullable=true)
	private String shipToCountry;
	
	@Column(name = "shipToZip", nullable=true)
	private String shipToZip;
	
	@Column(name = "shipToState", nullable=true)
	private String shipToState;
	
	@Column(name = "previousBalanceAmount", nullable=true)
	private String previousBalanceAmount;
	
	@Column(name = "RemainingBalanceAmount", nullable=true)
	private String RemainingBalanceAmount;
	
	@Column(name = "customerPartyNumber", nullable=true)
	private String customerPartyNumber;
	
	@Column(name = "isExtCom", nullable=true)
	private boolean isExtCom;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUUID() {
		return UUID;
	}

	public void setUUID(String uUID) {
		UUID = uUID;
	}

	public String getUUIDReference() {
		return UUIDReference;
	}

	public void setUUIDReference(String uUIDReference) {
		UUIDReference = uUIDReference;
	}

	public String getCustomerAddress1() {
		return customerAddress1;
	}

	public void setCustomerAddress1(String customerAddress1) {
		this.customerAddress1 = customerAddress1;
	}

	public String getCustomerAddress2() {
		return customerAddress2;
	}

	public void setCustomerAddress2(String customerAddress2) {
		this.customerAddress2 = customerAddress2;
	}

	public String getCustomerCity() {
		return customerCity;
	}

	public void setCustomerCity(String customerCity) {
		this.customerCity = customerCity;
	}

	public String getCustomerState() {
		return customerState;
	}

	public void setCustomerState(String customerState) {
		this.customerState = customerState;
	}

	public String getCustomerCountry() {
		return customerCountry;
	}

	public void setCustomerCountry(String customerCountry) {
		this.customerCountry = customerCountry;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerTaxIdentifier() {
		return customerTaxIdentifier;
	}

	public void setCustomerTaxIdentifier(String customerTaxIdentifier) {
		this.customerTaxIdentifier = customerTaxIdentifier;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public String getCustomerZip() {
		return customerZip;
	}

	public void setCustomerZip(String customerZip) {
		this.customerZip = customerZip;
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public double getInvoiceTotal() {
		return invoiceTotal;
	}

	public void setInvoiceTotal(double invoiceTotal) {
		this.invoiceTotal = invoiceTotal;
	}

	public double getInvoiceSubTotal() {
		return invoiceSubTotal;
	}

	public void setInvoiceSubTotal(double invoiceSubTotal) {
		this.invoiceSubTotal = invoiceSubTotal;
	}

	public double getInvoiceDiscount() {
		return invoiceDiscount;
	}

	public void setInvoiceDiscount(double invoiceDiscount) {
		this.invoiceDiscount = invoiceDiscount;
	}

	public double getInvoiceTaxAmount() {
		return invoiceTaxAmount;
	}

	public void setInvoiceTaxAmount(double invoiceTaxAmount) {
		this.invoiceTaxAmount = invoiceTaxAmount;
	}

	public boolean isInvoice() {
		return isInvoice;
	}

	public void setInvoice(boolean isInvoice) {
		this.isInvoice = isInvoice;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getCFDIUse() {
		return CFDIUse;
	}

	public void setCFDIUse(String cFDIUse) {
		CFDIUse = cFDIUse;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getInvoiceReferenceTransactionNumber() {
		return invoiceReferenceTransactionNumber;
	}

	public void setInvoiceReferenceTransactionNumber(String invoiceReferenceTransactionNumber) {
		this.invoiceReferenceTransactionNumber = invoiceReferenceTransactionNumber;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getCertComplement() {
		return certComplement;
	}

	public void setCertComplement(String certComplement) {
		this.certComplement = certComplement;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOrderSource() {
		return orderSource;
	}

	public void setOrderSource(String orderSource) {
		this.orderSource = orderSource;
	}

	public Set<InvoiceDetails> getInvoiceDetails() {
		return invoiceDetails;
	}

	public void setInvoiceDetails(Set<InvoiceDetails> invoiceDetails) {
		this.invoiceDetails = invoiceDetails;
	}
	
	public Set<Payments> getPayments() {
		return payments;
	}

	public void setPayments(Set<Payments> payments) {
		this.payments = payments;
	}

	public String getSetName() {
		return setName;
	}

	public void setSetName(String setName) {
		this.setName = setName;
	}

	public String getFromSalesOrder() {
		return fromSalesOrder;
	}

	public void setFromSalesOrder(String fromSalesOrder) {
		this.fromSalesOrder = fromSalesOrder;
	}

	public String getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	public String getInvoiceCurrency() {
		return invoiceCurrency;
	}

	public void setInvoiceCurrency(String invoiceCurrency) {
		this.invoiceCurrency = invoiceCurrency;
	}

	public double getInvoiceExchangeRate() {
		return invoiceExchangeRate;
	}

	public void setInvoiceExchangeRate(double invoiceExchangeRate) {
		this.invoiceExchangeRate = invoiceExchangeRate;
	}

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getInvoiceRelationType() {
		return invoiceRelationType;
	}

	public void setInvoiceRelationType(String invoiceRelationType) {
		this.invoiceRelationType = invoiceRelationType;
	}

	public String getShipToName() {
		return shipToName;
	}

	public void setShipToName(String shipToName) {
		this.shipToName = shipToName;
	}

	public String getShipToaddress() {
		return shipToaddress;
	}

	public void setShipToaddress(String shipToaddress) {
		this.shipToaddress = shipToaddress;
	}

	public String getShipToCity() {
		return shipToCity;
	}

	public void setShipToCity(String shipToCity) {
		this.shipToCity = shipToCity;
	}

	public String getShipToCountry() {
		return shipToCountry;
	}

	public void setShipToCountry(String shipToCountry) {
		this.shipToCountry = shipToCountry;
	}

	public String getShipToZip() {
		return shipToZip;
	}

	public void setShipToZip(String shipToZip) {
		this.shipToZip = shipToZip;
	}

	public String getShipToState() {
		return shipToState;
	}

	public void setShipToState(String shipToState) {
		this.shipToState = shipToState;
	}

	public String getPreviousBalanceAmount() {
		return previousBalanceAmount;
	}

	public void setPreviousBalanceAmount(String previousBalanceAmount) {
		this.previousBalanceAmount = previousBalanceAmount;
	}

	public String getRemainingBalanceAmount() {
		return RemainingBalanceAmount;
	}

	public void setRemainingBalanceAmount(String remainingBalanceAmount) {
		RemainingBalanceAmount = remainingBalanceAmount;
	}

	public String getCustomerPartyNumber() {
		return customerPartyNumber;
	}

	public void setCustomerPartyNumber(String customerPartyNumber) {
		this.customerPartyNumber = customerPartyNumber;
	}

	public boolean isExtCom() {
		return isExtCom;
	}

	public void setExtCom(boolean isExtCom) {
		this.isExtCom = isExtCom;
	}
	
}

