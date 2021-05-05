package com.smartech.invoicing.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartech.invoicing.dao.TaxCodesDao;
import com.smartech.invoicing.model.TaxCodes;

@Service("taxCodesService")
public class TaxCodesServiceImpl implements TaxCodesService{
	@Autowired
	TaxCodesDao taxCodesDao;
	
	@Override
	public boolean saveTC(TaxCodes r) {
		//Corroborar actionsRoles
		if(taxCodesDao.saveTaxCodes(r)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean updateTC(TaxCodes r) {
		//Corroborar actionsRoles
		if(taxCodesDao.updateTaxCodes(r)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteTC(int id) {
		if(taxCodesDao.deleteTaxCodes(id)) {
			return true;
		}
		return false;
	}

	@Override
	public TaxCodes getTCById(long id) {
		return taxCodesDao.getTaxCodesById(id);
	}

	@Override
	public List<TaxCodes> getTCList(int start, int limit) {
		return taxCodesDao.getTaxCodesList(start, limit);
	}
}
