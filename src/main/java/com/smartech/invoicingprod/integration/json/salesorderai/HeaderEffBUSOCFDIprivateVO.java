
package com.smartech.invoicing.integration.json.salesorderai;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "ContextCode",
    "EffLineId",
    "ObjectVersionNumber",
    "CreatedBy",
    "CreationDate",
    "HeaderId",
    "LastUpdateDate",
    "LastUpdateLogin",
    "LastUpdatedBy",
    "usocfdi"
})
public class HeaderEffBUSOCFDIprivateVO {

    @JsonProperty("ContextCode")
    private String contextCode;
    @JsonProperty("EffLineId")
    private Long effLineId;
    @JsonProperty("ObjectVersionNumber")
    private Long objectVersionNumber;
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
    @JsonProperty("usocfdi")
    private String usocfdi;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("ContextCode")
    public String getContextCode() {
        return contextCode;
    }

    @JsonProperty("ContextCode")
    public void setContextCode(String contextCode) {
        this.contextCode = contextCode;
    }

    @JsonProperty("EffLineId")
    public Long getEffLineId() {
        return effLineId;
    }

    @JsonProperty("EffLineId")
    public void setEffLineId(Long effLineId) {
        this.effLineId = effLineId;
    }

    @JsonProperty("ObjectVersionNumber")
    public Long getObjectVersionNumber() {
        return objectVersionNumber;
    }

    @JsonProperty("ObjectVersionNumber")
    public void setObjectVersionNumber(Long objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
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

    @JsonProperty("usocfdi")
    public String getUsocfdi() {
        return usocfdi;
    }

    @JsonProperty("usocfdi")
    public void setUsocfdi(String usocfdi) {
        this.usocfdi = usocfdi;
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
