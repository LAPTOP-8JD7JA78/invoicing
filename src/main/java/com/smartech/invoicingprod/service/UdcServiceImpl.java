package com.smartech.invoicingprod.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartech.invoicingprod.dao.UdcDao;
import com.smartech.invoicingprod.model.Udc;

@Service("udcService")
public class UdcServiceImpl implements UdcService{
	@Autowired
	private UdcDao udcDao;
	
	@Override
	public Udc getUdcById(int id){
		return udcDao.getUDCById(id);
	}
	
	@Override
	public List<Udc> getUDCList(int start, int limit) {
		return udcDao.getUDCList(start, limit);
	}
	
	@Override
	public List<Udc> searchCriteria(String query){
		return udcDao.searchCriteria(query);
	}
	
	@Override
	public Udc searchBySystemAndKey(String udcSystem, String udcKey){
		return udcDao.searchBySystemAndKey(udcSystem, udcKey);
	}
	@Override
	public List<Udc> advaceSearch(String udcSystem, String udcKey,String systemRef,String keyRef){
		return udcDao.advaceSearch(udcSystem, udcKey, systemRef, keyRef);
	}
	@Override
	public List<Udc> advaceSearch(String udcSystem, String udcKey,String systemRef,String keyRef, int start, int limit){
		return udcDao.advaceSearch(udcSystem, udcKey, systemRef, keyRef, start, limit);
	}
	
	@Override
	public List<Udc> searchBySystem(String udcSystem){
		return udcDao.searchBySystem(udcSystem);
	}
	
	@Override
	public Udc searchBySystemAndKeyRef(String udcSystem, String udcKey, String systemRef){
		return udcDao.searchBySystemAndKeyRef(udcSystem, udcKey, systemRef);
	}
	
	@Override
	public Udc searchBySystemAndStrValue(String udcSystem, String udcKey, String strValue){
		return udcDao.searchBySystemAndStrValue(udcSystem, udcKey, strValue);
	}
	
	@Override
	public boolean save(Udc udc, Date date, String user){
		udc.setCreationDate(date);
		udc.setCreatedBy(user);
		udc.setUpdatedDate(date);
		udc.setUpdatedBy(user);
		if(udcDao.saveUDC(udc)) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean update(Udc udc, Date date, String user){
		udc.setUpdatedDate(date);
		udc.setUpdatedBy(user);
		if(udcDao.updateUDC(udc)) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean delete(int id){
		if(udcDao.deleteUDC(id)) {
			return true;
		}
		return false;
	}

	@Override
	public int getTotalRecords() {
		return udcDao.getTotalRecords();
	}

	@Override
	public List<Udc> getStoreCB(String udcSystem, String udcKey, int start, int limit) {
		return udcDao.getStoreCB(udcSystem, udcKey, start, limit);
	}

	@Override
	public int getStoreCB(String udcSystem, String udcKey) {
		return udcDao.getStoreCB(udcSystem, udcKey);
	}
}
