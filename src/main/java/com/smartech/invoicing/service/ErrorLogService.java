package com.smartech.invoicing.service;

import java.util.List;

import com.smartech.invoicing.model.ErrorLog;

public interface ErrorLogService {
	boolean saveError(ErrorLog er);
	boolean updateError(ErrorLog er);
	ErrorLog searchError(String error, String orderNumber);
	List<ErrorLog> getAllError(boolean isNew);
}
