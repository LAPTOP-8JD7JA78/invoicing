package com.smartech.invoicingprod.distribuitorportal.dto;

public class HeadersRestDTO {

	String headerName;
	String headerValue;
	
	public HeadersRestDTO(String headerName, String headerValue) {
		super();
		this.headerName = headerName;
		this.headerValue = headerValue;
	}
	
	public String getHeaderName() {
		return headerName;
	}
	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}
	public String getHeaderValue() {
		return headerValue;
	}
	public void setHeaderValue(String headerValue) {
		this.headerValue = headerValue;
	}
	
}
