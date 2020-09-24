package com.smartech.invoicing.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.smartech.invoicing.dto.InvoicesByReportsDTO;
import com.smartech.invoicing.model.Invoice;

@Service("invoiceService")
public class InvoiceServiceImpl implements InvoiceService{
	@Override
	public List<Invoice> createInvoiceProcess(HttpServletRequest request, InvoicesByReportsDTO[] obj,
			String invoiceTotal, String invoiceCustomer, String invoiceStreet, String invoiceState, String invoiceCity,
			String invoiceCountry, String invoiceAddressNumber, String invoiceZip, String invoiceTaxIdentifier,
			String invoiceEmail, String invoiceUsoCFDI, String invoicePaymentType, long invoiceId, String invoiceUUID,
			String invoiceUUIDReference, boolean isGlobalInvoice, boolean isNCType, String paymentMethod, String usr,
			StringBuilder messageResult) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean reStampedInvoiceProcess(HttpServletRequest request, String id, String total, String number,
			String rfc, String email, String usr) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void downloadFilesPDFInvoice(long invoiceNumber, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void downloadFilesXmlInvoice(long invoiceNumber, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void generatePayments(InvoicesByReportsDTO so, double payment, int numberPayment,
			HttpServletRequest request, String user) {
		// TODO Auto-generated method stub
		
	}
}
