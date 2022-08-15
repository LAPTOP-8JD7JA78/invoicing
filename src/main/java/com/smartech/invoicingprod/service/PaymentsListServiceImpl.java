package com.smartech.invoicingprod.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartech.invoicingprod.dao.PaymentsListDao;
import com.smartech.invoicingprod.model.PaymentsList;

@Service("paymentsListService")
public class PaymentsListServiceImpl implements PaymentsListService{
	
	@Autowired
	PaymentsListDao paymentsListDao;

	@Override
	public boolean savePaymentsList(PaymentsList pl) {
		return paymentsListDao.saveListpay(pl);
	}

	@Override
	public boolean updatePaymentsList(PaymentsList pl) {
		return paymentsListDao.updateListPay(pl);
	}

	@Override
	public List<PaymentsList> getAllPayList(String status) {
		return paymentsListDao.getListPay(status);
	}

	@Override
	public PaymentsList getByReceiptNumber(String receiptNumber) {
		return paymentsListDao.getByReceiptNumber(receiptNumber);
	}

	@Override
	public PaymentsList getByReceiptNumberCustomer(String receiptNumber, String customer) {
		return paymentsListDao.getByReceiptNumberCustomer(receiptNumber, customer);
	}

	@Override	
	public PaymentsList getByReceiptIdCustomer(String receiptId, String customer) {
		return paymentsListDao.getByReceiptIdCustomer(receiptId, customer);
	}

	@Override	
	public PaymentsList getByReceiptIdCustomerLike(String receiptId, String customer) {
		return paymentsListDao.getByReceiptIdCustomerLike(receiptId, customer);
	}
	
}
