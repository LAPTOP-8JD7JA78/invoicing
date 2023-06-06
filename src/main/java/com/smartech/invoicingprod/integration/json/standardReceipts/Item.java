package com.smartech.invoicingprod.integration.json.standardReceipts;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "sucursal",
    "formaDePago"
})
public class Item {
	 	@JsonProperty("sucursal")
	    private String sucursal;
	    @JsonProperty("formaDePago")
	    private String formaDePago;
	    
	    @JsonProperty("sucursal")
		public String getSucursal() {
			return sucursal;
		}
	    
	    @JsonProperty("sucursal")
		public void setSucursal(String sucursal) {
			this.sucursal = sucursal;
		}
	    
	    @JsonProperty("formaDePago")
		public String getFormaDePago() {
			return formaDePago;
		}
	    
	    @JsonProperty("formaDePago")
		public void setFormaDePago(String formaDePago) {
			this.formaDePago = formaDePago;
		}
      
	    
}

