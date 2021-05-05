package com.smartech.invoicingprod.integration.dto;

public class ParamsRestDTO {

	String paramName;
	Object paramValue;
			
	public ParamsRestDTO(String paramName, Object paramValue) {
		this.paramName = paramName;
		this.paramValue = paramValue;
	}
	public String getParamName() {
		return paramName;
	}
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	public Object getParamValue() {
		return paramValue;
	}
	public void setParamValue(Object paramValue) {
		this.paramValue = paramValue;
	}
}
