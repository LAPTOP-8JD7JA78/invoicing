package com.smartech.invoicing.integration;

import com.smartech.invoicing.integration.json.invorg.InventoryOrganization;
import com.smartech.invoicing.integration.json.salesorder.SalesOrder;

public interface RESTService {

	public InventoryOrganization getInventoryOrganization();
	public SalesOrder getSalesOrderByOrderNumber(String orderNumber);
	
}
