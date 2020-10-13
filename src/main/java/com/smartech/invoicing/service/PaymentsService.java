package com.smartech.invoicing.service;

import java.util.List;

import com.smartech.invoicing.model.Payments;

public interface PaymentsService {
	List<Payments> getPaymentsList(String reference);
	Payments getPayment(long id);
	List<Payments> getPaymentsListByStatus(List<String> otList);
	boolean updatePayment(Payments pay); 
}