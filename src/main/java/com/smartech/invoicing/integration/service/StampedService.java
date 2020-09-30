package com.smartech.invoicing.integration.service;

import com.smartech.invoicing.model.Invoice;

public interface StampedService {
	public boolean createFileFac(Invoice i);
	public boolean createPaymentsFile(Invoice i);
}
