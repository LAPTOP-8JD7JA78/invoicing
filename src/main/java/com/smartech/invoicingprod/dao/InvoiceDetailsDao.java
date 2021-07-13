package com.smartech.invoicingprod.dao;

import java.util.List;

import com.smartech.invoicingprod.model.InvoiceDetails;

public interface InvoiceDetailsDao {
	boolean saveInvoiceDetails(InvoiceDetails r);
	boolean updateInvoiceDetails(InvoiceDetails r);
	List<InvoiceDetails> getInvoiceById(long id);
	List<InvoiceDetails> getInvoiceDetails(int start, int limit);
	List<InvoiceDetails> searchBySerialNumber(String itemSerial);
}
