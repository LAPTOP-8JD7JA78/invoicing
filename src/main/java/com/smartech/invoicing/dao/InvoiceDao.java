package com.smartech.invoicing.dao;

import java.util.List;

import com.smartech.invoicing.model.Invoice;

public interface InvoiceDao {
	Invoice getSingleInvoiceById(long id);
	Invoice getSingleInvoiceByFolio(String folio);
	boolean updateInvoice(Invoice o);
	boolean saveInvoice(Invoice o);
	List<Invoice> getInvoiceByParams(String folio, 
									 String Company, String status,
									 String startDate, String endDate,
									 int start, int limit,
									 String customer, String branch,
									 String orderType);
	List<Invoice> getInvoiceListByStatusCode(String status, String orderType);
	List<Invoice> getInvoiceListByStatusCode(String status, List<String> orderType);
	List<Invoice> getInvoiceListByStatusCode(List<String> status, List<String> orderType);
}
