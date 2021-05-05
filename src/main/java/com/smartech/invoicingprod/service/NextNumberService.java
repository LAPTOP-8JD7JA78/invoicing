package com.smartech.invoicingprod.service;

import com.smartech.invoicingprod.model.Branch;
import com.smartech.invoicingprod.model.Company;
import com.smartech.invoicingprod.model.NextNumber;

public interface NextNumberService {
	int getNextNumber(String orderType, Branch organization);
	NextNumber  getNumberCon(String orderType, Branch org);
	NextNumber  getNumber(String orderType, Branch org);
	NextNumber getNextNumberByItem(String orderType, Company organization);
	boolean existCombo(String orderType, Company organization);
	NextNumber getNumberById(int id);
}
