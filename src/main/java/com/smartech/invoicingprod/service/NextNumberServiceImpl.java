package com.smartech.invoicingprod.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartech.invoicingprod.dao.NextNumberDao;
import com.smartech.invoicingprod.model.Branch;
import com.smartech.invoicingprod.model.Company;
import com.smartech.invoicingprod.model.NextNumber;

@Service("nextNumberService")
public class NextNumberServiceImpl implements NextNumberService{
	@Autowired
	NextNumberDao nextNumberDao;
	
	@Override
	public int getNextNumber(String orderType, Branch organization) {
		return nextNumberDao.getLastNumber(orderType, organization);
	}

	@Override
	public NextNumber getNumberCon(String orderType, Branch org) {
		return nextNumberDao.getNumberCon(orderType, org);
	}

	@Override
	public NextNumber getNumber(String orderType, Branch org) {
		return nextNumberDao.getNumber(orderType, org);
	}
	
	@Override
	public NextNumber getNextNumberByItem(String orderType, Company organization) {
		return nextNumberDao.getLastNumberByItem(orderType, organization);
	}

	@Override
	public boolean existCombo(String orderType, Company organization) {
		NextNumber nn = nextNumberDao.existCombo(orderType, organization);
		if(nn != null) {
			return true;
		}
		return false;
	}

	@Override
	public NextNumber getNumberById(int id) {
		return nextNumberDao.getNumberById(id);
	}
}
