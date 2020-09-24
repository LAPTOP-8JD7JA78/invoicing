package com.smartech.invoicing.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.smartech.invoicing.dto.InvoicesByReportsDTO;
import com.smartech.invoicing.model.Invoice;

public interface InvoiceService {
	public List<Invoice> createInvoiceProcess(HttpServletRequest request, InvoicesByReportsDTO[] obj, String invoiceTotal, String invoiceCustomer,
			String invoiceStreet, String invoiceState, String invoiceCity, String invoiceCountry, String invoiceAddressNumber, 
			String invoiceZip, String invoiceTaxIdentifier, String invoiceEmail, String invoiceUsoCFDI, 
			String invoicePaymentType, long invoiceId, String invoiceUUID, String invoiceUUIDReference, boolean isGlobalInvoice,
			boolean isNCType, String paymentMethod,String usr, StringBuilder messageResult);
	public boolean reStampedInvoiceProcess(HttpServletRequest request, String id,String total,String number,String rfc,String email,String usr);
	public void downloadFilesPDFInvoice(long invoiceNumber, HttpServletResponse response);
	public void downloadFilesXmlInvoice(long invoiceNumber, HttpServletResponse response);
	public void generatePayments(InvoicesByReportsDTO so, double payment, int numberPayment, HttpServletRequest request, String user);
}
