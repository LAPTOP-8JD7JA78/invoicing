package com.smartech.invoicing.integration.service;

import java.text.ParseException;
import java.util.List;

import com.smartech.invoicing.integration.xml.rowset.Row;

public interface InvoicingService {
	boolean createStampInvoice(List<Row> r, String errors);
	void updateStartInvoiceSOAPList() throws ParseException;
	void getInvoicedListForUpdateUUID();
//	List<RESTInvoiceRespDTO> createInvoiceByREST(Invoice i);
	void updatePetitionInvoiceList();
	boolean createStampedPayments(List<Row> r);
	boolean createTransferInvoice(List<Row> r);
	void sendAllErrors();
}
