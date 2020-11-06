package com.smartech.invoicing.dto;

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
	
}
