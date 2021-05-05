
package com.smartech.invoicingprod.integration.json.salesorder;

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
    "ChargeTypeCode",
    "chargeComponents"
})
public class Charge {

    @JsonProperty("ChargeTypeCode")
    private String chargeTypeCode;
    @JsonProperty("chargeComponents")
    private List<ChargeComponent> chargeComponents = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("ChargeTypeCode")
    public String getChargeTypeCode() {
        return chargeTypeCode;
    }

    @JsonProperty("ChargeTypeCode")
    public void setChargeTypeCode(String chargeTypeCode) {
        this.chargeTypeCode = chargeTypeCode;
    }

    @JsonProperty("chargeComponents")
    public List<ChargeComponent> getChargeComponents() {
        return chargeComponents;
    }

    @JsonProperty("chargeComponents")
    public void setChargeComponents(List<ChargeComponent> chargeComponents) {
        this.chargeComponents = chargeComponents;
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
