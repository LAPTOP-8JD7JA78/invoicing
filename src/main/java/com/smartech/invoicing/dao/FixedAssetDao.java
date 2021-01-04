package com.smartech.invoicing.dao;

import com.smartech.invoicing.model.FixedAsset;

public interface FixedAssetDao {
	boolean saveFixAsset(FixedAsset fa);
	boolean updateFixAsset(FixedAsset fa);
	FixedAsset searchByAssetNumber(String fixedNumber);
	FixedAsset searchByPersonAssing(String personAssing);
}
