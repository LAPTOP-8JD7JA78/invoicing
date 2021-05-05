package com.smartech.invoicing.integration.service;

import com.smartech.invoicing.model.Invoice;
import com.smartech.invoicing.model.Payments;

public interface StampedService {
	public boolean createFileFac(Invoice i);
	public boolean createPaymentsFile(Payments i);
	public boolean readDataFromTxt();
}
