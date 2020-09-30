package com.smartech.invoicing.integration;

import com.smartech.invoicing.integration.json.invorg.InventoryOrganization;
import com.smartech.invoicing.integration.json.salesorder.SalesOrder;
import com.smartech.invoicing.integration.json.salesorderai.SalesOrderAI;

public interface RESTService {

	public InventoryOrganization getInventoryOrganization();
	public SalesOrder getSalesOrderByOrderNumber(String orderNumber);
	public SalesOrderAI getAddInfoBySalesNumber(SalesOrder salesOrder);
	
}
