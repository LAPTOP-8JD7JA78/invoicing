package com.smartech.invoicing.dao;

import java.util.List;

import com.smartech.invoicing.model.Payments;

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
}
