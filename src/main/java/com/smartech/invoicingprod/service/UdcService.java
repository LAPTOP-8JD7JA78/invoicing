package com.smartech.invoicingprod.service;

import java.util.Date;
import java.util.List;

import com.smartech.invoicingprod.model.Udc;

public interface UdcService {
	Udc getUdcById(int id);
	
	List<Udc> getUDCList(int start, int limit);
	public int getTotalRecords();
	
	List<Udc> searchCriteria(String query);
	Udc searchBySystemAndKey(String udcSystem, String udcKey);
	List<Udc> advaceSearch(String udcSystem, String udcKey,String systemRef,String keyRef);
	List<Udc> advaceSearch(String udcSystem, String udcKey,String systemRef,String keyRef, int start, int limit);
	List<Udc> searchBySystem(String udcSystem);
	Udc searchBySystemAndKeyRef(String udcSystem, String udcKey, String systemRef);
	Udc searchBySystemAndStrValue(String udcSystem, String udcKey, String strValue);
	boolean save(Udc udc, Date date, String user);
	boolean update(Udc udc, Date date, String user);
	boolean delete(int id);
	
	public List<Udc> getStoreCB(String udcSystem, String udcKey, int start, int limit);
	public int getStoreCB(String udcSystem, String udcKey);
}
