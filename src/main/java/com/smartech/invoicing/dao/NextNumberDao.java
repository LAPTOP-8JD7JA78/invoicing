package com.smartech.invoicing.dao;

import com.smartech.invoicing.model.Branch;
import com.smartech.invoicing.model.NextNumber;

public interface NextNumberDao {
	int getLastNumber(String OrderType, Branch Organzation);
	NextNumber getNumberCon(String OrderType, Branch Org);
	NextNumber getNumber(String orderType, Branch org); 
}
