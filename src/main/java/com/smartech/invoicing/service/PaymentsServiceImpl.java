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
	public Payments getPayment(String id) {
		return paymentsDao.getPayment(id);
	}

	@Override
	public List<Payments> getPaymentsListByStatus(List<String> otList) {
		return paymentsDao.getPaymentsListByStatus(otList);
	}

	@Override
	public boolean updatePayment(Payments pay) {
		return paymentsDao.updatePayment(pay);
	}

	@Override
	public List<Payments> getPaymentsStatus(String status) {
		return paymentsDao.getPaymentsByStatus(status);
	}
}
