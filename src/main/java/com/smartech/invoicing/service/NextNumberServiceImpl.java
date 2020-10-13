package com.smartech.invoicing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartech.invoicing.dao.NextNumberDao;
import com.smartech.invoicing.model.Branch;
import com.smartech.invoicing.model.NextNumber;

@Service("nextNumberService")
public class NextNumberServiceImpl implements NextNumberService{
	@Autowired
	NextNumberDao nextNumberDao;
	
	@Override
	public int getNextNumber(String orderType, Branch organization) {
		return nextNumberDao.getLastNumber(orderType, organization);
		//return 0;
	}

	@Override
	public NextNumber getNumberCon(String orderType, Branch org) {
		return nextNumberDao.getNumberCon(orderType, org);
	}

	@Override
	public NextNumber getNumber(String orderType, Branch org) {
		return nextNumberDao.getNumber(orderType, org);
	}
}
