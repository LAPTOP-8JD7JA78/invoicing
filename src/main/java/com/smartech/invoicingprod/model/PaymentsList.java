package com.smartech.invoicingprod.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity(name = "PaymentsList")
@Table(name = "paymentsList")
public class PaymentsList implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
	
	@Column(name = "customerName", nullable=true)
    private String customerName;
	
	@Column(name = "customerTaxId", nullable=true)
    private String customerTaxId;
	
	@Column(name = "customerCountry", nullable=true)
    private String customerCountry;
	
	@Column(name = "currency", nullable=true)
    private String currency;
	
	@Column(name = "folio", nullable=true)
    private String folio;
	
	@Column(name = "serial", nullable=true)
    private String serial;
	
	@Column(name = "paymentForm", nullable=true)
    private String paymentForm;
	
	@Column(name = "paymentAmount", nullable=true)
    private String paymentAmount;
	
	@Column(name = "exchangeRate", nullable=true)
    private String exchangeRate;
	
	@Column(name = "status", nullable=true)
    private String status;
	
	@Column(name = "uuid", nullable=true)
    private String uuid;
	
	@Column(name = "receiptNumber", nullable=true)
    private String receiptNumber;
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private Set<Payments> payments;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerTaxId() {
		return customerTaxId;
	}

	public void setCustomerTaxId(String customerTaxId) {
		this.customerTaxId = customerTaxId;
	}

	public String getCustomerCountry() {
		return customerCountry;
	}

	public void setCustomerCountry(String customerCountry) {
		this.customerCountry = customerCountry;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getPaymentForm() {
		return paymentForm;
	}

	public void setPaymentForm(String paymentForm) {
		this.paymentForm = paymentForm;
	}

	public String getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(String paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(String exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getReceiptNumber() {
		return receiptNumber;
	}

	public void setReceiptNumber(String receiptNumber) {
		this.receiptNumber = receiptNumber;
	}

	public Set<Payments> getPayments() {
		return payments;
	}

	public void setPayments(Set<Payments> payments) {
		this.payments = payments;
	}
	
}
