package com.smartech.invoicingprod.distribuitorportal.services;

import java.util.List;

import com.smartech.invoicingprod.distribuitorportal.dto.FileInfoDTO;
import com.smartech.invoicingprod.integration.dto.WarrantyDataDTO;

public interface DistribuitorServices {
	
	WarrantyDataDTO getDataInvoice(String invoiceNumber, String itemNumber, String itemSerial, String customerName);
	boolean insertData(String invoiceNumber, String itemNumber, String itemSerial, String productTypeCode);
	List<WarrantyDataDTO> retrieveAllData(String dataSearch);
	FileInfoDTO getFileInfo(String invoiceNumber, String invoiceType);
	
}
