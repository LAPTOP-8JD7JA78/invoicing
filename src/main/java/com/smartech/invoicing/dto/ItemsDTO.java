package com.smartech.invoicing.dto;

import java.util.List;

public class ItemsDTO {

	private String itemNumber;
	private String itemDescription;
	private String itemDFFClavProdServ;
	private List<CategoryDTO> itemCategory;
	
	public String getItemNumber() {
		return itemNumber;
	}
	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}
	public String getItemDescription() {
		return itemDescription;
	}
	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}
	public List<CategoryDTO> getItemCategory() {
		return itemCategory;
	}
	public void setItemCategory(List<CategoryDTO> itemCategory) {
		this.itemCategory = itemCategory;
	}
	public String getItemDFFClavProdServ() {
		return itemDFFClavProdServ;
	}
	public void setItemDFFClavProdServ(String itemDFFClavProdServ) {
		this.itemDFFClavProdServ = itemDFFClavProdServ;
	}
}
