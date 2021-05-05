package com.smartech.invoicingprod.service;

import java.util.List;

import com.smartech.invoicingprod.model.ErrorLog;

public interface ErrorLogService {
	boolean saveError(ErrorLog er);
	boolean updateError(ErrorLog er);
	ErrorLog searchError(String error, String orderNumber);
	List<ErrorLog> getAllError(boolean isNew);
}
