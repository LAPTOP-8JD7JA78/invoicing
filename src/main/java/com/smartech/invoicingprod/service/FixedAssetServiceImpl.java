package com.smartech.invoicing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartech.invoicing.dao.FixedAssetDao;
import com.smartech.invoicing.model.FixedAsset;

@Service("fixedAssetService")
public class FixedAssetServiceImpl implements FixedAssetService{

	@Autowired
	FixedAssetDao fixedAssetDao;
	
	@Override
	public boolean saveFA(FixedAsset fa) {
		return fixedAssetDao.saveFixAsset(fa);
	}

	@Override
	public boolean updateFA(FixedAsset fa) {
		return fixedAssetDao.updateFixAsset(fa);
	}

	@Override
	public FixedAsset searchByAssetNumber(String fixedNumber) {
		return fixedAssetDao.searchByAssetNumber(fixedNumber);
	}

	@Override
	public FixedAsset searchByPersonAssign(String person) {
		return fixedAssetDao.searchByPersonAssing(person);
	}

}
