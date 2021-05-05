package com.smartech.invoicingprod.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity(name = "ErrorLog")
@Table(name = "errorLog")
public class ErrorLog implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
	
	@Lob
	@Column(name = "errorMsg", nullable=true)
    private String errorMsg;
	
	@Column(name = "orderNumber", nullable=true)
    private String orderNumber;
	
	@Column(name = "invoiceType", nullable=true)
    private String invoiceType;
	
	@Column(name = "creationDate", nullable=true)
    private String creationDate;
	
	@Column(name = "updateDate", nullable=true)
    private String updateDate;

	@Column(name = "isNew", nullable=true)
    private boolean isNew;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
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

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}
	
}
