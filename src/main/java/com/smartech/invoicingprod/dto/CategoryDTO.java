package com.smartech.invoicingprod.dto;

import java.util.List;

public class CategoryDTO {
	private String catalogCode;
	private String categoryName;
	private String categoryCode;
	private List<CatAttachmentDTO> attachments;
	
	public String getCatalogCode() {
		return catalogCode;
	}
	public void setCatalogCode(String catalogCode) {
		this.catalogCode = catalogCode;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	public List<CatAttachmentDTO> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<CatAttachmentDTO> attachments) {
		this.attachments = attachments;
	}
	
}
