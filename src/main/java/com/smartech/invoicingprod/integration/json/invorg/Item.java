
package com.smartech.invoicingprod.integration.json.invorg;

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
    "OrganizationId",
    "OrganizationCode",
    "OrganizationName",
    "ManagementBusinessUnitId",
    "ManagementBusinessUnitName",
    "LegalEntityId",
    "LegalEntityName",
    "ProfitCenterBusinessUnitId",
    "ProfitCenterBusinessUnitName",
    "Status",
    "LocationId",
    "LocationCode",
    "InventoryFlag",
    "ManufacturingPlantFlag",
    "ContractManufacturingFlag",
    "MaintenanceEnabledFlag",
    "MasterOrganizationId",
    "MasterOrganizationCode",
    "MasterOrganizationName",
    "IntegratedSystemType",
    "ItemGroupingCode",
    "ItemDefinitionOrganizationId",
    "ItemDefinitionOrganizationCode",
    "ItemDefinitionOrganizationName",
    "ManufacturingParametersExistFlag",
    "links"
})
public class Item {

    @JsonProperty("OrganizationId")
    private Long organizationId;
    @JsonProperty("OrganizationCode")
    private String organizationCode;
    @JsonProperty("OrganizationName")
    private String organizationName;
    @JsonProperty("ManagementBusinessUnitId")
    private Long managementBusinessUnitId;
    @JsonProperty("ManagementBusinessUnitName")
    private String managementBusinessUnitName;
    @JsonProperty("LegalEntityId")
    private Long legalEntityId;
    @JsonProperty("LegalEntityName")
    private String legalEntityName;
    @JsonProperty("ProfitCenterBusinessUnitId")
    private Long profitCenterBusinessUnitId;
    @JsonProperty("ProfitCenterBusinessUnitName")
    private String profitCenterBusinessUnitName;
    @JsonProperty("Status")
    private String status;
    @JsonProperty("LocationId")
    private Long locationId;
    @JsonProperty("LocationCode")
    private String locationCode;
    @JsonProperty("InventoryFlag")
    private Boolean inventoryFlag;
    @JsonProperty("ManufacturingPlantFlag")
    private Boolean manufacturingPlantFlag;
    @JsonProperty("ContractManufacturingFlag")
    private Boolean contractManufacturingFlag;
    @JsonProperty("MaintenanceEnabledFlag")
    private Boolean maintenanceEnabledFlag;
    @JsonProperty("MasterOrganizationId")
    private Long masterOrganizationId;
    @JsonProperty("MasterOrganizationCode")
    private String masterOrganizationCode;
    @JsonProperty("MasterOrganizationName")
    private String masterOrganizationName;
    @JsonProperty("IntegratedSystemType")
    private Object integratedSystemType;
    @JsonProperty("ItemGroupingCode")
    private String itemGroupingCode;
    @JsonProperty("ItemDefinitionOrganizationId")
    private Long itemDefinitionOrganizationId;
    @JsonProperty("ItemDefinitionOrganizationCode")
    private String itemDefinitionOrganizationCode;
    @JsonProperty("ItemDefinitionOrganizationName")
    private String itemDefinitionOrganizationName;
    @JsonProperty("ManufacturingParametersExistFlag")
    private String manufacturingParametersExistFlag;
    @JsonProperty("links")
    private List<Link> links = null;
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

    @JsonProperty("OrganizationName")
    public String getOrganizationName() {
        return organizationName;
    }

    @JsonProperty("OrganizationName")
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    @JsonProperty("ManagementBusinessUnitId")
    public Long getManagementBusinessUnitId() {
        return managementBusinessUnitId;
    }

    @JsonProperty("ManagementBusinessUnitId")
    public void setManagementBusinessUnitId(Long managementBusinessUnitId) {
        this.managementBusinessUnitId = managementBusinessUnitId;
    }

    @JsonProperty("ManagementBusinessUnitName")
    public String getManagementBusinessUnitName() {
        return managementBusinessUnitName;
    }

    @JsonProperty("ManagementBusinessUnitName")
    public void setManagementBusinessUnitName(String managementBusinessUnitName) {
        this.managementBusinessUnitName = managementBusinessUnitName;
    }

    @JsonProperty("LegalEntityId")
    public Long getLegalEntityId() {
        return legalEntityId;
    }

    @JsonProperty("LegalEntityId")
    public void setLegalEntityId(Long legalEntityId) {
        this.legalEntityId = legalEntityId;
    }

    @JsonProperty("LegalEntityName")
    public String getLegalEntityName() {
        return legalEntityName;
    }

    @JsonProperty("LegalEntityName")
    public void setLegalEntityName(String legalEntityName) {
        this.legalEntityName = legalEntityName;
    }

    @JsonProperty("ProfitCenterBusinessUnitId")
    public Long getProfitCenterBusinessUnitId() {
        return profitCenterBusinessUnitId;
    }

    @JsonProperty("ProfitCenterBusinessUnitId")
    public void setProfitCenterBusinessUnitId(Long profitCenterBusinessUnitId) {
        this.profitCenterBusinessUnitId = profitCenterBusinessUnitId;
    }

    @JsonProperty("ProfitCenterBusinessUnitName")
    public String getProfitCenterBusinessUnitName() {
        return profitCenterBusinessUnitName;
    }

