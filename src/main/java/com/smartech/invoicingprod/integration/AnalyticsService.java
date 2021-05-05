package com.smartech.invoicingprod.integration;

import com.smartech.invoicingprod.integration.dto.AnalyticsDTO;
import com.smartech.invoicingprod.integration.xml.rowset.Rowset;

public interface AnalyticsService {

	public Rowset executeAnalyticsWS(String user, String pass, String service, AnalyticsDTO dto);
	
}
