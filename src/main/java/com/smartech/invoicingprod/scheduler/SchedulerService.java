package com.smartech.invoicingprod.scheduler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.smartech.invoicingprod.dao.InvoiceDao;
import com.smartech.invoicingprod.integration.AnalyticsService;
import com.smartech.invoicingprod.integration.RESTService;
import com.smartech.invoicingprod.integration.SOAPService;
import com.smartech.invoicingprod.integration.dto.AnalyticsDTO;
import com.smartech.invoicingprod.integration.json.invorg.InventoryOrganization;
import com.smartech.invoicingprod.integration.service.InvoicingService;
import com.smartech.invoicingprod.integration.service.MailService;
import com.smartech.invoicingprod.integration.service.NumberLetterService;
import com.smartech.invoicingprod.integration.service.StampedService;
import com.smartech.invoicingprod.integration.util.AppConstants;
import com.smartech.invoicingprod.integration.xml.rowset.Row;
import com.smartech.invoicingprod.integration.xml.rowset.Rowset;
import com.smartech.invoicingprod.model.Invoice;
import com.smartech.invoicingprod.model.Payments;
import com.smartech.invoicingprod.model.PaymentsList;
import com.smartech.invoicingprod.model.Udc;
import com.smartech.invoicingprod.service.InvoiceService;
import com.smartech.invoicingprod.service.PaymentsListService;
import com.smartech.invoicingprod.service.PaymentsService;
import com.smartech.invoicingprod.service.UdcService;

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
	@Autowired
	UdcService udcService;
	@Autowired
	MailService mailService;
	@Autowired
	NumberLetterService numberLetterService;
	@Autowired
	PaymentsListService paymentsListService;
	
	static Logger log = Logger.getLogger(SchedulerService.class.getName());
	
	SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	final SimpleDateFormat sdfNoTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
	final SimpleDateFormat formatterUTC = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
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
	public void testEmail() {
//		String date = sdfTime.format(new Date());
//		date = date.toString();
//		System.out.print(date.toString());
//		String alo = date.substring(0, 4);
//		String mes = date.substring(5, 7);
//		String dia = date.substring(8);
//		System.out.println(dia + " DE " + mes + " DEL " + alo);
	}

//	@Scheduled(fixedDelay = 30000, initialDelay = 30000)
	public void InvoicesSchedule() throws ParseException {
		log.info("\'InvoicesSchedule\' is started*******");	
		//sdf.setTimeZone(TimeZone.getTimeZone("UTC"));		
		String nextSearch = sdf.format(new Date());
		AnalyticsDTO analytics = new AnalyticsDTO();
		String errors = "";
		Udc da = udcService.searchBySystemAndKey(AppConstants.UDC_SYSTEM_SCHEDULER, AppConstants.UDC_STRVALUE1_INVOICES);
		if(da != null) {
			Date dateSearch = da.getDateValue();
			String search = sdf.format(dateSearch);
			analytics.setAr_Report_date(search);
			Rowset r = analyticsService.executeAnalyticsWS(AppConstants.ORACLE_USER, AppConstants.ORACLE_PASS, 
					AppConstants.SERVICE_AR_REPORT_INVOICES, analytics);
			if(!r.getRow().isEmpty()) {
				if(!invoicingService.createStampInvoice(r.getRow(), errors)) {
					if(da.getIntValue() == 5) {		
						List<Udc> emails = udcService.searchBySystem(AppConstants.UDC_SYSTEM_EMAILS);
						List<String> email = new ArrayList<String>();
						for(Udc u: emails) {
							email.add(u.getUdcKey());
						}
						mailService.sendMail(email,
								AppConstants.EMAIL_INVOICE_SUBJECT,
								AppConstants.EMAIL_INVOICE_CONTENT + sdf.format(new Date()),
								null);
						String date = sdf.format(new Date());
						da.setDateValue(sdf.parse(date));
						da.setIntValue(0);
						udcService.update(da, new Date(), AppConstants.USER_DEFAULT);
					}else {
						da.setIntValue(da.getIntValue() + 1);
						udcService.update(da, new Date(), AppConstants.USER_DEFAULT);
					}					
				}else {
					da.setDateValue(sdf.parse(nextSearch));
					udcService.update(da, new Date(), AppConstants.USER_DEFAULT);
				}
			}else {
				log.warn("REPORTS " + r.getRow() + " WITHOUT INFORMATON");
				da.setDateValue(sdf.parse(nextSearch));
				da.setIntValue(0);
				udcService.update(da, new Date(), AppConstants.USER_DEFAULT);
			}
		}else {
			log.error("ERROR EN LA BUSQUEDA DE LA UDC (INVOICES) PARA LAS FECHAS DEL REPORTE");
		}
		log.info("\'InvoicesSchedule\' is finished*******");
	}

	
