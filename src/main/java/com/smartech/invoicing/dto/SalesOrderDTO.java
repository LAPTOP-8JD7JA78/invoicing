package com.smartech.invoicing.dto;

import java.util.Date;
import java.util.List;

public class SalesOrderDTO {

	private String orderNumber;
	private String sourceTransactionNumber;
	private String SourceTransactionSystem;
	private String businessUnitName;
	private String businessUnitId;
	private String headerId;
	private String requestedFulfillmentOrganizationCode;
	private String requestedFulfillmentOrganizationId;
	private String transactionalCurrencyCode;
	private String currencyConversionRate;
	private String currencyConversionType;
	private String usoCFDI;
	private String metodoPago;
	private String formaPago;
	private String orderType;
	
	private String susticionCFDI;
	private String pedidoLiverpool;
	private String contraRecibo;
	private Date fechaContraRecibo;
	private String customerName;
	private String customerTaxIden;
	private String customerEmail;
	private String CustomerPONumber;
	private String customerZip;
	
	private List<SalesOrderLinesDTO> lines;

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getSourceTransactionNumber() {
		return sourceTransactionNumber;
	}

	public void setSourceTransactionNumber(String sourceTransactionNumber) {
		this.sourceTransactionNumber = sourceTransactionNumber;
	}

	public String getSourceTransactionSystem() {
		return SourceTransactionSystem;
	}

	public void setSourceTransactionSystem(String sourceTransactionSystem) {
		SourceTransactionSystem = sourceTransactionSystem;
	}

	public String getBusinessUnitName() {
		return businessUnitName;
	}

	public void setBusinessUnitName(String businessUnitName) {
		this.businessUnitName = businessUnitName;
	}

	public String getBusinessUnitId() {
		return businessUnitId;
	}

	public void setBusinessUnitId(String businessUnitId) {
		this.businessUnitId = businessUnitId;
	}

	public String getHeaderId() {
		return headerId;
	}

	public void setHeaderId(String headerId) {
		this.headerId = headerId;
	}

	public String getRequestedFulfillmentOrganizationCode() {
		return requestedFulfillmentOrganizationCode;
	}

	public void setRequestedFulfillmentOrganizationCode(String requestedFulfillmentOrganizationCode) {
		this.requestedFulfillmentOrganizationCode = requestedFulfillmentOrganizationCode;
	}

	public String getRequestedFulfillmentOrganizationId() {
		return requestedFulfillmentOrganizationId;
	}

	public void setRequestedFulfillmentOrganizationId(String requestedFulfillmentOrganizationId) {
		this.requestedFulfillmentOrganizationId = requestedFulfillmentOrganizationId;
	}

	public String getTransactionalCurrencyCode() {
		return transactionalCurrencyCode;
	}

	public void setTransactionalCurrencyCode(String transactionalCurrencyCode) {
		this.transactionalCurrencyCode = transactionalCurrencyCode;
	}

	public String getCurrencyConversionRate() {
		return currencyConversionRate;
	}

	public void setCurrencyConversionRate(String currencyConversionRate) {
		this.currencyConversionRate = currencyConversionRate;
	}

	public String getCurrencyConversionType() {
		return currencyConversionType;
	}

	public void setCurrencyConversionType(String currencyConversionType) {
		this.currencyConversionType = currencyConversionType;
	}

	public List<SalesOrderLinesDTO> getLines() {
		return lines;
	}

	public void setLines(List<SalesOrderLinesDTO> lines) {
		this.lines = lines;
	}

	public String getUsoCFDI() {
		return usoCFDI;
	}

	public void setUsoCFDI(String usoCFDI) {
		this.usoCFDI = usoCFDI;
	}

	public String getMetodoPago() {
		return metodoPago;
	}

	public void setMetodoPago(String metodoPago) {
		this.metodoPago = metodoPago;
	}

	public String getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getSusticionCFDI() {
		return susticionCFDI;
	}

	public void setSusticionCFDI(String susticionCFDI) {
		this.susticionCFDI = susticionCFDI;
	}

	public String getPedidoLiverpool() {
		return pedidoLiverpool;
	}

	public void setPedidoLiverpool(String pedidoLiverpool) {
		this.pedidoLiverpool = pedidoLiverpool;
	}

	public String getContraRecibo() {
		return contraRecibo;
	}

	public void setContraRecibo(String contraRecibo) {
		this.contraRecibo = contraRecibo;
	}

	public Date getFechaContraRecibo() {
		return fechaContraRecibo;
	}

	public void setFechaContraRecibo(Date fechaContraRecibo) {
		this.fechaContraRecibo = fechaContraRecibo;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerTaxIden() {
		return customerTaxIden;
	}

	public void setCustomerTaxIden(String customerTaxIden) {
		this.customerTaxIden = customerTaxIden;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public String getCustomerPONumber() {
		return CustomerPONumber;
	}

	public void setCustomerPONumber(String customerPONumber) {
		CustomerPONumber = customerPONumber;
	}

	public String getCustomerZip() {
		return customerZip;
	}

	public void setCustomerZip(String customerZip) {
		this.customerZip = customerZip;
	}
	
}
