package com.smartech.invoicingprod.integration.dto;

import java.util.List;

public class WarrantyDataLinesDTO {

	public String itemNumber;
	public boolean isEquipment;
	public String itemBrand;
	public String itemDescription;
	public String itemModel;
	public String productType;	
	public String invoiceLineType;
	public List<WarrantyDataSerialLinesDTO> itemSerial;
	public String getItemNumber() {
		return itemNumber;
	}
	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}
	public boolean isEquipment() {
		return isEquipment;
	}
	public void setEquipment(boolean isEquipment) {
		this.isEquipment = isEquipment;
	}
	public String getItemBrand() {
		return itemBrand;
	}
	public void setItemBrand(String itemBrand) {
		this.itemBrand = itemBrand;
	}
	public String getItemDescription() {
		return itemDescription;
	}
	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}
	public String getItemModel() {
		return itemModel;
	}
	public void setItemModel(String itemModel) {
		this.itemModel = itemModel;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public List<WarrantyDataSerialLinesDTO> getItemSerial() {
		return itemSerial;
	}
	public void setItemSerial(List<WarrantyDataSerialLinesDTO> itemSerial) {
		this.itemSerial = itemSerial;
	}
	public String getInvoiceLineType() {
		return invoiceLineType;
	}
	public void setInvoiceLineType(String invoiceLineType) {
		this.invoiceLineType = invoiceLineType;
	}
	
	
}
