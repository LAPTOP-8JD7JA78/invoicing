package com.smartech.invoicingprod.dao;

import com.smartech.invoicingprod.model.Branch;
import com.smartech.invoicingprod.model.Company;
import com.smartech.invoicingprod.model.NextNumber;

public interface NextNumberDao {
	int getLastNumber(String OrderType, Branch Organzation);
	NextNumber getNumberCon(String OrderType, Branch Org);
	NextNumber getNumber(String orderType, Branch org); 
	NextNumber getLastNumberByItem(String OrderType, Company Organzation);
	NextNumber existCombo(String OrderType, Company Organzation);
	NextNumber getNumberById(int id);
}
