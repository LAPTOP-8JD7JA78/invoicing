package com.smartech.invoicingprod.dto;

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
	private String receivables;
	private String salesOrderNumber;
	
	private String susticionCFDI;
	private String pedidoLiverpool;
	private String contraRecibo;
	private Date fechaContraRecibo;
	private String customerName;
	private String customerTaxIden;
	private String customerEmail;
	private String CustomerPONumber;
	private String customerZip;
	private String customerAddress;
	private String certificadoOrigen;
	private String valorCerOrigen;
	private boolean existCombo = false;
	
	private String discountValueText;
	private String salesPerson;
	private String salesOrderType;
	private String cancelationReason;
	private String catExportacion;
	private String regimenFiscal;
	private String relationTypeCFDI;
	//Requerimiento: Direccion Alterna
	private String da_customer;
	private String da_address;
	private String da_statecode;
	private String da_citycode;
	private String da_colony;
	private String da_zipcode;
	private String da_contact;
	private String da_email;
	private String da_telephone;
	private String da_shippingmethod;
	
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

	public String getDiscountValueText() {
		return discountValueText;
	}

	public void setDiscountValueText(String discountValueText) {
		this.discountValueText = discountValueText;
	}

	public String getReceivables() {
		return receivables;
	}

	public void setReceivables(String receivables) {
		this.receivables = receivables;
	}

	public String getSalesOrderNumber() {
		return salesOrderNumber;
	}

	public void setSalesOrderNumber(String salesOrderNumber) {
		this.salesOrderNumber = salesOrderNumber;
	}

	public String getSalesPerson() {
		return salesPerson;
	}

	public void setSalesPerson(String salesPerson) {
		this.salesPerson = salesPerson;
	}

	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	public String getCertificadoOrigen() {
		return certificadoOrigen;
	}

	public void setCertificadoOrigen(String certificadoOrigen) {
		this.certificadoOrigen = certificadoOrigen;
	}

	public String getValorCerOrigen() {
		return valorCerOrigen;
	}

	public void setValorCerOrigen(String valorCerOrigen) {
		this.valorCerOrigen = valorCerOrigen;
	}

	public boolean isExistCombo() {
		return existCombo;
	}

	public void setExistCombo(boolean existCombo) {
		this.existCombo = existCombo;
	}

	public String getSalesOrderType() {
		return salesOrderType;
	}

	public void setSalesOrderType(String salesOrderType) {
		this.salesOrderType = salesOrderType;
	}

	public String getCancelationReason() {
		return cancelationReason;
	}

	public void setCancelationReason(String cancelationReason) {
		this.cancelationReason = cancelationReason;
	}

	public String getCatExportacion() {
		return catExportacion;
	}

	public void setCatExportacion(String catExportacion) {
		this.catExportacion = catExportacion;
	}

	public String getRegimenFiscal() {
		return regimenFiscal;
	}

	public void setRegimenFiscal(String regimenFiscal) {
		this.regimenFiscal = regimenFiscal;
	}

	public String getRelationTypeCFDI() {
		return relationTypeCFDI;
	}

	public void setRelationTypeCFDI(String relationTypeCFDI) {
		this.relationTypeCFDI = relationTypeCFDI;
	}
	//Requerimiento: Direccion Alterna

	public String getDa_customer() {
		return da_customer;
	}

	public void setDa_customer(String da_customer) {
		this.da_customer = da_customer;
	}

	public String getDa_address() {
		return da_address;
	}

	public void setDa_address(String da_address) {
		this.da_address = da_address;
	}

	public String getDa_statecode() {
		return da_statecode;
	}

	public void setDa_statecode(String da_statecode) {
		this.da_statecode = da_statecode;
	}

	public String getDa_citycode() {
		return da_citycode;
	}

	public void setDa_citycode(String da_citycode) {
		this.da_citycode = da_citycode;
	}

	public String getDa_colony() {
		return da_colony;
	}

	public void setDa_colony(String da_colony) {
		this.da_colony = da_colony;
	}

	public String getDa_zipcode() {
		return da_zipcode;
	}

	public void setDa_zipcode(String da_zipcode) {
		this.da_zipcode = da_zipcode;
	}

	public String getDa_contact() {
		return da_contact;
	}

	public void setDa_contact(String da_contact) {
		this.da_contact = da_contact;
	}

	public String getDa_email() {
		return da_email;
	}

	public void setDa_email(String da_email) {
		this.da_email = da_email;
	}

	public String getDa_telephone() {
		return da_telephone;
	}

	public void setDa_telephone(String da_telephone) {
		this.da_telephone = da_telephone;
	}

	public String getDa_shippingmethod() {
		return da_shippingmethod;
	}

	public void setDa_shippingmethod(String da_shippingmethod) {
		this.da_shippingmethod = da_shippingmethod;
	}
	
}
