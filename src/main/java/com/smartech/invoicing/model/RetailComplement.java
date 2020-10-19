package com.smartech.invoicing.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity(name="RetailComplement")
@Table(name="retailComplement")
public class RetailComplement implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
	
	@Column(name = "documentStatus", nullable = true)
    private String documentStatus;
	
	@Column(name = "transactionType", nullable = true)
    private String transactionType;
	
	@Column(name = "instructionCode", nullable = true)
    private String instructionCode;
	
	@Column(name = "textNote", nullable = true)
    private String textNote;
	
	@Column(name = "referenceId", nullable = true)
    private String referenceId;
	
	@Column(name = "referenceDate", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
    private Date referenceDate;
	
	@Column(name = "adicionalInformation", nullable = true)
    private String adicionalInformation;
	
	@Column(name = "adicionalInformationNumber", nullable = true)
    private String adicionalInformationNumber;
	
	@Column(name = "adicionalInformationId", nullable = true)
    private String adicionalInformationId;
	
	@Column(name = "deliveryNote", nullable = true)
    private String deliveryNote;
	
	@Column(name = "buyerNumberFolio", nullable = true)
    private String buyerNumberFolio;
	
	@Column(name = "buyerDateFolio", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
    private Date buyerDateFolio;
	
	@Column(name = "globalLocationNumberBuyer", nullable = true)
    private String globalLocationNumberBuyer;
	
	@Column(name = "purchasingContact", nullable = true)
    private String purchasingContact;
	
	@Column(name = "seller", nullable = true)
    private String seller;
	
	@Column(name = "globalLocationNumberProvider", nullable = true)
    private String globalLocationNumberProvider;
	
	@Column(name = "AlternativeId", nullable = true)
    private String AlternativeId;
	
	@Column(name = "identificationType", nullable = true)
    private String identificationType;
	
	@Column(name = "elementOnline", nullable = true)
    private String elementOnline;
	
	@Column(name = "type", nullable = true)
    private String type;
	
	@Column(name = "number", nullable = true)
    private String number;
	
	@Column(name = "gTin", nullable = true)
    private String gTin;
	
	@Column(name = "inovicedQuantity", nullable = true)
    private String inovicedQuantity;
	
	@Column(name = "uomCode", nullable = true)
    private String uomCode;
	
	@Column(name = "priceTotal", nullable = true)
    private String priceTotal;
	
	@Column(name = "total", nullable = true)
    private String total;

	public String getDocumentStatus() {
		return documentStatus;
	}

	public void setDocumentStatus(String documentStatus) {
		this.documentStatus = documentStatus;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getInstructionCode() {
		return instructionCode;
	}

	public void setInstructionCode(String instructionCode) {
		this.instructionCode = instructionCode;
	}

	public String getTextNote() {
		return textNote;
	}

	public void setTextNote(String textNote) {
		this.textNote = textNote;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public Date getReferenceDate() {
		return referenceDate;
	}

	public void setReferenceDate(Date referenceDate) {
		this.referenceDate = referenceDate;
	}

	public String getAdicionalInformation() {
		return adicionalInformation;
	}

	public void setAdicionalInformation(String adicionalInformation) {
		this.adicionalInformation = adicionalInformation;
	}

	public String getAdicionalInformationNumber() {
		return adicionalInformationNumber;
	}

	public void setAdicionalInformationNumber(String adicionalInformationNumber) {
		this.adicionalInformationNumber = adicionalInformationNumber;
	}

	public String getAdicionalInformationId() {
		return adicionalInformationId;
	}

	public void setAdicionalInformationId(String adicionalInformationId) {
		this.adicionalInformationId = adicionalInformationId;
	}

	public String getDeliveryNote() {
		return deliveryNote;
	}

	public void setDeliveryNote(String deliveryNote) {
		this.deliveryNote = deliveryNote;
	}

	public String getBuyerNumberFolio() {
		return buyerNumberFolio;
	}

	public void setBuyerNumberFolio(String buyerNumberFolio) {
		this.buyerNumberFolio = buyerNumberFolio;
	}

	public Date getBuyerDateFolio() {
		return buyerDateFolio;
	}

	public void setBuyerDateFolio(Date buyerDateFolio) {
		this.buyerDateFolio = buyerDateFolio;
	}

	public String getGlobalLocationNumberBuyer() {
		return globalLocationNumberBuyer;
	}

	public void setGlobalLocationNumberBuyer(String globalLocationNumberBuyer) {
		this.globalLocationNumberBuyer = globalLocationNumberBuyer;
	}

	public String getPurchasingContact() {
		return purchasingContact;
	}

	public void setPurchasingContact(String purchasingContact) {
		this.purchasingContact = purchasingContact;
	}

	public String getSeller() {
		return seller;
	}

	public void setSeller(String seller) {
		this.seller = seller;
	}

	public String getGlobalLocationNumberProvider() {
		return globalLocationNumberProvider;
	}

	public void setGlobalLocationNumberProvider(String globalLocationNumberProvider) {
		this.globalLocationNumberProvider = globalLocationNumberProvider;
	}

	public String getAlternativeId() {
		return AlternativeId;
	}

	public void setAlternativeId(String alternativeId) {
		AlternativeId = alternativeId;
	}

	public String getIdentificationType() {
		return identificationType;
	}

	public void setIdentificationType(String identificationType) {
		this.identificationType = identificationType;
	}

	public String getElementOnline() {
		return elementOnline;
	}

	public void setElementOnline(String elementOnline) {
		this.elementOnline = elementOnline;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getgTin() {
		return gTin;
	}

	public void setgTin(String gTin) {
		this.gTin = gTin;
	}

	public String getInovicedQuantity() {
		return inovicedQuantity;
	}

	public void setInovicedQuantity(String inovicedQuantity) {
		this.inovicedQuantity = inovicedQuantity;
	}

	public String getUomCode() {
		return uomCode;
	}

	public void setUomCode(String uomCode) {
		this.uomCode = uomCode;
	}

	public String getPriceTotal() {
		return priceTotal;
	}

	public void setPriceTotal(String priceTotal) {
		this.priceTotal = priceTotal;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}
	
}