//	@Scheduled(fixedDelay=1000, initialDelay=1000)
	public void testRestService() {
		InventoryOrganization response = restService.getInventoryOrganization();
		if(response != null && !response.getItems().isEmpty()) {
			for(com.smartech.invoicingprod.integration.json.invorg.Item item : response.getItems()) {
				System.out.println(item.getOrganizationCode() + "-" + item.getOrganizationId());
			}
		}
	}
	
//	@Scheduled(fixedDelay = 45000, initialDelay = 45000)
	public void getDataForNewOrders() {
		log.info("\'getDataForNewOrders\' is started*******");
		try {
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
	
//	@Scheduled(fixedDelay = 30000, initialDelay = 30000)
	public void getPendingData() {
		log.info("\'getPendingData\' is started*******");
		//Facturas, notas de credito, transferencias, anticipos
		List<String> arr = new ArrayList<String>();
		List<Invoice> cinv = new ArrayList<Invoice>();
		List<Invoice> inv = invoiceDao.getInvoiceListByStatusCode(AppConstants.STATUS_PENDING, "");		
		for(Invoice i: inv) {
			if(!arr.contains(i.getFolio())) {				
				cinv.add(i);
				arr.add(i.getFolio());
			}
		}
		if(!cinv.isEmpty()) {
			for(Invoice in: cinv) {
				if(!stampedService.createFileFac(in)) {
					log.error("PENDING STATUS (INVOICES): " + in.getFolio() + " - " + in.getFromSalesOrder());
				}	
			}
		}else {
			log.warn("PENDING STATUS " + cinv + "DON´T HAVA ANY DATA (INVOICES)");
		}
		//Pago uno a uno
		List<Payments> payList = paymentsService.getPaymentsStatus(AppConstants.STATUS_PENDING);
		if(!payList.isEmpty()) {
			for(Payments pay: payList) {
				if(!stampedService.createPaymentsFile(pay)) {
					log.error("PENDING STATUS (PAYMENTS): " + pay);
				}
			}
		}else {
			log.warn("PENDING STATUS " + payList + "DON´T HAVA ANY DATA (PAYMENTS)");
		}
		//Lista de pagos
		List<PaymentsList> payListList = paymentsListService.getAllPayList(AppConstants.STATUS_PENDING);
		List<PaymentsList> pl = new ArrayList<PaymentsList>();
		List<String> arrPayList = new ArrayList<String>();
		if(!payListList.isEmpty()) {
			for(PaymentsList p: payListList) {
				if(!arrPayList.contains(String.valueOf(p.getId()))) {				
					pl.add(p);
					arrPayList.add(String.valueOf(p.getId()));
				}
			}
			if(!stampedService.creatPaymentListFile(pl)) {
				log.error("PENDING STATUS (PAYMENTS): " + payListList);
			}
		}else {
			log.warn("PENDING STATUS " + payListList + "DON´T HAVA ANY DATA (PAYMENTS)");
		}
		
		log.info("\'getPendingData\': is finished********");
		
	}
	
//	@Scheduled(fixedDelay = 15000, initialDelay = 15000)
	//@Scheduled(fixedDelay = 30000, initialDelay = 15000)
	public void readDataPac() {
		log.info("\'readDataPac\' is started*******");
		if(!stampedService.readDataFromTxt()) {
			log.warn("READ DATA PAC: ERROR WHEN READ DATA FROM PAC");
		}
		log.info("\'readDataPac\': is finished********");
	}
	
//	@Scheduled(fixedDelay = 15000, initialDelay = 15000)
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
	
//	@Scheduled(fixedDelay = 30000, initialDelay = 30000)
	public void createPayments() {
		log.info("\'createPayments\' is started*******");
		try {
			String nextSearch = sdf.format(new Date());
			AnalyticsDTO analytics = new AnalyticsDTO();
			Udc da = udcService.searchBySystemAndKey(AppConstants.UDC_SYSTEM_SCHEDULER, AppConstants.UDC_STRVALUE1_PAYMENTS);
			if(da != null) {
				Date dateSearch = da.getDateValue();
				String search = sdf.format(dateSearch);
				analytics.setAr_Report_date(search);
				Rowset r = analyticsService.executeAnalyticsWS(AppConstants.ORACLE_USER, AppConstants.ORACLE_PASS, 
						AppConstants.SERVICE_AR_REPORT_PAYMENTS, analytics);
				if(!r.getRow().isEmpty()) {
					if(!invoicingService.createStampedPayments(r.getRow())) {
						System.out.println(false);
						if(da.getIntValue() == 5) {
							List<Udc> emails = udcService.searchBySystem(AppConstants.UDC_SYSTEM_EMAILS);
							List<String> email = new ArrayList<String>();
							for(Udc u: emails) {
								email.add(u.getUdcKey());
							}
							mailService.sendMail(email,
									AppConstants.EMAIL_PAYMENTS_SUBJECT,
									AppConstants.EMAIL_PAYMENTS_CONTENT + sdf.format(new Date()),
									null);
							String date = sdf.format(new Date());
							da.setDateValue(sdf.parse(date));
							da.setIntValue(0);
							udcService.update(da, new Date(), AppConstants.USER_DEFAULT);							
						}else {
							da.setIntValue(da.getIntValue() + 1);
							udcService.update(da, new Date(), AppConstants.USER_DEFAULT);
						}
					}else {
						da.setDateValue(sdf.parse(nextSearch));
						udcService.update(da, new Date(), AppConstants.USER_DEFAULT);
					}
				}else {
					log.warn("REPORTS " + r.getRow() + " MESSAGE TO READ");
					da.setDateValue(sdf.parse(nextSearch));
					da.setIntValue(0);
					udcService.update(da, new Date(), AppConstants.USER_DEFAULT);
				}
			}else {
				log.error("ERROR EN LA BUSQUEDA DE LA UDC (PAYMENTS) PARA LAS FECHAS DEL REPORTE");
			}			
		}catch(Exception e) {
			e.printStackTrace();
			log.error("ERROR DURANTE EL PROCESO DE \'createPayments\'-----------------------------", e);
		}
		log.info("\'createPayments\': is finished********");
	}
	
//	@Scheduled(fixedDelay = 30000, initialDelay = 30000)
	public void createTransfer() {
		log.info("\'createTransfer\' is started*******");
		String nextSearch = sdf.format(new Date());
		AnalyticsDTO analytics = new AnalyticsDTO();
		try {
			Udc da = udcService.searchBySystemAndKey(AppConstants.UDC_SYSTEM_SCHEDULER, AppConstants.UDC_KEY_TRANSFER);
			if(da != null) {
				Date dateSearch = da.getDateValue();
				String search = sdf.format(dateSearch);
				analytics.setAr_Report_date(search);
				Rowset r = analyticsService.executeAnalyticsWS(AppConstants.ORACLE_USER, AppConstants.ORACLE_PASS, 
						AppConstants.SERVICE_AR_REPORT_TRANSFER, analytics);
				if(!r.getRow().isEmpty()) {
					if(!invoicingService.createTransferInvoice(r.getRow())) {
						if(da.getIntValue() == 5) {
							List<Udc> emails = udcService.searchBySystem(AppConstants.UDC_SYSTEM_EMAILS);
							List<String> email = new ArrayList<String>();
							for(Udc u: emails) {
								email.add(u.getUdcKey());
							}
							mailService.sendMail(email,
									AppConstants.EMAIL_TRANSFER_SUBJECT,
									AppConstants.EMAIL_TRANSFER_CONTENT + sdf.format(new Date()), null);
							String date = sdf.format(new Date());
							da.setDateValue(sdf.parse(date));
							da.setIntValue(0);
							udcService.update(da, new Date(), AppConstants.USER_DEFAULT);							
						}else {
							da.setIntValue(da.getIntValue() + 1);
							udcService.update(da, new Date(), AppConstants.USER_DEFAULT);
						}
					}else {
						da.setDateValue(sdf.parse(nextSearch));
						da.setIntValue(0);
						udcService.update(da, new Date(), AppConstants.USER_DEFAULT);
					}
				}else {
					log.warn("REPORTS " + r.getRow() + " MESSAGE TO READ");
				}
			}else {
				log.error("ERROR EN LA BUSQUEDA DE LA UDC (createTransfer) PARA LAS FECHAS DEL REPORTE");
			}
		}catch(Exception e) {
			e.printStackTrace();
			log.error("ERROR AL CREAR EL CFDI DE TRASLADOS: " + e);
		}
		log.info("\'createTransfer\': is finished********");
	}
	
//	@Scheduled(fixedDelay=40000, initialDelay=40000)
	public void sendAllErrors() {
		log.info("\'sendAllErrors\' is started*******");
		try {
			invoicingService.sendAllErrors();
		}catch(Exception e) {
			e.printStackTrace();
			log.error("ERROR DURANTE EL PROCESO DE \'sendAllErrors\'-----------------------------", e);
		}
		log.info("\'sendAllErrors\': is finished********");
	}
	
//	@Scheduled(fixedDelay=15000, initialDelay=15000)
	public void recolectListPayments() {
		log.info("\'recolectListPayments\' is started*******");
		try {
			invoicingService.recolectListPayments();
		}catch(Exception e) {
			e.printStackTrace();
			log.error("ERROR DURANTE EL PROCESO DE \'recolectListPayments\'-----------------------------", e);
		}
		log.info("\'recolectListPayments\': is finished********");
	}
	
//	@Scheduled(fixedDelay = 10000, initialDelay = 10000)
	public void invoicesInitialCharge() {
		log.info("\'invoicesInitialCharge\' is started*******");
		String nextSearch = sdf.format(new Date());
		AnalyticsDTO analytics = new AnalyticsDTO();
		try {
			Udc da = udcService.searchBySystemAndKey(AppConstants.UDC_SYSTEM_SCHEDULER, AppConstants.UDC_KEY_INITIAL_CHARGE);
			if(da != null) {
				Date dateSearch = da.getDateValue();
				String search = sdf.format(dateSearch);
				analytics.setAr_Report_date(search);
				Rowset r = analyticsService.executeAnalyticsWS(AppConstants.ORACLE_USER, AppConstants.ORACLE_PASS, 
						AppConstants.SERVICE_AR_REPORT_INITIAL_CHARGE, analytics);
				if(!r.getRow().isEmpty()) {
					if(!invoicingService.createInvoiceByInitialCharge(r.getRow())) {
						if(da.getIntValue() == 5) {
							List<Udc> emails = udcService.searchBySystem(AppConstants.UDC_SYSTEM_EMAILS);
							List<String> email = new ArrayList<String>();
							for(Udc u: emails) {
								email.add(u.getUdcKey());
							}
							mailService.sendMail(email,
									AppConstants.EMAIL_TRANSFER_SUBJECT,
									AppConstants.EMAIL_TRANSFER_CONTENT + sdf.format(new Date()), null);
							String date = sdf.format(new Date());
							da.setDateValue(sdf.parse(date));
							da.setIntValue(0);
							udcService.update(da, new Date(), AppConstants.USER_DEFAULT);							
						}else {
							da.setIntValue(da.getIntValue() + 1);
							udcService.update(da, new Date(), AppConstants.USER_DEFAULT);
						}
					}else {
						da.setDateValue(sdf.parse(nextSearch));
						da.setIntValue(0);
						udcService.update(da, new Date(), AppConstants.USER_DEFAULT);
					}
				}else {
					log.warn("REPORTS " + r.getRow() + " MESSAGE TO READ");
				}
			}else {
				log.error("ERROR EN LA BUSQUEDA DE LA UDC (createTransfer) PARA LAS FECHAS DEL REPORTE");
			}
		}catch(Exception e) {
			e.printStackTrace();
			log.error("ERROR AL CREAR EL CFDI DE TRASLADOS: " + e);
		}
		log.info("\'invoicesInitialCharge\': is finished********");
	}
}
