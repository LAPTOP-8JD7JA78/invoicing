package com.smartech.invoicing.scheduler;

import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.smartech.invoicing.integration.AnalyticsService;
import com.smartech.invoicing.integration.RESTService;
import com.smartech.invoicing.integration.SOAPService;
import com.smartech.invoicing.integration.dto.AnalyticsDTO;
import com.smartech.invoicing.integration.json.invorg.InventoryOrganization;
import com.smartech.invoicing.integration.json.salesorder.SalesOrder;
import com.smartech.invoicing.integration.json.salesorderai.SalesOrderAI;
import com.smartech.invoicing.integration.service.InvoicingService;
import com.smartech.invoicing.integration.util.AppConstants;
import com.smartech.invoicing.integration.xml.rowset.Row;
import com.smartech.invoicing.integration.xml.rowset.Rowset;

public class SchedulerService {
	
	@Autowired
	AnalyticsService analyticsService;
	@Autowired
	RESTService restService;
	@Autowired
	SOAPService soapService;
	@Autowired
	InvoicingService invoicingService;
	
	static Logger log = Logger.getLogger(SchedulerService.class.getName());
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
//	@Scheduled(fixedDelay=1000, initialDelay=1000)
	public void testSchedule() {
		log.info("\'testSchedule\' is started*******");
		Rowset r = analyticsService.executeAnalyticsWS(AppConstants.ORACLE_USER, AppConstants.ORACLE_PASS, 
				AppConstants.SERVICE_TEST1, null);
		if(r != null && !r.getRow().isEmpty()) {
			for(Row row : r.getRow()) {
				System.out.println(row.getColumn0());
			}
		}
		log.info("\'testSchedule\' is finished*******");
	}
	
	@Scheduled(fixedDelay=1000, initialDelay=1000)
	public void InvoicesSchedule() {
		AnalyticsDTO analytics = new AnalyticsDTO();
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(new Date());
//		calendar.add(Calendar.HOUR, 5);
//		calendar.add(Calendar.MINUTE, -6);	
//		String fecha = sdf.format(calendar.getTime());
//		System.out.println(sdf.format(calendar.getTime()));
		analytics.setAr_Report_date("2020-09-01 14:30:30");;
		Rowset r = analyticsService.executeAnalyticsWS(AppConstants.ORACLE_USER, AppConstants.ORACLE_PASS, 
				AppConstants.SERVICE_AR_REPORT_INVOICES, analytics);
		if(!r.getRow().isEmpty()) {
			if(!invoicingService.createStampInvoice(r.getRow())) {
				System.out.println(false);
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
	
//	@Scheduled(fixedDelay=1000, initialDelay=1000)
	public void getDataForNewOrders() {
		log.info("\'getDataForNewOrders\' is started*******");
		SalesOrder so = restService.getSalesOrderByOrderNumber("81");
		if(so != null) {
			SalesOrderAI soai = restService.getAddInfoBySalesNumber(so);
			if(soai != null && !soai.getItems().isEmpty()) {
				System.out.println(soai.getItems().get(0).getHeaderEffBUSOCFDIprivateVO().get(0).getUsocfdi());
				System.out.println(soai.getItems().get(0).getHeaderEffBMETODOPAGOprivateVO().get(0).getMetodopago());
				System.out.println(soai.getItems().get(0).getHeaderEffBFORMAPAGOprivateVO().get(0).getFormapago());
			}else {
				log.warn("SALES ORDER " + so.getItems().get(0).getOrderNumber() + "DOESN'T HAVE ADDITIONAL INFORMATION");
			}
		}	
		log.info("\'getDataForNewOrders\' is finished*******");
	}
    
}
