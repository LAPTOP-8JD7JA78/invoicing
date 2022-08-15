package com.smartech.invoicingprod.dao;

import java.util.List;

import com.smartech.invoicingprod.model.Payments;

public interface PaymentsDao {
	List<Payments> getPayments(String uuid);
	List<Payments> getPaymentsList(String uuid, String customerName, String folioRel);
	List<Payments> getInvoiceDetails(int start, int limit);
	Payments getPayment(String receiptNumber);
	Payments getReceiptId(String receiptNumberId);
	List<Payments> getPaymentsListByStatus(List<String> otList);
	boolean updatePayment(Payments pay);
	List<Payments> getPaymentsByStatus(String status);
	Payments getPaymentsByName(String fileName);
	Payments getPaymentById(String Id);
	List<Payments> PaymentsByAdv(String uuid);
	Payments getPayByUuidRNumber(String receipt, String uuid);
	Payments getPayByUuidRId(String receiptId, String uuid, String folioRel);
	List<Payments> getAllError(boolean error);
	Payments getPayByRecNumberAndCustomer(String receipt, String customerName);
	List<Payments> getPaymentListReceitId(String receiptId);
	List<Payments> getPaymentsListCustomer(String uuid, String customerName, String folioRel);
}
