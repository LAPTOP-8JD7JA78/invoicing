package com.smartech.invoicingprod.integration.dto;

import java.util.List;

public class WarrantyDataDTO {
	
	//Datos generales de la factura
	public String invoiceNumber;
	public String customerClass;
	public String salesOrder;
	public String salesOrderType;
	public String invoiceSerial;
	public String invoiceDate;
	public String branchNumber;
	//Datos del distribuidor o sucursal
	public boolean isDistribuitor;
	public String name1;
	public String name2;
	public String name3;
	public String address;
	public String internalNumber;
	public String outdoorNumber;
	public String referenceAddress;
	public String colony;
	public String location;
	public String population;
	public String state;
	public String country;
	public String zip;
	public String telephoneNumber;
	public String email;		
	public List<WarrantyDataLinesDTO> linesWarranty;
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public String getCustomerClass() {
		return customerClass;
	}
	public void setCustomerClass(String customerClass) {
		this.customerClass = customerClass;
	}
	public String getSalesOrder() {
		return salesOrder;
	}
	public void setSalesOrder(String salesOrder) {
		this.salesOrder = salesOrder;
	}
	public String getSalesOrderType() {
		return salesOrderType;
	}
	public void setSalesOrderType(String salesOrderType) {
		this.salesOrderType = salesOrderType;
	}
	public String getInvoiceSerial() {
		return invoiceSerial;
	}
	public void setInvoiceSerial(String invoiceSerial) {
		this.invoiceSerial = invoiceSerial;
	}
	public String getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public String getBranchNumber() {
		return branchNumber;
	}
	public void setBranchNumber(String branchNumber) {
		this.branchNumber = branchNumber;
	}
	public boolean isDistribuitor() {
		return isDistribuitor;
	}
	public void setDistribuitor(boolean isDistribuitor) {
		this.isDistribuitor = isDistribuitor;
	}
	public String getName1() {
		return name1;
	}
	public void setName1(String name1) {
		this.name1 = name1;
	}
	public String getName2() {
		return name2;
	}
	public void setName2(String name2) {
		this.name2 = name2;
	}
	public String getName3() {
		return name3;
	}
	public void setName3(String name3) {
		this.name3 = name3;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getInternalNumber() {
		return internalNumber;
	}
	public void setInternalNumber(String internalNumber) {
		this.internalNumber = internalNumber;
	}
	public String getOutdoorNumber() {
		return outdoorNumber;
	}
	public void setOutdoorNumber(String outdoorNumber) {
		this.outdoorNumber = outdoorNumber;
	}
	public String getReferenceAddress() {
		return referenceAddress;
	}
	public void setReferenceAddress(String referenceAddress) {
		this.referenceAddress = referenceAddress;
	}
	public String getColony() {
		return colony;
	}
	public void setColony(String colony) {
		this.colony = colony;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getPopulation() {
		return population;
	}
	public void setPopulation(String population) {
		this.population = population;
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
	public String getTelephoneNumber() {
		return telephoneNumber;
	}
	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<WarrantyDataLinesDTO> getLinesWarranty() {
		return linesWarranty;
	}
	public void setLinesWarranty(List<WarrantyDataLinesDTO> linesWarranty) {
		this.linesWarranty = linesWarranty;
	}
	
}
