package com.smartech.invoicing.integration;

import com.smartech.invoicing.dto.CategoryDTO;
import com.smartech.invoicing.dto.ItemGtinDTO;
import com.smartech.invoicing.dto.ItemsDTO;
import com.smartech.invoicing.dto.SalesOrderDTO;
import com.smartech.invoicing.model.Invoice;
import com.smartech.invoicing.model.Payments;

public interface SOAPService {

	public ItemsDTO getItemDataByItemNumberOrgCode(String ItemNumber, String orgCode);
	public ItemsDTO getItemDataByItemIdOrgCode(String ItemId, String orgCode);
	public Invoice updateUUIDToOracleERPInvoice(Invoice inv);
	public SalesOrderDTO getSalesOrderInformation(String orderNumber);
	public Payments updateUUIDToOracleERPPayments(Payments pay);
	public CategoryDTO getCategoryDataFrom(String categoryCode);
	public ItemGtinDTO getItemGTINData(String itemNumber, String orgCode, String partyNumber);
}
