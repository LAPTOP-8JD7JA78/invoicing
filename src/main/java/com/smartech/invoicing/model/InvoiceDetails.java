package com.smartech.invoicing.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

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
	
	@Column(name = "totalDiscount", nullable=true)
	private double totalDiscount;
	
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
	
	@Column(name = "lineType", nullable=true)
	private String lineType;
	
	@Column(name = "itemSerial", nullable=true)
	private String itemSerial;
	
	@Column(name = "itemLot", nullable=true)
	private String itemLot;
	
	@Column(name = "transactionLineNumber", nullable=true)
	private String transactionLineNumber;
	
	@Lob
	@Column(name = "addtionalDescription", nullable=true)
	private String addtionalDescription;
	
	@ManyToMany(fetch=FetchType.EAGER)
	private Set<TaxCodes> taxCodes;
	
	@Column(name = "fraccionArancelaria", nullable=true)
	private String fraccionArancelaria;
	
	@Column(name = "incotermKey", nullable=true)
	private String incotermKey;
	
	@Column(name = "itemModel", nullable=true)
	private String itemModel;
	
	@Column(name = "itemBrand", nullable=true)
	private String itemBrand;
	
	@Column(name = "isImport", nullable=true)
	private boolean isImport;
	
	@Column(name = "customskey", nullable=true)
	private String customskey;
	
	@Column(name = "datePetition", nullable = true)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private String datePetition;
	
	@Column(name = "numberPetiton", nullable=true)
	private String numberPetiton;
	
	@Column(name = "itemUomCustoms", nullable=true)
	private String itemUomCustoms;
	
	@Column(name = "itemNotes", nullable=true)
	private String itemNotes;
	
	@OneToOne(cascade=CascadeType.ALL)
	RetailComplement retailComplements;

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

	public double getTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(double totalDiscount) {
		this.totalDiscount = totalDiscount;
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

	public String getLineType() {
		return lineType;
	}

	public void setLineType(String lineType) {
		this.lineType = lineType;
	}

	public String getItemSerial() {
		return itemSerial;
	}

	public void setItemSerial(String itemSerial) {
		this.itemSerial = itemSerial;
	}

	public String getItemLot() {
		return itemLot;
	}

	public void setItemLot(String itemLot) {
		this.itemLot = itemLot;
	}

	public String getTransactionLineNumber() {
		return transactionLineNumber;
	}

	public void setTransactionLineNumber(String transactionLineNumber) {
		this.transactionLineNumber = transactionLineNumber;
	}

	public Set<TaxCodes> getTaxCodes() {
		return taxCodes;
	}

	public void setTaxCodes(Set<TaxCodes> taxCodes) {
		this.taxCodes = taxCodes;
	}

	public String getAddtionalDescription() {
		return addtionalDescription;
	}

	public void setAddtionalDescription(String addtionalDescription) {
		this.addtionalDescription = addtionalDescription;
	}

	public String getFraccionArancelaria() {
		return fraccionArancelaria;
	}

	public void setFraccionArancelaria(String fraccionArancelaria) {
		this.fraccionArancelaria = fraccionArancelaria;
	}

	public String getIncotermKey() {
		return incotermKey;
	}

	public void setIncotermKey(String incotermKey) {
		this.incotermKey = incotermKey;
	}

	public String getItemModel() {
		return itemModel;
	}

	public void setItemModel(String itemModel) {
		this.itemModel = itemModel;
	}

	public String getItemBrand() {
		return itemBrand;
	}

	public void setItemBrand(String itemBrand) {
		this.itemBrand = itemBrand;
	}

	public boolean isImport() {
		return isImport;
	}

	public void setImport(boolean isImport) {
		this.isImport = isImport;
	}

	public String getCustomskey() {
		return customskey;
	}

	public void setCustomskey(String customskey) {
		this.customskey = customskey;
	}

	public String getDatePetition() {
		return datePetition;
	}

	public void setDatePetition(String datePetition) {
		this.datePetition = datePetition;
	}

	public String getNumberPetiton() {
		return numberPetiton;
	}

	public void setNumberPetiton(String numberPetiton) {
		this.numberPetiton = numberPetiton;
	}

	public String getItemUomCustoms() {
		return itemUomCustoms;
	}

	public void setItemUomCustoms(String itemUomCustoms) {
		this.itemUomCustoms = itemUomCustoms;
	}

	public String getItemNotes() {
		return itemNotes;
	}

	public void setItemNotes(String itemNotes) {
		this.itemNotes = itemNotes;
	}

	public RetailComplement getRetailComplements() {
		return retailComplements;
	}

	public void setRetailComplements(RetailComplement retailComplements) {
		this.retailComplements = retailComplements;
	}
	
}