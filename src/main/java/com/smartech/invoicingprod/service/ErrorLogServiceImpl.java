package com.smartech.invoicing.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartech.invoicing.dao.ErrorLogDao;
import com.smartech.invoicing.model.ErrorLog;

@Service("errorLogService")
public class ErrorLogServiceImpl implements ErrorLogService{

	@Autowired
	ErrorLogDao errorLogDao;
	
	@Override
	public boolean saveError(ErrorLog er) {
		return errorLogDao.saveError(er);
	}

	@Override
	public boolean updateError(ErrorLog er) {
		return errorLogDao.updateError(er);
	}

	@Override
	public ErrorLog searchError(String error, String orderNumber) {
		return errorLogDao.searchError(error, orderNumber);
	}

	@Override
	public List<ErrorLog> getAllError(boolean isNew) {
		return errorLogDao.getAllError(isNew);
	}

}
