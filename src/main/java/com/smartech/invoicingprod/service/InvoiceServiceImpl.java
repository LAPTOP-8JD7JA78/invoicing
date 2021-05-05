package com.smartech.invoicingprod.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartech.invoicingprod.dao.InvoiceDao;
import com.smartech.invoicingprod.dto.InvoicesByReportsDTO;
import com.smartech.invoicingprod.model.Invoice;

@Service("invoiceService")
public class InvoiceServiceImpl implements InvoiceService{
	@Autowired
	InvoiceDao invoiceDao;
	
	@Override
	public List<Invoice> createInvoiceProcess(HttpServletRequest request, InvoicesByReportsDTO[] obj,
			String invoiceTotal, String invoiceCustomer, String invoiceStreet, String invoiceState, String invoiceCity,
			String invoiceCountry, String invoiceAddressNumber, String invoiceZip, String invoiceTaxIdentifier,
			String invoiceEmail, String invoiceUsoCFDI, String invoicePaymentType, long invoiceId, String invoiceUUID,
			String invoiceUUIDReference, boolean isGlobalInvoice, boolean isNCType, String paymentMethod, String usr,
			StringBuilder messageResult) {
		return null;
	}

	@Override
	public boolean reStampedInvoiceProcess(HttpServletRequest request, String id, String total, String number,
			String rfc, String email, String usr) {
		return false;
	}

	@Override
	public void downloadFilesPDFInvoice(long invoiceNumber, HttpServletResponse response) {
		
	}

	@Override
	public void downloadFilesXmlInvoice(long invoiceNumber, HttpServletResponse response) {
				
	}

	@Override
	public void generatePayments(InvoicesByReportsDTO so, double payment, int numberPayment,
			HttpServletRequest request, String user) {
				
	}

	@Override
	public boolean createInvoice(Invoice inv) {
		try {			
			if(!invoiceDao.saveInvoice(inv)) {
				return false;
			}
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
}
