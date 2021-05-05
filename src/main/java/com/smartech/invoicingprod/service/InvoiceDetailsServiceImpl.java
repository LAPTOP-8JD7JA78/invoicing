package com.smartech.invoicingprod.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartech.invoicingprod.dao.InvoiceDetailsDao;
import com.smartech.invoicingprod.model.InvoiceDetails;

@Service("invoiceDetailsService")
public class InvoiceDetailsServiceImpl implements InvoiceDetailsService{
	@Autowired
	InvoiceDetailsDao invoiceDetailsDao;
	
	public boolean saveInvoiceDetails(InvoiceDetails r) {
		return invoiceDetailsDao.saveInvoiceDetails(r);
	}

	public boolean updateInvoiceDetails(InvoiceDetails r) {
		return invoiceDetailsDao.updateInvoiceDetails(r);
	}

	@Override
	public List<InvoiceDetails> getInvoiceById(long id) {
		return invoiceDetailsDao.getInvoiceById(id);
	}

	@Override
	public List<InvoiceDetails> getInvoiceDetails(int start, int limit) {
		return invoiceDetailsDao.getInvoiceDetails(start, limit);
	}
}
