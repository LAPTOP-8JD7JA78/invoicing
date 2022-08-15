package com.smartech.invoicingprod.dao;

import java.util.List;

import com.smartech.invoicingprod.model.PaymentsList;

public interface PaymentsListDao {
	
	boolean saveListpay(PaymentsList pl);
	boolean updateListPay(PaymentsList pl);
	List<PaymentsList> getListPay(String status);
	PaymentsList getByReceiptNumber(String receiptNumber);
	PaymentsList getByReceiptNumberCustomer(String receiptNumber, String customer);
	PaymentsList getByReceiptIdCustomer(String receiptId, String customer);
	PaymentsList getByReceiptIdCustomerLike(String receiptId, String customer);

}
