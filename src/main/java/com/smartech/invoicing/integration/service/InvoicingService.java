package com.smartech.invoicing.integration.service;

import java.util.List;

import com.smartech.invoicing.integration.xml.rowset.Row;

public interface InvoicingService {
	boolean createStampInvoice(List<Row> r);
}
