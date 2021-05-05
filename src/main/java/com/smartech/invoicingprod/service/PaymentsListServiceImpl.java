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
	
}
