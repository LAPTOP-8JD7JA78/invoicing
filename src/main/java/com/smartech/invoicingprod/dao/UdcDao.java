package com.smartech.invoicingprod.dao;

import java.util.List;

import com.smartech.invoicingprod.model.Udc;

public interface UdcDao {
	Udc getUDCById(int id);
	List<Udc> getUDCList(int start, int limit);
	List<Udc> searchCriteria(String query);
	Udc searchBySystemAndKey(String udcSystem, String udcKey);
	Udc searchBySystemAndKeyRef(String udcSystem, String udcKey, String systemRef);
	Udc searchBySystemAndStrValue(String udcSystem, String udcKey, String strValue);
	List<Udc> searchBySystem(String udcSystem);
	List<Udc> advaceSearch(String udcSystem, String udcKey,String systemRef,String keyRef);
	List<Udc> advaceSearch(String udcSystem, String udcKey,String strValue1,String strValue2, int start, int limit);
	boolean saveUDC(Udc udc);
	boolean updateUDC(Udc udc);
	boolean deleteUDC(int id);
	
	int getTotalRecords();
	
	List<Udc> getStoreCB(String udcSystem, String udcKey, int start, int limit);
	int getStoreCB(String udcSystem, String udcKey);
}
