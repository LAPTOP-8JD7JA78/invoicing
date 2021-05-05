
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
    "SourceTransactionLineNumber",
    "AssessableValue",
    "FulfilledQuantity",
    "ProductId",
    "ProductNumber",
    "ProductDescription",
    "InventoryOrganizationCode",
    "OrderedQuantity",
    "OrderedUOMCode",
    "OrderedUOM",
    "StatusCode",
    "TaxClassificationCode",
    "TaxClassification",
    "LineNumber",
    "charges",
    "lotSerials"
})
public class Line {

    @JsonProperty("SourceTransactionLineNumber")
    private String sourceTransactionLineNumber;
    @JsonProperty("AssessableValue")
    private Integer assessableValue;
    @JsonProperty("FulfilledQuantity")
    private Object fulfilledQuantity;
    @JsonProperty("ProductId")
    private Long productId;
    @JsonProperty("ProductNumber")
    private String productNumber;
    @JsonProperty("ProductDescription")
    private String productDescription;
    @JsonProperty("InventoryOrganizationCode")
    private String inventoryOrganizationCode;
    @JsonProperty("OrderedQuantity")
    private Double orderedQuantity;
    @JsonProperty("OrderedUOMCode")
    private String orderedUOMCode;
    @JsonProperty("OrderedUOM")
    private String orderedUOM;
    @JsonProperty("StatusCode")
    private String statusCode;
    @JsonProperty("TaxClassificationCode")
    private String taxClassificationCode;
    @JsonProperty("TaxClassification")
    private String taxClassification;
    @JsonProperty("LineNumber")
    private Integer lineNumber;
    @JsonProperty("charges")
    private List<Charge> charges = null;
    @JsonProperty("lotSerials")
    private List<LotSerials> lotSerials = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("SourceTransactionLineNumber")
    public String getSourceTransactionLineNumber() {
        return sourceTransactionLineNumber;
    }

    @JsonProperty("SourceTransactionLineNumber")
    public void setSourceTransactionLineNumber(String sourceTransactionLineNumber) {
        this.sourceTransactionLineNumber = sourceTransactionLineNumber;
    }

    @JsonProperty("AssessableValue")
    public Integer getAssessableValue() {
        return assessableValue;
    }

    @JsonProperty("AssessableValue")
    public void setAssessableValue(Integer assessableValue) {
        this.assessableValue = assessableValue;
    }

    @JsonProperty("FulfilledQuantity")
    public Object getFulfilledQuantity() {
        return fulfilledQuantity;
    }

    @JsonProperty("FulfilledQuantity")
    public void setFulfilledQuantity(Object fulfilledQuantity) {
        this.fulfilledQuantity = fulfilledQuantity;
    }

    @JsonProperty("ProductId")
    public Long getProductId() {
        return productId;
    }

    @JsonProperty("ProductId")
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @JsonProperty("ProductNumber")
    public String getProductNumber() {
        return productNumber;
    }

    @JsonProperty("ProductNumber")
    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    @JsonProperty("ProductDescription")
    public String getProductDescription() {
        return productDescription;
    }

    @JsonProperty("ProductDescription")
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    @JsonProperty("InventoryOrganizationCode")
    public String getInventoryOrganizationCode() {
        return inventoryOrganizationCode;
    }

    @JsonProperty("InventoryOrganizationCode")
    public void setInventoryOrganizationCode(String inventoryOrganizationCode) {
        this.inventoryOrganizationCode = inventoryOrganizationCode;
    }

    @JsonProperty("OrderedQuantity")
    public Double getOrderedQuantity() {
        return orderedQuantity;
    }

    @JsonProperty("OrderedQuantity")
    public void setOrderedQuantity(Double orderedQuantity) {
        this.orderedQuantity = orderedQuantity;
    }

    @JsonProperty("OrderedUOMCode")
    public String getOrderedUOMCode() {
        return orderedUOMCode;
    }

    @JsonProperty("OrderedUOMCode")
    public void setOrderedUOMCode(String orderedUOMCode) {
        this.orderedUOMCode = orderedUOMCode;
    }

    @JsonProperty("OrderedUOM")
    public String getOrderedUOM() {
        return orderedUOM;
    }

    @JsonProperty("OrderedUOM")
    public void setOrderedUOM(String orderedUOM) {
        this.orderedUOM = orderedUOM;
    }

    @JsonProperty("StatusCode")
    public String getStatusCode() {
        return statusCode;
    }

    @JsonProperty("StatusCode")
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    @JsonProperty("TaxClassificationCode")
    public String getTaxClassificationCode() {
        return taxClassificationCode;
    }

    @JsonProperty("TaxClassificationCode")
    public void setTaxClassificationCode(String taxClassificationCode) {
        this.taxClassificationCode = taxClassificationCode;
    }

    @JsonProperty("TaxClassification")
    public String getTaxClassification() {
        return taxClassification;
    }

    @JsonProperty("TaxClassification")
    public void setTaxClassification(String taxClassification) {
        this.taxClassification = taxClassification;
    }

    @JsonProperty("LineNumber")
    public Integer getLineNumber() {
        return lineNumber;
    }

    @JsonProperty("LineNumber")
    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    @JsonProperty("charges")
    public List<Charge> getCharges() {
        return charges;
    }

    @JsonProperty("charges")
    public void setCharges(List<Charge> charges) {
        this.charges = charges;
    }

    @JsonProperty("lotSerials")
    public List<LotSerials> getLotSerials() {
        return lotSerials;
    }

    @JsonProperty("lotSerials")
    public void setLotSerials(List<LotSerials> lotSerials) {
        this.lotSerials = lotSerials;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
