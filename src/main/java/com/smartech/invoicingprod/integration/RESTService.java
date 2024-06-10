package com.smartech.invoicingprod.integration;

import com.smartech.invoicingprod.dto.InvoiceInsertHeaderDTO;
import com.smartech.invoicingprod.dto.responseInsertInvoiceDTO;
import com.smartech.invoicingprod.integration.json.IncotermByRest.IncotermByRest;
import com.smartech.invoicingprod.integration.json.dailyRates.CurrencyRates;
import com.smartech.invoicingprod.integration.json.inventoryItemSerialNumbers.InventoryItemSerialNumbers;
import com.smartech.invoicingprod.integration.json.invitemlot.InventoryItemLots;
import com.smartech.invoicingprod.integration.json.invorg.InventoryOrganization;
import com.smartech.invoicingprod.integration.json.itemCategories.ItemCategory;
import com.smartech.invoicingprod.integration.json.priceList.PriceLists;
import com.smartech.invoicingprod.integration.json.priceListByItem.PriceListByItem;
import com.smartech.invoicingprod.integration.json.receivablesInvoices.ReceivablesInvoices;
import com.smartech.invoicingprod.integration.json.salesorder.SalesOrder;
import com.smartech.invoicingprod.integration.json.salesorderai.SalesOrderAI;
import com.smartech.invoicingprod.integration.json.standardReceipts.StandardReceipts;
import com.smartech.invoicingprod.integration.json.unitCost.ItemCosts;
import com.smartech.invoicingprod.model.Invoice;

public interface RESTService {

	public InventoryOrganization getInventoryOrganization();
	public SalesOrder getSalesOrderByOrderNumber(String orderNumber);
	public SalesOrderAI getAddInfoBySalesNumber(SalesOrder salesOrder);
	public InventoryItemLots getInventoryLot(String invOrgCode, String itemNumber, String itemLot);
	public CurrencyRates getDailyCurrency(String date, String fromCurrency, String toCurrency);
	public PriceLists getPriceList(Invoice i);
	public PriceListByItem getPrice(String itemNumber, String priceListId);
	public IncotermByRest getIncoterm(String orderNumber);
	public ItemCategory getCategoryCode(String categoryName);
	public InventoryItemSerialNumbers getInventoryItemSerialModel(String orgCode, String itemNumber, String SerialNumber);
	public ItemCosts getItemCostWitoutSerialNumber(String itemNumber);
	public ReceivablesInvoices getInvoiceData(String transactionNumber);
	public CurrencyRates getDailyCurrencyExportacion(String date, String fromCurrency, String toCurrency);
	public StandardReceipts getStandardReceipts(String receiptNumber);
	public String insertInvoiceToCloud(InvoiceInsertHeaderDTO invoiceAr);
	public ReceivablesInvoices getInvoiceData2(String invoiceFolio);
}
