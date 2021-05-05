package com.smartech.invoicing.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "FixedAsset")
@Table(name = "fixedAsset")
public class FixedAsset implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
	
	@Column(name = "assetNumber", nullable=true)
    private String assetNumber;
	
	@Column(name = "assetDescription", nullable=true)
    private String assetDescription;
	
	@Column(name = "serialNumber", nullable=true)
    private String serialNumber;
	
	@Column(name = "assetModel", nullable=true)
    private String assetModel;
	
	@Column(name = "assetQuantity", nullable=true)
    private String assetQuantity;
	
	@Column(name = "personAssigned", nullable=true)
    private String personAssigned;
	
	@Column(name = "personNumber", nullable=true)
    private String personNumber;
	
	@Column(name = "assetCode", nullable=true)
    private String assetCode;
	
	@Column(name = "bussinesUnit", nullable=true)
    private String bussinesUnit;
	
	@Column(name = "creationDate", nullable=true)
    private String creationDate;
	
	@Column(name = "updateDate", nullable=true)
    private String updateDate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAssetNumber() {
		return assetNumber;
	}

	public void setAssetNumber(String assetNumber) {
		this.assetNumber = assetNumber;
	}

	public String getAssetDescription() {
		return assetDescription;
	}

	public void setAssetDescription(String assetDescription) {
		this.assetDescription = assetDescription;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getAssetModel() {
		return assetModel;
	}

	public void setAssetModel(String assetModel) {
		this.assetModel = assetModel;
	}

	public String getAssetQuantity() {
		return assetQuantity;
	}

	public void setAssetQuantity(String assetQuantity) {
		this.assetQuantity = assetQuantity;
	}

	public String getPersonAssigned() {
		return personAssigned;
	}

	public void setPersonAssigned(String personAssigned) {
		this.personAssigned = personAssigned;
	}

	public String getBussinesUnit() {
		return bussinesUnit;
	}

	public void setBussinesUnit(String bussinesUnit) {
		this.bussinesUnit = bussinesUnit;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getAssetCode() {
		return assetCode;
	}

	public void setAssetCode(String assetCode) {
		this.assetCode = assetCode;
	}

	public String getPersonNumber() {
		return personNumber;
	}

	public void setPersonNumber(String personNumber) {
		this.personNumber = personNumber;
	}
	
}
