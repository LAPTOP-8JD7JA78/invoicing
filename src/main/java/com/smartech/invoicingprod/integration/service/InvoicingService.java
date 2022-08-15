package com.smartech.invoicingprod.integration.service;

import java.text.ParseException;
import java.util.List;

import com.smartech.invoicingprod.integration.xml.rowset.Row;
import com.smartech.invoicingprod.model.Invoice;

public interface InvoicingService {
	boolean createStampInvoice(List<Row> r, String errors);
	void updateStartInvoiceSOAPList() throws ParseException;
	boolean createAdvPayNC(Invoice invoice, double paymentAmount, double exchangeRate, String currencyCode,  String orderNumberCloud);
	void getInvoicedListForUpdateUUID();
	void updatePetitionInvoiceList();
	boolean createStampedPayments(List<Row> r);
	boolean createTransferInvoice(List<Row> r);
	void sendAllErrors();
	void recolectListPayments();
	boolean createInvoiceByInitialCharge(List<Row> r);
	boolean debitMemoProcess(List<Row> r);
}
