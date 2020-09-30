package com.smartech.invoicing.integration.service;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.smartech.invoicing.model.Invoice;

@Service("stampedService")
public class StampedServiceImpl implements StampedService{
	
	static Logger log = Logger.getLogger(StampedServiceImpl.class.getName());
	
	@Override
	public boolean createFileFac(Invoice i) {
		try {
		
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean createPaymentsFile(Invoice i) {
		try {
			
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
