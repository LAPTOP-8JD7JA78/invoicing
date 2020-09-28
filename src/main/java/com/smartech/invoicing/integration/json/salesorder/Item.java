
package com.smartech.invoicing.integration.json.salesorder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "HeaderId",
    "OrderNumber",
    "SourceTransactionNumber",
    "BusinessUnitId",
    "BusinessUnitName",
    "RequestedFulfillmentOrganizationId",
    "RequestedFulfillmentOrganizationCode",
    "PaymentTermsCode",
    "PaymentTerms",
    "TransactionalCurrencyCode",
    "TransactionalCurrencyName",
    "CurrencyConversionRate",
    "CurrencyConversionType",
    "StatusCode",
    "lines",
    "totals"
})
public class Item {

    @JsonProperty("HeaderId")
    private Long headerId;
    @JsonProperty("OrderNumber")
    private String orderNumber;
    @JsonProperty("SourceTransactionNumber")
    private String sourceTransactionNumber;
    @JsonProperty("BusinessUnitId")
    private Long businessUnitId;
    @JsonProperty("BusinessUnitName")
    private String businessUnitName;
    @JsonProperty("RequestedFulfillmentOrganizationId")
    private Long requestedFulfillmentOrganizationId;
    @JsonProperty("RequestedFulfillmentOrganizationCode")
    private String requestedFulfillmentOrganizationCode;
    @JsonProperty("PaymentTermsCode")
    private Long paymentTermsCode;
    @JsonProperty("PaymentTerms")
    private String paymentTerms;
    @JsonProperty("TransactionalCurrencyCode")
    private String transactionalCurrencyCode;
    @JsonProperty("TransactionalCurrencyName")
    private String transactionalCurrencyName;
    @JsonProperty("CurrencyConversionRate")
    private Object currencyConversionRate;
    @JsonProperty("CurrencyConversionType")
    private Object currencyConversionType;
    @JsonProperty("StatusCode")
    private String statusCode;
    @JsonProperty("lines")
    private List<Line> lines = null;
    @JsonProperty("totals")
    private List<Total> totals = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("HeaderId")
    public Long getHeaderId() {
        return headerId;
    }

    @JsonProperty("HeaderId")
    public void setHeaderId(Long headerId) {
        this.headerId = headerId;
    }

    @JsonProperty("OrderNumber")
    public String getOrderNumber() {
        return orderNumber;
    }

    @JsonProperty("OrderNumber")
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    @JsonProperty("SourceTransactionNumber")
    public String getSourceTransactionNumber() {
        return sourceTransactionNumber;
    }

    @JsonProperty("SourceTransactionNumber")
    public void setSourceTransactionNumber(String sourceTransactionNumber) {
        this.sourceTransactionNumber = sourceTransactionNumber;
    }

    @JsonProperty("BusinessUnitId")
    public Long getBusinessUnitId() {
        return businessUnitId;
    }

    @JsonProperty("BusinessUnitId")
    public void setBusinessUnitId(Long businessUnitId) {
        this.businessUnitId = businessUnitId;
    }

    @JsonProperty("BusinessUnitName")
    public String getBusinessUnitName() {
        return businessUnitName;
    }

    @JsonProperty("BusinessUnitName")
    public void setBusinessUnitName(String businessUnitName) {
        this.businessUnitName = businessUnitName;
    }

    @JsonProperty("RequestedFulfillmentOrganizationId")
    public Long getRequestedFulfillmentOrganizationId() {
        return requestedFulfillmentOrganizationId;
    }

    @JsonProperty("RequestedFulfillmentOrganizationId")
    public void setRequestedFulfillmentOrganizationId(Long requestedFulfillmentOrganizationId) {
        this.requestedFulfillmentOrganizationId = requestedFulfillmentOrganizationId;
    }

    @JsonProperty("RequestedFulfillmentOrganizationCode")
    public String getRequestedFulfillmentOrganizationCode() {
        return requestedFulfillmentOrganizationCode;
    }

    @JsonProperty("RequestedFulfillmentOrganizationCode")
    public void setRequestedFulfillmentOrganizationCode(String requestedFulfillmentOrganizationCode) {
        this.requestedFulfillmentOrganizationCode = requestedFulfillmentOrganizationCode;
    }

    @JsonProperty("PaymentTermsCode")
    public Long getPaymentTermsCode() {
        return paymentTermsCode;
    }

    @JsonProperty("PaymentTermsCode")
    public void setPaymentTermsCode(Long paymentTermsCode) {
        this.paymentTermsCode = paymentTermsCode;
    }

    @JsonProperty("PaymentTerms")
    public String getPaymentTerms() {
        return paymentTerms;
    }

    @JsonProperty("PaymentTerms")
    public void setPaymentTerms(String paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    @JsonProperty("TransactionalCurrencyCode")
    public String getTransactionalCurrencyCode() {
        return transactionalCurrencyCode;
    }

    @JsonProperty("TransactionalCurrencyCode")
    public void setTransactionalCurrencyCode(String transactionalCurrencyCode) {
        this.transactionalCurrencyCode = transactionalCurrencyCode;
    }

    @JsonProperty("TransactionalCurrencyName")
    public String getTransactionalCurrencyName() {
        return transactionalCurrencyName;
    }

    @JsonProperty("TransactionalCurrencyName")
    public void setTransactionalCurrencyName(String transactionalCurrencyName) {
        this.transactionalCurrencyName = transactionalCurrencyName;
    }

    @JsonProperty("CurrencyConversionRate")
    public Object getCurrencyConversionRate() {
        return currencyConversionRate;
    }

    @JsonProperty("CurrencyConversionRate")
    public void setCurrencyConversionRate(Object currencyConversionRate) {
        this.currencyConversionRate = currencyConversionRate;
    }

    @JsonProperty("CurrencyConversionType")
    public Object getCurrencyConversionType() {
        return currencyConversionType;
    }

    @JsonProperty("CurrencyConversionType")
    public void setCurrencyConversionType(Object currencyConversionType) {
        this.currencyConversionType = currencyConversionType;
    }

    @JsonProperty("StatusCode")
    public String getStatusCode() {
        return statusCode;
    }

    @JsonProperty("StatusCode")
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    @JsonProperty("lines")
    public List<Line> getLines() {
        return lines;
    }

    @JsonProperty("lines")
    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    @JsonProperty("totals")
    public List<Total> getTotals() {
        return totals;
    }

    @JsonProperty("totals")
    public void setTotals(List<Total> totals) {
        this.totals = totals;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

	@Override
	public String toString() {
		return "Item [headerId=" + headerId + ", orderNumber=" + orderNumber + ", sourceTransactionNumber="
				+ sourceTransactionNumber + ", businessUnitId=" + businessUnitId + ", businessUnitName="
				+ businessUnitName + ", requestedFulfillmentOrganizationId=" + requestedFulfillmentOrganizationId
				+ ", requestedFulfillmentOrganizationCode=" + requestedFulfillmentOrganizationCode
				+ ", paymentTermsCode=" + paymentTermsCode + ", paymentTerms=" + paymentTerms
				+ ", transactionalCurrencyCode=" + transactionalCurrencyCode + ", transactionalCurrencyName="
				+ transactionalCurrencyName + ", currencyConversionRate=" + currencyConversionRate
				+ ", currencyConversionType=" + currencyConversionType + ", statusCode=" + statusCode + ", lines="
				+ lines + ", totals=" + totals + ", additionalProperties=" + additionalProperties + "]";
	}
    
}
