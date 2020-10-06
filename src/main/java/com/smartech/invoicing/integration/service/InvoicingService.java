package com.smartech.invoicing.integration.service;

import java.util.List;

import com.smartech.invoicing.dto.RESTInvoiceRespDTO;
import com.smartech.invoicing.integration.xml.rowset.Row;
import com.smartech.invoicing.model.Invoice;

public interface InvoicingService {
	boolean createStampInvoice(List<Row> r);
	void updateStartInvoiceList();
	void updateStartInvoiceSOAPList();
	void getInvoicedListForUpdateUUID();
	List<RESTInvoiceRespDTO> createInvoiceByREST(Invoice i);
}
