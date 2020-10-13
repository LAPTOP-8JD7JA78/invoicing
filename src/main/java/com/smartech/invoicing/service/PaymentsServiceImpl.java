package com.smartech.invoicing.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartech.invoicing.dao.PaymentsDao;
import com.smartech.invoicing.model.Payments;

@Service("paymentsService")
public class PaymentsServiceImpl implements PaymentsService{

	@Autowired
	PaymentsDao paymentsDao;

	@Override
	public List<Payments> getPaymentsList(String reference) {
		return paymentsDao.getPayments(reference);
	}

	@Override
	public Payments getPayment(long id) {
		return paymentsDao.getPayment(id);
	}
}
