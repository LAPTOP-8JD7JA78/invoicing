package com.smartech.invoicingprod.integration.service;

import com.smartech.invoicingprod.dto.InsertInvoiceCloudBodyDTO;
import com.smartech.invoicingprod.dto.ResponseInvoiceCloudBodyDTO;

public interface InsertInvoiceService {
	
	ResponseInvoiceCloudBodyDTO createInvoice(InsertInvoiceCloudBodyDTO data);
}
