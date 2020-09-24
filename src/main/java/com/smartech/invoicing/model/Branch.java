package com.smartech.invoicing.model;

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
	
	
}