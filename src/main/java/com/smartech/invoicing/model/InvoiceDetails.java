package com.smartech.invoicing.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity(name = "InvoiceDetails")
@Table(name = "invoiceDetails")
public class InvoiceDetails implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column(name = "itemNumber", nullable=true)
	private String itemNumber;
	
	@Column(name = "itemDescription", nullable=true)
	private String itemDescription;
	
	@Column(name = "quantity", nullable=true)
	private double quantity;
	
	@Column(name = "unitPrice", nullable=true)
	private double unitPrice;
	
	@Column(name = "totalAmount", nullable=true)
	private double totalAmount;
	
	@Column(name = "exchangeRate", nullable=true)
	private double exchangeRate;
	
	@Column(name = "totalTaxAmount", nullable=true)
	private double totalTaxAmount;
	
	@Column(name = "unitProdServ", nullable=true)
	private String unitProdServ;
	
	@Column(name = "uomName", nullable=true)
	private String uomName;
	
	@Column(name = "uomCode", nullable=true)
	private String uomCode;
	
	@Column(name = "secUomName", nullable=true)
	private String secUomName;
	
	@Column(name = "secUomCode", nullable=true)
	private String secUomCode;
	
	@Column(name = "currency", nullable=true)
	private String currency;

	@Column(name = "status", nullable=true)
	private String status; 	
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private List<TaxCodes> taxCodes;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public double getTotalTaxAmount() {
		return totalTaxAmount;
	}

	public void setTotalTaxAmount(double totalTaxAmount) {
		this.totalTaxAmount = totalTaxAmount;
	}

	public String getUnitProdServ() {
		return unitProdServ;
	}

	public void setUnitProdServ(String unitProdServ) {
		this.unitProdServ = unitProdServ;
	}

	public String getUomName() {
		return uomName;
	}

	public void setUomName(String uomName) {
		this.uomName = uomName;
	}

	public String getUomCode() {
		return uomCode;
	}

	public void setUomCode(String uomCode) {
		this.uomCode = uomCode;
	}

	public String getSecUomName() {
		return secUomName;
	}

	public void setSecUomName(String secUomName) {
		this.secUomName = secUomName;
	}

	public String getSecUomCode() {
		return secUomCode;
	}

	public void setSecUomCode(String secUomCode) {
		this.secUomCode = secUomCode;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<TaxCodes> getTaxCodes() {
		return taxCodes;
	}

	public void setTaxCodes(List<TaxCodes> taxCodes) {
		this.taxCodes = taxCodes;
	}
}