package com.smartech.invoicing.integration.service;

import java.util.List;

import com.smartech.invoicing.integration.xml.rowset.Row;

public interface InvoicingService {
	boolean createStampInvoice(List<Row> r);
	void updateStartInvoiceSOAPList();
	void getInvoicedListForUpdateUUID();
//	List<RESTInvoiceRespDTO> createInvoiceByREST(Invoice i);
	void updatePetitionInvoiceList();
	boolean createStampedPayments(List<Row> r);
}
