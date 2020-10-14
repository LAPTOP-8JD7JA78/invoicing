
package com.smartech.invoicing.integration.json.invitemlot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "OrganizationId",
    "OrganizationCode",
    "InventoryItemId",
    "ItemNumber",
    "LotNumber",
    "OriginationDate"
})
public class Item {

    @JsonProperty("OrganizationId")
    private Long organizationId;
    @JsonProperty("OrganizationCode")
    private String organizationCode;
    @JsonProperty("InventoryItemId")
    private Long inventoryItemId;
    @JsonProperty("ItemNumber")
    private String itemNumber;
    @JsonProperty("LotNumber")
    private String lotNumber;
    @JsonProperty("OriginationDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss+hh:mm", timezone = "CST")
    private Date originationDate;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("OrganizationId")
    public Long getOrganizationId() {
        return organizationId;
    }

    @JsonProperty("OrganizationId")
    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    @JsonProperty("OrganizationCode")
    public String getOrganizationCode() {
        return organizationCode;
    }

    @JsonProperty("OrganizationCode")
    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    @JsonProperty("InventoryItemId")
    public Long getInventoryItemId() {
        return inventoryItemId;
    }

    @JsonProperty("InventoryItemId")
    public void setInventoryItemId(Long inventoryItemId) {
        this.inventoryItemId = inventoryItemId;
    }

    @JsonProperty("ItemNumber")
    public String getItemNumber() {
        return itemNumber;
    }

    @JsonProperty("ItemNumber")
    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    @JsonProperty("LotNumber")
    public String getLotNumber() {
        return lotNumber;
    }

    @JsonProperty("LotNumber")
    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }

    @JsonProperty("OriginationDate")
    public Date getOriginationDate() {
        return originationDate;
    }

    @JsonProperty("OriginationDate")
    public void setOriginationDate(Date originationDate) {
        this.originationDate = originationDate;
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
