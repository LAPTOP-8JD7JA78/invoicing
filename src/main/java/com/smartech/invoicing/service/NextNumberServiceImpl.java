package com.smartech.invoicing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartech.invoicing.dao.NextNumberDao;
import com.smartech.invoicing.model.NextNumber;

@Service("nextNumberService")
public class NextNumberServiceImpl implements NextNumberService{
	@Autowired
	NextNumberDao nextNumberDao;
	
	@Override
	public int getNextNumber(String orderType, String organization) {
		// TODO Auto-generated method stub
		return nextNumberDao.getLastNumber(orderType, organization);
		//return 0;
	}

	@Override
	public NextNumber getNumber(String orderType, String org) {
		// TODO Auto-generated method stub
		return nextNumberDao.getNumber(orderType, org);
	}
}
