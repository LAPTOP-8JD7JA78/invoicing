package com.smartech.invoicing.scheduler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.smartech.invoicing.dao.InvoiceDao;
import com.smartech.invoicing.integration.AnalyticsService;
import com.smartech.invoicing.integration.RESTService;
import com.smartech.invoicing.integration.SOAPService;
import com.smartech.invoicing.integration.dto.AnalyticsDTO;
import com.smartech.invoicing.integration.json.invorg.InventoryOrganization;
import com.smartech.invoicing.integration.service.InvoicingService;
import com.smartech.invoicing.integration.service.StampedService;
import com.smartech.invoicing.integration.util.AppConstants;
import com.smartech.invoicing.integration.xml.rowset.Row;
import com.smartech.invoicing.integration.xml.rowset.Rowset;
import com.smartech.invoicing.model.Invoice;
import com.smartech.invoicing.model.Payments;
import com.smartech.invoicing.service.InvoiceService;
import com.smartech.invoicing.service.PaymentsService;

public class SchedulerService {
	
	@Autowired
	AnalyticsService analyticsService;
	@Autowired
	RESTService restService;
	@Autowired
	SOAPService soapService;
	@Autowired
	InvoicingService invoicingService;
	@Autowired
	InvoiceService invoiceService;
	@Autowired
	InvoiceDao invoiceDao;
	@Autowired
	StampedService stampedService;
	@Autowired
	PaymentsService paymentsService;
	
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
	
//	@Scheduled(fixedDelay=1000, initialDelay=1000)
	public void InvoicesSchedule() {
		log.info("\'InvoicesSchedule\' is started*******");
		AnalyticsDTO analytics = new AnalyticsDTO();
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(new Date());
//		calendar.add(Calendar.HOUR, 5);
//		calendar.add(Calendar.MINUTE, -6);	
//		String fecha = sdf.format(calendar.getTime());
//		System.out.println(sdf.format(calendar.getTime()));
		analytics.setAr_Report_date("2020-10-01 00:00:00");;
		Rowset r = analyticsService.executeAnalyticsWS(AppConstants.ORACLE_USER, AppConstants.ORACLE_PASS, 
				AppConstants.SERVICE_AR_REPORT_INVOICES, analytics);
		if(!r.getRow().isEmpty()) {
			if(!invoicingService.createStampInvoice(r.getRow())) {
				System.out.println(false);
			}
		}else {
			log.warn("REPORTS " + r.getRow() + " MESSAGE TO READ");
		}
		log.info("\'InvoicesSchedule\' is finished*******");
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
		try {
//			invoicingService.updateStartInvoiceList();
			invoicingService.updateStartInvoiceSOAPList();
		}catch(Exception e) {
			e.printStackTrace();
			log.error("ERROR DURANTE EL PROCESO DE \'getDataForNewOrders\'-----------------------------", e);
		}
		log.info("\'getDataForNewOrders\' is finished*******");
	}
	
//	@Scheduled(fixedDelay=1000, initialDelay=1000)
	public void getDataForPetitionOrders() {
		log.info("\'getDataForPetitionOrders\' is started*******");
		try {
			invoicingService.updatePetitionInvoiceList();
		}catch(Exception e) {
			e.printStackTrace();
			log.error("ERROR DURANTE EL PROCESO DE \'getDataForPetitionOrders\'-----------------------------", e);
		}
		log.info("\'getDataForPetitionOrders\' is finished*******");
	}
	
//	@Scheduled(fixedDelay=1000, initialDelay=1000)
	public void getPendingData() {
		log.info("\'getPendingData\' is started*******");
		List<String> arr = new ArrayList<String>();
		List<Invoice> cinv = new ArrayList<Invoice>();
		List<Invoice> inv = invoiceDao.getInvoiceListByStatusCode(AppConstants.STATUS_PENDING, "");
		List<Payments> payList = paymentsService.getPaymentsStatus(AppConstants.STATUS_PENDING);
		for(Invoice i: inv) {
			if(!arr.contains(i.getFolio())) {				
				cinv.add(i);
				arr.add(i.getFolio());
			}
		}
		if(!cinv.isEmpty()) {
			for(Invoice in: cinv) {
				if(!stampedService.createFileFac(in)) {
					log.error("PENDING STATUS (INVOICES): " + in);
				}	
			}
		}else {
			log.warn("PENDING STATUS " + cinv + "DON´T HAVA ANY DATA (INVOICES)");
		}
		
		if(!payList.isEmpty()) {
			for(Payments pay: payList) {
				if(!stampedService.createPaymentsFile(pay)) {
					log.error("PENDING STATUS (PAYMENTS): " + pay);
				}
			}
		}else {
			log.warn("PENDING STATUS " + cinv + "DON´T HAVA ANY DATA (PAYMENTS)");
		}
		log.info("\'getPendingData\': is finished********");
		
	}
	
//	@Scheduled(fixedDelay=1000, initialDelay=1000)
	public void readDataPac() {
		log.info("\'readDataPac\' is started*******");
		if(!stampedService.readDataFromTxt()) {
			log.warn("READ DATA PAC: ERROR WHEN READ DATA FROM PAC");
		}
		log.info("\'readDataPac\': is finished********");
	}
	
	@Scheduled(fixedDelay=1000, initialDelay=1000)
	public void updateUUIDOracleERP() {
		log.info("\'updateUUIDOracleERP\' is started*******");
		try {
			invoicingService.getInvoicedListForUpdateUUID();
		}catch(Exception e) {
			e.printStackTrace();
			log.error("ERROR DURANTE EL PROCESO DE \'updateUUIDOracleERP\'-----------------------------", e);
		}
		log.info("\'updateUUIDOracleERP\': is finished********");
	}
	
//	@Scheduled(fixedDelay=1000, initialDelay=5000)
	public void createPayments() {
		log.info("\'createPayments\' is started*******");
		try {
			AnalyticsDTO analytics = new AnalyticsDTO();
//			Calendar calendar = Calendar.getInstance();
//			calendar.setTime(new Date());
//			calendar.add(Calendar.HOUR, 5);
//			calendar.add(Calendar.MINUTE, -6);	
//			String fecha = sdf.format(calendar.getTime());
//			System.out.println(sdf.format(calendar.getTime()));
			analytics.setAr_Report_date("2020-10-14 00:00:00");;
			Rowset r = analyticsService.executeAnalyticsWS(AppConstants.ORACLE_USER, AppConstants.ORACLE_PASS, 
					AppConstants.SERVICE_AR_REPORT_PAYMENTS, analytics);
			if(!r.getRow().isEmpty()) {
				if(!invoicingService.createStampedPayments(r.getRow())) {
					System.out.println(false);
				}
			}else {
				log.warn("REPORTS " + r.getRow() + " MESSAGE TO READ");
			}
		}catch(Exception e) {
			e.printStackTrace();
			log.error("ERROR DURANTE EL PROCESO DE \'createPayments\'-----------------------------", e);
		}
		log.info("\'createPayments\': is finished********");
	}
}
