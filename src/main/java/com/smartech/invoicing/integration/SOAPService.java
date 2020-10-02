package com.smartech.invoicing.integration;

import com.smartech.invoicing.dto.ItemsDTO;
import com.smartech.invoicing.dto.SalesOrderDTO;
import com.smartech.invoicing.model.Invoice;

public interface SOAPService {

	public ItemsDTO getItemDataByItemNumberOrgCode(String ItemNumber, String orgCode);
	public Invoice updateUUIDToOracleERP(Invoice inv);
	public SalesOrderDTO getSalesOrderInformation(String orderNumber);
}
