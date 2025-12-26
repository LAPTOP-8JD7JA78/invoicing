package com.smartech.invoicingprod.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity(name = "Branch")
@Table(name = "branch")
public class Branch implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
	
	@OneToOne
	private Company company;
	
	@Column(name = "name", nullable=true)
	private String name;
	
	@Column(name = "code", nullable=true)
	private String code;
	
	@Column(name = "enabled", nullable=true)
	private boolean enabled;
	
	@Column(name = "invorganizationid", nullable=true)
	private String invOrganizationId;
	
	@Column(name = "invorganizationcode", nullable=true)
	private String invOrganizationCode;
	
	@Column(name = "type", nullable=true)
	private String type;
	
	@Column(name = "address", nullable=true)
    private String address;
	
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
	
	@Column(name = "cellPhoneNumber", nullable=true)
    private String cellPhoneNumber;
	
	@Column(name = "zipAddressPdf", nullable=true)
    private String zipAddressPdf;
	
	@Column(name = "taxRegime", nullable=true)
    private String taxRegime;
	
	@Column(name = "nameTransfer", nullable=true)
	private String nameTransfer;
	
	@Column(name = "addressTransfer", nullable=true)
    private String addressTransfer;
	
	@Column(name = "colonyTransfer", nullable=true)
    private String colonyTransfer;
	
	@Column(name = "cityTransfer", nullable=true)
    private String cityTransfer;
	
	@Column(name = "stateTransfer", nullable=true)
    private String stateTransfer;
	
	@Column(name = "countryTransfer", nullable=true)
    private String countryTransfer;
	
	@Column(name = "zipTransfer", nullable=true)
    private String zipTransfer;
	
	@Column(name = "cellPhoneNumberTransfer", nullable=true)
    private String cellPhoneNumberTransfer;
	
	@Column(name = "zipAddressPdfTransfer", nullable=true)
    private String zipAddressPdfTransfer;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getInvOrganizationId() {
		return invOrganizationId;
	}

	public void setInvOrganizationId(String invOrganizationId) {
		this.invOrganizationId = invOrganizationId;
	}

	public String getInvOrganizationCode() {
		return invOrganizationCode;
	}

	public void setInvOrganizationCode(String invOrganizationCode) {
		this.invOrganizationCode = invOrganizationCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getCellPhoneNumber() {
		return cellPhoneNumber;
	}

	public void setCellPhoneNumber(String cellPhoneNumber) {
		this.cellPhoneNumber = cellPhoneNumber;
	}

	public String getZipAddressPdf() {
		return zipAddressPdf;
	}

	public void setZipAddressPdf(String zipAddressPdf) {
		this.zipAddressPdf = zipAddressPdf;
	}

	public String getTaxRegime() {
		return taxRegime;
	}

	public void setTaxRegime(String taxRegime) {
		this.taxRegime = taxRegime;
	}

	public String getNameTransfer() {
		return nameTransfer;
	}

	public void setNameTransfer(String nameTransfer) {
		this.nameTransfer = nameTransfer;
	}

	public String getAddressTransfer() {
		return addressTransfer;
	}

	public void setAddressTransfer(String addressTransfer) {
		this.addressTransfer = addressTransfer;
	}

	public String getColonyTransfer() {
		return colonyTransfer;
	}

	public void setColonyTransfer(String colonyTransfer) {
		this.colonyTransfer = colonyTransfer;
	}

	public String getCityTransfer() {
		return cityTransfer;
	}

	public void setCityTransfer(String cityTransfer) {
		this.cityTransfer = cityTransfer;
	}

	public String getStateTransfer() {
		return stateTransfer;
	}

	public void setStateTransfer(String stateTransfer) {
		this.stateTransfer = stateTransfer;
	}

	public String getCountryTransfer() {
		return countryTransfer;
	}

	public void setCountryTransfer(String countryTransfer) {
		this.countryTransfer = countryTransfer;
	}

	public String getZipTransfer() {
		return zipTransfer;
	}

	public void setZipTransfer(String zipTransfer) {
		this.zipTransfer = zipTransfer;
	}

	public String getCellPhoneNumberTransfer() {
		return cellPhoneNumberTransfer;
	}

	public void setCellPhoneNumberTransfer(String cellPhoneNumberTransfer) {
		this.cellPhoneNumberTransfer = cellPhoneNumberTransfer;
	}

	public String getZipAddressPdfTransfer() {
		return zipAddressPdfTransfer;
	}

	public void setZipAddressPdfTransfer(String zipAddressPdfTransfer) {
		this.zipAddressPdfTransfer = zipAddressPdfTransfer;
	}
		
}