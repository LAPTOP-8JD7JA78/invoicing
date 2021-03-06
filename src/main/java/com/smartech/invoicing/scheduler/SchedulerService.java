package com.smartech.invoicing.scheduler;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.smartech.invoicing.dao.InvoiceDao;
import com.smartech.invoicing.integration.AnalyticsService;
import com.smartech.invoicing.integration.RESTService;
import com.smartech.invoicing.integration.SOAPService;
import com.smartech.invoicing.integration.dto.AnalyticsDTO;
import com.smartech.invoicing.integration.json.invorg.InventoryOrganization;
import com.smartech.invoicing.integration.service.InvoicingService;
import com.smartech.invoicing.integration.service.LabelService;
import com.smartech.invoicing.integration.service.MailService;
import com.smartech.invoicing.integration.service.NumberLetterService;
import com.smartech.invoicing.integration.service.StampedService;
import com.smartech.invoicing.integration.util.AppConstants;
import com.smartech.invoicing.integration.util.ResponsiveLetter;
import com.smartech.invoicing.integration.xml.rowset.Row;
import com.smartech.invoicing.integration.xml.rowset.Rowset;
import com.smartech.invoicing.model.Invoice;
import com.smartech.invoicing.model.Payments;
import com.smartech.invoicing.model.Udc;
import com.smartech.invoicing.service.InvoiceService;
import com.smartech.invoicing.service.PaymentsService;
import com.smartech.invoicing.service.UdcService;

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
	LabelService labelService;
	@Autowired
	ResponsiveLetter responsiveLetter;
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
//		List<Udc> emails = udcService.searchBySystem("EMAILS");
//		String e = "lopluis98@gmail.com";
//		List<String> email = new ArrayList<String>();
//		email.add(e);
//		for(Udc u: emails) {
//			email.add(u.getUdcKey());
//		}
//		mailService.sendMail(email,
//				"ERROR EN PROCESO DE REPORTE (INVOICE)",
//				"SE HAN HECHO 5 INTENTOS DE PROCESAR LA INFORMACION PERO SE HAN OBTENIDO ERRORES",
//				null);

//		//Datos para pobrar el total con letra
//		String data = numberLetterService.getNumberLetter("10460224.76", true, "MXN");
//		System.out.println(data);

		String date = sdfTime.format(new Date());
		date = date.toString();
		System.out.print(date.toString());
		String alo = date.substring(0, 4);
		String mes = date.substring(5, 7);
		String dia = date.substring(8);
		System.out.println(dia + " DE " + mes + " DEL " + alo);
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
			for(com.smartech.invoicing.integration.json.invorg.Item item : response.getItems()) {
				System.out.println(item.getOrganizationCode() + "-" + item.getOrganizationId());
			}
		}
	}
	
//	@Scheduled(fixedDelay = 30000, initialDelay = 30000)
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
	
//	@Scheduled(fixedDelay = 30000, initialDelay = 30000)
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
					log.error("PENDING STATUS (INVOICES): " + in.getFolio() + " - " + in.getFromSalesOrder());
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
	
//	@Scheduled(fixedDelay = 15000, initialDelay = 15000)
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
//			String search = sdf.format("2020-08-05 00:15:23");
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
//					da.setDateValue(sdf.parse(nextSearch));
//					da.setIntValue(0);
//					udcService.update(da, new Date(), AppConstants.USER_DEFAULT);
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
	
//	@Scheduled(fixedDelay=30000, initialDelay=30000)
	public void labelProccessScheduler() {
		log.info("\'labelProccessScheduler\' is started*******");
		String nextSearch = sdf.format(new Date());
		AnalyticsDTO analytics = new AnalyticsDTO();
		try {
			Udc da = udcService.searchBySystemAndKey(AppConstants.UDC_SYSTEM_SCHEDULER, AppConstants.UDC_KEY_LABEL);
			if(da != null) {
				Date dateSearch = da.getDateValue();
				String search = sdf.format(dateSearch);
				analytics.setAr_Report_date(search);
				Rowset r = analyticsService.executeAnalyticsWS(AppConstants.ORACLE_USER, AppConstants.ORACLE_PASS, 
						AppConstants.SERVICE_ASSET_LABEL_REPORT, analytics);
				if(!r.getRow().isEmpty()) {
					if(!labelService.createLabel(r.getRow())) {
						log.error("HUBO ALGUN ERROR AL MOMENTO DE LA GENERACION DE LAS ETIQUETAS" + search + " ---" + new Date());
						if(da.getIntValue() == 5) {
							List<Udc> emails = udcService.searchBySystem(AppConstants.UDC_SYSTEM_EMAILS);
							List<String> email = new ArrayList<String>();
							for(Udc u: emails) {
								email.add(u.getUdcKey());
							}
							mailService.sendMail(email,
									AppConstants.EMAIL_LABEL_SUBJECT,
									AppConstants.EMAIL_LABEL_CONTENT + sdf.format(new Date()), null);
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
				}else{
					log.warn("REPORTS " + r.getRow() + " MESSAGE TO READ");
				}
			}else {
				log.error("NO SE A ENCONTRADO LA UDC CORRESPONDIENTE PARA EL PROCESA DE LAS ETIQUETAS " + new Date());
			}

		}catch(Exception e) {
			log.error("ERROR EN LA GENERACIÓN DE LA ETIQUETA" + e);
		}
		log.info("\'labelProccessScheduler\': is finished********");
	}
	
//	@Scheduled(fixedDelay=30000, initialDelay=30000)
	public void createResponsiveLetter() throws IOException {
		log.info("\'createResponsiveLetter\' is started*******");
		String nextSearch = sdf.format(new Date());
		AnalyticsDTO analytics = new AnalyticsDTO();
		try {
			Udc da = udcService.searchBySystemAndKey(AppConstants.UDC_SYSTEM_SCHEDULER, AppConstants.UDC_KEY_UPDATE_LABEL);
			if(da != null) {
				Date dateSearch = da.getDateValue();
				String search = sdf.format(dateSearch);
				analytics.setAr_Report_date(search);
				Rowset r = analyticsService.executeAnalyticsWS(AppConstants.ORACLE_USER, AppConstants.ORACLE_PASS, 
						AppConstants.SERVICE_RESPONSIVE_LETTER, analytics);
				if(!r.getRow().isEmpty()) {
					if(!responsiveLetter.createFile(r.getRow())) {
						log.error("HUBO ALGUN ERROR AL MOMENTO DE GENERAR LA CARTA RESPONSIVA" + search + " ---" + new Date());
						if(da.getIntValue() == 5) {
							List<Udc> emails = udcService.searchBySystem(AppConstants.UDC_SYSTEM_EMAILS);
							List<String> email = new ArrayList<String>();
							for(Udc u: emails) {
								email.add(u.getUdcKey());
							}
							mailService.sendMail(email,
									AppConstants.EMAIL_LABEL_SUBJECT,
									AppConstants.EMAIL_LABEL_CONTENT + sdf.format(new Date()), null);
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
				}else{
					log.warn("REPORTS " + r.getRow() + " MESSAGE TO READ");
				}
			}else {
				log.error("NO SE A ENCONTRADO LA UDC CORRESPONDIENTE PARA EL PROCESA DE LAS ETIQUETAS " + new Date());
			}

		}catch(Exception e) {
			log.error("ERROR PARA LA GENERACIÓN DE LA CARTA RESPONSIVA" + e);
		}
		log.info("\'createResponsiveLetter\': is finished********");
//		responsiveLetter.createFile();
//		rL.createCon();
		
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
}
