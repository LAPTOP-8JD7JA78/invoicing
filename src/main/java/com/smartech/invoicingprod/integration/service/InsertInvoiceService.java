package com.smartech.invoicingprod.integration.service;

import com.smartech.invoicingprod.dto.CustomeResponseDTO;
import com.smartech.invoicingprod.dto.InsertInvoiceCloudBodyDTO;
import com.smartech.invoicingprod.dto.ResponseInvoiceCloudBodyDTO;

public interface InsertInvoiceService {
	
	ResponseInvoiceCloudBodyDTO createInvoice(InsertInvoiceCloudBodyDTO data);
	CustomeResponseDTO updatePwdDistPortal(String pwd, String user);
	CustomeResponseDTO sendInvToDisPortal(String invoices, String initialDate, String endDate);
}
