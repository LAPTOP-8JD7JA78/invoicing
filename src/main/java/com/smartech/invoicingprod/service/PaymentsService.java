package com.smartech.invoicingprod.service;

import java.util.List;

import com.smartech.invoicingprod.model.Payments;

public interface PaymentsService {
	List<Payments> getPaymentsList(String reference);
	Payments getPayment(String id);
	List<Payments> getPaymentsListByStatus(List<String> otList);
	boolean updatePayment(Payments pay); 
	List<Payments> getPaymentsStatus(String status);
	Payments getPaymentByName(String fileName);
	Payments getPaymentsById(String id);
	List<Payments> getPayByAdv(String uuid);
	Payments getPayByUuidRNumber(String receipt, String uuid);
	List<Payments> getAllError(boolean isError);
	Payments getPaymentsByCusAndReceipt(String receiptNumber, String customerName);
	
}
