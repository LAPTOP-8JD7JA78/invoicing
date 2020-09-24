package com.smartech.invoicing.service;

import java.util.List;

import com.smartech.invoicing.model.InvoiceDetails;

public interface InvoiceDetailsService {
	boolean saveInvoiceDetails(InvoiceDetails r);
	boolean updateInvoiceDetails(InvoiceDetails r);
	List<InvoiceDetails> getInvoiceById(long id);
	List<InvoiceDetails> getInvoiceDetails(int start, int limit);
}
