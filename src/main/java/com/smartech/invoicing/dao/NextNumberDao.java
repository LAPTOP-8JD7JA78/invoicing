package com.smartech.invoicing.dao;

import com.smartech.invoicing.model.Branch;
import com.smartech.invoicing.model.NextNumber;

public interface NextNumberDao {
	int getLastNumber(String OrderType, String Organzation);
	NextNumber getNumber(String OrderType, String Org);
	NextNumber getNumber(String orderType, Branch org); 
}
