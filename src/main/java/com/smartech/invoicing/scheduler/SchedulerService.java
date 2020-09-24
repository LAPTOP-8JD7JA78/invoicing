package com.smartech.invoicing.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.smartech.invoicing.integration.AnalyticsService;
import com.smartech.invoicing.integration.RESTService;
import com.smartech.invoicing.integration.dto.AnalyticsDTO;
import com.smartech.invoicing.integration.json.invorg.InventoryOrganization;
import com.smartech.invoicing.integration.util.AppConstants;
import com.smartech.invoicing.integration.xml.rowset.Row;
import com.smartech.invoicing.integration.xml.rowset.Rowset;

public class SchedulerService {
	
	@Autowired
	AnalyticsService analyticsService;
	@Autowired
	RESTService restService;
	
	//@Scheduled(fixedDelay=1000, initialDelay=1000)
	public void testSchedule() {
		Rowset r = analyticsService.executeAnalyticsWS(AppConstants.ORACLE_USER, AppConstants.ORACLE_PASS, 
				AppConstants.SERVICE_TEST1, null);
		if(!r.getRow().isEmpty()) {
			for(Row row : r.getRow()) {
				System.out.println(row.getColumn0());
			}
		}
		
	}
	
	//@Scheduled(fixedDelay=1000, initialDelay=1000)
	public void InvoicesSchedule() {
		AnalyticsDTO analytics = new AnalyticsDTO();
		analytics.setAr_Report_date("2020-09-21 21:00:00");;
		Rowset r = analyticsService.executeAnalyticsWS(AppConstants.ORACLE_USER, AppConstants.ORACLE_PASS, 
				AppConstants.SERVICE_AR_REPORT_INVOICES, analytics);
		if(!r.getRow().isEmpty()) {
			for(Row ro: r.getRow()) {
				System.out.println(ro);
			}
		}
		
	}

	
//	@Scheduled(fixedDelay=1000, initialDelay=1000)
	public void testRestService() {
		InventoryOrganization response = restService.getInventoryOrganization();
		if(response != null && !response.getItems().isEmpty()) {
			for(com.smartech.invoicing.integration.json.invorg.Item item : response.getItems()) {
				System.out.println(item.getOrganizationCode() + "-" + item.getOrganizationId());
			}
		}
	}
    
}
