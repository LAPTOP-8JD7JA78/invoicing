package com.smartech.invoicingprod.distribuitorportal.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"id",
	"branch",
	"company",
	"customerName",
	"customerTaxId",
	"salesOrder",
	"invoiceType",
	"serial",
	"folio",
	"uuid",
	"invoiceSubTotal",
	"invoiceTaxAmount",
	"invoiceTotal",
	"invoiceCurrency",
	"stampDate"
})
public class InvoiceJSON {

	@JsonProperty("id")
	private int id;
	
	@JsonProperty("branch")
	private String branch;
	
	@JsonProperty("company")
	private String company;
	
	@JsonProperty("customerName")	
	private String customerName;
	
	@JsonProperty("customerTaxId")
	private String customerTaxId;
	
	@JsonProperty("salesOrder")
	private String salesOrder;
	
	@JsonProperty("invoiceType")
	private String invoiceType;
	
	@JsonProperty("serial")
	private String serial;
	
	@JsonProperty("folio")
	private String folio;
	
	@JsonProperty("uuid")
	private String uuid;
	
	@JsonProperty("invoiceSubTotal")
	private double invoiceSubTotal;
	
	@JsonProperty("invoiceTaxAmount")
	private double invoiceTaxAmount;
	
	@JsonProperty("invoiceTotal")
	private double invoiceTotal;
	
	@JsonProperty("invoiceCurrency")
	private String invoiceCurrency;
	
	@JsonProperty("stampDate")
	private String stampDate;

	
	@JsonProperty("id")
	public int getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(int id) {
		this.id = id;
	}

	@JsonProperty("branch")
	public String getBranch() {
		return branch;
	}

	@JsonProperty("branch")
	public void setBranch(String branch) {
		this.branch = branch;
	}

	@JsonProperty("company")
	public String getCompany() {
		return company;
	}

	@JsonProperty("company")
	public void setCompany(String company) {
		this.company = company;
	}

	@JsonProperty("customerName")
	public String getCustomerName() {
		return customerName;
	}

	@JsonProperty("customerName")
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	@JsonProperty("customerTaxId")
	public String getCustomerTaxId() {
		return customerTaxId;
	}

	@JsonProperty("customerTaxId")
	public void setCustomerTaxId(String customerTaxId) {
		this.customerTaxId = customerTaxId;
	}

	@JsonProperty("salesOrder")
	public String getSalesOrder() {
		return salesOrder;
	}

	@JsonProperty("salesOrder")
	public void setSalesOrder(String salesOrder) {
		this.salesOrder = salesOrder;
	}

	@JsonProperty("invoiceType")
	public String getInvoiceType() {
		return invoiceType;
	}

	@JsonProperty("invoiceType")
	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	@JsonProperty("serial")
	public String getSerial() {
		return serial;
	}

	@JsonProperty("serial")
	public void setSerial(String serial) {
		this.serial = serial;
	}

	@JsonProperty("folio")
	public String getFolio() {
		return folio;
	}

	@JsonProperty("folio")
	public void setFolio(String folio) {
		this.folio = folio;
	}

	@JsonProperty("uuid")
	public String getUuid() {
		return uuid;
	}

	@JsonProperty("uuid")
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@JsonProperty("invoiceSubTotal")
	public double getInvoiceSubTotal() {
		return invoiceSubTotal;
	}

	@JsonProperty("invoiceSubTotal")
	public void setInvoiceSubTotal(double invoiceSubTotal) {
		this.invoiceSubTotal = invoiceSubTotal;
	}

	@JsonProperty("invoiceTaxAmount")
	public double getInvoiceTaxAmount() {
		return invoiceTaxAmount;
	}

	@JsonProperty("invoiceTaxAmount")
	public void setInvoiceTaxAmount(double invoiceTaxAmount) {
		this.invoiceTaxAmount = invoiceTaxAmount;
	}

	@JsonProperty("invoiceTotal")
	public double getInvoiceTotal() {
		return invoiceTotal;
	}

	@JsonProperty("invoiceTotal")
	public void setInvoiceTotal(double invoiceTotal) {
		this.invoiceTotal = invoiceTotal;
	}

	@JsonProperty("invoiceCurrency")
	public String getInvoiceCurrency() {
		return invoiceCurrency;
	}

	@JsonProperty("invoiceCurrency")
	public void setInvoiceCurrency(String invoiceCurrency) {
		this.invoiceCurrency = invoiceCurrency;
	}

	@JsonProperty("stampDate")
	public String getStampDate() {
		return stampDate;
	}

	@JsonProperty("stampDate")
	public void setStampDate(String stampDate) {
		this.stampDate = stampDate;
	}
	
	

}
