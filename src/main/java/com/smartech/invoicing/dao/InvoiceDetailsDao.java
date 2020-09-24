package com.smartech.invoicing.dao;

import java.util.List;

import com.smartech.invoicing.model.InvoiceDetails;

public interface InvoiceDetailsDao {
	boolean saveInvoiceDetails(InvoiceDetails r);
	boolean updateInvoiceDetails(InvoiceDetails r);
	List<InvoiceDetails> getInvoiceById(long id);
	List<InvoiceDetails> getInvoiceDetails(int start, int limit);
}
