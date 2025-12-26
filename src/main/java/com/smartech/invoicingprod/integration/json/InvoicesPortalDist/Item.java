package com.smartech.invoicingprod.integration.json.InvoicesPortalDist;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "uuid",
    "folio",
    "customerTaxId"
})
public class Item {
	 	@JsonProperty("uuid")
	    private String uuid;
	 	
	    @JsonProperty("folio")
	    private String folio;
	    
	    @JsonProperty("customerTaxId")
	    private String customerTaxId;

	    @JsonProperty("uuid")
		public String getUuid() {
			return uuid;
		}

	    @JsonProperty("uuid")
		public void setUuid(String uuid) {
			this.uuid = uuid;
		}

	    @JsonProperty("folio")
		public String getFolio() {
			return folio;
		}

	    @JsonProperty("folio")
		public void setFolio(String folio) {
			this.folio = folio;
		}

	    @JsonProperty("customerTaxId")
		public String getCustomerTaxId() {
			return customerTaxId;
		}

	    @JsonProperty("customerTaxId")
		public void setCustomerTaxId(String customerTaxId) {
			this.customerTaxId = customerTaxId;
		}
	    
}
