
package com.smartech.invoicing.integration.json.salesorderai;

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
    "Category",
    "CreatedBy",
    "CreationDate",
    "HeaderId",
    "LastUpdateDate",
    "LastUpdateLogin",
    "LastUpdatedBy",
    "ObjectVersionNumber",
    "SourceTransactionIdentifier",
    "SourceTransactionSystem",
    "HeaderEffBFORMAPAGOprivateVO",
    "HeaderEffBMETODOPAGOprivateVO",
    "HeaderEffBUSOCFDIprivateVO"
})
public class Item {

    @JsonProperty("Category")
    private String category;
    @JsonProperty("CreatedBy")
    private String createdBy;
    @JsonProperty("CreationDate")
    private String creationDate;
    @JsonProperty("HeaderId")
    private Long headerId;
    @JsonProperty("LastUpdateDate")
    private String lastUpdateDate;
    @JsonProperty("LastUpdateLogin")
    private String lastUpdateLogin;
    @JsonProperty("LastUpdatedBy")
    private String lastUpdatedBy;
    @JsonProperty("ObjectVersionNumber")
    private Long objectVersionNumber;
    @JsonProperty("SourceTransactionIdentifier")
    private String sourceTransactionIdentifier;
    @JsonProperty("SourceTransactionSystem")
    private String sourceTransactionSystem;
    @JsonProperty("HeaderEffBFORMAPAGOprivateVO")
    private List<HeaderEffBFORMAPAGOprivateVO> headerEffBFORMAPAGOprivateVO = null;
    @JsonProperty("HeaderEffBMETODOPAGOprivateVO")
    private List<HeaderEffBMETODOPAGOprivateVO> headerEffBMETODOPAGOprivateVO = null;
    @JsonProperty("HeaderEffBUSOCFDIprivateVO")
    private List<HeaderEffBUSOCFDIprivateVO> headerEffBUSOCFDIprivateVO = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("Category")
    public String getCategory() {
        return category;
    }

    @JsonProperty("Category")
    public void setCategory(String category) {
        this.category = category;
    }

    @JsonProperty("CreatedBy")
    public String getCreatedBy() {
        return createdBy;
    }

    @JsonProperty("CreatedBy")
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @JsonProperty("CreationDate")
    public String getCreationDate() {
        return creationDate;
    }

    @JsonProperty("CreationDate")
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    @JsonProperty("HeaderId")
    public Long getHeaderId() {
        return headerId;
    }

    @JsonProperty("HeaderId")
    public void setHeaderId(Long headerId) {
        this.headerId = headerId;
    }

    @JsonProperty("LastUpdateDate")
    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    @JsonProperty("LastUpdateDate")
    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    @JsonProperty("LastUpdateLogin")
    public String getLastUpdateLogin() {
        return lastUpdateLogin;
    }

    @JsonProperty("LastUpdateLogin")
    public void setLastUpdateLogin(String lastUpdateLogin) {
        this.lastUpdateLogin = lastUpdateLogin;
    }

    @JsonProperty("LastUpdatedBy")
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    @JsonProperty("LastUpdatedBy")
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    @JsonProperty("ObjectVersionNumber")
    public Long getObjectVersionNumber() {
        return objectVersionNumber;
    }

    @JsonProperty("ObjectVersionNumber")
    public void setObjectVersionNumber(Long objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    @JsonProperty("SourceTransactionIdentifier")
    public String getSourceTransactionIdentifier() {
        return sourceTransactionIdentifier;
    }

    @JsonProperty("SourceTransactionIdentifier")
    public void setSourceTransactionIdentifier(String sourceTransactionIdentifier) {
        this.sourceTransactionIdentifier = sourceTransactionIdentifier;
    }

    @JsonProperty("SourceTransactionSystem")
    public String getSourceTransactionSystem() {
        return sourceTransactionSystem;
    }

    @JsonProperty("SourceTransactionSystem")
    public void setSourceTransactionSystem(String sourceTransactionSystem) {
        this.sourceTransactionSystem = sourceTransactionSystem;
    }

    @JsonProperty("HeaderEffBFORMAPAGOprivateVO")
    public List<HeaderEffBFORMAPAGOprivateVO> getHeaderEffBFORMAPAGOprivateVO() {
        return headerEffBFORMAPAGOprivateVO;
    }

    @JsonProperty("HeaderEffBFORMAPAGOprivateVO")
    public void setHeaderEffBFORMAPAGOprivateVO(List<HeaderEffBFORMAPAGOprivateVO> headerEffBFORMAPAGOprivateVO) {
        this.headerEffBFORMAPAGOprivateVO = headerEffBFORMAPAGOprivateVO;
    }

    @JsonProperty("HeaderEffBMETODOPAGOprivateVO")
    public List<HeaderEffBMETODOPAGOprivateVO> getHeaderEffBMETODOPAGOprivateVO() {
        return headerEffBMETODOPAGOprivateVO;
    }

    @JsonProperty("HeaderEffBMETODOPAGOprivateVO")
    public void setHeaderEffBMETODOPAGOprivateVO(List<HeaderEffBMETODOPAGOprivateVO> headerEffBMETODOPAGOprivateVO) {
        this.headerEffBMETODOPAGOprivateVO = headerEffBMETODOPAGOprivateVO;
    }

    @JsonProperty("HeaderEffBUSOCFDIprivateVO")
    public List<HeaderEffBUSOCFDIprivateVO> getHeaderEffBUSOCFDIprivateVO() {
        return headerEffBUSOCFDIprivateVO;
    }

    @JsonProperty("HeaderEffBUSOCFDIprivateVO")
    public void setHeaderEffBUSOCFDIprivateVO(List<HeaderEffBUSOCFDIprivateVO> headerEffBUSOCFDIprivateVO) {
        this.headerEffBUSOCFDIprivateVO = headerEffBUSOCFDIprivateVO;
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
