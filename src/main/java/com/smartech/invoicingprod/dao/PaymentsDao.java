package com.smartech.invoicingprod.dao;

import java.util.List;

import com.smartech.invoicingprod.model.Payments;

public interface PaymentsDao {
	List<Payments> getPayments(String uuid);
	List<Payments> getInvoiceDetails(int start, int limit);
	Payments getPayment(String receiptNumber);
	List<Payments> getPaymentsListByStatus(List<String> otList);
	boolean updatePayment(Payments pay);
	List<Payments> getPaymentsByStatus(String status);
	Payments getPaymentsByName(String fileName);
	Payments getPaymentById(String Id);
	List<Payments> PaymentsByAdv(String uuid);
	Payments getPayByUuidRNumber(String receipt, String uuid);
	List<Payments> getAllError(boolean error);
	Payments getPayByRecNumberAndCustomer(String receipt, String customerName);
}
