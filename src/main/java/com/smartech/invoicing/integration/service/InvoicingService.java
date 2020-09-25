package com.smartech.invoicing.integration.service;

import com.smartech.invoicing.integration.xml.rowset.Row;

public interface InvoicingService {
	boolean createStampInvoice(Row r);
}
