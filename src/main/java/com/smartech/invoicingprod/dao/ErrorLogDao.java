package com.smartech.invoicingprod.dao;

import java.util.List;

import com.smartech.invoicingprod.model.ErrorLog;

public interface ErrorLogDao {
	boolean saveError(ErrorLog e);
	boolean updateError(ErrorLog e);
	ErrorLog searchError(String error, String orderNumber);
	List<ErrorLog> getAllError(boolean isNew);
}
