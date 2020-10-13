package com.smartech.invoicing.dao;

import java.util.List;

import com.smartech.invoicing.model.Payments;

public interface PaymentsDao {
	List<Payments> getPayments(String uuid);
	List<Payments> getInvoiceDetails(int start, int limit);
	Payments getPayment(long id);
	List<Payments> getPaymentsListByStatus(List<String> otList);
	boolean updatePayment(Payments pay);
	List<Payments> getPaymentsByStatus(String status);
}
