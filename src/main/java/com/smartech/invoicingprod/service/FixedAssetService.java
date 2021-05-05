package com.smartech.invoicing.service;

import com.smartech.invoicing.model.FixedAsset;

public interface FixedAssetService {
	boolean saveFA(FixedAsset fa);
	boolean updateFA(FixedAsset fa);
	FixedAsset searchByAssetNumber(String fixedNumber);
	FixedAsset searchByPersonAssign(String person);
}
