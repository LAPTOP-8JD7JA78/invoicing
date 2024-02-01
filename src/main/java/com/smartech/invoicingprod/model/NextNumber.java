package com.smartech.invoicingprod.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity(name="NextNumber")
@Table(name="nextnumber")
public class NextNumber implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
	
	@Column(name = "OrderType", nullable = true)
    private String OrderType;
	
	@Column(name = "DocumentType", nullable = true)
    private String DocumentType;
	
	@OneToOne
    private Company company;
	
	@OneToOne
    private Branch branch;
	
	@Column(name = "Serie", nullable = true)
    private String Serie;
	
	@Column(name = "Folio", nullable = true)
    private int Folio;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOrderType() {
		return OrderType;
	}

	public void setOrderType(String orderType) {
		OrderType = orderType;
	}

	public String getDocumentType() {
		return DocumentType;
	}

	public void setDocumentType(String documentType) {
		DocumentType = documentType;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public String getSerie() {
		return Serie;
	}

	public void setSerie(String serie) {
		Serie = serie;
	}

	public int getFolio() {
		return Folio;
	}

	public void setFolio(int folio) {
		Folio = folio;
	}

}
