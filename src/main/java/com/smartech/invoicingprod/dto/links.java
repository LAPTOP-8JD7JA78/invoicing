package com.smartech.invoicingprod.dto;

public class links {

	private String rel;
	private String href;
	private String name;
	private String kind;
	private properties properties;
	public String getRel() {
		return rel;
	}
	public void setRel(String rel) {
		this.rel = rel;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public properties getProperties() {
		return properties;
	}
	public void setProperties(properties properties) {
		this.properties = properties;
	}
	
}
