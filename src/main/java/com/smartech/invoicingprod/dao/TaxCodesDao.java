package com.smartech.invoicing.dao;

import java.util.List;

import com.smartech.invoicing.model.TaxCodes;

public interface TaxCodesDao {
	boolean saveTaxCodes(TaxCodes tc);
	boolean updateTaxCodes(TaxCodes tc);
	boolean deleteTaxCodes(int id);
	TaxCodes getTaxCodesById(long id);
	List<TaxCodes> getTaxCodesList(int start, int limit);
}
