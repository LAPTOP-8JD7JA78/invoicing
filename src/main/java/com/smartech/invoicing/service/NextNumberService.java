package com.smartech.invoicing.service;

import com.smartech.invoicing.model.Branch;
import com.smartech.invoicing.model.NextNumber;

public interface NextNumberService {
	int getNextNumber(String orderType, String organization);
	NextNumber  getNumber(String orderType, String org);
	NextNumber  getNumber(String orderType, Branch org);
}
