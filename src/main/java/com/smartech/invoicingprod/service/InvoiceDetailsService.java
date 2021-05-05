package com.smartech.invoicingprod.service;

import java.util.List;

import com.smartech.invoicingprod.model.InvoiceDetails;

public interface InvoiceDetailsService {
	boolean saveInvoiceDetails(InvoiceDetails r);
	boolean updateInvoiceDetails(InvoiceDetails r);
	List<InvoiceDetails> getInvoiceById(long id);
	List<InvoiceDetails> getInvoiceDetails(int start, int limit);
}
