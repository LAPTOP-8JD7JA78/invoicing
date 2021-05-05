package com.smartech.invoicing.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "Company")
@Table(name = "company")
public class Company implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
	
	@Column(name = "enabled", nullable=true)
    private String enabled;
	
	@Column(name = "businessUnitName", nullable=true)
    private String businessUnitName;
	
	@Column(name = "name", nullable=false)
    private String name;
	
	@Column(name = "address", nullable=true)
    private String address;
	
	@Column(name = "taxIdentifier", nullable=true)
    private String taxIdentifier;
	
	@Column(name = "colony", nullable=true)
    private String colony;
	
	@Column(name = "city", nullable=true)
    private String city;
	
	@Column(name = "state", nullable=true)
    private String state;
	
	@Column(name = "country", nullable=true)
    private String country;
	
	@Column(name = "zip", nullable=true)
    private String zip;
	
	@Column(name = "email", nullable=true)
    private String email;
	
	@Column(name = "telephoneNumber", nullable=true)
    private String telephoneNumber;
	
	@Column(name = "taxRegime", nullable=true)
    //UDC´s taxRegime
    private String taxRegime;

	@Column(name = "assignedCertificate", nullable=true)
    private String assignedCertificate;
	
	@Column(name = "assignedKey", nullable=true)
    private String assignedKey;
	
	@Column(name = "csdPassCode", nullable=true)
    private String csdPassCode;
    
	@Column(name = "isFusionCloud", nullable=true)
    private boolean isFusionCloud;
	
	@Column(name = "globalLocationNumberProvider", nullable=true)
    private String globalLocationNumberProvider;
	
	@Column(name = "AlternativeId", nullable=true)
    private String AlternativeId;
	
	@Column(name = "assetBook", nullable=true)
    private String assetBook;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public String getBusinessUnitName() {
		return businessUnitName;
	}

	public void setBusinessUnitName(String businessUnitName) {
		this.businessUnitName = businessUnitName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTaxIdentifier() {
		return taxIdentifier;
	}

	public void setTaxIdentifier(String taxIdentifier) {
		this.taxIdentifier = taxIdentifier;
	}

	public String getColony() {
		return colony;
	}

	public void setColony(String colony) {
		this.colony = colony;
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

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	public String getTaxRegime() {
		return taxRegime;
	}

	public void setTaxRegime(String taxRegime) {
		this.taxRegime = taxRegime;
	}

	public String getAssignedCertificate() {
		return assignedCertificate;
	}

	public void setAssignedCertificate(String assignedCertificate) {
		this.assignedCertificate = assignedCertificate;
	}

	public String getAssignedKey() {
		return assignedKey;
	}

	public void setAssignedKey(String assignedKey) {
		this.assignedKey = assignedKey;
	}

	public String getCsdPassCode() {
		return csdPassCode;
	}

	public void setCsdPassCode(String csdPassCode) {
		this.csdPassCode = csdPassCode;
	}

	public boolean isFusionCloud() {
		return isFusionCloud;
	}

	public void setFusionCloud(boolean isFusionCloud) {
		this.isFusionCloud = isFusionCloud;
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

	public String getAssetBook() {
		return assetBook;
	}

	public void setAssetBook(String assetBook) {
		this.assetBook = assetBook;
	}
	
}
