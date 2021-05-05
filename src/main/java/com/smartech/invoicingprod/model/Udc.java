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

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity(name = "Udc")
@Table(name = "udc")
public class Udc implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(name = "udcSystem", nullable = false, length = 20)
	private String udcSystem;
	
	@Column(name = "udcKey", nullable = false, length = 50)
	private String udcKey;
	
	@Column(name = "strValue1", nullable = true, length = 100)
	private String strValue1;
	
	@Column(name = "strValue2", nullable = true, length = 100)
	private String strValue2;
	
	@Column(name = "intValue", nullable = true, length = 20)
	private int intValue;
	
	@Column(name = "note", nullable = true, length = 250)
	private String note;
	
	@Column(name = "booleanValue", nullable = true, columnDefinition = "TINYINT", length = 1)
	private boolean booleanValue;
	
	@Column(name = "description", nullable = true, length = 250)
	private String description;

	@Column(name = "dateValue", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateValue;
	
	@Column(name = "systemRef", nullable = true, length = 50)
	private String systemRef;
	
	@Column(name = "KEYREF", nullable = true, length = 100)
	private String keyRef;
	
	@Column(name = "createdBy", nullable = true, length = 20)
	private String createdBy;
	
	@Column(name = "creationDate", nullable = true)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date creationDate;
	
	@Column(name = "updatedBy", nullable = true, length = 20)
	private String updatedBy;
	
	@Column(name = "updatedDate", nullable = true)
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date updatedDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUdcSystem() {
		return udcSystem;
	}

	public void setUdcSystem(String udcSystem) {
		this.udcSystem = udcSystem;
	}

	public String getUdcKey() {
		return udcKey;
	}

	public void setUdcKey(String udcKey) {
		this.udcKey = udcKey;
	}

	public String getStrValue1() {
		return strValue1;
	}

	public void setStrValue1(String strValue1) {
		this.strValue1 = strValue1;
	}

	public String getStrValue2() {
		return strValue2;
	}

	public void setStrValue2(String strValue2) {
		this.strValue2 = strValue2;
	}

	public int getIntValue() {
		return intValue;
	}

	public void setIntValue(int intValue) {
		this.intValue = intValue;
	}

	public String getNote() {
		return note;
	}	

	public void setNote(String note) {
		this.note = note;
	}

	public boolean isBooleanValue() {
		return booleanValue;
	}

	public void setBooleanValue(boolean booleanValue) {
		this.booleanValue = booleanValue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDateValue() {
		return dateValue;
	}

	public void setDateValue(Date dateValue) {
		this.dateValue = dateValue;
	}

	public String getSystemRef() {
		return systemRef;
	}

	public void setSystemRef(String systemRef) {
		this.systemRef = systemRef;
	}

	public String getKeyRef() {
		return keyRef;
	}

	public void setKeyRef(String keyRef) {
		this.keyRef = keyRef;
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
	
}
