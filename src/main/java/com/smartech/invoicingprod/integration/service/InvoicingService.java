package com.smartech.invoicingprod.integration.service;

import java.text.ParseException;
import java.util.List;

import com.smartech.invoicingprod.integration.xml.rowset.Row;

public interface InvoicingService {
	boolean createStampInvoice(List<Row> r, String errors);
	void updateStartInvoiceSOAPList() throws ParseException;
	void getInvoicedListForUpdateUUID();
	void updatePetitionInvoiceList();
	boolean createStampedPayments(List<Row> r);
	boolean createTransferInvoice(List<Row> r);
	void sendAllErrors();
	void recolectListPayments();
	boolean createInvoiceByInitialCharge(List<Row> r);
}
