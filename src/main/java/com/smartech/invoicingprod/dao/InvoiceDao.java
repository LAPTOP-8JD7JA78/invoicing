package com.smartech.invoicingprod.dao;

import java.util.List;

import com.smartech.invoicingprod.dto.WarrantyDataProcessDTO;
import com.smartech.invoicingprod.model.Invoice;

public interface InvoiceDao {
	Invoice getSingleInvoiceById(long id);
	Invoice getSingleInvoiceByFolio(String folio, String invType);
	Invoice getSingleInvoiceByFolioLike(String folio, String invType);
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
	public Invoice getSingleInvoiceByFolioSerial(String folioSerial);
	Invoice getInvoiceByUuid(String uuid);
	public Invoice getInvoiceWithOutUuid(String id);
	List<Invoice> getInvoiceToAdv(String orderType, boolean advApplied);
	Invoice getInvoiceByOtFolio(String orderType, String salesOrder, String customerName);
	List<Invoice> getInvoiceByOtFolioCustomer(String orderType, String salesOrder, String customerName);
	List<Invoice> getAllError(boolean isError);
	Invoice getSingleInvoiceByFolioAndType(String folio, String orderType);
	List<Invoice> getAllInvoiceToWarranty(String dataSearch);
	Invoice getInvoiceIdFromInvoiceDetails(long id);
	Invoice getSingleInvoiceByFolioCustomer(String folio, String invType, String customer);
	Invoice getSingleInvoiceByFolioCustomerLike(String folio, String invType, String customer);
	List<Invoice> getInvoiceByUuidReference(String uuidReference);
	Invoice getSingleInvoiceByFromSalesOrderAndType(String fromSalesOrder, String orderType);
	public List<WarrantyDataProcessDTO> getInvoiceForWarranty();
	List<Invoice> getInvoicesByFolio(String folio);
	List<Invoice> getInvoiceByDates(String startDate, String endDate);
}
