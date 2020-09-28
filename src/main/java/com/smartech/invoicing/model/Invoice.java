package com.smartech.invoicing.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
	
	@Column(name = "address1", nullable=true)
	private String address1;
	
	@Column(name = "address2", nullable=true)
	private String address2;
	
	@Column(name = "address3", nullable=true)
	private String address3;
	
	@Column(name = "address4", nullable=true)
	private String address4;
	
	@Column(name = "city", nullable=true)
	private String city;
	
	@Column(name = "state", nullable=true)
	private String state;
	
	@Column(name = "country", nullable=true)
	private String country;
	
	@Column(name = "createdBy", nullable=true)
	private String createdBy;
	
	@Column(name = "creationDate", nullable = true)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date creationDate;
	
	@Column(name = "customerName", nullable=true)
	private String customerName;
	
	@Column(name = "customerEmail", nullable=true)
	private String customerEmail;
	
	@Column(name = "customerZip", nullable=true)
	private String customerZip;
	
	@Column(name = "folio", nullable=true)
	private String folio;
	
	@Column(name = "invoiceTotal", nullable=true)
	private double invoiceTotal;
	
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
	
	@Column(name = "errorMsg", nullable=true)
	private String errorMsg;
	
	@Column(name = "customerTaxIdentifier", nullable=true)
	private String customerTaxIdentifier;
	
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
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private List<InvoiceDetails> invoiceDetails;

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

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public String getAddress4() {
		return address4;
	}

	public void setAddress4(String address4) {
		this.address4 = address4;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
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

	public String getCustomerTaxIdentifier() {
		return customerTaxIdentifier;
	}

	public void setCustomerTaxIdentifier(String customerTaxIdentifier) {
		this.customerTaxIdentifier = customerTaxIdentifier;
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

	public List<InvoiceDetails> getInvoiceDetails() {
		return invoiceDetails;
	}

	public void setInvoiceDetails(List<InvoiceDetails> invoiceDetails) {
		this.invoiceDetails = invoiceDetails;
	}
}

