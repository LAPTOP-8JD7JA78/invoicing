package com.smartech.invoicingprod.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartech.invoicingprod.dao.PaymentsDao;
import com.smartech.invoicingprod.model.Payments;

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

	@Override
	public Payments getPaymentByName(String fileName) {
		return paymentsDao.getPaymentsByName(fileName);
	}

	@Override
	public Payments getPaymentsById(String id) {
		return paymentsDao.getPaymentById(id);
	}

	@Override
	public List<Payments> getPayByAdv(String uuid) {
		return paymentsDao.PaymentsByAdv(uuid);
	}

	@Override
	public Payments getPayByUuidRNumber(String receipt, String uuid) {
		return paymentsDao.getPayByUuidRNumber(receipt, uuid);
	}

	@Override
	public List<Payments> getAllError(boolean isError) {
		return paymentsDao.getAllError(isError);
	}
	
	@Override
	public Payments getPaymentsByCusAndReceipt(String receiptNumber, String customerName) {
		return paymentsDao.getPayByRecNumberAndCustomer(receiptNumber, customerName);
	}
}