    @JsonProperty("ProfitCenterBusinessUnitName")
    public void setProfitCenterBusinessUnitName(String profitCenterBusinessUnitName) {
        this.profitCenterBusinessUnitName = profitCenterBusinessUnitName;
    }

    @JsonProperty("Status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("Status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("LocationId")
    public Long getLocationId() {
        return locationId;
    }

    @JsonProperty("LocationId")
    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    @JsonProperty("LocationCode")
    public String getLocationCode() {
        return locationCode;
    }

    @JsonProperty("LocationCode")
    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    @JsonProperty("InventoryFlag")
    public Boolean getInventoryFlag() {
        return inventoryFlag;
    }

    @JsonProperty("InventoryFlag")
    public void setInventoryFlag(Boolean inventoryFlag) {
        this.inventoryFlag = inventoryFlag;
    }

    @JsonProperty("ManufacturingPlantFlag")
    public Boolean getManufacturingPlantFlag() {
        return manufacturingPlantFlag;
    }

    @JsonProperty("ManufacturingPlantFlag")
    public void setManufacturingPlantFlag(Boolean manufacturingPlantFlag) {
        this.manufacturingPlantFlag = manufacturingPlantFlag;
    }

    @JsonProperty("ContractManufacturingFlag")
    public Boolean getContractManufacturingFlag() {
        return contractManufacturingFlag;
    }

    @JsonProperty("ContractManufacturingFlag")
    public void setContractManufacturingFlag(Boolean contractManufacturingFlag) {
        this.contractManufacturingFlag = contractManufacturingFlag;
    }

    @JsonProperty("MaintenanceEnabledFlag")
    public Boolean getMaintenanceEnabledFlag() {
        return maintenanceEnabledFlag;
    }

    @JsonProperty("MaintenanceEnabledFlag")
    public void setMaintenanceEnabledFlag(Boolean maintenanceEnabledFlag) {
        this.maintenanceEnabledFlag = maintenanceEnabledFlag;
    }

    @JsonProperty("MasterOrganizationId")
    public Long getMasterOrganizationId() {
        return masterOrganizationId;
    }

    @JsonProperty("MasterOrganizationId")
    public void setMasterOrganizationId(Long masterOrganizationId) {
        this.masterOrganizationId = masterOrganizationId;
    }

    @JsonProperty("MasterOrganizationCode")
    public String getMasterOrganizationCode() {
        return masterOrganizationCode;
    }

    @JsonProperty("MasterOrganizationCode")
    public void setMasterOrganizationCode(String masterOrganizationCode) {
        this.masterOrganizationCode = masterOrganizationCode;
    }

    @JsonProperty("MasterOrganizationName")
    public String getMasterOrganizationName() {
        return masterOrganizationName;
    }

    @JsonProperty("MasterOrganizationName")
    public void setMasterOrganizationName(String masterOrganizationName) {
        this.masterOrganizationName = masterOrganizationName;
    }

    @JsonProperty("IntegratedSystemType")
    public Object getIntegratedSystemType() {
        return integratedSystemType;
    }

    @JsonProperty("IntegratedSystemType")
    public void setIntegratedSystemType(Object integratedSystemType) {
        this.integratedSystemType = integratedSystemType;
    }

    @JsonProperty("ItemGroupingCode")
    public String getItemGroupingCode() {
        return itemGroupingCode;
    }

    @JsonProperty("ItemGroupingCode")
    public void setItemGroupingCode(String itemGroupingCode) {
        this.itemGroupingCode = itemGroupingCode;
    }

    @JsonProperty("ItemDefinitionOrganizationId")
    public Long getItemDefinitionOrganizationId() {
        return itemDefinitionOrganizationId;
    }

    @JsonProperty("ItemDefinitionOrganizationId")
    public void setItemDefinitionOrganizationId(Long itemDefinitionOrganizationId) {
        this.itemDefinitionOrganizationId = itemDefinitionOrganizationId;
    }

    @JsonProperty("ItemDefinitionOrganizationCode")
    public String getItemDefinitionOrganizationCode() {
        return itemDefinitionOrganizationCode;
    }

    @JsonProperty("ItemDefinitionOrganizationCode")
    public void setItemDefinitionOrganizationCode(String itemDefinitionOrganizationCode) {
        this.itemDefinitionOrganizationCode = itemDefinitionOrganizationCode;
    }

    @JsonProperty("ItemDefinitionOrganizationName")
    public String getItemDefinitionOrganizationName() {
        return itemDefinitionOrganizationName;
    }

    @JsonProperty("ItemDefinitionOrganizationName")
    public void setItemDefinitionOrganizationName(String itemDefinitionOrganizationName) {
        this.itemDefinitionOrganizationName = itemDefinitionOrganizationName;
    }

    @JsonProperty("ManufacturingParametersExistFlag")
    public String getManufacturingParametersExistFlag() {
        return manufacturingParametersExistFlag;
    }

    @JsonProperty("ManufacturingParametersExistFlag")
    public void setManufacturingParametersExistFlag(String manufacturingParametersExistFlag) {
        this.manufacturingParametersExistFlag = manufacturingParametersExistFlag;
    }

    @JsonProperty("links")
    public List<Link> getLinks() {
        return links;
    }

    @JsonProperty("links")
    public void setLinks(List<Link> links) {
        this.links = links;
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
