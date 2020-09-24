package com.smartech.invoicing.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartech.invoicing.dao.UdcDao;
import com.smartech.invoicing.model.Udc;

@Service("udcService")
public class UdcServiceImpl {
	@Autowired
	private UdcDao udcDao;
	
	public Udc getUdcById(int id){
		return udcDao.getUDCById(id);
	}
	
	public List<Udc> getUDCList(int start, int limit) {
		return udcDao.getUDCList(start, limit);
	}
	
	public List<Udc> searchCriteria(String query){
		return udcDao.searchCriteria(query);
	}
	
	public Udc searchBySystemAndKey(String udcSystem, String udcKey){
		return udcDao.searchBySystemAndKey(udcSystem, udcKey);
	}
	public List<Udc> advaceSearch(String udcSystem, String udcKey,String systemRef,String keyRef){
		return udcDao.advaceSearch(udcSystem, udcKey, systemRef, keyRef);
	}
	public List<Udc> advaceSearch(String udcSystem, String udcKey,String systemRef,String keyRef, int start, int limit){
		return udcDao.advaceSearch(udcSystem, udcKey, systemRef, keyRef, start, limit);
	}
	
	public List<Udc> searchBySystem(String udcSystem){
		return udcDao.searchBySystem(udcSystem);
	}
	
	public Udc searchBySystemAndKeyRef(String udcSystem, String udcKey, String systemRef){
		return udcDao.searchBySystemAndKeyRef(udcSystem, udcKey, systemRef);
	}
	
	public Udc searchBySystemAndStrValue(String udcSystem, String udcKey, String strValue){
		return udcDao.searchBySystemAndStrValue(udcSystem, udcKey, strValue);
	}
	
	public boolean save(Udc udc, Date date, String user){
		udc.setCreationDate(date);
		udc.setCreatedBy(user);
		udc.setUpdatedDate(date);
		udc.setUpdatedBy(user);
		//boolean isTrue = udcDao.saveUDC(udc);
		if(udcDao.saveUDC(udc)) {
			return true;
		}
		return false;
	}
	
	public boolean update(Udc udc, Date date, String user){
		udc.setUpdatedDate(date);
		udc.setUpdatedBy(user);
		//boolean isTrue = udcDao.updateUDC(udc);
		if(udcDao.updateUDC(udc)) {
			return true;
		}
		return false;
	}
	
	public boolean delete(int id){
		//boolean isTrue = udcDao.deleteUDC(id);
		if(udcDao.deleteUDC(id)) {
			return true;
		}
		return false;
	}

	public int getTotalRecords() {
		return udcDao.getTotalRecords();
	}

	public List<Udc> getStoreCB(String udcSystem, String udcKey, int start, int limit) {
		return udcDao.getStoreCB(udcSystem, udcKey, start, limit);
	}

	public int getStoreCB(String udcSystem, String udcKey) {
		return udcDao.getStoreCB(udcSystem, udcKey);
	}
}
