package com.smartech.invoicingprod.service;

import java.util.List;

import com.smartech.invoicingprod.model.Payments;

public interface PaymentsService {
	List<Payments> getPaymentsList(String reference);
	List<Payments> getPaymentsListReference(String reference, String customerName, String folioRel);
	Payments getPayment(String id);
	Payments getReceiptById(String id);
	List<Payments> getPaymentsListByStatus(List<String> otList);
	boolean updatePayment(Payments pay); 
	List<Payments> getPaymentsStatus(String status);
	Payments getPaymentByName(String fileName);
	Payments getPaymentsById(String id);
	List<Payments> getPayByAdv(String uuid);
	Payments getPayByUuidRNumber(String receipt, String uuid);
	Payments getPayByUuidRId(String receiptId, String uuid, String folioRel);
	List<Payments> getAllError(boolean isError);
	Payments getPaymentsByCusAndReceipt(String receiptNumber, String customerName);
	List<Payments> getPaymentsListReceiptId(String receiptId);;
	List<Payments> getPaymentsListCustomer(String uuid, String customerName, String folioRel);
	Payments getPaymentByFolio(String folioPay);
	
}
