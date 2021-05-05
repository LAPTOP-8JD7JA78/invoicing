package com.smartech.invoicing.integration;

import com.smartech.invoicing.integration.dto.AnalyticsDTO;
import com.smartech.invoicing.integration.xml.rowset.Rowset;

public interface AnalyticsService {

	public Rowset executeAnalyticsWS(String user, String pass, String service, AnalyticsDTO dto);
	
}
