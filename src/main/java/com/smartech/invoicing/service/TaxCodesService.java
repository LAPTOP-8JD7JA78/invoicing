package com.smartech.invoicing.service;

import java.util.List;

import com.smartech.invoicing.model.TaxCodes;

public interface TaxCodesService {
	boolean saveTC(TaxCodes r);
	boolean updateTC(TaxCodes r);
	boolean deleteTC(int id);
	TaxCodes getTCById(long id);
	List<TaxCodes> getTCList(int start, int limit);
}
