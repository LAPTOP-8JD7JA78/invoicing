package com.smartech.invoicingprod.service;

import java.util.List;

import com.smartech.invoicingprod.model.PaymentsList;

public interface PaymentsListService {
	boolean savePaymentsList(PaymentsList pl);
	boolean updatePaymentsList(PaymentsList pl);
	List<PaymentsList> getAllPayList(String status);
	PaymentsList getByReceiptNumber(String receiptNumber);
}
