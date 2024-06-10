package com.smartech.invoicingprod.integration;

import com.smartech.invoicingprod.dto.CategoryDTO;
import com.smartech.invoicingprod.dto.CustomerAccountDTO;
import com.smartech.invoicingprod.dto.CustomerInformation2DTO;
import com.smartech.invoicingprod.dto.CustomerInformationDTO;
import com.smartech.invoicingprod.dto.ItemGtinDTO;
import com.smartech.invoicingprod.dto.ItemsDTO;
import com.smartech.invoicingprod.dto.SalesOrderDTO;
import com.smartech.invoicingprod.model.Invoice;
import com.smartech.invoicingprod.model.Payments;

public interface SOAPService {

	public ItemsDTO getItemDataByItemNumberOrgCode(String ItemNumber, String orgCode);
	public ItemsDTO getItemDataByItemIdOrgCode(String ItemId, String orgCode);
	public Invoice updateUUIDToOracleERPInvoice(Invoice inv);
	public SalesOrderDTO getSalesOrderInformation(String orderNumber);
	public Payments updateUUIDToOracleERPPayments(Payments pay);
	public CategoryDTO getCategoryDataFrom(String categoryCode);
	public ItemGtinDTO getItemGTINData(String itemNumber, String orgCode, String partyNumber);
	public String getItemId(String itemId);
	public CustomerInformationDTO getEmaiAdress(String customerName, String customerPatyNumber);
	public String getRegimenFiscal(String partyNumber);
	public CustomerInformation2DTO getDataCustomer(String customerName);
	public CustomerAccountDTO getDataCustomerAccount(String customerId);
}
