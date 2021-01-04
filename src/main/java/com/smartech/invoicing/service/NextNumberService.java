package com.smartech.invoicing.service;

import com.smartech.invoicing.model.Branch;
import com.smartech.invoicing.model.Company;
import com.smartech.invoicing.model.NextNumber;

public interface NextNumberService {
	int getNextNumber(String orderType, Branch organization);
	NextNumber  getNumberCon(String orderType, Branch org);
	NextNumber  getNumber(String orderType, Branch org);
	NextNumber getNextNumberByItem(String orderType, Company organization);
}
