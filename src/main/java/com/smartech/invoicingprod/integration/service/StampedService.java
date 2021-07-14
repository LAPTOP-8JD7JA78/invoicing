package com.smartech.invoicingprod.integration.service;

import java.util.List;

import com.smartech.invoicingprod.model.Invoice;
import com.smartech.invoicingprod.model.Payments;
import com.smartech.invoicingprod.model.PaymentsList;

public interface StampedService {
	public boolean createFileFac(Invoice i);
	public boolean createPaymentsFile(Payments i);
	public boolean readDataFromTxt();
	public boolean creatPaymentListFile(List<PaymentsList> pl);
	public boolean createFileForCancel(Invoice inv);
	public boolean createDistPortalInvoice(Invoice inv);
	public boolean createDistPortalPayment(Payments pay, PaymentsList payList, boolean isUniquePayment);
}
