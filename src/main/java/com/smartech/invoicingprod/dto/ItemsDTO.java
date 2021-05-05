package com.smartech.invoicingprod.dto;

import java.util.List;

public class ItemsDTO {

	private String itemNumber;
	private String itemDescription;
	private String itemDFFClavProdServ;
	private String itemDFFFraccionArancelaria;
	private String itemDFFMarca;
	private String itemDFFModelo;
	private boolean itemDFFIsImported;
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
	public String getItemDFFFraccionArancelaria() {
		return itemDFFFraccionArancelaria;
	}
	public void setItemDFFFraccionArancelaria(String itemDFFFraccionArancelaria) {
		this.itemDFFFraccionArancelaria = itemDFFFraccionArancelaria;
	}
	public String getItemDFFMarca() {
		return itemDFFMarca;
	}
	public void setItemDFFMarca(String itemDFFMarca) {
		this.itemDFFMarca = itemDFFMarca;
	}
	public String getItemDFFModelo() {
		return itemDFFModelo;
	}
	public void setItemDFFModelo(String itemDFFModelo) {
		this.itemDFFModelo = itemDFFModelo;
	}
	public boolean isItemDFFIsImported() {
		return itemDFFIsImported;
	}
	public void setItemDFFIsImported(boolean itemDFFIsImported) {
		this.itemDFFIsImported = itemDFFIsImported;
	}
}
