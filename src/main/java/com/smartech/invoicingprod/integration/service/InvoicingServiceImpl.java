package com.smartech.invoicingprod.integration.service;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartech.invoicingprod.dao.InvoiceDao;
import com.smartech.invoicingprod.dto.CategoryDTO;
import com.smartech.invoicingprod.dto.CustomerInformationDTO;
import com.smartech.invoicingprod.dto.EmailAdressDTO;
import com.smartech.invoicingprod.dto.InvoicesByReportsDTO;
import com.smartech.invoicingprod.dto.ItemGtinDTO;
import com.smartech.invoicingprod.dto.ItemsDTO;
import com.smartech.invoicingprod.dto.SalesLineLotSerDTO;
import com.smartech.invoicingprod.dto.SalesOrderDTO;
import com.smartech.invoicingprod.dto.SalesOrderLinesDTO;
import com.smartech.invoicingprod.integration.AnalyticsService;
import com.smartech.invoicingprod.integration.RESTService;
import com.smartech.invoicingprod.integration.SOAPService;
import com.smartech.invoicingprod.integration.dto.AnalyticsDTO;
import com.smartech.invoicingprod.integration.json.IncotermByRest.IncotermByRest;
import com.smartech.invoicingprod.integration.json.IncotermByRest.totals;
import com.smartech.invoicingprod.integration.json.dailyRates.CurrencyRates;
import com.smartech.invoicingprod.integration.json.inventoryItemSerialNumbers.InventoryItemSerialNumbers;
import com.smartech.invoicingprod.integration.json.invitemlot.InventoryItemLots;
import com.smartech.invoicingprod.integration.json.itemCategories.ItemCategory;
import com.smartech.invoicingprod.integration.json.priceList.Item;
import com.smartech.invoicingprod.integration.json.priceList.PriceLists;
import com.smartech.invoicingprod.integration.json.priceListByItem.PriceListByItem;
import com.smartech.invoicingprod.integration.json.unitCost.CostDetails;
import com.smartech.invoicingprod.integration.json.unitCost.ItemCosts;
import com.smartech.invoicingprod.integration.util.AppConstants;
import com.smartech.invoicingprod.integration.xml.rowset.Row;
import com.smartech.invoicingprod.integration.xml.rowset.Rowset;
import com.smartech.invoicingprod.model.Branch;
import com.smartech.invoicingprod.model.ErrorLog;
import com.smartech.invoicingprod.model.Invoice;
import com.smartech.invoicingprod.model.InvoiceDetails;
import com.smartech.invoicingprod.model.NextNumber;
import com.smartech.invoicingprod.model.Payments;
import com.smartech.invoicingprod.model.PaymentsList;
import com.smartech.invoicingprod.model.RetailComplement;
import com.smartech.invoicingprod.model.TaxCodes;
import com.smartech.invoicingprod.model.Udc;
import com.smartech.invoicingprod.service.BranchService;
import com.smartech.invoicingprod.service.CompanyService;
import com.smartech.invoicingprod.service.ErrorLogService;
import com.smartech.invoicingprod.service.InvoiceService;
import com.smartech.invoicingprod.service.NextNumberService;
import com.smartech.invoicingprod.service.PaymentsListService;
import com.smartech.invoicingprod.service.PaymentsService;
import com.smartech.invoicingprod.service.TaxCodesService;
import com.smartech.invoicingprod.service.UdcService;
import com.smartech.invoicingprod.util.AppConstantsUtil;
import com.smartech.invoicingprod.util.NullValidator;
import com.smartech.invoicingprod.util.StringUtils;

@Service("invoicingService")
public class InvoicingServiceImpl implements InvoicingService{
	
	@Autowired
	InvoiceService invoiceService;
	
	@Autowired
	UdcService udcService;
	
	@Autowired
	CompanyService companyService;
	
	@Autowired
	BranchService branchService;
	
	@Autowired
	NextNumberService nextNumberService;
	
	@Autowired
	TaxCodesService taxCodesService;
	
	@Autowired
	InvoiceDao invoiceDao;
		
	@Autowired
	SOAPService soapService;
	
	@Autowired
	RESTService restService;
	
	@Autowired
	PaymentsService paymentsService;
	
	@Autowired
	NumberLetterService numberLetterService;
	
	@Autowired
	MailService mailService;
	
	@Autowired
	ErrorLogService errorLogService;
	
	@Autowired
	PaymentsListService paymentsListService;
	
	@Autowired
	AnalyticsService analyticsService;
	
	static Logger log = Logger.getLogger(InvoicingServiceImpl.class.getName());
	
	final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");	
	final SimpleDateFormat formatterUTC = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	SimpleDateFormat sdfNoTime = new SimpleDateFormat("yyyy-MM-dd");
	DecimalFormat df = new DecimalFormat("#0.00");
	DecimalFormat dfM = new DecimalFormat("#.000000");
	
	@Override
	public boolean createStampInvoice(List<Row> r, String errors) {		
		List<Udc> udc = new ArrayList<Udc>();
		String country = "";
		String shipCountry = "";
		String timeZone = "";
		String pTerms = "";
		List<String> facturas = new ArrayList<String>();
		List<String> notasCredito = new ArrayList<String>();
		List<String> noIva = new ArrayList<String>();
		List<String> anticipos = new ArrayList<String>();
		List<String> fiextAsset = new ArrayList<String>();
		List<String> service1 = new ArrayList<String>();
		List<String> serviceSeamex = new ArrayList<String>();
		List<String> othersProducts = new ArrayList<String>();
		try {
			//Fechas
			List<Udc> tZone = udcService.searchBySystem(AppConstants.UDC_SYSTEM_TIMEZONE);
			for(Udc u: tZone) {
				if(u.getStrValue1().equals(AppConstants.UDC_STRVALUE1_TIMEZONE)) {
					timeZone = u.getUdcKey();
				}
			}
			Udc noteCredite = udcService.searchBySystemAndKey(AppConstants.UDC_SYSTEM_RTYPE, AppConstants.INVOICE_SAT_TYPE_E);
			List<Udc> invcnni = udcService.searchBySystem(AppConstants.UDC_SYSTEM_REPINVOICE);
			if(invcnni != null) {
				for(Udc u: invcnni) {
					if(u.getUdcKey().equals(AppConstants.ORDER_TYPE_FACTURA)) {
						facturas.add(u.getStrValue1());
					}else if(u.getUdcKey().equals(AppConstants.ORDER_TYPE_NC)) {
						notasCredito.add(u.getStrValue1());
					}else if(u.getUdcKey().equals(AppConstants.UDC_KEY_NOIVA)) {
						noIva.add(u.getStrValue1());
					}else if(u.getUdcKey().equals(AppConstants.UDC_KEY_ANTICIPOS)) {
						anticipos.add(u.getStrValue1());
					}else if(u.getUdcKey().equals(AppConstants.UDC_KEY_FIXED_ASSET)) {
						fiextAsset.add(u.getStrValue1());
					}else if(u.getUdcKey().equals(AppConstants.UDC_KEY_SERVICE1)) {
						service1.add(u.getStrValue1());
					}else if(u.getUdcKey().equals(AppConstants.UDC_KEY_SERVICE_SEAMEX)) {
						serviceSeamex.add(u.getStrValue1());
					}else if(u.getUdcKey().equals(AppConstants.UDC_KEY_OTHERS_PRODUCTS)) {
						othersProducts.add(u.getStrValue1());
					}
				}
			}else {
				return false;
			}
			
			sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
			sdfNoTime.setTimeZone(TimeZone.getTimeZone("UTC"));
			formatterUTC.setTimeZone(TimeZone.getTimeZone(timeZone));
			//Llenado de objeto DTO de la respuesta del reporte de facturas
			List<String> arr = new ArrayList<String>();
			List<String> arrSales = new ArrayList<String>();
			List<InvoicesByReportsDTO> invlist = new ArrayList<InvoicesByReportsDTO>();	
			List<Invoice> invList = new ArrayList<Invoice>();
			for(Row ro: r) {
				Invoice searchExistingInvoice = invoiceDao.getSingleInvoiceByFolio(NullValidator.isNull(ro.getColumn9()));
				if(searchExistingInvoice == null) {
					InvoicesByReportsDTO invReports = new InvoicesByReportsDTO();
					invReports = fullDTO(ro);
					if(invReports != null) {			
						invlist.add(invReports);
						
					}	
				}		
			}
			
			//llenar header---------------------------------------------------------------------------------------------------
			for(InvoicesByReportsDTO inv: invlist) {	
				//Datos para anticipos
				Invoice searchExistingInvoice = invoiceDao.getSingleInvoiceByFolio(inv.getTransactionNumber());
				if(searchExistingInvoice == null) {
					if(inv.getPreviousSalesOrder() != null && !inv.getPreviousSalesOrder().isEmpty() || fiextAsset.toString().contains(inv.getTransactionTypeName()) || service1.toString().contains(inv.getTransactionTypeName()) || serviceSeamex.toString().contains(inv.getTransactionTypeName()) || othersProducts.toString().contains(inv.getTransactionTypeName())) {
						if(!arr.contains(inv.getTransactionNumber()) && !arrSales.contains(inv.getPreviousSalesOrder()) || fiextAsset.toString().contains(inv.getTransactionTypeName()) || service1.toString().contains(inv.getTransactionTypeName()) || serviceSeamex.toString().contains(inv.getTransactionTypeName()) || othersProducts.toString().contains(inv.getTransactionTypeName())) {					
							udc = udcService.searchBySystem(AppConstants.UDC_SYSTEM_COUNTRY);
							for(Udc u: udc) {
								if(u.getStrValue1().equals(inv.getCustomerCountry())) {
									country = u.getUdcKey();
								}
								if(u.getStrValue1().equals(inv.getShipToCountry())) {
									shipCountry = u.getUdcKey();
								}
							}
							if(inv.getPaymentTerms().equals(AppConstants.PTERMS_CONTADO)) {
								pTerms = AppConstants.PTERMS_CONTADO;
							}else {
								List<Udc> payTerms = udcService.searchBySystem(AppConstants.UDC_SYSTEM_PAYTERMS);
								for(Udc u: payTerms) {
									if(u.getStrValue1().equals(inv.getPaymentTerms()) || u.getUdcKey().equals(inv.getPaymentTerms())) {
										pTerms = String.valueOf(u.getIntValue());
										break;
									}
								}
							}
							if(country.isEmpty()) {
								return false;
							}else {
								if(shipCountry.isEmpty()) {
									shipCountry = country;
								}							
							}
							if(shipCountry.isEmpty()) {
								return false;
							}
							Invoice invoice = new Invoice();
							//Datos del cliente facturacion---------------------------------------------------------------------------------------
							invoice.setCustomerName(inv.getCustomerName());
							invoice.setCustomerZip(inv.getCustomerPostalCode());
							invoice.setCustomerAddress1(inv.getCustomerAddress1());
							invoice.setCustomerState(inv.getCustomerState());
							invoice.setCustomerCountry(country);
							invoice.setCustomerTaxIdentifier(inv.getCustomerTaxIdentifier().replaceAll(" ", ""));
							invoice.setCustomerEmail(inv.getCustomerEmail());
							invoice.setCustomerPartyNumber(inv.getCustomerPartyNumber());
							invoice.setCustomerClass(inv.getCustomerClass());
							
							//Datos del cliente envío---------------------------------------------------------------------------------------
							invoice.setShipToName(inv.getShipToName());
							invoice.setShipToaddress(inv.getShipToAddress());
							invoice.setShipToCity(inv.getShipToCity());
							invoice.setShipToState(inv.getShipToState());
							invoice.setShipToCountry(shipCountry);
							invoice.setShipToZip(inv.getShipToZip());
							
							//Datos de la unidad de negocio---------------------------------------------------------------------------
							invoice.setCompany(companyService.getCompanyByName(inv.getBusisinesUnitName()));
							invoice.setBranch(null);
							invoice.setPayments(null);
							invoice.setAdvanceAplied(false);
							
							//Datos generales---------------------------------------------------------------------------------------
							invoice.setSetName(inv.getSetName());						
							invoice.setPaymentTerms(pTerms);
							invoice.setFolio(inv.getTransactionNumber());
							invoice.setInvoiceDetails(null);
							invoice.setStatus(AppConstants.STATUS_START);
							if(facturas.toString().contains(inv.getTransactionTypeName())) {
								invoice.setInvoice(true);
								invoice.setInvoiceType(AppConstants.ORDER_TYPE_FACTURA);
								invoice.setFromSalesOrder(inv.getPreviousSalesOrder());
							}else if(notasCredito.toString().contains(inv.getTransactionTypeName())) {
								invoice.setInvoice(false);
								invoice.setInvoiceReferenceTransactionNumber(inv.getPreviousTransactionNumber());
								invoice.setInvoiceType(AppConstants.ORDER_TYPE_NC);
								invoice.setFromSalesOrder(inv.getPreviousSalesOrder());	
								invoice.setInvoiceRelationType(noteCredite.getStrValue1());						
							}else if(noIva.toString().contains(inv.getTransactionTypeName())) {
								invoice.setErrorMsg("ERROR EN EL TIPO DE LINEA, NO EXISTE, EL ERROR SURGE POR QUE EL ARTÍCULO: " + inv.getItemName() + "DE LA ORDEN: "
										+ inv.getPreviousSalesOrder() + " NO TIENE IMPUESTO RELACIONADO");
								ErrorLog seaE = errorLogService.searchError(invoice.getErrorMsg(), inv.getPreviousSalesOrder());
								if(seaE == null) {
									ErrorLog eLog = new ErrorLog();
									eLog.setErrorMsg(invoice.getErrorMsg());
									eLog.setCreationDate(sdf.format(new Date()));
									eLog.setUpdateDate(sdf.format(new Date()));
									eLog.setNew(true);
									eLog.setOrderNumber(inv.getPreviousSalesOrder());
									eLog.setInvoiceType(AppConstants.ORDER_TYPE_FACTURA);
									errorLogService.saveError(eLog);
									continue;
								}else {
									if(seaE.getErrorMsg().equals(invoice.getErrorMsg())) {
										seaE.setNew(false);
										seaE.setUpdateDate(sdf.format(new Date()));
										errorLogService.updateError(seaE);
									}else {
										seaE.setErrorMsg(invoice.getErrorMsg());
										seaE.setNew(true);
										seaE.setUpdateDate(sdf.format(new Date()));
										errorLogService.updateError(seaE);
									}
									continue;
								}
							}else if(fiextAsset.toString().contains(inv.getTransactionTypeName())) {							
								Invoice seaExisCNAdv = invoiceDao.getSingleInvoiceByFolioAndType(inv.getTransactionNumber(), AppConstants.ORDER_TYPE_FACTURA);
								if(seaExisCNAdv == null) {
									log.info("AQUI EMPIEZA LA FACTURA PARA ACTIVOS FIJOS" + inv.getTransactionNumber());
									Branch branch = branchService.getBranchByCode("CEDIS");
									NextNumber nNumber = nextNumberService.getNumberCon(AppConstants.ORDER_TYPE_FACTURA, branch);
									invoice.setInvoice(true);	
									invoice.setInvoiceType(AppConstants.ORDER_TYPE_FACTURA);
									invoice.setBranch(branch);
									invoice.setSerial(nNumber.getSerie());
									invoice.setStatus(AppConstants.STATUS_PENDING);
									invoice.setFromSalesOrder(inv.getTransactionNumber());
									inv.setItemDescription(inv.getItemDescriptionFA());
									if(inv.getFausoCFDI() != null && !inv.getFausoCFDI().isEmpty()) {
										invoice.setCFDIUse(inv.getFausoCFDI());	
									}else {
										ErrorLog seaE = errorLogService.searchError("FALTAN DATOS PARA EL TIMBRE", inv.getTransactionNumber());
										if(seaE == null) {
											ErrorLog eLog = new ErrorLog();
											eLog.setErrorMsg("FALTAN DATOS PARA EL TIMBRE");
											eLog.setCreationDate(sdf.format(new Date()));
											eLog.setUpdateDate(sdf.format(new Date()));
											eLog.setNew(true);
											eLog.setOrderNumber(inv.getTransactionNumber());
											eLog.setInvoiceType(AppConstants.ORDER_TYPE_FACTURA);
											errorLogService.saveError(eLog);
											continue;
										}else {
											continue;
										}
									}
									if(inv.getFaPaymentMethod() != null && !inv.getFaPaymentMethod().isEmpty()) {
										invoice.setPaymentMethod(inv.getFaPaymentMethod());
									}else {
										ErrorLog seaE = errorLogService.searchError("FALTAN DATOS PARA EL TIMBRE", inv.getTransactionNumber());
										if(seaE == null) {
											ErrorLog eLog = new ErrorLog();
											eLog.setErrorMsg("FALTAN DATOS PARA EL TIMBRE");
											eLog.setCreationDate(sdf.format(new Date()));
											eLog.setUpdateDate(sdf.format(new Date()));
											eLog.setNew(true);
											eLog.setOrderNumber(inv.getTransactionNumber());
											eLog.setInvoiceType(AppConstants.ORDER_TYPE_FACTURA);
											errorLogService.saveError(eLog);
											continue;
										}else {
											continue;
										}
									}
									if(inv.getFaPaymentForm() != null && !inv.getFaPaymentForm().isEmpty()) {
										invoice.setPaymentType(inv.getFaPaymentForm());
									}else {
										ErrorLog seaE = errorLogService.searchError("FALTAN DATOS PARA EL TIMBRE", inv.getTransactionNumber());
										if(seaE == null) {
											ErrorLog eLog = new ErrorLog();
											eLog.setErrorMsg("FALTAN DATOS PARA EL TIMBRE");
											eLog.setCreationDate(sdf.format(new Date()));
											eLog.setUpdateDate(sdf.format(new Date()));
											eLog.setNew(true);
											eLog.setOrderNumber(inv.getTransactionNumber());
											eLog.setInvoiceType(AppConstants.ORDER_TYPE_FACTURA);
											errorLogService.saveError(eLog);
											continue;
										}else {
											continue;
										}
									}
								}else {
									continue;
								}
							}else if(service1.toString().contains(inv.getTransactionTypeName())) {							
								Invoice seaExisCNAdv = invoiceDao.getSingleInvoiceByFolioAndType(inv.getTransactionNumber(), AppConstants.ORDER_TYPE_FACTURA);
								if(seaExisCNAdv == null) {
									log.info("AQUI EMPIEZA LA FACTURA PARA SERVICIOS1" + inv.getTransactionNumber());
									Branch branch = branchService.getBranchByCode("SERVICIOS");
									NextNumber nNumber = nextNumberService.getNumberCon(AppConstants.ORDER_TYPE_FACTURA, branch);
//									NextNumber nNumber = nextNumberService.getNumberById(Integer.parseInt(String.valueOf(branch.getId())));
									invoice.setInvoice(true);	
									invoice.setInvoiceType(AppConstants.ORDER_TYPE_FACTURA);
									invoice.setBranch(branch);
									if(nNumber != null){
										invoice.setSerial(nNumber.getSerie());
									}else {
										invoice.setSerial("MEFAC");
									}
									
									invoice.setStatus(AppConstants.STATUS_PENDING);
									invoice.setFromSalesOrder(inv.getTransactionNumber());
									if(inv.getFausoCFDI() != null && !inv.getFausoCFDI().isEmpty()) {
										invoice.setCFDIUse(inv.getFausoCFDI());	
									}else {
										ErrorLog seaE = errorLogService.searchError("FALTAN DATOS PARA EL TIMBRE", inv.getTransactionNumber());
										if(seaE == null) {
											ErrorLog eLog = new ErrorLog();
											eLog.setErrorMsg("FALTAN DATOS PARA EL TIMBRE");
											eLog.setCreationDate(sdf.format(new Date()));
											eLog.setUpdateDate(sdf.format(new Date()));
											eLog.setNew(true);
											eLog.setOrderNumber(inv.getTransactionNumber());
											eLog.setInvoiceType(AppConstants.ORDER_TYPE_FACTURA);
											errorLogService.saveError(eLog);
											continue;
										}else {
											continue;
										}
									}
									if(inv.getFaPaymentMethod() != null && !inv.getFaPaymentMethod().isEmpty()) {
										invoice.setPaymentMethod(inv.getFaPaymentMethod());
									}else {
										ErrorLog seaE = errorLogService.searchError("FALTAN DATOS PARA EL TIMBRE", inv.getTransactionNumber());
										if(seaE == null) {
											ErrorLog eLog = new ErrorLog();
											eLog.setErrorMsg("FALTAN DATOS PARA EL TIMBRE");
											eLog.setCreationDate(sdf.format(new Date()));
											eLog.setUpdateDate(sdf.format(new Date()));
											eLog.setNew(true);
											eLog.setOrderNumber(inv.getTransactionNumber());
											eLog.setInvoiceType(AppConstants.ORDER_TYPE_FACTURA);
											errorLogService.saveError(eLog);
											continue;
										}else {
											continue;
										}
									}
									if(inv.getFaPaymentForm() != null && !inv.getFaPaymentForm().isEmpty()) {
										invoice.setPaymentType(inv.getFaPaymentForm());
									}else {
										ErrorLog seaE = errorLogService.searchError("FALTAN DATOS PARA EL TIMBRE", inv.getTransactionNumber());
										if(seaE == null) {
											ErrorLog eLog = new ErrorLog();
											eLog.setErrorMsg("FALTAN DATOS PARA EL TIMBRE");
											eLog.setCreationDate(sdf.format(new Date()));
											eLog.setUpdateDate(sdf.format(new Date()));
											eLog.setNew(true);
											eLog.setOrderNumber(inv.getTransactionNumber());
											eLog.setInvoiceType(AppConstants.ORDER_TYPE_FACTURA);
											errorLogService.saveError(eLog);
											continue;
										}else {
											continue;
										}
									}
								}else {
									continue;
								}
							}else if(serviceSeamex.toString().contains(inv.getTransactionTypeName())) {							
								Invoice seaExisCNAdv = invoiceDao.getSingleInvoiceByFolioAndType(inv.getTransactionNumber(), AppConstants.ORDER_TYPE_FACTURA);
								if(seaExisCNAdv == null) {
									log.info("AQUI EMPIEZA LA FACTURA PARA SERVICIOS SEAMEX" + inv.getTransactionNumber());
									Branch branch = branchService.getBranchByCode("PRESTACIONES_SERVICIOS");
									NextNumber nNumber = nextNumberService.getNumberCon(AppConstants.ORDER_TYPE_FACTURA, branch);
//									NextNumber nNumber = nextNumberService.getNumberById(Integer.parseInt(String.valueOf(branch.getId())));
									invoice.setSerial(nNumber.getSerie());
									invoice.setInvoice(true);	
									invoice.setInvoiceType(AppConstants.ORDER_TYPE_FACTURA);
									invoice.setBranch(branch);
									invoice.setStatus(AppConstants.STATUS_PENDING);
									invoice.setFromSalesOrder(inv.getTransactionNumber());
									if(inv.getFausoCFDI() != null && !inv.getFausoCFDI().isEmpty()) {
										invoice.setCFDIUse(inv.getFausoCFDI());	
									}else {
										ErrorLog seaE = errorLogService.searchError("FALTAN DATOS PARA EL TIMBRE", inv.getTransactionNumber());
										if(seaE == null) {
											ErrorLog eLog = new ErrorLog();
											eLog.setErrorMsg("FALTAN DATOS PARA EL TIMBRE");
											eLog.setCreationDate(sdf.format(new Date()));
											eLog.setUpdateDate(sdf.format(new Date()));
											eLog.setNew(true);
											eLog.setOrderNumber(inv.getTransactionNumber());
											eLog.setInvoiceType(AppConstants.ORDER_TYPE_FACTURA);
											errorLogService.saveError(eLog);
											continue;
										}else {
											continue;
										}
									}
									if(inv.getFaPaymentMethod() != null && !inv.getFaPaymentMethod().isEmpty()) {
										invoice.setPaymentMethod(inv.getFaPaymentMethod());
									}else {
										ErrorLog seaE = errorLogService.searchError("FALTAN DATOS PARA EL TIMBRE", inv.getTransactionNumber());
										if(seaE == null) {
											ErrorLog eLog = new ErrorLog();
											eLog.setErrorMsg("FALTAN DATOS PARA EL TIMBRE");
											eLog.setCreationDate(sdf.format(new Date()));
											eLog.setUpdateDate(sdf.format(new Date()));
											eLog.setNew(true);
											eLog.setOrderNumber(inv.getTransactionNumber());
											eLog.setInvoiceType(AppConstants.ORDER_TYPE_FACTURA);
											errorLogService.saveError(eLog);
											continue;
										}else {
											continue;
										}
									}
									if(inv.getFaPaymentForm() != null && !inv.getFaPaymentForm().isEmpty()) {
										invoice.setPaymentType(inv.getFaPaymentForm());
									}else {
										ErrorLog seaE = errorLogService.searchError("FALTAN DATOS PARA EL TIMBRE", inv.getTransactionNumber());
										if(seaE == null) {
											ErrorLog eLog = new ErrorLog();
											eLog.setErrorMsg("FALTAN DATOS PARA EL TIMBRE");
											eLog.setCreationDate(sdf.format(new Date()));
											eLog.setUpdateDate(sdf.format(new Date()));
											eLog.setNew(true);
											eLog.setOrderNumber(inv.getTransactionNumber());
											eLog.setInvoiceType(AppConstants.ORDER_TYPE_FACTURA);
											errorLogService.saveError(eLog);
											continue;
										}else {
											continue;
										}
									}
								}else {
									continue;
								}
							}else if(othersProducts.toString().contains(inv.getTransactionTypeName())) {							
								Invoice seaExisCNAdv = invoiceDao.getSingleInvoiceByFolioAndType(inv.getTransactionNumber(), AppConstants.ORDER_TYPE_FACTURA);
								if(seaExisCNAdv == null) {
									log.info("AQUI EMPIEZA LA FACTURA PARA OTROS PRODCUTOS " + inv.getTransactionNumber());
									Branch branch = new Branch();
									NextNumber nNumber = new NextNumber();
									if(invoice.getCompany().getName().equals("EQUIPO MARINO") || invoice.getCompany().getName().equals("EQUIPO MARINO IDEA")) {
										branch = branchService.getBranchByCode("SERVICIOS");
										nNumber = nextNumberService.getNumberCon(AppConstants.ORDER_TYPE_FACTURA, branch);
									}else if(invoice.getCompany().getName().equals("FABRICA DE LANCHAS")){
										branch = branchService.getBranchByCode("PLR");
										nNumber = nextNumberService.getNumberCon(AppConstants.ORDER_TYPE_FACTURA, branch);
									}else if(invoice.getCompany().getName().equals("PRESTACION DE SERVICIOS")) {
										branch = branchService.getBranchByCode("PRESTACIONES_SERVICIOS");
										nNumber = nextNumberService.getNumberCon(AppConstants.ORDER_TYPE_FACTURA, branch);
									}
//									NextNumber nNumber = nextNumberService.getNumberById(Integer.parseInt(String.valueOf(branch.getId())));
									invoice.setSerial(nNumber.getSerie());
									invoice.setInvoice(true);	
									invoice.setInvoiceType(AppConstants.ORDER_TYPE_FACTURA);
									invoice.setBranch(branch);
									invoice.setStatus(AppConstants.STATUS_PENDING);
									invoice.setFromSalesOrder(inv.getTransactionNumber());
									if(inv.getFausoCFDI() != null && !inv.getFausoCFDI().isEmpty()) {
										invoice.setCFDIUse(inv.getFausoCFDI());	
									}else {
										ErrorLog seaE = errorLogService.searchError("FALTAN DATOS PARA EL TIMBRE", inv.getTransactionNumber());
										if(seaE == null) {
											ErrorLog eLog = new ErrorLog();
											eLog.setErrorMsg("FALTAN DATOS PARA EL TIMBRE");
											eLog.setCreationDate(sdf.format(new Date()));
											eLog.setUpdateDate(sdf.format(new Date()));
											eLog.setNew(true);
											eLog.setOrderNumber(inv.getTransactionNumber());
											eLog.setInvoiceType(AppConstants.ORDER_TYPE_FACTURA);
											errorLogService.saveError(eLog);
											continue;
										}else {
											continue;
										}
									}
									if(inv.getFaPaymentMethod() != null && !inv.getFaPaymentMethod().isEmpty()) {
										invoice.setPaymentMethod(inv.getFaPaymentMethod());
									}else {
										ErrorLog seaE = errorLogService.searchError("FALTAN DATOS PARA EL TIMBRE", inv.getTransactionNumber());
										if(seaE == null) {
											ErrorLog eLog = new ErrorLog();
											eLog.setErrorMsg("FALTAN DATOS PARA EL TIMBRE");
											eLog.setCreationDate(sdf.format(new Date()));
											eLog.setUpdateDate(sdf.format(new Date()));
											eLog.setNew(true);
											eLog.setOrderNumber(inv.getTransactionNumber());
											eLog.setInvoiceType(AppConstants.ORDER_TYPE_FACTURA);
											errorLogService.saveError(eLog);
											continue;
										}else {
											continue;
										}
									}
									if(inv.getFaPaymentForm() != null && !inv.getFaPaymentForm().isEmpty()) {
										invoice.setPaymentType(inv.getFaPaymentForm());
									}else {
										ErrorLog seaE = errorLogService.searchError("FALTAN DATOS PARA EL TIMBRE", inv.getTransactionNumber());
										if(seaE == null) {
											ErrorLog eLog = new ErrorLog();
											eLog.setErrorMsg("FALTAN DATOS PARA EL TIMBRE");
											eLog.setCreationDate(sdf.format(new Date()));
											eLog.setUpdateDate(sdf.format(new Date()));
											eLog.setNew(true);
											eLog.setOrderNumber(inv.getTransactionNumber());
											eLog.setInvoiceType(AppConstants.ORDER_TYPE_FACTURA);
											errorLogService.saveError(eLog);
											continue;
										}else {
											continue;
										}
									}
								}else {
									continue;
								}
							}else {
								continue;
							}
							invoice.setInvoiceCurrency(inv.getCurrency());
							if(inv.getExchangeRate().isEmpty()) {
								invoice.setInvoiceExchangeRate(AppConstants.INVOICE_EXCHANGE_RATE);
							}else {
								invoice.setInvoiceExchangeRate(Double.parseDouble(df.format(Double.parseDouble(inv.getExchangeRate()))));
							}
							
							invoice.setOrderSource(inv.getTransactionSource());
							invoice.setOrderType(inv.getTransactionTypeName());
							
							//Datos extras------------------------------------------------------------------------------------------------
							Date date = sdfNoTime.parse(inv.getTransactionDate());
							String dateT = formatterUTC.format(date);					
							invoice.setCreatedBy(AppConstants.USER_DEFAULT);
							invoice.setCreationDate(sdfNoTime.parse(dateT));
							invoice.setUpdatedBy(AppConstants.USER_DEFAULT);
							invoice.setUpdatedDate(new Date());
							invoice.setExtCom(false);					
							
							//Añadir registro a la lista facturas
							invList.add(invoice);
							arr.add(inv.getTransactionNumber());
							arrSales.add(inv.getPreviousSalesOrder());
						}
					}
				}
			}
			
			//Llenado de líneas---------------------------------------------------------------------------------------
			for(Invoice i: invList) {
				if(i.getErrorMsg() == null || i.getErrorMsg().isEmpty()) {
					Set<InvoiceDetails> invDListNormal = new HashSet<InvoiceDetails>();
					Set<InvoiceDetails> invDListDiscount = new HashSet<InvoiceDetails>();
					int disc = 0;
					
					for(InvoicesByReportsDTO in : invlist) {
						Invoice searchExistingInvoice = invoiceDao.getSingleInvoiceByFolio(in.getTransactionNumber());
						if(searchExistingInvoice == null) {
							if(i.getInvoiceType() != null) {							
								if(i.getFolio().equals(in.getTransactionNumber()) || NullValidator.isNull(i.getFromSalesOrder()).equals((in.getPreviousSalesOrder()))) {
									if(!i.getFolio().contains(in.getTransactionNumber())){
										i.setFolio(i.getFolio() + "-" + in.getTransactionNumber());
									}
									
									InvoiceDetails invDetails = new InvoiceDetails();
									Set<TaxCodes> tcList = new HashSet<TaxCodes>();
									
									invDetails.setItemNumber(in.getItemName());
									invDetails.setItemDescription(in.getItemDescription());
									invDetails.setCurrency(in.getCurrency());
									invDetails.setUomName(in.getUomCode());
									if(in.getExchangeRate().isEmpty()) {
										invDetails.setExchangeRate(AppConstants.INVOICE_EXCHANGE_RATE);
									}else {
										invDetails.setExchangeRate(Double.parseDouble(df.format(Double.parseDouble(in.getExchangeRate()))));
									}
									if(NullValidator.isNull(i.getInvoiceType()).equals(AppConstants.ORDER_TYPE_FACTURA)){
										if(NullValidator.isNull(Double.parseDouble(in.getTransactionLineUnitSellingPrice())) > 0) {
											invDetails.setUnitPrice(NullValidator.isNull(Double.parseDouble(df.format(Double.parseDouble(in.getTransactionLineUnitSellingPrice())))));
											invDetails.setLineType(AppConstants.REPORT_LINE_TYPE_NOR);
										}else {
											invDetails.setUnitPrice(Math.abs(Double.parseDouble(df.format(Double.parseDouble(in.getTransactionLineUnitSellingPrice())))));
											invDetails.setLineType(AppConstants.REPORT_LINE_TYPE_DIS);
										}	
										in.setTransactionType(AppConstants.LIVERPOOL_INVOICE);
									}else if(NullValidator.isNull(i.getInvoiceType()).equals(AppConstants.ORDER_TYPE_NC)) {
										if(NullValidator.isNull(Double.parseDouble(in.getTransactionLineUnitSellingPrice())) < 0) {
											invDetails.setUnitPrice(NullValidator.isNull(Math.abs(Double.parseDouble(df.format(Double.parseDouble(in.getTransactionLineUnitSellingPrice()))))));
											invDetails.setLineType(AppConstants.REPORT_LINE_TYPE_NOR);
										}else {
											invDetails.setUnitPrice(Math.abs(Double.parseDouble(df.format(Double.parseDouble(in.getTransactionLineUnitSellingPrice())))));
											invDetails.setLineType(AppConstants.REPORT_LINE_TYPE_DIS);
										}
										in.setTransactionType(AppConstants.LIVERPOOL_CREDIT_NOTE);
									}
									//Datos para activos fijos
									if(i.getStatus().equals(AppConstants.STATUS_PENDING)) {
										invDetails.setUnitProdServ(NullValidator.isNull(in.getFaCodigoSat()));
										Udc satUOM = udcService.searchBySystemAndKey(AppConstants.UDC_SYSTEM_UOMSAT, in.getUomCode());
										invDetails.setUomName(satUOM.getStrValue2().toUpperCase());
										invDetails.setUomCode(satUOM.getStrValue1());
										invDetails.setItemUomCustoms(String.valueOf(satUOM.getIntValue()));
										if(in.getItemDescriptionDetailsForService() == null){
											if(in.getItemDescription() != null && !in.getItemDescription().isEmpty()) {
												invDetails.setItemDescription(in.getItemDescription());
											}else {
												invDetails.setItemDescription("Venta de activo fijo");
											}
										}else {//Descripción para servicios
											invDetails.setItemDescription(NullValidator.isNull(in.getItemDescriptionDetailsForService()));
										}
										
									}
									invDetails.setTransactionLineNumber(in.getTransactionLineNumber());
									if(i.isInvoice()) {
										if(in.getQuantityInvoiced() != null && !in.getQuantityInvoiced().isEmpty()) {
											invDetails.setQuantity(Double.parseDouble(df.format(Double.parseDouble(NullValidator.isNull(in.getQuantityInvoiced())))));
											in.setInvoicedQuantity(String.valueOf((invDetails.getQuantity())));
										}else {
											continue;
										}
									}else {
										if(in.getQuantityCredited() != null && !in.getQuantityCredited().isEmpty()) {
											invDetails.setQuantity(Double.parseDouble(df.format(Double.parseDouble(NullValidator.isNull(in.getQuantityCredited())))));
											in.setInvoicedQuantity(String.valueOf((invDetails.getQuantity())));
										}else {
											continue;
										}
									}
									if(in.getTaxRecoverableAmount().isEmpty()) {
										invDetails.setTotalTaxAmount(0.00);
									}else {
										invDetails.setTotalTaxAmount(NullValidator.isNull(Math.abs(Double.parseDouble(df.format(Double.parseDouble(in.getTaxRecoverableAmount()))))));
									}						
									if(Double.parseDouble(in.getTotal()) > 0) {
										invDetails.setTotalAmount(Double.parseDouble(df.format(Double.parseDouble(in.getTotal()))));
									}else {
										invDetails.setTotalAmount(Double.parseDouble(df.format(Double.parseDouble(in.getTotal())*(-1))));
									}
									//COLOCAR QUE LA LÍNEA ES TRANSACCIONABLE
									invDetails.setIsInvoiceLine("D");
									List<TaxCodes> tclConsult = taxCodesService.getTCList(0, 10);
									Set<TaxCodes> tcl = new HashSet<TaxCodes>(tclConsult);
									for(TaxCodes tc: tcl) {
										if(tc.getTaxName().equals(in.getTaxClassificationCode())) {
											tcList.add(tc);
										}
									}
									if(tcList.size() == 0) {
										tcList.add(taxCodesService.getTCById(2));
									}
									
									//Complemento detallista						
									if(in.isDetCom()) {
										RetailComplement rc = new RetailComplement();
										rc.setDocumentStatus(in.getDocumentStatus());
										rc.setTransactionType(in.getTransactionType());
										rc.setInstructionCode(in.getInstructionCode());
										rc.setTextNote(numberLetterService.getNumberLetter(String.valueOf(invDetails.getTotalAmount() + invDetails.getTotalTaxAmount()), true, invDetails.getCurrency()));
										rc.setAdicionalInformation("");
										rc.setAdicionalInformationNumber(in.getAdicionalInformationNumber());
										rc.setAdicionalInformationId(in.getAdicionalInformationId());
										rc.setDeliveryNote("");
										rc.setGlobalLocationNumberBuyer(in.getGLNBuyer());
										rc.setPurchasingContact(in.getPurchasingContact());
										rc.setSeller("");
										rc.setIdentificationType(in.getIdentificationType());
										rc.setElementOnline("");
										rc.setInovicedQuantity(in.getInvoicedQuantity());
										rc.setPriceTotal(String.valueOf(invDetails.getTotalAmount()));
										rc.setTotal(String.valueOf(invDetails.getTotalAmount() + invDetails.getTotalTaxAmount()));
										
										invDetails.setRetailComplements(rc);
									}else {
										invDetails.setRetailComplements(null);
									}	
									
									invDetails.setTaxCodes(tcList);
									if(invDetails.getLineType().equals(AppConstants.REPORT_LINE_TYPE_NOR)) {							
										invDListNormal.add(invDetails);
									}else {	
										invDListDiscount.add(invDetails);
										disc+=1;
									}
								}	
							}
						}
					}
					if(i.getInvoiceType() != null) {
						//Valida si hay descuentos
						if(disc > 0) {
							i.setInvoiceDetails(this.getDiscount(invDListNormal, invDListDiscount));
						}else {
							i.setInvoiceDetails(invDListNormal);
						}				
						//Obtiene y setea los valores de total, descuento y total de impuestos
						double taxAmount = 0.00;
						double subtotal = 0.00;
						double discount = 0.00;
						String valorDisc = null;
						int count = 1;
						for(InvoiceDetails id: i.getInvoiceDetails()) {
							taxAmount = taxAmount + id.getTotalTaxAmount();
							subtotal = subtotal + id.getTotalAmount();
							discount = discount + id.getTotalDiscount();
							id.setTransactionLineNumber(String.valueOf(count));
							count++;
						}
						i.setInvoiceTaxAmount(Double.parseDouble(df.format(taxAmount)));
						i.setInvoiceTotal(Double.parseDouble(df.format(subtotal + taxAmount)));
						i.setInvoiceSubTotal(Double.parseDouble(df.format(subtotal)));
						i.setInvoiceDiscount(Double.parseDouble(df.format(discount)));
						//Llenar el valor del descuento en notas
						if(discount != 0.00) {
							Double val = ((subtotal*100)/(subtotal + discount))/100;
							valorDisc = "0" + df.format(val);
							i.setNotes(valorDisc);	
						}else {
							i.setNotes(valorDisc);	
						}
						if(i.getStatus().equals(AppConstants.STATUS_PENDING)) {
							if(i.getPaymentMethod().equals("PPD")) {
								i.setRemainingBalanceAmount(df.format(subtotal + taxAmount));
								i.setPreviousBalanceAmount(null);
							}
						}						
						//Guarda los datos en la base de datos pero antes valida si ya existe esa factura
						if(invoiceDao.getSingleInvoiceByFolio(i.getFolio()) == null) {
							if(i.getInvoiceDetails().size() > 0) {
								if(!invoiceService.createInvoice(i)) {
									System.out.println(false);
								}
							}
						}
					}
				}
			}
			return true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public InvoicesByReportsDTO fullDTO (Row r) {
		InvoicesByReportsDTO invoice = new InvoicesByReportsDTO();
		List<String> service1 = new ArrayList<String>();
		List<String> fixedAsset = new ArrayList<String>();
		List<String> serviceSeamex = new ArrayList<String>();
		List<String> otrosProductos = new ArrayList<String>();
		try {
			List<Udc> ant = udcService.searchBySystem(AppConstants.UDC_SYSTEM_REPINVOICE);
			for(Udc u: ant) {
				if(u.getUdcKey().equals(AppConstants.UDC_KEY_SERVICE1)) {
					service1.add(u.getStrValue1());
				}else if(u.getUdcKey().equals(AppConstants.UDC_KEY_FIXED_ASSET)) {
					fixedAsset.add(u.getStrValue1());
				}else if(u.getUdcKey().equals(AppConstants.UDC_KEY_SERVICE_SEAMEX)) {
					serviceSeamex.add(u.getStrValue1());
				}else if(u.getUdcKey().equals(AppConstants.UDC_KEY_OTHERS_PRODUCTS)) {
					otrosProductos.add(u.getStrValue1());
				}
			}
			//Datos del cliente para facturación
			invoice.setCustomerName(NullValidator.isNull(r.getColumn0()));
			invoice.setCustomerNumber(NullValidator.isNull(r.getColumn1()));
			invoice.setCustomerState(NullValidator.isNull(r.getColumn51()));
			invoice.setCustomerTaxIdentifier(NullValidator.isNull(r.getColumn2()));
			invoice.setCustomerCountry(NullValidator.isNull(r.getColumn3()));
			invoice.setCustomerPostalCode(NullValidator.isNull(r.getColumn4()));
			invoice.setCustomerAddress1(NullValidator.isNull(r.getColumn5()));	
			invoice.setCustomerEmail(NullValidator.isNull(r.getColumn52()));;
			invoice.setPaymentTerms(NullValidator.isNull(r.getColumn6()));			
			invoice.setTransactionDate(NullValidator.isNull(r.getColumn7()));
			invoice.setExchangeRate(NullValidator.isNull(r.getColumn8()));
			invoice.setTransactionNumber(NullValidator.isNull(r.getColumn9()));
			invoice.setTransactionSource(NullValidator.isNull(r.getColumn10()));
			invoice.setTransactionTypeName(NullValidator.isNull(r.getColumn11().replaceAll("\"", "")));	
			invoice.setSalesOrderNumber(NullValidator.isNull(r.getColumn12()));	
			invoice.setTransactionLineNumber(NullValidator.isNull(r.getColumn13()));
			invoice.setUomCode(NullValidator.isNull(r.getColumn14()));
			invoice.setTransactionLineUnitSellingPrice(NullValidator.isNull(r.getColumn15()));
			invoice.setItemDescription(NullValidator.isNull(r.getColumn16()));
			invoice.setItemName(NullValidator.isNull(r.getColumn17()));
			invoice.setCreationDate(NullValidator.isNull(r.getColumn18()));
			invoice.setPreviousTransactionNumber(NullValidator.isNull(r.getColumn19()));
			invoice.setJournalLineDescriptor(NullValidator.isNull(r.getColumn20()));			
			invoice.setTaxClassificationCode(NullValidator.isNull(r.getColumn21()));
			invoice.setBusisinesUnitName(NullValidator.isNull(r.getColumn22()));			
			invoice.setLegalEntityName(NullValidator.isNull(r.getColumn24()));
			invoice.setSetName(NullValidator.isNull(r.getColumn25()));				
			invoice.setLegalEntityAddress(NullValidator.isNull(r.getColumn26()));
			invoice.setCurrency(NullValidator.isNull(r.getColumn28()));
			invoice.setLegalEntityId(NullValidator.isNull(r.getColumn29()));			
			invoice.setQuantityCredited(NullValidator.isNull(r.getColumn30()));
			invoice.setQuantityInvoiced(NullValidator.isNull(r.getColumn31()));
			invoice.setTaxRecoverableAmount(NullValidator.isNull(r.getColumn32()));
			invoice.setTransactionEnteredAmouny(NullValidator.isNull(r.getColumn33()));
			invoice.setPreviousSalesOrder(NullValidator.isNull(r.getColumn34()));
			invoice.setCustomerClass(NullValidator.isNull(r.getColumn53()));
			invoice.setTotal(r.getColumn54());
			invoice.setUuidRelated(NullValidator.isNull(r.getColumn55()));
			//Datos del cliente para envío
			invoice.setShipToName(NullValidator.isNull(r.getColumn35()));
			invoice.setShipToAddress(NullValidator.isNull(r.getColumn36()));
			invoice.setShipToCity(NullValidator.isNull(r.getColumn37()));
			invoice.setShipToCountry(NullValidator.isNull(r.getColumn38()));
			invoice.setShipToZip(NullValidator.isNull(r.getColumn39()));
			invoice.setShipToState(NullValidator.isNull(r.getColumn40()));
			invoice.setCustomerPartyNumber(NullValidator.isNull(r.getColumn41()));
			//Datos para activos Fijos
			invoice.setFaCodigoSat(NullValidator.isNull(r.getColumn55()));
			invoice.setFaPaymentForm(NullValidator.isNull(r.getColumn56()));
			invoice.setFaPaymentMethod(NullValidator.isNull(r.getColumn57()));
			invoice.setFausoCFDI(NullValidator.isNull(r.getColumn58()));
			invoice.setItemDescriptionFA(NullValidator.isNull(r.getColumn59()));
			invoice.setItemDescriptionDetailsForService(r.getColumn60());
			invoice.setUuidInitialCharge(NullValidator.isNull(r.getColumn61()).replaceAll("\\s",""));
			//Complemento detallista
			if(r.getColumn43() != null) {
				if(r.getColumn43().equals("Y")) {
					invoice.setDetCom(true);
					invoice.setDocumentStatus(NullValidator.isNull(r.getColumn42()));
					invoice.setInstructionCode(NullValidator.isNull(r.getColumn44()));
					invoice.setAdicionalInformationNumber(NullValidator.isNull(r.getColumn49()));//Listo
					invoice.setAdicionalInformationId(NullValidator.isNull(r.getColumn47()));
					invoice.setGLNBuyer(NullValidator.isNull(r.getColumn46()));
					invoice.setPurchasingContact(NullValidator.isNull(r.getColumn45()));
					invoice.setIdentificationType(NullValidator.isNull(r.getColumn48()));
				}else {
					invoice.setDetCom(false);
				}
			}else {
				invoice.setDetCom(false);
			}
			if((invoice.getSalesOrderNumber() != null && !invoice.getSalesOrderNumber().isEmpty()) || 
					service1.toString().contains(invoice.getTransactionTypeName()) ||
					fixedAsset.toString().contains(invoice.getTransactionTypeName()) ||
					invoice.getTransactionSource().equals("CARGA INICIAL") ||
					serviceSeamex.toString().contains(invoice.getTransactionTypeName())||
					otrosProductos.toString().contains(invoice.getTransactionTypeName())) {
				log.info(invoice.getTransactionTypeName());
				return invoice;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	public Set<InvoiceDetails> getDiscount(Set<InvoiceDetails> Normal, Set<InvoiceDetails> discount){
		Set<InvoiceDetails> detailList = new HashSet<InvoiceDetails>();
		try {
			for(InvoiceDetails iN: Normal) {
				double total = 0.00;
				double tax = 0.00;
				double unitPrice = 0.00;
				double disc = 0.00;
				for(InvoiceDetails iD: discount) {
					if(iD.getItemNumber().equals(iN.getItemNumber()) &&
							iD.getUomName().equals(iN.getUomName()) &&
							iD.getQuantity() == iN.getQuantity()) {
						total = Math.abs(iN.getTotalAmount()) - Math.abs(iD.getTotalAmount());
						tax = Math.abs(iN.getTotalTaxAmount()) - Math.abs(iD.getTotalTaxAmount());
						unitPrice = Math.abs(iN.getUnitPrice()) - Math.abs(iD.getUnitPrice());
						disc = iD.getTotalAmount();
						
						iN.setTotalAmount(total);
						iN.setTotalTaxAmount(tax);
						iN.setTotalDiscount(disc);
						iN.setUnitPrice(unitPrice);
					}
				}
				detailList.add(iN);
			}		
			
			return detailList;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void updateStartInvoiceSOAPList() throws ParseException {
		List<String> otList = new ArrayList<String>();
		otList.add(AppConstants.ORDER_TYPE_FACTURA);
		otList.add(AppConstants.ORDER_TYPE_NC);
		
		List<String> sList = new ArrayList<String>();
		sList.add(AppConstants.STATUS_START);
		sList.add(AppConstants.STATUS_ERROR_DATA);
		
		
		String incoterm = null;
		List<Invoice> invoiceList = invoiceDao.getInvoiceListByStatusCode(sList, otList);
		if(invoiceList != null && !invoiceList.isEmpty()) {
			for(Invoice inv : invoiceList) {
				String salesOrderNumber = "";
				String msg = "";
				String msgError = "";
				boolean invStatus = true;
				boolean havePetition = false;
				
				//Obtención de Datos de OM
				SalesOrderDTO so = soapService.getSalesOrderInformation(inv.getFromSalesOrder());
				IncotermByRest inco = restService.getIncoterm(inv.getFromSalesOrder()); 
				if(so != null && !so.getLines().isEmpty()) {
					salesOrderNumber = so.getSalesOrderNumber();
					msg = inv.getErrorMsg();
					//OBTENER EL DESCUENTO A NIVEL CABERO
					String discountTotal = null;
					if(inco != null) {
						if(inv.getNotes() == null ) {
							double netPrice = 0;
							double listPrice = 0;
							if(inco.getItems().size() > 0) {
								for(totals item: inco.getItems().get(0).getTotals()) {
									if(item.getTotalCode().equals("QP_TOTAL_NET_PRICE")) {
										netPrice = item.getTotalAmount();
									}else if(item.getTotalCode().equals("QP_TOTAL_LIST_PRICE")) {
										listPrice = item.getTotalAmount();
									}
								}						
								if(netPrice == listPrice) {
									discountTotal = null;
								}else {
									double totalM = Double.parseDouble(df.format(netPrice / listPrice))* 100;
									discountTotal = "0" + String.valueOf(totalM);
									discountTotal = discountTotal.substring(0, discountTotal.indexOf("."));
								}
							}
							inv.setNotes(NullValidator.isNull(discountTotal));
						}						
					}
					
					//Proceso de llenado con los datos de OM
					//CABECERO
					Branch br = branchService.getBranchByCode(so.getRequestedFulfillmentOrganizationCode());
					if(br != null) {
						inv.setBranch(br);
					}else {
						if(so.getLines().get(0).getInventoryOrganizationName() != null && !so.getLines().get(0).getInventoryOrganizationName().isEmpty()) {
							Branch branch = branchService.getBranchByCode(so.getLines().get(0).getInventoryOrganizationName());
							inv.setBranch(branch);
						}else {
							invStatus = false;
							msgError = msgError + ";BRANCH-Error al obtener la sucursal";
							log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL TRAER LA SUCURSAL");
						}
					}
					//Saber si tiene complemento exterior
					if(so.getOrderType().equals(AppConstants.INVOICE_EXTERIOR_COMPLEMENT)) {
						inv.setExtCom(true);
						//buscar el dato del catalogo de incoterm
						try {
							if(inco != null) {
								incoterm = inco.getItems().get(0).getFreightTermsCode();
							}else {
								incoterm = null;
							}
						}catch(Exception e) {
							log.warn("ERROR AL CONSULTAR Y TRAER EL INCOTERM DE LA ORDEN: " + inv.getFromSalesOrder());
							incoterm = null;
						}
					}else {
						inv.setExtCom(false);
					}
					//Serie del NN
					if(inv.getBranch() != null) {
						NextNumber nn = nextNumberService.getNumber(inv.getInvoiceType(), inv.getBranch());
						if(nn != null) {
							inv.setSerial(nn.getSerie());
						}else {
							invStatus = false;
							msgError = msgError + ";SERIAL-Error al obtener la serie de los NN";
							log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL TRAER LA SERIE DE LOS NN");
						}
					}else {
						invStatus = false;
						msgError = msgError + ";SERIAL-Error al obtener la serie";
						log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL TRAER LA SERIE");
					}
					//Uso CFDI
					if(so.getUsoCFDI() != null && !"".contains(so.getUsoCFDI())) {
						inv.setCFDIUse(so.getUsoCFDI());
					}else {
						invStatus = false;
						msgError = msgError + ";USOCFDI-Error al obtener el Uso CFDI";
						log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL TRAER EL USO CFDI");
					}
					//Proceso anticipo					
					if(so.getReceivables() != null && !so.getReceivables().isEmpty()) {	
//						if(so.getReceivables().contains("|")) {//Varios cobros
//						String[] cobros = so.getReceivables().split("|");
//						for(String s: cobros) {
//							Payments advInvoice = paymentsService.getPaymentsByCusAndReceipt(s, inv.getCustomerName());
//							if(advInvoice != null) {
//								if(advInvoice.getUUID() != null) {
//									inv.setInvoiceRelationType("07");
//									if(inv.getUUIDReference() == null){
//										inv.setUUIDReference(advInvoice.getUUID());
//									}else {
//										inv.setUUIDReference(inv.getUUIDReference() + ","+advInvoice.getUUID());
//									}										
//								}else {
//									invStatus = false;
//									msgError = msgError + ";ANTICIPOS-Error al obtener el UUID relacionado";
//									log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL OBTENER EL UUID RELACIONADO ANTICIPOS");
//								}
//								advInvoice.setAdvanceApplied(true);
//								paymentsService.updatePayment(advInvoice);
//								Invoice advInvoice2 = invoiceDao.getInvoiceByUuid(advInvoice.getUUID());
//								advInvoice2.setAdvanceAplied(true);
//								invoiceDao.updateInvoice(advInvoice2);
//							}
//						}
//					}else {//Un solo cobro
//						Payments advInvoice = paymentsService.getPaymentsByCusAndReceipt(so.getReceivables(), inv.getCustomerName());
//						if(advInvoice != null) {
//							if(advInvoice.getUUID() != null) {
//								inv.setInvoiceRelationType("07");
//								inv.setUUIDReference(advInvoice.getUUID());
//								advInvoice.setAdvanceApplied(true);
//								paymentsService.updatePayment(advInvoice);
//								Invoice advInvoice2 = invoiceDao.getInvoiceByUuid(advInvoice.getUUID());
//								advInvoice2.setAdvanceAplied(true);
//								invoiceDao.updateInvoice(advInvoice2);
//							}else {
//								invStatus = false;
//								msgError = msgError + ";ANTICIPOS-Error al obtener el UUID relacionado";
//								log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL OBTENER EL UUID RELACIONADO ANTICIPOS");
//							}
//						}
//					}
					//Cambios pendientes
						System.out.println(inv.getFromSalesOrder());
						List<Invoice> listNC = invoiceDao.getInvoiceByOtFolioCustomer(AppConstants.ORDER_TYPE_NC, inv.getFromSalesOrder(), inv.getCustomerName());
						if(listNC == null || (listNC != null && listNC.isEmpty())) {
							int pipeMatches = org.apache.commons.lang3.StringUtils.countMatches(so.getReceivables(), "|");
							int equalMatches = org.apache.commons.lang3.StringUtils.countMatches(so.getReceivables(), "=");						
							if(equalMatches > 0 && (equalMatches == pipeMatches+1)) {
								
								//Se obtienen los Anticipos
								String[ ] anticipos;
								if(so.getReceivables().contains("|")) {//Varios anticipos
									anticipos = so.getReceivables().split("\\|");
								}else {//Un solo anticipo
									anticipos = new String[]{so.getReceivables()};
								}
								
								//Validaciones y aplicación de anticipos
								if(invStatus) {
									List<Payments> updatedPaymentList = new ArrayList<Payments>();
									List<String> receiptNumberList = new ArrayList<String>();
									double invoiceAvailable = Math.round(inv.getInvoiceTotal()*100.00)/100.00;
									double invoiceExchangeRate = inv.getInvoiceExchangeRate();
									
									for(String s: anticipos) {
										if(s.contains("=")) {
											String [ ] anticipo = s.split("\\=");
											String receiptNumber = anticipo[0].trim();
											
											if(!receiptNumberList.contains(receiptNumber)) {
												receiptNumberList.add(receiptNumber);
												NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
												Number number = format.parse(anticipo[1].trim());
												
												if(number != null) {
													Payments advInvoice = paymentsService.getPaymentsByCusAndReceipt(receiptNumber, inv.getCustomerName());
													if(advInvoice != null) {
														if(advInvoice.getUUID() != null) {
															Udc relationTypeUDC = udcService.searchBySystemAndKey(AppConstants.UDC_SYSTEM_INVOICE_RELTYPE, AppConstants.UDC_KEY_ADVPAYMENT);
															inv.setInvoiceRelationType(relationTypeUDC.getStrValue1());
															inv.setUUIDReference(advInvoice.getUUID());
															double paymentAmount = Math.round(number.doubleValue()*100.00)/100.00;
															double paymentAvailable = Math.round(NullValidator.isNullD(advInvoice.getRemainingBalanceAmount())*100.00)/100.00;
															double paymentExchangeRate = Math.round(NullValidator.isNullD(advInvoice.getExchangeRate())*100.00)/100.00;
															
															if(paymentAvailable > 0D) {
																if(Double.compare(paymentAmount, paymentAvailable)  <= 0) {
																	
																	//Verificar que la moneda del Anticipo sea igual a la moneda de la Factura
																	if(inv.getInvoiceCurrency().equals(advInvoice.getCurrency())) {
																		if(Double.compare(paymentAmount, invoiceAvailable) <= 0D){
																			//Se recalcula el monto disponible en la factura
																			inv.setAdvanceAplied(true);
																			invoiceAvailable = Math.round((invoiceAvailable - paymentAmount)*100.00)/100.00;
																			
																			//Se recalcula el monto disponible del anticipo																
																			paymentAvailable = paymentAvailable - paymentAmount;
																			advInvoice.setAdvanceApplied(true);
																			advInvoice.setRemainingBalanceAmount(String.valueOf(Math.round(paymentAvailable*100.00)/100.00));
																			updatedPaymentList.add(advInvoice);																
																		}else {
																			invStatus = false;
																			msgError = msgError + ";ANTICIPOS-LA SUMATORIA DE LOS ANTICIPOS QUE DESEA APLICAR ES MAS GRANDE QUE EL MONTO DE LA FACTURA";
																			log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL APLICAR EL MONTO DE ANTICIPO, LA SUMATORIA DE LOS ANTICIPOS QUE DESEA APLICAR ES MAS GRANDE QUE EL MONTO DE LA FACTURA");
																		}
																	} else {//Moneda del Anticipo diferente a la moneda de la Factura																
																		double currentPaymentAmount = 0D;
																		double currentAvailableAmount = 0D;
																		
																		//Se convierten los montos de los Anticipos y de la Factura para validaciones y operaciones
																		if("MXN".equals(advInvoice.getCurrency())) {
																			currentAvailableAmount = Math.round((invoiceAvailable*invoiceExchangeRate)*100.00)/100.00;//Redondeo a 2 decimales
																			currentPaymentAmount = Math.round((paymentAmount/invoiceExchangeRate)*100.00)/100.00;
																		} else {
																			currentAvailableAmount = Math.round((invoiceAvailable/paymentExchangeRate)*100.00)/100.00;//Redondeo a 2 decimales
																			currentPaymentAmount = Math.round((paymentAmount*paymentExchangeRate)*100.00)/100.00;
																		}
																		
																		if(Double.compare(paymentAmount, currentAvailableAmount) <= 0){
																			//Se recalcula el monto disponible en la factura
																			inv.setAdvanceAplied(true);
																			invoiceAvailable = Math.round((invoiceAvailable - currentPaymentAmount)*100.00)/100.00;
																			
																			//Se recalcula el monto disponible del anticipo
																			paymentAvailable = paymentAvailable - paymentAmount;
																			advInvoice.setAdvanceApplied(true);
																			advInvoice.setRemainingBalanceAmount(String.valueOf(paymentAvailable));
																			updatedPaymentList.add(advInvoice);																
																		}else {
																			invStatus = false;
																			msgError = msgError + ";ANTICIPOS-LA SUMATORIA DE LOS ANTICIPOS QUE DESEA APLICAR ES MAS GRANDE QUE EL MONTO DE LA FACTURA (DIFERENTE MONEDA)";
																			log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL APLICAR EL MONTO DE ANTICIPO, LA SUMATORIA DE LOS ANTICIPOS QUE DESEA APLICAR ES MAS GRANDE QUE EL MONTO DE LA FACTURA (DIFERENTE MONEDA)");
																		}
																	}															
																}else{
																	invStatus = false;
																	msgError = msgError + ";ANTICIPOS-EL MONTO QUE DESEA APLICAR ES MAS GRANDE QUE EL MONTO DISPONIBLE DEL ANTICIPO";
																	log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL APLICAR EL MONTO DE ANTICIPO, EL MONTO ES MAS GRANDE QUE EL MONTO DISPONIBLE DEL ANTICIPO");
																}	
															}else {
																invStatus = false;
																msgError = msgError + ";ANTICIPOS-EL ANTICIPO NO TIENE SALDO PENDIENTE POR APLICAR";
																log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL APLICAR EL MONTO DE ANTICIPO, EL ANTICIPO NO TIENE SALDO PENDIENTE POR APLICAR");
															}											
														}else {
															invStatus = false;
															msgError = msgError + ";ANTICIPOS-Error al obtener el UUID relacionado";
															log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL OBTENER EL UUID RELACIONADO ANTICIPOS");
														}
													}else {
														invStatus = false;
														msgError = msgError + ";LA RELACIÓN DE ANTICIPO EL NOMBRE DEL COBRO ES INCORRECTO O NO PERTENECE AL CLIENTE ASIGNADO";
														log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL OBTENER LA RELACIÓN DE ANTICIPOS DADO QUE EL NOMBRE DEL COBRO NO EXISTE O NO ESTA RELACIONADO CON EL CLIENTE");
													}	
												}else {
													invStatus = false;
													msgError = msgError + ";LA RELACIÓN DE ANTICIPO, EL FORMATO DEL MONTO NO ES VÁLIDO";
													log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL OBTENER EL MONTO DEL ANTICIPO");
												}	
											}else {
												invStatus = false;
												msgError = msgError + ";EXISTE UN NUMERO DE ANTICIPO DUPLICADO EN LA RELACIÓN DE ANTICIPOS";
												log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL OBTENER LA RELACIÓN DE ANTICIPOS DADO QUE EXISTE UN NUMERO DE ANTICIPO DUPLICADO EN LA RELACIÓN DE ANTICIPOS");
											}
										}else {
											invStatus = false;
											msgError = msgError + ";LA RELACIÓN DE ANTICIPO, NO TIENE MONTO ASIGNADO";
											log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL OBTENER LA RELACIÓN DE ANTICIPOS");
										}
										
										if(!invStatus) {
											//No se cumplió con una validación se setea el valor inicial y se sale del ciclo (for)
											break;
										}
									}
																
									if(invStatus) {
										//Crea NC y actualiza registros de pagos
										for(String s: anticipos) {
											String [ ] anticipo = s.split("\\=");
											String receiptNumber = anticipo[0].trim();
											NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
											Number number = format.parse(anticipo[1].trim());
											double paymentAmount = Math.round(number.doubleValue()*100.00)/100.00;
											
											for(Payments payment : updatedPaymentList) {
												if(receiptNumber.equals(payment.getReceiptNumber())) {
													double paymentExchangeRate = Math.round(Double.valueOf(payment.getExchangeRate())*100.00)/100.00;
													this.createAdvPayNC(inv, paymentAmount, paymentExchangeRate, payment.getCurrency());
													paymentsService.updatePayment(payment);
													break;
												}
											}
										}

										//Notificación de Pagos Pendientes
										if(invoiceAvailable > 0D) {
											List<Udc> emails = udcService.searchBySystem(AppConstants.UDC_SYSTEM_EMAILSERRORS);
											List<String> email = new ArrayList<String>();
											for(Udc u: emails) {
												email.add(u.getUdcKey());
											}
											mailService.sendMail(email,
													AppConstants.EMAIL_ADV_PAYMENTS_SUBJECT,
													AppConstants.EMAIL_ADV_PAYMENTS_CONTENT_PENDING_PAY.replace("_FOLIO_", inv.getFolio()),
													null);
										}
									}
								}
							} else {
								invStatus = false;
								msgError = msgError + ";LA RELACIÓN DEL ANTICIPO NO TIENE UN FORMATO VÁLIDO";
								log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL OBTENER LA RELACIÓN DE ANTICIPOS, NO TIENE UN FORMATO VÁLIDO");
							}	
						}
					}
					
					//Método de pago
					if(so.getMetodoPago() != null && !"".contains(so.getMetodoPago())) {
						if(so.getMetodoPago().equals(AppConstants.PAY_METHOD)) {
							inv.setRemainingBalanceAmount(null);
							inv.setPreviousBalanceAmount(null);
						}else {
							List<Payments> searchadvPay = paymentsService.getPayByAdv(inv.getUUIDReference());
							if(searchadvPay != null && searchadvPay.size() > 0) {
								double total = 0.00;
								double totalFac = 0.00;
								for(Payments p: searchadvPay) {
									total = Double.parseDouble(p.getPaymentAmount()) + total;
								}
								totalFac = inv.getInvoiceTotal() - total;
								inv.setRemainingBalanceAmount(df.format(totalFac));
								inv.setPreviousBalanceAmount(null);
							}else {
								inv.setRemainingBalanceAmount(String.valueOf(inv.getInvoiceTotal()));
								inv.setPreviousBalanceAmount(null);
							}
						}
						inv.setPaymentMethod(so.getMetodoPago());
					}else {
						invStatus = false;
						msgError = msgError + ";METODOPAGO-Error al obtener el Método de Pago";
						log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL TRAER EL MÉTODO DE PAGO");
					}
					//Forma de pago
					if(so.getFormaPago() != null && !"".contains(so.getFormaPago())) {
						inv.setPaymentType(so.getFormaPago());
					}else {
						invStatus = false;
						msgError = msgError + ";FORMAPAGO-Error al obtener la Forma de Pago";
						log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL TRAER LA FORMA DE PAGO");
					}
					//DATOS DEL CLIENTE
					if(so.getCustomerName() != null && !"".contains(so.getCustomerName().replace(" ", "")) && !so.getCustomerName().isEmpty()) {
						inv.setCustomerName(so.getCustomerName());
						inv.setShipToName(so.getCustomerName());
					}
					if(so.getCustomerTaxIden() != null && !"".contains(so.getCustomerTaxIden().replaceAll(" ", ""))) {
						inv.setCustomerTaxIdentifier(so.getCustomerTaxIden().replaceAll(" ", ""));
					}
					if(so.getCustomerEmail() != null && !"".contains(so.getCustomerEmail().replaceAll(" ", ""))) {
						inv.setCustomerEmail(so.getCustomerEmail());
					}
					if(so.getCustomerZip() != null && !so.getCustomerZip().isEmpty()) {
						inv.setCustomerZip(so.getCustomerZip());
						inv.setShipToZip(so.getCustomerZip());
					}
					if(so.getCustomerAddress() != null && !so.getCustomerAddress().isEmpty()) {
						inv.setCustomerAddress1(so.getCustomerAddress());
						inv.setShipToaddress(so.getCustomerAddress());
						inv.setCustomerState(null);
						inv.setShipToState(null);
					}
					//Consulta de la dirreción del cliente de correo electrónico
					if(inv.getCustomerEmail() == null || "".contains(NullValidator.isNull(inv.getCustomerEmail()))) {
						CustomerInformationDTO ciDTO = soapService.getEmaiAdress(inv.getCustomerName(), inv.getCustomerPartyNumber());
						if(ciDTO != null) {
							if(ciDTO.getEmailAdress() != null) {
								for(EmailAdressDTO eA: ciDTO.getEmailAdress()) {
									if(!eA.getPartyName().equals("COSME MONGE")) {
										inv.setCustomerEmail(eA.getObjectEmailAddress());
										break;
									}
								}
							}									
						}
					}
					//Purchase Order
					if(inco != null) {
						if(inco.getItems().size() > 0) {
							inv.setPurchaseOrder(NullValidator.isNull(inco.getItems().get(0).getCustomerPONumber()));
						}else {
							if(so.getCustomerPONumber() != null && !"".contains(so.getCustomerPONumber())) {
								inv.setPurchaseOrder(so.getCustomerPONumber());
							}	
						}
					}
					//Sustitución del CFDI
					if(so.getSusticionCFDI() != null && !so.getSusticionCFDI().isEmpty()) {
						inv.setUUIDReference(so.getSusticionCFDI());
						inv.setInvoiceRelationType("04");
					}
					//SI ES NC
					if(!inv.isInvoice()) {
						Invoice invRef = invoiceDao.getSingleInvoiceByFolio(inv.getInvoiceReferenceTransactionNumber());
						if(invRef != null && (invRef.getUUID() != null && !"".contains(invRef.getUUID()))) {
							inv.setUUIDReference(invRef.getUUID());
							if(invRef.isExtCom()) {
								inv.setExtCom(true);
							}else {
								inv.setExtCom(true);
							}
						}else {
							invStatus = false;
							msgError = msgError + ";DATOSREF-Error al obtener la Factura de referencia";
							log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL TRAER LA FACTURA DE REFEENCIA");
						}
					}
					
					int count = 0;
					int countCombo = 0;
					String productsType = "";
					String singletype = "";

					
					//Revisar las lineas
					for(InvoiceDetails invLine: inv.getInvoiceDetails()) {
						for(SalesOrderLinesDTO line: so.getLines()) {						
							if(!line.isUsedTheLine() && line.getProductNumber().contains(invLine.getItemNumber()) && Double.parseDouble(line.getOrderedQuantity()) == invLine.getQuantity() 
									&& (line.getOrderedUOMCode().contains(invLine.getUomName()) || line.getOrderedUOM().toUpperCase().contains(invLine.getUomName().toUpperCase())) 
											&& "CLOSED".contains(line.getStatusCode())) {
								if(invLine.getIsInvoiceLine().equals("D")){
									count++;
									//Metodo para combo
									String leyendas = "";
									String unitCostForCombo= "";
									List<TaxCodes> tcodesCombo = new ArrayList<TaxCodes>(invLine.getTaxCodes());
									boolean isExist = nextNumberService.existCombo(invLine.getItemNumber(), inv.getCompany());
									if(isExist) {
										NextNumber nCombo = nextNumberService.getNextNumberByItem(invLine.getItemNumber(), inv.getCompany());
										if(nCombo != null) {
											for(TaxCodes tc: tcodesCombo) {
											if(tc.getId() == 2) {										
													invLine.setItemSerial(String.valueOf(nCombo.getFolio()));
													invLine.setSerialPdf(String.valueOf(nCombo.getFolio()));
													leyendas = AppConstants.LEY_EMB_COM;
													for(SalesOrderLinesDTO lineCombo: so.getLines()) {
														Set<InvoiceDetails> invDListNormal = new HashSet<InvoiceDetails>(inv.getInvoiceDetails());
														InvoiceDetails dCombo = new InvoiceDetails();
														boolean isAlreadyIn = true;
														if(!lineCombo.getProductNumber().equals(invLine.getItemNumber())) {
															if(line.getSourceTransactionLineIdentifier().equals(lineCombo.getSourceTransactionLineIdentifier())) {
																ItemsDTO itemSat = soapService.getItemDataByItemIdOrgCode(lineCombo.getProductIdentifier(), AppConstants.ORACLE_ITEMMASTER);
																if(itemSat != null) {																	
																	if(itemSat.getItemCategory().get(0).getCategoryName().contains(AppConstants.LEY_INV_CAT_EMB)) {
																		leyendas = leyendas + " MOTOR: MODELO: " + lineCombo.getProductNumber() + " SERIE: " + lineCombo.getLotSerials().get(0).getSerialNumberFrom() + "\r\n ";
																	}else if(itemSat.getItemCategory().get(0).getCategoryName().contains(AppConstants.LEY_INV_CAT_LAN)) {
																		leyendas = leyendas + " LANCHA: MODELO: " + lineCombo.getProductNumber() + " SERIE: " + lineCombo.getLotSerials().get(0).getSerialNumberFrom() + "\r\n ";
																	}
																	for(InvoiceDetails iD: inv.getInvoiceDetails()) {
																		if(iD.getItemNumber().equals(lineCombo.getProductNumber())
																				&& iD.getItemSerial().equals(lineCombo.getLotSerials().get(0).getSerialNumberFrom())) {
																			isAlreadyIn = false;
																			break;
																		}
																	}
																	if(isAlreadyIn) {
																		dCombo.setItemNumber(lineCombo.getProductNumber());
																		dCombo.setItemSerial(lineCombo.getLotSerials().get(0).getSerialNumberFrom());
																		invLine.setItemSerial(invLine.getItemSerial() + ", " + dCombo.getItemSerial()); 
																		dCombo.setQuantity(1);
																		dCombo.setIsInvoiceLine("C");
																		dCombo.setUomName("PZA");
																		dCombo.setUomCode("H87");
																		dCombo.setTransactionLineNumber(NullValidator.isNull(invLine.getTransactionLineNumber()));
																		dCombo.setImport(itemSat.isItemDFFIsImported());
																		if(lineCombo.getLotSerials().get(0).getSerialNumberFrom() != null) {
																			dCombo.setEquipmentReference("E");
																		}else{
																			dCombo.setEquipmentReference("R");
																		}
																		//CONTROL VEHICULAR
																		if(inv.isInvoice()) {
																			//Saber si va para control vehicular
																			dCombo.setIsVehicleControl("1");
																			//Tipo de cambio diario
																			CurrencyRates cRates = restService.getDailyCurrency(sdfNoTime.format(new Date()), "USD", "MXN");
																			if(cRates != null) {
																				float eRate = 0;
																				if(cRates.getItems() != null ) {
																					if(cRates.getItems().size() > 0) {
																						eRate = cRates.getItems().get(0).getConversionRate();
																					}else {
																						eRate = Float.parseFloat(String.valueOf(inv.getInvoiceExchangeRate()));													
																					}
																				}
																				invLine.setExchangeDailyRate(String.valueOf(eRate));
																			}
																			//tipo de producto código
																			ItemCategory iCat = restService.getCategoryCode(itemSat.getItemCategory().get(0).getCategoryName());
																			if(iCat != null) {
																				dCombo.setProductTypeCode(String.valueOf(iCat.getItems().get(0).getDff().get(0).getTipoProducto()));
																			}else {
																				log.error("ERROR AL TRAER EL CODIGO DEL TIPO DE PRODUCTO PARA CONTROL VEHICULAR");
																			}
																			//Costo unitario
																			String unitCostByItem = this.getUnitCostByWsForSalesOrders(inv, dCombo, so.getSalesOrderNumber());
																			dCombo.setUnitCost(NullValidator.isNull(unitCostByItem));
																			//Precio producto venta sin iva
																			String priceListItem = this.getPriceListByWs(inv, dCombo);
																			dCombo.setPriceListWTax(NullValidator.isNull(priceListItem));
																		}else {
																			dCombo.setIsVehicleControl("0");
																		}	
																		invDListNormal.add(dCombo);
																		inv.setInvoiceDetails(invDListNormal);
																		countCombo++;
																	}else {
																		countCombo++;
																	}
																}else {
																	invStatus = false;
																	msgError = msgError + ";ITEMMAST-Error al consultar los datos del IMA";
																	log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL OBTENER LOS DATOS DEL ITEM MASTER de la linea "+ invLine.getTransactionLineNumber() + ":" + inv.getFolio());
																	countCombo++;
																}
															}
														}
													}
												}
											}
										}	
									}else if(so.getLines().size() > inv.getInvoiceDetails().size()) {//Para productos marina o productos kits sin serie
										for(SalesOrderLinesDTO lineCombo: so.getLines()) {
											Set<InvoiceDetails> invDListNormal = new HashSet<InvoiceDetails>(inv.getInvoiceDetails());
											InvoiceDetails dCombo = new InvoiceDetails();
											boolean isAlreadyIn = true;
											if(!lineCombo.getProductNumber().equals(invLine.getItemNumber())) {
												if(line.getSourceTransactionLineIdentifier().equals(lineCombo.getSourceTransactionLineIdentifier())) {
													ItemsDTO itemSat = soapService.getItemDataByItemIdOrgCode(lineCombo.getProductIdentifier(), AppConstants.ORACLE_ITEMMASTER);
													ItemsDTO itemSatHeader = soapService.getItemDataByItemIdOrgCode(line.getProductIdentifier(), AppConstants.ORACLE_ITEMMASTER);
													if(itemSat != null) {	
														for(InvoiceDetails iD: inv.getInvoiceDetails()) {
															if(lineCombo.getLotSerials() != null) {
																if(iD.getItemNumber().equals(lineCombo.getProductNumber())
																		&& iD.getItemSerial().equals(lineCombo.getLotSerials().get(0).getSerialNumberFrom())) {
																	isAlreadyIn = false;
																	break;
																}
															}else {
																if(iD.getItemNumber().equals(lineCombo.getProductNumber())) {
																	isAlreadyIn = false;
																	break;
																}
															}
														}		
														if(!inv.getCustomerName().contains("SECRETARIA DE MARINA")) {
															if(itemSatHeader != null) {
																if(itemSatHeader.getItemCategory() != null) {																		
																	for(CategoryDTO ic: itemSatHeader.getItemCategory()) {
																		if(ic.getCategoryName().equals("EMBARCACION")) {
																			for(TaxCodes tc: tcodesCombo) {
																				if(tc.getId() == 2) {	
																					if(!leyendas.contains(AppConstants.LEY_EMB_COM)) {
																						leyendas = AppConstants.LEY_EMB_COM;
																					}
																				}
																			}
																			if(ic.getCategoryName().equals("EMBARCACION")) {
																				if(itemSat.getItemCategory().get(0).getCategoryName().contains(AppConstants.LEY_INV_CAT_EMB)) {
																					leyendas = leyendas + " MOTOR: MODELO: " + lineCombo.getProductNumber() + " SERIE: " + NullValidator.isNull(lineCombo.getLotSerials().get(0).getSerialNumberFrom()) + "\r\n ";
																				}else if(itemSat.getItemCategory().get(0).getCategoryName().contains(AppConstants.LEY_INV_CAT_LAN)) {
																					leyendas = leyendas + " LANCHA: MODELO: " + lineCombo.getProductNumber() + " SERIE: " + NullValidator.isNull(lineCombo.getLotSerials().get(0).getSerialNumberFrom()) + "\r\n ";
																				}
																			}
																		}																			
																	}
																}
															}
														}																													
														if(isAlreadyIn) {
															dCombo.setItemNumber(lineCombo.getProductNumber());
															if(lineCombo.getLotSerials() == null) {
																dCombo.setItemSerial(null);
															}else {
																dCombo.setItemSerial(NullValidator.isNull(lineCombo.getLotSerials().get(0).getSerialNumberFrom()));	
															}
															dCombo.setQuantity(1);
															dCombo.setIsInvoiceLine("C");
															dCombo.setUomName("PZA");
															dCombo.setUomCode("H87");
															dCombo.setTransactionLineNumber(NullValidator.isNull(invLine.getTransactionLineNumber()));
															dCombo.setImport(itemSat.isItemDFFIsImported());
															if(lineCombo.getLotSerials() != null) {
																dCombo.setEquipmentReference("E");
																if(invLine.getItemSerial() == null) {
																	invLine.setItemSerial(dCombo.getItemSerial()); 
																}else {
																	invLine.setItemSerial(invLine.getItemSerial() + ", " + dCombo.getItemSerial()); 
																}
																
															}else{
																dCombo.setEquipmentReference("R");
															}
															//CONTROL VEHICULAR
															if(inv.isInvoice()) {
																//Saber si va para control vehicular
																dCombo.setIsVehicleControl("1");
																//Tipo de cambio diario
																CurrencyRates cRates = restService.getDailyCurrency(sdfNoTime.format(new Date()), "USD", "MXN");
																if(cRates != null) {
																	float eRate = 0;
																	if(cRates.getItems() != null ) {
																		if(cRates.getItems().size() > 0) {
																			eRate = cRates.getItems().get(0).getConversionRate();
																		}else {
																			eRate = Float.parseFloat(String.valueOf(inv.getInvoiceExchangeRate()));													
																		}
																	}
																	invLine.setExchangeDailyRate(String.valueOf(eRate));
																}
																//tipo de producto código
																ItemCategory iCat = restService.getCategoryCode(itemSat.getItemCategory().get(0).getCategoryName());
																if(iCat != null) {
																	dCombo.setProductTypeCode(String.valueOf(iCat.getItems().get(0).getDff().get(0).getTipoProducto()));
																}else {
																	log.error("ERROR AL TRAER EL CODIGO DEL TIPO DE PRODUCTO PARA CONTROL VEHICULAR");
																}
																//Costo unitario
																String unitCostByItem = this.getUnitCostByWsForSalesOrders(inv, dCombo, so.getSalesOrderNumber());
																dCombo.setUnitCost(NullValidator.isNull(unitCostByItem));
																if(unitCostByItem != null) {
																	if(unitCostForCombo.isEmpty()) {
																		unitCostForCombo = unitCostByItem;
																	}else {
																		unitCostForCombo = unitCostForCombo + "," + unitCostByItem;
																	}	
																}else {
																	if(unitCostForCombo.isEmpty()) {
																		unitCostForCombo = NullValidator.isNullUnitCost(unitCostByItem);
																	}else {
																		unitCostForCombo = unitCostForCombo + "," + NullValidator.isNullUnitCost(unitCostByItem);
																	}	
																}
																															
																//Precio producto venta sin iva
																String priceListItem = this.getPriceListByWs(inv, dCombo);
																dCombo.setPriceListWTax(NullValidator.isNull(priceListItem));
															}else {
																dCombo.setIsVehicleControl("0");
															}	
															invDListNormal.add(dCombo);
															inv.setInvoiceDetails(invDListNormal);
															countCombo++;
														}else {
															countCombo++;
														}
													}else {
														invStatus = false;
														msgError = msgError + ";ITEMMAST-Error al consultar los datos del IMA";
														log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL OBTENER LOS DATOS DEL ITEM MASTER de la linea "+ invLine.getTransactionLineNumber() + ":" + inv.getFolio());
														countCombo++;
													}
												}
											}
										}
									}	
									if(leyendas != null && !leyendas.isEmpty()) {
//										invLine.setAddtionalDescription(leyendas);
										if(inv.getLongDescription() != null && !inv.getLongDescription().isEmpty()) {
											String leyCombos = inv.getLongDescription() + ", " + leyendas;
											inv.setLongDescription(leyCombos);
										}else {
											inv.setLongDescription(leyendas);
										}
										
									}
									
									//Item Master
									//ItemsDTO itemSat = soapService.getItemDataByItemNumberOrgCode(line.getProductNumber(), AppConstants.ORACLE_ITEMMASTER);							
									ItemsDTO itemSat = soapService.getItemDataByItemIdOrgCode(line.getProductIdentifier(), AppConstants.ORACLE_ITEMMASTER);
									if(itemSat != null) {
										//Clave Producto Servicio
										if(itemSat.getItemDFFClavProdServ() != null && !"".contains(itemSat.getItemDFFClavProdServ())) {
											invLine.setUnitProdServ(itemSat.getItemDFFClavProdServ());
										}else {
											invStatus = false;
											msgError = msgError + ";PRODSERVSAT-No existe la Clave ProdServ SAT -" + invLine.getItemNumber() + " en ItemMaster";
											log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL OBTENER CLAVPRODSER de la linea "+ invLine.getTransactionLineNumber() + ":" + inv.getFolio());
										}									
										//Importación
										invLine.setImport(itemSat.isItemDFFIsImported());
										//Colocar la descripción del artículo
										invLine.setItemDescription(NullValidator.isNull(itemSat.getItemDescription()));	
										//Llenar el dato del tipo de productos
										String valT = itemSat.getItemCategory().get(0).getCategoryName();
										singletype = valT;
										if(!productsType.contains(valT)) {
											if(count > 1) {
												productsType = itemSat.getItemCategory().get(0).getCategoryName() + ", " + productsType;
											}else {
												productsType = itemSat.getItemCategory().get(0).getCategoryName();
											}
										}
										//SABER SI EL PRODUCTO ES UNA LANCHA
										if(!inv.isExtCom()){
											if(valT.contains(AppConstants.LEY_INV_CAT_LAN)) {
												for(TaxCodes tc: tcodesCombo) {
													if(tc.getId() == 2) {
														if(inv.getLongDescription() != null && !inv.getLongDescription().isEmpty()) {
															String leyCombos = inv.getLongDescription() + ", " + AppConstants.LEY_LANCHAS;
															inv.setLongDescription(leyCombos);
														}else {
															inv.setLongDescription(AppConstants.LEY_LANCHAS);
														}
													}
												}
											}
										}
										
										//Flexfield no Obligatorios
										invLine.setFraccionArancelaria(NullValidator.isNull(itemSat.getItemDFFFraccionArancelaria()));
										invLine.setItemBrand(NullValidator.isNull(itemSat.getItemDFFMarca()));
										invLine.setItemModel(NullValidator.isNull(itemSat.getItemDFFModelo()));
									}else {
										invStatus = false;
										msgError = msgError + ";ITEMMAST-Error al consultar los datos del IMA PARA EL ARTÍCULO:" + invLine.getItemNumber();
										log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL OBTENER LOS DATOS DEL ITEM MASTER de la linea "+ invLine.getTransactionLineNumber() + ":" + inv.getFolio());
									}
									//Demas Flexfields
									//Colocar el metodo de envío 
									if(inco.getItems().size() > 0) {
										inv.setShippingMethod(NullValidator.isNull(inco.getItems().get(0).getShippingCarrier()));	
									}else {
										inv.setShippingMethod(NullValidator.isNull(line.getShippingMethod()));	
									}															
									//Comercio Exterior
									if(inv.isExtCom()) {
										//Catalogo del INCOTERM
										if(line.getFreightTermsCode() != null && !"".contains(line.getFreightTermsCode())) {
											invLine.setIncotermKey(line.getFreightTermsCode());
										}else {
											//buscarlo en el otro web service
											if(incoterm != null && !incoterm.isEmpty()) {
												invLine.setIncotermKey(incoterm);
											}else {
												invStatus = false;
												msgError = msgError + "; NO TIENE EL CATALOGO DE INCOTERM" + invLine.getItemNumber();
												log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL OBTENER EL DATO PARA EL CAMPO INCOTERM "+ invLine.getUomName() + ":" + inv.getFolio());
											}
										}
										//Colocar el certificado de origen
										if(so.getCertificadoOrigen() != null && !so.getCertificadoOrigen().isEmpty()) {
											if(so.getCertificadoOrigen().equals("V")) {
												invLine.setCertificateOrigin(NullValidator.isNull(so.getValorCerOrigen()));
											}else {
												invLine.setCertificateOrigin("");
											}
										}else {
											invLine.setCertificateOrigin("");
										}										
									}
									
									
									Udc satUOM = udcService.searchBySystemAndKey(AppConstants.UDC_SYSTEM_UOMSAT, line.getOrderedUOMCode());
									if(satUOM != null && satUOM.getStrValue1() != null && !"".contains(satUOM.getStrValue1())) {
										//UOM del SAT
										invLine.setUomName(satUOM.getStrValue2().toUpperCase());
										invLine.setUomCode(satUOM.getStrValue1());
										//UOM de Aduana
										invLine.setItemUomCustoms(String.valueOf(satUOM.getIntValue()));
									}else {
										invStatus = false;
										msgError = msgError + ";UOMSAT-No existe la Unidad de Medida SAT -" + invLine.getUomName() + " EN UDC";
										log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL OBTENER UDC UOMSAT de la linea "+ invLine.getUomName() + ":" + inv.getFolio());
									}
									
									if(line.getAdditionalInformation() != null && !"".contains(line.getAdditionalInformation())) {
										invLine.setAddtionalDescription(line.getAdditionalInformation());
									}
									
									//Serie y lote (Datos Opcionales)
									if(line.getLotSerials() != null) {
										String lots = "";
										String serials = "";
										for(SalesLineLotSerDTO lotSer: line.getLotSerials()) {
											if(lotSer.getLotNumber() != null && !"".contains(lotSer.getLotNumber())) {
												lots = lots + lotSer.getLotNumber() + ",";
											}
											
											if(lotSer.getSerialNumberFrom() != null && !"".contains(lotSer.getSerialNumberFrom())) {
												if(lotSer.getSerialNumberFrom().contains(lotSer.getSerialNumberTo())) {
													serials = serials + lotSer.getSerialNumberFrom() + ",";
												}else {
													String serVar = StringUtils.getAllSerialsNumbersByRange(lotSer.getSerialNumberFrom(), lotSer.getSerialNumberTo());
													serials = serials + NullValidator.isNull(serVar) + ",";
												}
											}
										}
										lots=lots!=""?lots.substring(0, lots.length() - 1):"";
										serials=serials!=""?serials.substring(0, serials.length() - 1):"";
										
										invLine.setItemLot(lots);
										invLine.setItemSerial(serials);
										invLine.setSerialPdf(serials);
									}
									//CONTROL VEHICULAR
									if(inv.isInvoice()) {
										//Saber si va para control vehicular
										invLine.setIsVehicleControl("1");
										//Tipo de cambio diario
										CurrencyRates cRates = restService.getDailyCurrency(sdfNoTime.format(new Date()), "USD", "MXN");
										if(cRates != null) {
											float eRate = 0;
											if(cRates.getItems() != null ) {
												if(cRates.getItems().size() > 0) {
													eRate = cRates.getItems().get(0).getConversionRate();
												}else {
													eRate = Float.parseFloat(String.valueOf(inv.getInvoiceExchangeRate()));													
												}
											}
											invLine.setExchangeDailyRate(String.valueOf(eRate));
										}
										//referencia de equipo
										if(invLine.getItemSerial() != null && !invLine.getItemSerial().isEmpty()) {
											invLine.setEquipmentReference("E");
											invLine.setItemDescription(invLine.getItemDescription() + " " + invLine.getItemNumber());
										}else {
											invLine.setEquipmentReference("R");
											invLine.setSerialPdf(invLine.getItemNumber());
										}
										//tipo de producto código
										ItemCategory iCat = restService.getCategoryCode(singletype);
	//									Udc searchProductTypeCode = udcService.searchBySystemAndKey(AppConstants.UDC_SYSTEM_IMEMSAPRDTYPE, singletype);
										if(iCat != null) {
											String productoType = String.valueOf(iCat.getItems().get(0).getDff().get(0).getTipoProducto());
											productoType = productoType.substring(0, productoType.indexOf("."));
											invLine.setProductTypeCode(productoType);
											if(NullValidator.isNull(iCat.getItems().get(0).getDff().get(0).getYearModel()).equals("Y")) {
												if(invLine.getItemSerial().contains(",")) {
													String [] itemSerials = null;
													itemSerials = invLine.getItemSerial().split(",");
													for(String serial: itemSerials) {
														InventoryItemSerialNumbers yearModel = restService.getInventoryItemSerialModel(inv.getBranch().getInvOrganizationCode(), invLine.getItemNumber(), serial);
														if(yearModel != null) {
															NullValidator.isNull(yearModel.getItems().get(0).getSupplierSerialNumber());
														}
													}														
												}else {
													InventoryItemSerialNumbers yearModel = restService.getInventoryItemSerialModel(inv.getBranch().getInvOrganizationCode(), invLine.getItemNumber(), invLine.getItemSerial());
													NullValidator.isNull(yearModel.getItems().get(0).getSupplierSerialNumber());
												}
											}
										}else {
											log.error("ERROR AL TRAER EL CODIGO DEL TIPO DE PRODUCTO PARA CONTROL VEHICULAR");
										}
										//Costo unitario
										if(unitCostForCombo.isEmpty()) {
											String unitCostByItem = this.getUnitCostByWsForSalesOrders(inv, invLine, so.getSalesOrderNumber());
											if(unitCostByItem == null) {
												msgError = msgError + ";RETAILS-ERROR AL OBTENER EL COSTO UNITARIO: " + inv.getFolio();
												log.warn("RETAILS-ERROR AL OBTENER EL COSTO UNITARIO: " + inv.getFolio());
											}
											invLine.setUnitCost(NullValidator.isNullUnitCost(unitCostByItem));
										}else {
//											String unitCostByItem = this.getUnitCostByWsForSalesOrders(inv, invLine);
											invLine.setUnitCost(unitCostForCombo);
										}										
										//Precio producto venta sin iva
										if(inv.getInvoiceCurrency().equals(AppConstants.DEFAUL_CURRENCY)) {
											invLine.setPriceListWTax(String.valueOf(NullValidator.isNull(invLine.getUnitPrice())));
										}else {
											String priceWTax=  df.format(invLine.getUnitPrice() * NullValidator.isNullPrice(inv.getInvoiceExchangeRate()));
											invLine.setPriceListWTax(priceWTax);
										}
										
										
									}else {
										invLine.setIsVehicleControl("0");
									}								
									
									//complemento detallista---------------------------------------------------------------
									if(invLine.getRetailComplements() != null) {
										
										//UOM 
										invLine.getRetailComplements().setUomCode(satUOM.getStrValue1());	
										//Llenar el textCode
										if(invLine.getRetailComplements().getTextNote() == null || invLine.getRetailComplements().getTextNote().isEmpty()) {
											invLine.getRetailComplements().setTextNote(NullValidator.isNull(numberLetterService.getNumberLetter(String.valueOf(invLine.getTotalAmount()), true, "MXN")));
										}
										//Datos de pedido para liverpool
										if(so.getPedidoLiverpool() != null && !so.getPedidoLiverpool().isEmpty()) {
											invLine.getRetailComplements().setReferenceId(so.getPedidoLiverpool());
										}else {
											invStatus = false;
											msgError = msgError + ";RETAILS-ERROR AL OBTENER EL NUMERO DE PEDIDO LIVERPOOL EN LA ORDEN: " + inv.getFolio();
											log.warn("RETAILS-ERROR AL OBTENER EL NUMERO DE PEDIDO LIVERPOOL EN LA ORDEN: " + inv.getFolio());
										}
										if(so.getContraRecibo() != null && !so.getContraRecibo().isEmpty()) {
											invLine.getRetailComplements().setBuyerNumberFolio(so.getContraRecibo());										
										}else {
											invStatus = false;
											msgError = msgError + ";RETAILS-ERROR AL OBTENER EL NUMERO DE CONTRA RECIBO LIVERPOOL EN LA ORDEN: " + inv.getFolio();
											log.warn("RETAILS-ERROR AL OBTENER EL NUMERO DE CONTRA RECIBO LIVERPOOL EN LA ORDEN: " + inv.getFolio());
										}
										if(so.getFechaContraRecibo() != null) {
											invLine.getRetailComplements().setBuyerDateFolio(so.getFechaContraRecibo());
											invLine.getRetailComplements().setReferenceDate(sdfNoTime.parse(sdfNoTime.format(inv.getCreationDate())));										
										}else {
											invStatus = false;
											msgError = msgError + ";RETAILS-ERROR AL OBTENER LA FECHA PEDIDO LIVERPOOL EN LA ORDEN: " + inv.getFolio();
											log.warn("RETAILS-ERROR AL OBTENER EL NUMERO LA FECHA LIVERPOOL EN LA ORDEN: " + inv.getFolio());
										}
										
										invLine.getRetailComplements().setSeller(NullValidator.isNull(so.getSalesPerson()));
										
										//complemento Destallista GTIN
//										ItemGtinDTO gtin = soapService.getItemGTINData(invLine.getItemNumber(), AppConstants.ORACLE_ITEMMASTER, inv.getCustomerPartyNumber());
										ItemGtinDTO gtin = soapService.getItemGTINData(invLine.getItemNumber(), AppConstants.ORACLE_ITEMMASTER, AppConstants.CROSS_REFERENCE_TYPE_LIV);
										if(gtin != null) {
											if((gtin.getGtin() != null && !"".contains(gtin.getGtin()))) {
												invLine.getRetailComplements().setgTin(gtin.getGtin());
//												invLine.getRetailComplements().setNumber(gtin.getItemDescription());
											}else {
												invStatus = false;
												msgError = msgError + ";RETAILSGTIN-GTIN NULO O VACIO -" + invLine.getItemNumber();
												log.warn("PARA LA ORDEN " + inv.getFolio() + " GTIN VACIO O NULO AL OBTENER el GTIN de la linea "+ invLine.getItemNumber() + ":" + inv.getFolio() + " del cliente " + inv.getCustomerPartyNumber());
											}
										}else {
											invStatus = false;
											msgError = msgError + ";RETAILSGTIN-ERROR AL OBTENER EL GTIN DEL ITEM -" + invLine.getItemNumber();
											log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL OBTENER el GTIN de la linea "+ invLine.getItemNumber() + ":" + inv.getFolio() + " del cliente " + inv.getCustomerPartyNumber());
										}
									}
									//complemento detallista---------------------------------------------------------------
									//bandera en verdadero
									line.setUsedTheLine(true);
									break;
								}
							}
						}
					}
					inv.setProductType(productsType);
					if((count + countCombo) != inv.getInvoiceDetails().size()) {
						invStatus = false;
						msgError = msgError + ";OMSALESORDERLINES-Error al actualizar las lineas, puede que alguna falte información.";
						log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL TRAER LA INFO. DE LAS LINEAS DE LA ORDEN EN OM");
					}
					
				}else {
					invStatus = false;
					msgError = msgError + ";OMSALESORDER-Error al obtener la Order en OM (La factura puede no haberse cerrado)";
					log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL TRAER LA ORDEN EN OM");
				}
				
				if(invStatus) {
					if(havePetition) {
						inv.setStatus(AppConstants.STATUS_PETITIONDATA);
					}else {
						inv.setStatus(AppConstants.STATUS_PENDING);
					}
					inv.setFromSalesOrder(salesOrderNumber);
					inv.setUpdatedBy("SYSTEM");
					inv.setUpdatedDate(new Date());
					inv.setErrorMsg(null);
					inv.setErrorActive(false);
					invoiceDao.updateInvoice(inv);
				}else {
					inv.setStatus(AppConstants.STATUS_ERROR_DATA);
					inv.setUpdatedBy("SYSTEM");
					inv.setUpdatedDate(new Date());
					if(!msgError.equals(inv.getErrorMsg())) {
						inv.setErrorMsg(msgError);	
						inv.setErrorActive(true);
					}
					invoiceDao.updateInvoice(inv);
				}
			}
		}
		
	}

	@Override
	public boolean createAdvPayNC(Invoice invoice, double paymentAmount, double exchangeRate, String currencyCode) {
		Invoice newInv = new Invoice();
		
		try {
			NextNumber nNumber = nextNumberService.getNumberCon(AppConstants.ORDER_TYPE_NC, invoice.getBranch());
			Udc creditNote = udcService.searchBySystemAndKey(AppConstants.PAYMENTS_ADVPAY, AppConstants.INVOICE_SAT_TYPE_E);
			Udc relationType = udcService.searchBySystemAndKey(AppConstants.UDC_SYSTEM_INVOICE_RELTYPE, AppConstants.UDC_KEY_ADVPAYMENT);
			
			if(paymentAmount > 0D && exchangeRate > 0D && currencyCode != null && !currencyCode.isEmpty()) {
				if (creditNote != null) {
					//Para identificar la NC, si aún no se timbra la factura de orígen
					newInv.setFromSalesOrder(invoice.getFromSalesOrder());
					
					double total = Math.round(paymentAmount*100.00)/100.00;//Redondeo a 2 decimales
					double subtotal = Math.round((total/AppConstants.INVOICE_TAX_CODE_116)*100.00)/100.00;
					double taxAmount = Math.round((total - subtotal)*100.00)/100.00;
					
					newInv.setInvoiceTotal(total);
					newInv.setInvoiceSubTotal(subtotal);
					newInv.setInvoiceTaxAmount(taxAmount);
					newInv.setInvoiceDiscount(0);
					
					newInv.setCustomerName(invoice.getCustomerName());
					newInv.setCustomerPartyNumber(invoice.getCustomerPartyNumber());
					newInv.setCustomerTaxIdentifier(invoice.getCustomerTaxIdentifier());
					newInv.setCustomerAddress1(invoice.getCustomerAddress1());
					newInv.setCustomerCity(invoice.getCustomerCity());
					newInv.setCustomerCountry(invoice.getCustomerCountry());
					newInv.setCustomerEmail(invoice.getCustomerEmail());
					newInv.setCustomerState(invoice.getCustomerState());
					newInv.setCustomerZip(invoice.getCustomerZip());
					
					newInv.setShipToName(NullValidator.isNull(invoice.getShipToName()));
					newInv.setShipToaddress(NullValidator.isNull(invoice.getShipToaddress()));
					newInv.setShipToCity(NullValidator.isNull(invoice.getShipToCity()));
					newInv.setShipToCountry(NullValidator.isNull(invoice.getShipToCountry()));
					newInv.setShipToState(NullValidator.isNull(invoice.getShipToState()));
					newInv.setShipToZip(NullValidator.isNull(invoice.getShipToZip()));
					
					newInv.setCFDIUse(creditNote.getNote());
					newInv.setBranch(invoice.getBranch());
					newInv.setCompany(invoice.getCompany());
					newInv.setCreatedBy(invoice.getCreatedBy());
					newInv.setCreationDate(dateFormat.parse(dateFormat.format(new Date())));
					newInv.setUpdatedBy(invoice.getUpdatedBy());
					newInv.setUpdatedDate(newInv.getCreationDate());
					newInv.setInvoiceRelationType(relationType.getStrValue1());
					newInv.setUUIDReference(null);
					newInv.setPayments(null);					
					newInv.setSetName(invoice.getSetName());
					newInv.setPaymentTerms("CONTADO");
					newInv.setFolio(String.valueOf(nNumber.getFolio()));
					newInv.setSerial(nNumber.getSerie());
					newInv.setStatus(AppConstants.STATUS_PENDING_UUID_NC);
					newInv.setInvoice(false);
					newInv.setInvoiceType(AppConstants.ORDER_TYPE_NC);
					newInv.setInvoiceCurrency(invoice.getInvoiceCurrency());
					newInv.setInvoiceExchangeRate(invoice.getInvoiceExchangeRate());
					newInv.setOrderSource(NullValidator.isNull(invoice.getOrderSource()));
					newInv.setOrderType(AppConstants.ORDER_TYPE_NC);
					newInv.setProductType("");
					newInv.setExtCom(invoice.isExtCom());
					newInv.setPaymentMethod(AppConstants.PAY_METHOD);
					newInv.setPaymentType(creditNote.getDescription());
					
					InvoiceDetails iD = new InvoiceDetails();
					iD.setIsInvoiceLine("D");
					iD.setItemNumber("");
					iD.setItemDescription(creditNote.getStrValue1());
					iD.setUnitProdServ(String.valueOf(creditNote.getIntValue()));
					iD.setUnitPrice(subtotal);
					iD.setTotalTaxAmount(taxAmount);
					iD.setTotalAmount(subtotal);
					iD.setTotalDiscount(0);
					iD.setUomName(AppConstants.INVOICE_ADVPAY_DEFAULT_UOM);
					iD.setUomCode(creditNote.getStrValue2());
					iD.setCurrency(currencyCode);
					iD.setExchangeRate(exchangeRate);
					iD.setImport(false);
					iD.setLineType(AppConstants.REPORT_LINE_TYPE_NOR);
					iD.setQuantity(AppConstants.INVOICE_ADVPAY_DEFAULT_QUANTITY);
					iD.setTransactionLineNumber(AppConstants.INVOICE_ADVPAY_DEFAULT_TRANSLINNUMBER);
					iD.setRetailComplements(null);
					iD.setIsVehicleControl("0");
					
					List<TaxCodes> tcs = new ArrayList<TaxCodes>();
					tcs = taxCodesService.getTCList(0, 10);
					for(TaxCodes tc: tcs) {
						if(tc.getTaxValue() == AppConstants.INVOICE_TAX_CODE_016) {
							List<TaxCodes> taxCodes = new ArrayList<TaxCodes>();
							taxCodes.add(tc);
							Set<TaxCodes> tCodes = new HashSet<TaxCodes>(taxCodes);
							iD.setTaxCodes(tCodes);
							break;
						}
					}
					
					List<InvoiceDetails> idList = new ArrayList<InvoiceDetails>();
					idList.add(iD);
					Set<InvoiceDetails> sId = new HashSet<InvoiceDetails>(idList);
					newInv.setInvoiceDetails(sId);
					
					if(!invoiceDao.saveInvoice(newInv)){
						log.error("ERROR AL CREAR LA NOTA DE CREDITO RELACIONADA CON LA ORDEN: " + invoice.getFromSalesOrder()
						+ " Y EL UUID CORRESPONDIENTE: " + invoice.getUUID());
						return false;
					}
				} else {
					log.error("ERROR AL CREAR LA NOTA DE CREDITO RELACIONADA CON LA ORDEN: " + invoice.getFromSalesOrder()
					+ " Y EL UUID CORRESPONDIENTE: " + invoice.getUUID());
					return false;
				}	
			} else {
				log.error("ERROR AL CREAR LA NOTA DE CREDITO RELACIONADA CON LA ORDEN: " + invoice.getFromSalesOrder()
				+ " Y EL UUID CORRESPONDIENTE: " + invoice.getUUID() + ", LOS DATOS DEL PAGO NO SON VÁLIDOS");
				return false;
			}
			
			return true;
		}catch(Exception e) {
			log.error("ERROR AL CREAR LA NOTA DE CREDITO RELACIONADA CON LA ORDEN: " + invoice.getFromSalesOrder() + " Y EL UUID CORRESPONDIENTE: " + invoice.getUUID());
			return false;
		}
	}
	
	@Override
	public void getInvoicedListForUpdateUUID() {
		log.info("HA INICIADO EL SERVICIO PARA INSERTAR EL UUID");
		List<String> otList = new ArrayList<String>();
		otList.add(AppConstants.ORDER_TYPE_FACTURA);
		otList.add(AppConstants.ORDER_TYPE_NC);
		
		List<String> sList = new ArrayList<String>();
		sList.add(AppConstants.STATUS_INVOICED);
		
		List<Invoice> invoiceList = invoiceDao.getInvoiceListByStatusCode(sList, otList);		
		if(invoiceList != null && ! invoiceList.isEmpty()) {
			for(Invoice inv: invoiceList) {
				if(inv.getCompany() != null) {
					if(inv.getCompany().isFusionCloud()) {
						inv = soapService.updateUUIDToOracleERPInvoice(inv);
					}else {
						inv.setStatus(AppConstants.STATUS_FINISHED);
						inv.setUpdatedBy("SYSTEM");
						inv.setUpdatedDate(new Date());
					}
					invoiceDao.updateInvoice(inv);
				}else {					
					log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL TRAER LA COMPAÑIA");
				}
			}
		}
		
		List<Payments> payList = paymentsService.getPaymentsListByStatus(sList);	
		if(payList != null && ! payList.isEmpty()) {
			for(Payments pay: payList) {
				if(pay.getCompany() != null) {
					if(pay.getCompany().isFusionCloud()) {
						pay = soapService.updateUUIDToOracleERPPayments(pay);
					}else {
						pay.setPaymentStatus(AppConstants.STATUS_FINISHED);
						pay.setUpdateDate(sdf.format(new Date()));
					}
					paymentsService.updatePayment(pay);
				}else {
					log.warn("PARA EL PAGO " + pay.getFolio() + " ERROR AL TRAER LA COMPAÑIA");
				}
			}
		}
		
		List<Invoice> otListError = new ArrayList<Invoice>();
		List<Invoice> invListErrorDATA = invoiceDao.getInvoiceListByStatusCode(AppConstants.STATUS_ERROR_DATA, "");
		List<Invoice> invListErrorCREATEFILE = invoiceDao.getInvoiceListByStatusCode(AppConstants.STATUS_ERROR_CREATE_FILE, "");
		List<Invoice> invListErroPAC = invoiceDao.getInvoiceListByStatusCode(AppConstants.STATUS_ERROR_PAC, "");
		otListError.addAll(invListErrorDATA);
		otListError.addAll(invListErrorCREATEFILE);
		otListError.addAll(invListErroPAC);
		if(otListError != null && !otListError.isEmpty()) {
			for(Invoice error: otListError) {
				if(error.getCompany() != null) {
					if(error.getCompany().isFusionCloud()) {
						error = soapService.updateUUIDToOracleERPInvoice(error);
					}else {
						error.setUpdatedBy("SYSTEM");
						error.setUpdatedDate(new Date());
					}
					invoiceDao.updateInvoice(error);
				}else {
					log.warn("PARA LA ORDEN " + error.getFolio() + " ERROR AL TRAER LA COMPAÑIA");
				}
			}
		}
		
		List<Payments> invListErrorDATAPAY = paymentsService.getPaymentsListByStatus(sList);
		if(invListErrorDATAPAY != null && ! invListErrorDATAPAY.isEmpty()) {
			for(Payments pay: invListErrorDATAPAY) {
				if(pay.getCompany() != null) {
					if(pay.getCompany().isFusionCloud()) {
						pay = soapService.updateUUIDToOracleERPPayments(pay);
					}else {
						pay.setPaymentStatus(AppConstants.STATUS_FINISHED);
						pay.setUpdateDate(sdf.format(new Date()));
					}
					paymentsService.updatePayment(pay);
				}else {
					log.warn("PARA EL PAGO " + pay.getFolio() + " ERROR AL TRAER LA COMPAÑIA");
				}
			}
		}
	}

	@Override
	public void updatePetitionInvoiceList() {
		List<String> otList = new ArrayList<String>();
		otList.add(AppConstants.ORDER_TYPE_FACTURA);
		otList.add(AppConstants.ORDER_TYPE_NC);
		
		List<String> sList = new ArrayList<String>();
		sList.add(AppConstants.STATUS_PETITIONDATA);
		sList.add(AppConstants.STATUS_ERROR_PETITION);
		List<Invoice> invoiceList = invoiceDao.getInvoiceListByStatusCode(sList, otList);	
		if(invoiceList != null && !invoiceList.isEmpty()) {
			boolean hasLot = true;
			String msgError = "";
			for(Invoice inv: invoiceList) {
				for(InvoiceDetails invLine: inv.getInvoiceDetails()) {
					if(invLine.isImport()) {
						if(invLine.getItemLot() != null && !"".contains(invLine.getItemLot())) {
							List<String> lots = StringUtils.getSerialLotsListByString(invLine.getItemLot());
							for(String lot: lots) {
								InventoryItemLots lotData = restService.getInventoryLot(inv.getBranch().getCode(), invLine.getItemNumber(), lot);
								if(lotData != null && lotData.getItems() != null && !lotData.getItems().isEmpty()) {
									invLine.setNumberPetiton(lot);
									invLine.setDatePetition(sdf.format(lotData.getItems().get(0).getOriginationDate()));
									String pet = invLine.getNumberPetiton();
									pet = pet.replace(" ", "");
									pet = pet.substring(2, 4);
									invLine.setCustomskey(pet);
								}else {
									msgError = msgError + ";ITEMLOT-Error al obtener información del Lote " + lot;
									log.warn("EL ARTICULO " + invLine.getItemNumber() + " DE LA ORDEN " + inv.getFromSalesOrder() + " ERROR AL CONSULTAR INFORMACIÓN WS DEL LOTE " + lot);
									hasLot = false;
								}
							}
						}else {
							msgError = msgError + ";ITEMDATALOT-Item de importación sin Lote " + invLine.getItemNumber();
							log.warn("EL ARTICULO " + invLine.getItemNumber() + " DE LA ORDEN " + inv.getFromSalesOrder() + " ES DE IMPORTACIÓN SIN INFORMACION DEL LOTE");
							hasLot = false;
						}
					}
				}
				
				if(hasLot) {
					inv.setStatus(AppConstants.STATUS_PENDING);
					inv.setErrorMsg("");
				}else {
					inv.setStatus(AppConstants.STATUS_ERROR_PETITION);
					inv.setErrorMsg(msgError);
				}
				
				inv.setUpdatedDate(new Date());
				inv.setUpdatedBy("SYSTEM");
				invoiceDao.updateInvoice(inv);
			}
		}
	}

	@SuppressWarnings({ "unused" })
	@Override
	public boolean createStampedPayments(List<Row> r) {
		String timeZone = "";
		String cPago = "";	
		String eerr = "";
		try {
			List<Udc> tZone = udcService.searchBySystem(AppConstants.UDC_SYSTEM_TIMEZONE);
			for(Udc u: tZone) {
				if(u.getStrValue1().equals(AppConstants.UDC_STRVALUE1_TIMEZONE)) {
					timeZone = u.getUdcKey();
				}
			}
			sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
			formatterUTC.setTimeZone(TimeZone.getTimeZone(timeZone));
			
			//Actualización del UUID en los complementos de pago
			List<Payments> listPayments = paymentsService.getPaymentsStatus(AppConstants.STATUS_ERROR_DATA_PAY);
			if(listPayments != null && !listPayments.isEmpty()) {
				for(Payments lPay: listPayments) {
					Invoice invoiceErrorPay = new Invoice();
					invoiceErrorPay = invoiceDao.getInvoiceWithOutUuid(String.valueOf(lPay.getId()));
					if(invoiceErrorPay != null) {
						if(invoiceErrorPay.getUUID() != null) {
							lPay.setUuidReference(invoiceErrorPay.getUUID());
							lPay.setPaymentError("");
							lPay.setPaymentStatus(AppConstants.STATUS_PENDING);
							paymentsService.updatePayment(lPay);
						}
					}
				}
			}
			
			List<Udc> catPago = udcService.searchBySystem(AppConstants.UDC_SYSTEM_RTYPE);
			for(Udc cp: catPago) {
				if(cp.getStrValue1().equals(AppConstants.UDC_STRVALUE1_CPAGOS)) {
					cPago = cp.getUdcKey();
					break;
				}
			}
			
			//Llenado de objeto DTO de la respuesta del reporte de pagos	
			for(Row ro: r) {
				if(ro.getColumn29() != null) {
					if(!ro.getColumn29().equals("0")) {
						Invoice invReports = new Invoice();
						invReports = fullPaymentsDTO(ro);
						if(invReports != null) {
							if(invReports.getSerial() != null && !invReports.getSerial().isEmpty()) {
								if(!invoiceDao.updateInvoice(invReports)) {
									List<Payments> pays = new ArrayList<Payments>(invReports.getPayments());
									if(pays != null) {
										Payments pay = new Payments();
										pay = pays.get(0);
										log.error("NO SE CREO EL REGISTRO DE PAYMENTS CON EL RECEIPT NUMBER: " + pay.getReceiptNumber());
									}else {
										log.error("EXISTIO ALGÚN ERROR EN EL PROCESO DE PAGOS Y ANTICIPOS");
									}
								}
							}
						}
					}					
				}						
			}
			
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@SuppressWarnings("unused")
	public Invoice fullPaymentsDTO (Row r) {
		Invoice invoice = new Invoice();
		Payments pay = new Payments();
		List<Udc> countries = new ArrayList<Udc>();
		String country = "";
		String timeZone = "";
		String eRate = String.valueOf(AppConstants.INVOICE_EXCHANGE_RATE);
		String cPago = "";
		try {			
			countries = udcService.searchBySystem(AppConstants.UDC_SYSTEM_COUNTRY);
			for(Udc u: countries) {
				if(u.getStrValue1().equals(r.getColumn3())) {
					country = u.getUdcKey();
					break;
				}
			}
			
			List<Udc> catPago = udcService.searchBySystem(AppConstants.UDC_SYSTEM_RTYPE);
			for(Udc cp: catPago) {
				if(cp.getStrValue1().equals(AppConstants.UDC_STRVALUE1_CPAGOS)) {
					cPago = cp.getUdcKey();
					break;
				}
			}
			
			List<Udc> tZone = udcService.searchBySystem(AppConstants.UDC_SYSTEM_TIMEZONE);
			for(Udc u: tZone) {
				if(u.getStrValue1().equals(AppConstants.UDC_STRVALUE1_TIMEZONE)) {
					timeZone = u.getUdcKey();
					break;
				}
			}
			sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
			formatterUTC.setTimeZone(TimeZone.getTimeZone(timeZone));
			Date date = sdf.parse(r.getColumn19());
			String dateT = formatterUTC.format(date);
			
			Branch branchPago = new Branch();
			
			Invoice inv = new Invoice();
			if(r.getColumn12() != null && !r.getColumn12().isEmpty()) {
				inv = invoiceDao.getSingleInvoiceByFolio(r.getColumn12());//TransactionReference
				if(inv.getBranch().getCode().equals("SERVICIOS")) {
					branchPago = branchService.getBranchByCode("CEDIS");
					inv.setBranch(branchPago);
				}
				if(inv != null && r.getColumn17() != null) {
					if(inv.getPaymentMethod() != null) {
						if(!inv.getPaymentMethod().equals("PUE")) {
							if(inv.getInvoiceCurrency().equals(r.getColumn16())) {//Misma moneda
								if(r.getColumn29().equals(r.getColumn31())) {//COMPLEMENTO DE PAGO UNO A UNO
									List<Payments> payments = paymentsService.getPaymentsList(inv.getUUID());
									Payments p = paymentsService.getPayment(r.getColumn23());//Receipt Number
									if(payments != null && p == null) {
										NextNumber nN = new NextNumber();
										nN = nextNumberService.getNumberCon(AppConstants.ORDER_TYPE_CPAGO, inv.getBranch());
										if(r.getColumn7() != null) {//Cambio de moneda
											eRate = r.getColumn7();
										}
										
										int con = payments.size() + 1;	
										pay.setPaymentType(AppConstants.PAYMENTS_CPAGO);				
										pay.setSerial(nN.getSerie());
										pay.setFolio(String.valueOf(nN.getFolio()));
										pay.setSerialRel(inv.getSerial());
										if(inv.getFolio().contains("-")) {
											pay.setFolioRel(inv.getFolio().substring(0,inv.getFolio().indexOf("-")));										
										}else {
											pay.setFolioRel(inv.getFolio());										
										}
										pay.setCreationDate(dateT);
										pay.setPostalCode(inv.getBranch().getZip());
										pay.setRelationType(null);
										pay.setBranch(inv.getBranch());
										pay.setCompany(companyService.getCompanyByName(r.getColumn8()));
										pay.setCountry(country);
										pay.setCustomerName(r.getColumn0());
										pay.setPartyNumber(inv.getCustomerPartyNumber());
										pay.setCustomerEmail(inv.getCustomerEmail());
										pay.setCurrency(r.getColumn16());
										pay.setExchangeRate(eRate);
										pay.setPaymentAmount(r.getColumn29());
										pay.setBankReference("");//Cliente
										pay.setAcountBankTaxIdentifier("");//Cliente
										pay.setPayerAccount("");//Cliente
										pay.setBeneficiaryAccount(r.getColumn10());
										pay.setBenBankAccTaxIden("");	
										pay.setReceiptId(r.getColumn22());
										pay.setReceiptNumber(r.getColumn23());
										pay.setPaymentNumber(String.valueOf(con));	
										pay.setTaxIdentifier(r.getColumn1().replaceAll(" ", ""));//Utilizados para nacional o extranjero
										pay.setPostalCode(r.getColumn4());
										pay.setTransactionReference(r.getColumn12());	
										pay.setPaymentMethod("PPD");
										pay.setAdvanceApplied(false);
										pay.setPaymentStatus(AppConstants.STATUS_PENDING);
										pay.setErrorActive(false);
										if(NullValidator.isNull(r.getColumn37()) != null && !NullValidator.isNull(r.getColumn37()).isEmpty()) {
											pay.setPaymentForm(r.getColumn37());
										}else {
											pay.setPaymentError("El PAGO NO TIENE FORMA DE PAGO");
											pay.setErrorActive(true);
											pay.setPaymentStatus(AppConstants.STATUS_ERROR_DATA_PAY);
										}
										if(inv.getUUID()!=null && !inv.getUUID().isEmpty()) {
											pay.setPaymentError(null);
											pay.setUuidReference(inv.getUUID());
										}else {
											pay.setPaymentError("SE HIZO UN PAGO PERO NO TIENE FOLIO FISCAL RELACIONADO ");
											pay.setErrorActive(true);
											pay.setPaymentStatus(AppConstants.STATUS_ERROR_DATA_PAY);
										}
										
										if(inv.getPreviousBalanceAmount() == null ) {
											pay.setPreviousBalanceAmount(String.valueOf(inv.getInvoiceTotal()));
											pay.setRemainingBalanceAmount(df.format(Double.parseDouble(inv.getRemainingBalanceAmount()) - Double.parseDouble(pay.getPaymentAmount())));
											inv.setPreviousBalanceAmount(pay.getRemainingBalanceAmount());
											inv.setRemainingBalanceAmount("0");
										}else {
											pay.setPreviousBalanceAmount(inv.getPreviousBalanceAmount());
											pay.setRemainingBalanceAmount(df.format(Double.parseDouble(inv.getPreviousBalanceAmount()) - Double.parseDouble(pay.getPaymentAmount())));
											inv.setPreviousBalanceAmount(pay.getRemainingBalanceAmount());
											inv.setRemainingBalanceAmount("0");
										}
										
										String bank = pay.getBeneficiaryAccount();
										bank = bank.substring(bank.length() -4);
										List<Udc> bList = udcService.searchBySystem(AppConstants.UDC_SYSTEM_ACCBANK);
										for(Udc bl: bList) {
											if(bl.getStrValue1().equals(r.getColumn11())) {
												if(bl.getUdcKey().contains(bank)) {
													pay.setBeneficiaryAccount(bl.getUdcKey());
													break;
												}
											}
										}
										
										if(pay.getPaymentAmount() != null) {
											List<Payments> nPay= new ArrayList<Payments>();
											nPay.add(pay);
											Set<Payments> realPay = new HashSet<Payments>(nPay);
											inv.setPayments(realPay);
										}										
									}
									return inv;
								}else {//Pago a varias facturas relacionadas
									PaymentsList p = paymentsListService.getByReceiptNumber(r.getColumn23());//Receipt Number
									if(p == null) {//se crea un registro 				
										PaymentsList  pList = new PaymentsList();									
										NextNumber nN = new NextNumber();							
										nN = nextNumberService.getNumberCon(AppConstants.ORDER_TYPE_CPAGO, inv.getBranch());
										
										if(r.getColumn7() != null) {//Cambio de moneda
											eRate = r.getColumn7();
										}
										//PaymentsList
										pList.setReceiptNumber(r.getColumn23());
										pList.setFolio(String.valueOf(nN.getFolio()));
										pList.setSerial(nN.getSerie());
										pList.setCustomerName(inv.getCustomerName());
										pList.setCustomerTaxId(inv.getCustomerTaxIdentifier());
										pList.setCustomerCountry(inv.getCustomerCountry());
										pList.setStatus(AppConstants.STATUS_PAYMENT_LIST_START);
										pList.setPaymentForm(NullValidator.isNull(r.getColumn37()));
										pList.setPaymentAmount(r.getColumn31());
										pList.setCurrency(r.getColumn16());
										pList.setExchangeRate(eRate);
										
										//Payments
										Payments bPay = new Payments();	
										
										List<Payments> payments = paymentsService.getPaymentsList(inv.getUUID());
										if(!payments.isEmpty()) {//Ya hay pago previos
											Payments getPay = paymentsService.getPayByUuidRNumber(r.getColumn23(), inv.getUUID());//Receipt Number & UUID
											if(getPay == null) {
												int con = payments.size() + 1;	
												
												bPay.setPaymentType(AppConstants.PAYMENTS_CPAGO);	
												bPay.setSerial(pList.getSerial());
												bPay.setFolio(pList.getFolio());
												bPay.setSerialRel(inv.getSerial());
												if(inv.getFolio().contains("-")) {
													bPay.setFolioRel(inv.getFolio().substring(0,inv.getFolio().indexOf("-")));
												}else {
													bPay.setFolioRel(inv.getFolio());
												}											
												bPay.setCreationDate(dateT);
												bPay.setPostalCode(inv.getCustomerZip());
												bPay.setRelationType(null);
												bPay.setBranch(inv.getBranch());
												bPay.setCompany(companyService.getCompanyByName(r.getColumn8()));
												bPay.setCountry(country);
												bPay.setCustomerName(r.getColumn0());
												bPay.setPartyNumber(inv.getCustomerPartyNumber());
												bPay.setCustomerEmail(inv.getCustomerEmail());
												bPay.setCurrency(r.getColumn16());
												bPay.setExchangeRate(eRate);
												bPay.setPaymentAmount(r.getColumn29());
												bPay.setBankReference("");//Cliente
												bPay.setAcountBankTaxIdentifier("");//Cliente
												bPay.setPayerAccount("");//Cliente
												bPay.setBeneficiaryAccount(r.getColumn10());
												bPay.setBenBankAccTaxIden("");	
												bPay.setReceiptId(r.getColumn22());
												bPay.setReceiptNumber(r.getColumn23());
												bPay.setPaymentNumber(String.valueOf(con));	
												bPay.setTaxIdentifier(r.getColumn1().replaceAll(" ", ""));//Utilizados para nacional o extranjero
												bPay.setPostalCode(r.getColumn4());
												bPay.setTransactionReference(r.getColumn12());	
												bPay.setPaymentMethod("PPD");
												bPay.setAdvanceApplied(false);
												bPay.setPaymentStatus(AppConstants.STATUS_UPDUUID);
												bPay.setErrorActive(false);
												if(r.getColumn37() != null && !r.getColumn37().isEmpty()) {
													bPay.setPaymentForm(r.getColumn37());
												}else {
													bPay.setPaymentError("SE HIZO UN PAGO PERO FORMA DE PAGO" );
													bPay.setErrorActive(true);
													bPay.setPaymentStatus(AppConstants.STATUS_ERROR_DATA_PAY);
												}
												if(inv.getUUID()!=null && !inv.getUUID().isEmpty()) {
													bPay.setPaymentError(null);
													bPay.setUuidReference(inv.getUUID());
												}else {
													bPay.setPaymentError("SE HIZO UN PAGO PERO NO TIENE FOLIO FISCAL RELACIONADO " );
													bPay.setErrorActive(true);
													bPay.setPaymentStatus(AppConstants.STATUS_ERROR_DATA_PAY);	
												}
												if(inv.getPreviousBalanceAmount() == null ) {
													bPay.setPreviousBalanceAmount(String.valueOf(inv.getInvoiceTotal()));
													bPay.setRemainingBalanceAmount(df.format(Double.parseDouble(inv.getRemainingBalanceAmount()) - Double.parseDouble(bPay.getPaymentAmount())));
													inv.setPreviousBalanceAmount(bPay.getRemainingBalanceAmount());
													inv.setRemainingBalanceAmount("0");
												}else {
													bPay.setPreviousBalanceAmount(inv.getPreviousBalanceAmount());
													bPay.setRemainingBalanceAmount(df.format(Double.parseDouble(inv.getPreviousBalanceAmount()) - Double.parseDouble(bPay.getPaymentAmount())));
													inv.setPreviousBalanceAmount(bPay.getRemainingBalanceAmount());
													inv.setRemainingBalanceAmount("0");
												}
												
												String bank = bPay.getBeneficiaryAccount();
												bank = bank.substring(bank.length() -4);
												List<Udc> bList = udcService.searchBySystem(AppConstants.UDC_SYSTEM_ACCBANK);
												for(Udc bl: bList) {
													if(bl.getStrValue1().equals(r.getColumn11())) {
														if(bl.getUdcKey().contains(bank)) {
															bPay.setBeneficiaryAccount(bl.getUdcKey());
															break;
														}
													}
												}
											}							
										}else {//No hay pagos previos
											int con = 1;
											
											bPay.setPaymentType(AppConstants.PAYMENTS_CPAGO);				
											bPay.setSerial(pList.getSerial());
											bPay.setFolio(pList.getFolio());
											bPay.setSerialRel(inv.getSerial());
											if(inv.getFolio().contains("-")) {
												bPay.setFolioRel(inv.getFolio().substring(0,inv.getFolio().indexOf("-")));
											}else {
												bPay.setFolioRel(inv.getFolio());
											}	
											bPay.setCreationDate(dateT);
											bPay.setPostalCode(inv.getCustomerZip());
											bPay.setRelationType(null);
											bPay.setBranch(inv.getBranch());
											bPay.setCompany(companyService.getCompanyByName(r.getColumn8()));
											bPay.setCountry(country);
											bPay.setCustomerName(r.getColumn0());
											bPay.setPartyNumber(inv.getCustomerPartyNumber());
											bPay.setCustomerEmail(inv.getCustomerEmail());
											bPay.setCurrency(r.getColumn16());
											bPay.setExchangeRate(eRate);
											bPay.setPaymentAmount(r.getColumn29());
											bPay.setBankReference("");//Cliente
											bPay.setAcountBankTaxIdentifier("");//Cliente
											bPay.setPayerAccount("");//Cliente
											bPay.setBeneficiaryAccount(r.getColumn10());
											bPay.setBenBankAccTaxIden("");	
											bPay.setReceiptId(r.getColumn22());
											bPay.setReceiptNumber(r.getColumn23());
											bPay.setPaymentNumber(String.valueOf(con));	
											bPay.setTaxIdentifier(r.getColumn1().replaceAll(" ", ""));//Utilizados para nacional o extranjero
											bPay.setPostalCode(r.getColumn4());
											bPay.setTransactionReference(r.getColumn12());	
											bPay.setPaymentMethod("PPD");
											bPay.setAdvanceApplied(false);
											bPay.setPaymentStatus(AppConstants.STATUS_UPDUUID);
											bPay.setErrorActive(false);
											if(r.getColumn37() != null && !r.getColumn37().isEmpty()) {
												bPay.setPaymentForm(r.getColumn37());
											}else {
												bPay.setPaymentError("SE HIZO UN PAGO PERO FORMA DE PAGO" );
												bPay.setErrorActive(true);
												bPay.setPaymentStatus(AppConstants.STATUS_ERROR_DATA_PAY);
											}
											if(inv.getUUID()!=null && !inv.getUUID().isEmpty()) {
												bPay.setPaymentError(null);
												bPay.setUuidReference(inv.getUUID());
											}else {
												bPay.setPaymentError("SE HIZO UN PAGO PERO NO TIENE FOLIO FISCAL RELACIONADO: " );
												bPay.setErrorActive(true);
												bPay.setPaymentStatus(AppConstants.STATUS_ERROR_DATA_PAY);
											}
											if(inv.getPreviousBalanceAmount() == null ) {
												bPay.setPreviousBalanceAmount(String.valueOf(inv.getInvoiceTotal()));
												bPay.setRemainingBalanceAmount(df.format(Double.parseDouble(inv.getRemainingBalanceAmount()) - Double.parseDouble(bPay.getPaymentAmount())));
												inv.setPreviousBalanceAmount(bPay.getRemainingBalanceAmount());
												inv.setRemainingBalanceAmount("0");
											}else {
												bPay.setPreviousBalanceAmount(inv.getPreviousBalanceAmount());
												bPay.setRemainingBalanceAmount(df.format(Double.parseDouble(inv.getPreviousBalanceAmount()) - Double.parseDouble(bPay.getPaymentAmount())));
												inv.setPreviousBalanceAmount(bPay.getRemainingBalanceAmount());
												inv.setRemainingBalanceAmount("0");
											}
											
											String bank = bPay.getBeneficiaryAccount();
											bank = bank.substring(bank.length() -4);
											List<Udc> bList = udcService.searchBySystem(AppConstants.UDC_SYSTEM_ACCBANK);
											for(Udc bl: bList) {
												if(bl.getStrValue1().equals(r.getColumn11())) {
													if(bl.getUdcKey().contains(bank)) {
														bPay.setBeneficiaryAccount(bl.getUdcKey());
														break;
													}
												}
											}
										}
										
										if(bPay.getPaymentAmount() != null) {
											List<Payments> nPay= new ArrayList<Payments>();
											nPay.add(bPay);
											Set<Payments> realPay = new HashSet<Payments>(nPay);
											pList.setPayments(realPay);
											paymentsListService.savePaymentsList(pList);
											inv.setPayments(realPay);
										}										
									}else {//se agregan datos al registro
										
										if(r.getColumn7() != null) {//Cambio de moneda
											eRate = r.getColumn7();
										}
										
										//Payments
										Payments bPay = new Payments();	
										List<Payments> payments = paymentsService.getPaymentsList(inv.getUUID());
										if(!payments.isEmpty()) {//Ya hay pago previos
											Payments getPay = paymentsService.getPayByUuidRNumber(r.getColumn23(), inv.getUUID());//Receipt Number
											if(getPay == null) {
												int con = payments.size() + 1;	
												
												bPay.setPaymentType(AppConstants.PAYMENTS_CPAGO);				
												bPay.setSerial(p.getSerial());
												bPay.setFolio(p.getFolio());
												bPay.setSerialRel(inv.getSerial());
												if(inv.getFolio().contains("-")) {
													bPay.setFolioRel(inv.getFolio().substring(0,inv.getFolio().indexOf("-")));
												}else {
													bPay.setFolioRel(inv.getFolio());
												}	
												bPay.setCreationDate(dateT);
												bPay.setPostalCode(inv.getCustomerZip());
												bPay.setRelationType(null);
												bPay.setBranch(inv.getBranch());
												bPay.setCompany(companyService.getCompanyByName(r.getColumn8()));
												bPay.setCountry(country);
												bPay.setCustomerName(r.getColumn0());
												bPay.setPartyNumber(inv.getCustomerPartyNumber());
												bPay.setCustomerEmail(inv.getCustomerEmail());
												bPay.setCurrency(r.getColumn16());
												bPay.setExchangeRate(eRate);
												bPay.setPaymentAmount(r.getColumn29());
												bPay.setBankReference("");//Cliente
												bPay.setAcountBankTaxIdentifier("");//Cliente
												bPay.setPayerAccount("");//Cliente
												bPay.setBeneficiaryAccount(r.getColumn10());
												bPay.setBenBankAccTaxIden("");	
												bPay.setReceiptId(r.getColumn22());
												bPay.setReceiptNumber(r.getColumn23());
												bPay.setPaymentNumber(String.valueOf(con));	
												bPay.setTaxIdentifier(r.getColumn1().replaceAll(" ", ""));//Utilizados para nacional o extranjero
												bPay.setPostalCode(r.getColumn4());
												bPay.setTransactionReference(r.getColumn12());	
												bPay.setPaymentMethod("PPD");
												bPay.setAdvanceApplied(false);
												bPay.setPaymentStatus(AppConstants.STATUS_UPDUUID);
												if(r.getColumn37() != null && !r.getColumn37().isEmpty()) {
													bPay.setPaymentForm(r.getColumn37());
												}else {
													bPay.setPaymentError("SE HIZO UN PAGO PERO FORMA DE PAGO" );
													bPay.setErrorActive(true);
													bPay.setPaymentStatus(AppConstants.STATUS_ERROR_DATA_PAY);
												}
												
												if(inv.getUUID()!=null && !inv.getUUID().isEmpty()) {
													bPay.setPaymentError(null);
													bPay.setUuidReference(inv.getUUID());
												}else {
													bPay.setPaymentError("SE HIZO UN PAGO PERO NO TIENE FOLIO FISCAL RELACIONADO " );
													bPay.setErrorActive(true);
													bPay.setPaymentStatus(AppConstants.STATUS_ERROR_DATA_PAY);
												}
												if(inv.getPreviousBalanceAmount() == null ) {
													bPay.setPreviousBalanceAmount(String.valueOf(inv.getInvoiceTotal()));
													bPay.setRemainingBalanceAmount(df.format(Double.parseDouble(inv.getRemainingBalanceAmount()) - Double.parseDouble(bPay.getPaymentAmount())));
													inv.setPreviousBalanceAmount(bPay.getRemainingBalanceAmount());
													inv.setRemainingBalanceAmount("0");
												}else {
													bPay.setPreviousBalanceAmount(inv.getPreviousBalanceAmount());
													bPay.setRemainingBalanceAmount(df.format(Double.parseDouble(inv.getPreviousBalanceAmount()) - Double.parseDouble(bPay.getPaymentAmount())));
													inv.setPreviousBalanceAmount(bPay.getRemainingBalanceAmount());
													inv.setRemainingBalanceAmount("0");
												}
												
												String bank = bPay.getBeneficiaryAccount();
												bank = bank.substring(bank.length() -4);
												List<Udc> bList = udcService.searchBySystem(AppConstants.UDC_SYSTEM_ACCBANK);
												for(Udc bl: bList) {
													if(bl.getStrValue1().equals(r.getColumn11())) {
														if(bl.getUdcKey().contains(bank)) {
															bPay.setBeneficiaryAccount(bl.getUdcKey());
															break;
														}
													}
												}
											}							
										}else {//No hay pagos previos
											int con = 1;
											
											bPay.setPaymentType(AppConstants.PAYMENTS_CPAGO);				
											bPay.setSerial(p.getSerial());
											bPay.setFolio(p.getFolio());
											bPay.setSerialRel(inv.getSerial());
											if(inv.getFolio().contains("-")) {
												bPay.setFolioRel(inv.getFolio().substring(0,inv.getFolio().indexOf("-")));
											}else {
												bPay.setFolioRel(inv.getFolio());
											}	
											bPay.setCreationDate(dateT);
											bPay.setPostalCode(inv.getCustomerZip());
											bPay.setRelationType(null);
											bPay.setBranch(inv.getBranch());
											bPay.setCompany(companyService.getCompanyByName(r.getColumn8()));
											bPay.setCountry(country);
											bPay.setCustomerName(r.getColumn0());
											bPay.setPartyNumber(inv.getCustomerPartyNumber());
											bPay.setCustomerEmail(inv.getCustomerEmail());
											bPay.setCurrency(r.getColumn16());
											bPay.setExchangeRate(eRate);
											bPay.setPaymentAmount(r.getColumn29());
											bPay.setBankReference("");//Cliente
											bPay.setAcountBankTaxIdentifier("");//Cliente
											bPay.setPayerAccount("");//Cliente
											bPay.setBeneficiaryAccount(r.getColumn10());
											bPay.setBenBankAccTaxIden("");	
											bPay.setReceiptId(r.getColumn22());
											bPay.setReceiptNumber(r.getColumn23());
											bPay.setPaymentNumber(String.valueOf(con));	
											bPay.setTaxIdentifier(r.getColumn1().replaceAll(" ", ""));//Utilizados para nacional o extranjero
											bPay.setPostalCode(r.getColumn4());
											bPay.setTransactionReference(r.getColumn12());	
											bPay.setPaymentMethod("PPD");
											bPay.setAdvanceApplied(false);
											bPay.setErrorActive(false);
											bPay.setPaymentStatus(AppConstants.STATUS_UPDUUID);
											if(r.getColumn37() != null && !r.getColumn37().isEmpty()) {
												bPay.setPaymentForm(r.getColumn37());
											}else {
												bPay.setPaymentError("SE HIZO UN PAGO PERO FORMA DE PAGO" );
												bPay.setErrorActive(true);
												bPay.setPaymentStatus(AppConstants.STATUS_ERROR_DATA_PAY);
											}
											if(inv.getUUID()!=null && !inv.getUUID().isEmpty()) {
												bPay.setPaymentError(null);
												bPay.setUuidReference(inv.getUUID());
											}else {
												bPay.setPaymentError("SE HIZO UN PAGO PERO NO TIENE FOLIO FISCAL RELACIONADO: " );
												bPay.setErrorActive(true);
												bPay.setPaymentStatus(AppConstants.STATUS_ERROR_DATA_PAY);
											}
											if(inv.getPreviousBalanceAmount() == null ) {
												bPay.setPreviousBalanceAmount(String.valueOf(inv.getInvoiceTotal()));
												bPay.setRemainingBalanceAmount(df.format(Double.parseDouble(inv.getRemainingBalanceAmount()) - Double.parseDouble(bPay.getPaymentAmount())));
												inv.setPreviousBalanceAmount(bPay.getRemainingBalanceAmount());
												inv.setRemainingBalanceAmount("0");
											}else {
												bPay.setPreviousBalanceAmount(inv.getPreviousBalanceAmount());
												bPay.setRemainingBalanceAmount(df.format(Double.parseDouble(inv.getPreviousBalanceAmount()) - Double.parseDouble(bPay.getPaymentAmount())));
												inv.setPreviousBalanceAmount(bPay.getRemainingBalanceAmount());
												inv.setRemainingBalanceAmount("0");
											}
											
											String bank = bPay.getBeneficiaryAccount();
											bank = bank.substring(bank.length() -4);
											List<Udc> bList = udcService.searchBySystem(AppConstants.UDC_SYSTEM_ACCBANK);
											for(Udc bl: bList) {
												if(bl.getStrValue1().equals(r.getColumn11())) {
													if(bl.getUdcKey().contains(bank)) {
														bPay.setBeneficiaryAccount(bl.getUdcKey());
														break;
													}
												}
											}
										}							
										
										if(bPay.getReceiptNumber() != null) {
											List<Payments> nnPay= new ArrayList<Payments>(p.getPayments());
											nnPay.add(bPay);
											Set<Payments> realPayL = new HashSet<Payments>(nnPay);
											p.setPayments(realPayL);
											paymentsListService.updatePaymentsList(p);
											
											List<Payments> nPay= new ArrayList<Payments>();
											nPay.add(bPay);
											Set<Payments> realPay = new HashSet<Payments>(nPay);								
											inv.setPayments(realPay);
										}							
									}
									return inv;
								}
							}else {//Diferente moneda
								double montoaplicado = Double.parseDouble(r.getColumn29());
								double pago = Double.parseDouble(r.getColumn31());
								double pagoMonedaCambio = 0.00;
								String moneda = r.getColumn16();
								double cambioMonedaDivision = 0.00;
								double cambioMoneda = 0;
								if(r.getColumn7() != null) {
									Double.parseDouble(r.getColumn7());
								}
								if(moneda.equals(AppConstants.DEFAUL_CURRENCY)) {//venta en DLLS
									if(cambioMoneda == 0) {
										cambioMonedaDivision = pago/montoaplicado;
										pagoMonedaCambio = Double.parseDouble(df.format(pago / cambioMonedaDivision)); 
									}else {
										cambioMonedaDivision = cambioMoneda;
										pagoMonedaCambio = Double.parseDouble(df.format(pago / cambioMonedaDivision)); 
									}
									r.setColumn7(String.valueOf(cambioMonedaDivision));
									r.setColumn31(String.valueOf(pagoMonedaCambio));
								}else {//venta en MXN
									if(cambioMoneda == 0) {
										cambioMonedaDivision = montoaplicado/pago;
										pagoMonedaCambio = Double.parseDouble(df.format(pago * cambioMonedaDivision)); 
									}else {
										cambioMonedaDivision = cambioMoneda;
										pagoMonedaCambio = Double.parseDouble(df.format(pago * cambioMonedaDivision)); 
									}
									r.setColumn7(String.valueOf(cambioMonedaDivision));
									r.setColumn31(String.valueOf(pagoMonedaCambio));
								}
								
								if(pagoMonedaCambio == montoaplicado) {//COMPLEMENTO DE PAGO UNO A UNO DIFERENTE MONEDA
									PaymentsList payList = paymentsListService.getByReceiptNumber(r.getColumn23());//Receipt Number
									if(payList == null) {//Creamos registros de lista de pagos 
										NextNumber nN = new NextNumber();							
										nN = nextNumberService.getNumberCon(AppConstants.ORDER_TYPE_CPAGO, inv.getBranch());
										
										PaymentsList pList = new PaymentsList();
										pList.setCurrency(r.getColumn16());
										pList.setCustomerName(r.getColumn0());
										pList.setCustomerTaxId(r.getColumn1().replaceAll(" ", ""));
										pList.setCustomerCountry(inv.getCustomerCountry());
										pList.setPaymentAmount(String.valueOf(pago));
										pList.setPaymentForm(r.getColumn37());
										pList.setExchangeRate(r.getColumn7());
										pList.setReceiptNumber(r.getColumn23());
										pList.setFolio(String.valueOf(nN.getFolio()));
										pList.setSerial(nN.getSerie());
										pList.setStatus(AppConstants.STATUS_PAYMENT_LIST_START);
										
										Payments bPay = new Payments();
										List<Payments> payments = paymentsService.getPaymentsList(inv.getUUID());
										if(!payments.isEmpty()) {//Ya hay pagos previos 
											Payments getPay = paymentsService.getPayByUuidRNumber(r.getColumn23(), inv.getUUID());//Receipt Number & UUID
											if(getPay == null) {
												int con = payments.size() + 1;	
												
												bPay.setPaymentType(AppConstants.PAYMENTS_CPAGO);				
												bPay.setSerial(pList.getSerial());
												bPay.setFolio(pList.getFolio());
												bPay.setSerialRel(inv.getSerial());
												if(inv.getFolio().contains("-")) {
													bPay.setFolioRel(inv.getFolio().substring(0,inv.getFolio().indexOf("-")));
												}else {
													bPay.setFolioRel(inv.getFolio());
												}	
												bPay.setCreationDate(dateT);
												bPay.setPostalCode(inv.getCustomerZip());
												bPay.setRelationType(null);
												bPay.setBranch(inv.getBranch());
												bPay.setCompany(companyService.getCompanyByName(r.getColumn8()));
												bPay.setCountry(country);
												bPay.setCustomerName(r.getColumn0());
												bPay.setPartyNumber(inv.getCustomerPartyNumber());
												bPay.setCustomerEmail(inv.getCustomerEmail());
												bPay.setCurrency(inv.getInvoiceCurrency());
												bPay.setExchangeRate(r.getColumn7());
												bPay.setPaymentAmount(r.getColumn29());
												bPay.setBankReference("");//Cliente
												bPay.setAcountBankTaxIdentifier("");//Cliente
												bPay.setPayerAccount("");//Cliente
												bPay.setBeneficiaryAccount(r.getColumn10());
												bPay.setBenBankAccTaxIden("");	
												bPay.setReceiptId(r.getColumn22());
												bPay.setReceiptNumber(r.getColumn23());
												bPay.setPaymentNumber(String.valueOf(con));	
												bPay.setTaxIdentifier(r.getColumn1().replaceAll(" ", ""));//Utilizados para nacional o extranjero
												bPay.setPostalCode(r.getColumn4());
												bPay.setTransactionReference(r.getColumn12());	
												bPay.setPaymentMethod("PPD");
												bPay.setAdvanceApplied(false);
												bPay.setErrorActive(false);
												bPay.setPaymentStatus(AppConstants.STATUS_UPDUUID);
												if(r.getColumn37() != null && !r.getColumn37().isEmpty()) {
													bPay.setPaymentForm(r.getColumn37());
												}else {
													bPay.setPaymentError("SE HIZO UN PAGO PERO FORMA DE PAGO" );
													bPay.setErrorActive(true);
													bPay.setPaymentStatus(AppConstants.STATUS_ERROR_DATA_PAY);
												}										
												if(inv.getUUID()!=null && !inv.getUUID().isEmpty()) {
													bPay.setPaymentError(null);
													bPay.setUuidReference(inv.getUUID());
												}else {
													bPay.setPaymentError("SE HIZO UN PAGO PERO NO TIENE FOLIO FISCAL RELACIONADO: " );
													bPay.setErrorActive(true);
													bPay.setPaymentStatus(AppConstants.STATUS_ERROR_DATA_PAY);
												}
												if(inv.getPreviousBalanceAmount() == null ) {
													bPay.setPreviousBalanceAmount(String.valueOf(inv.getInvoiceTotal()));
													bPay.setRemainingBalanceAmount(df.format(Double.parseDouble(inv.getRemainingBalanceAmount()) - Double.parseDouble(bPay.getPaymentAmount())));
													inv.setPreviousBalanceAmount(bPay.getRemainingBalanceAmount());
													inv.setRemainingBalanceAmount("0");
												}else {
													bPay.setPreviousBalanceAmount(inv.getPreviousBalanceAmount());
													bPay.setRemainingBalanceAmount(df.format(Double.parseDouble(inv.getPreviousBalanceAmount()) - Double.parseDouble(bPay.getPaymentAmount())));
													inv.setPreviousBalanceAmount(bPay.getRemainingBalanceAmount());
													inv.setRemainingBalanceAmount("0");
												}
												
												String bank = bPay.getBeneficiaryAccount();
												bank = bank.substring(bank.length() -4);
												List<Udc> bList = udcService.searchBySystem(AppConstants.UDC_SYSTEM_ACCBANK);
												for(Udc bl: bList) {
													if(bl.getStrValue1().equals(r.getColumn11())) {
														if(bl.getUdcKey().contains(bank)) {
															bPay.setBeneficiaryAccount(bl.getUdcKey());
															break;
														}
													}
												}
											}
										}else {//No hay pagos previos
											int con = 1;
											
											bPay.setPaymentType(AppConstants.PAYMENTS_CPAGO);				
											bPay.setSerial(pList.getSerial());
											bPay.setFolio(pList.getFolio());
											bPay.setSerialRel(inv.getSerial());
											if(inv.getFolio().contains("-")) {
												bPay.setFolioRel(inv.getFolio().substring(0,inv.getFolio().indexOf("-")));
											}else {
												bPay.setFolioRel(inv.getFolio());
											}	
											bPay.setCreationDate(dateT);
											bPay.setPostalCode(inv.getCustomerZip());
											bPay.setRelationType(null);
											bPay.setBranch(inv.getBranch());
											bPay.setCompany(companyService.getCompanyByName(r.getColumn8()));
											bPay.setCountry(country);
											bPay.setCustomerName(r.getColumn0());
											bPay.setPartyNumber(inv.getCustomerPartyNumber());
											bPay.setCustomerEmail(inv.getCustomerEmail());
											bPay.setCurrency(inv.getInvoiceCurrency());
											bPay.setExchangeRate(r.getColumn7());
											bPay.setPaymentAmount(r.getColumn29());
											bPay.setBankReference("");//Cliente
											bPay.setAcountBankTaxIdentifier("");//Cliente
											bPay.setPayerAccount("");//Cliente
											bPay.setBeneficiaryAccount(r.getColumn10());
											bPay.setBenBankAccTaxIden("");	
											bPay.setReceiptId(r.getColumn22());
											bPay.setReceiptNumber(r.getColumn23());
											bPay.setPaymentNumber(String.valueOf(con));	
											bPay.setTaxIdentifier(r.getColumn1().replaceAll(" ", ""));//Utilizados para nacional o extranjero
											bPay.setPostalCode(r.getColumn4());
											bPay.setTransactionReference(r.getColumn12());	
											bPay.setPaymentMethod("PPD");
											bPay.setAdvanceApplied(false);
											bPay.setErrorActive(false);
											bPay.setPaymentStatus(AppConstants.STATUS_UPDUUID);
											if(r.getColumn37() != null && !r.getColumn37().isEmpty()) {
												bPay.setPaymentForm(r.getColumn37());
											}else {
												bPay.setPaymentError("SE HIZO UN PAGO PERO FORMA DE PAGO" );
												bPay.setErrorActive(true);
												bPay.setPaymentStatus(AppConstants.STATUS_ERROR_DATA_PAY);
											}									
											if(inv.getUUID()!=null && !inv.getUUID().isEmpty()) {
												bPay.setPaymentError(null);
												bPay.setUuidReference(inv.getUUID());
											}else {
												bPay.setPaymentError("SE HIZO UN PAGO PERO NO TIENE FOLIO FISCAL RELACIONADO: " );
												bPay.setErrorActive(true);
												pay.setPaymentStatus(AppConstants.STATUS_ERROR_DATA_PAY);
											}
											if(inv.getPreviousBalanceAmount() == null ) {
												bPay.setPreviousBalanceAmount(String.valueOf(inv.getInvoiceTotal()));
												bPay.setRemainingBalanceAmount(df.format(Double.parseDouble(inv.getRemainingBalanceAmount()) - Double.parseDouble(bPay.getPaymentAmount())));
												inv.setPreviousBalanceAmount(bPay.getRemainingBalanceAmount());
												inv.setRemainingBalanceAmount("0");
											}else {
												bPay.setPreviousBalanceAmount(inv.getPreviousBalanceAmount());
												bPay.setRemainingBalanceAmount(df.format(Double.parseDouble(inv.getPreviousBalanceAmount()) - Double.parseDouble(bPay.getPaymentAmount())));
												inv.setPreviousBalanceAmount(bPay.getRemainingBalanceAmount());
												inv.setRemainingBalanceAmount("0");
											}
											
											String bank = bPay.getBeneficiaryAccount();
											bank = bank.substring(bank.length() -4);
											List<Udc> bList = udcService.searchBySystem(AppConstants.UDC_SYSTEM_ACCBANK);
											for(Udc bl: bList) {
												if(bl.getStrValue1().equals(r.getColumn11())) {
													if(bl.getUdcKey().contains(bank)) {
														bPay.setBeneficiaryAccount(bl.getUdcKey());
														break;
													}
												}
											}
										}
										if(bPay.getPaymentAmount() !=null) {
											List<Payments> nPay= new ArrayList<Payments>();
											nPay.add(bPay);
											Set<Payments> realPay = new HashSet<Payments>(nPay);
											pList.setPayments(realPay);
											paymentsListService.savePaymentsList(pList);
											inv.setPayments(realPay);
										}										
									}else {//Añadimos registros para lista de pagos diferente moneda
										//Payments
										Payments bPay = new Payments();	
										List<Payments> payments = paymentsService.getPaymentsList(inv.getUUID());
										if(!payments.isEmpty()) {//Ya hay pago previos
											Payments getPay = paymentsService.getPayByUuidRNumber(r.getColumn23(), inv.getUUID());//Receipt Number
											if(getPay == null) {
												int con = payments.size() + 1;	
												
												bPay.setPaymentType(AppConstants.PAYMENTS_CPAGO);				
												bPay.setSerial(payList.getSerial());
												bPay.setFolio(payList.getFolio());
												bPay.setSerialRel(inv.getSerial());
												if(inv.getFolio().contains("-")) {
													bPay.setFolioRel(inv.getFolio().substring(0,inv.getFolio().indexOf("-")));
												}else {
													bPay.setFolioRel(inv.getFolio());
												}	
												bPay.setCreationDate(dateT);
												bPay.setPostalCode(inv.getCustomerZip());
												bPay.setRelationType(null);
												bPay.setBranch(inv.getBranch());
												bPay.setCompany(companyService.getCompanyByName(r.getColumn8()));
												bPay.setCountry(country);
												bPay.setCustomerName(r.getColumn0());
												bPay.setPartyNumber(inv.getCustomerPartyNumber());
												bPay.setCustomerEmail(inv.getCustomerEmail());
												bPay.setCurrency(inv.getInvoiceCurrency());
												bPay.setExchangeRate(r.getColumn7());
												bPay.setPaymentAmount(r.getColumn29());
												bPay.setBankReference("");//Cliente
												bPay.setAcountBankTaxIdentifier("");//Cliente
												bPay.setPayerAccount("");//Cliente
												bPay.setBeneficiaryAccount(r.getColumn10());
												bPay.setBenBankAccTaxIden("");	
												bPay.setReceiptId(r.getColumn22());
												bPay.setReceiptNumber(r.getColumn23());
												bPay.setPaymentNumber(String.valueOf(con));	
												bPay.setTaxIdentifier(r.getColumn1().replaceAll(" ", ""));//Utilizados para nacional o extranjero
												bPay.setPostalCode(r.getColumn4());
												bPay.setTransactionReference(r.getColumn12());	
												bPay.setPaymentMethod("PPD");
												bPay.setAdvanceApplied(false);
												bPay.setPaymentStatus(AppConstants.STATUS_UPDUUID);
												bPay.setErrorActive(false);
												if(r.getColumn37() != null && !r.getColumn37().isEmpty()) {
													bPay.setPaymentForm(r.getColumn37());
												}else {
													bPay.setPaymentError("SE HIZO UN PAGO PERO FORMA DE PAGO" );
													bPay.setErrorActive(true);
													bPay.setPaymentStatus(AppConstants.STATUS_ERROR_DATA_PAY);
												}										
												if(inv.getUUID()!=null && !inv.getUUID().isEmpty()) {
													bPay.setPaymentError(null);
													bPay.setUuidReference(inv.getUUID());
												}else {
													bPay.setPaymentError("SE HIZO UN PAGO PERO NO TIENE FOLIO FISCAL RELACIONADO: " );
													bPay.setErrorActive(true);
													bPay.setPaymentStatus(AppConstants.STATUS_ERROR_DATA_PAY);
												}
												if(inv.getPreviousBalanceAmount() == null ) {
													bPay.setPreviousBalanceAmount(String.valueOf(inv.getInvoiceTotal()));
													bPay.setRemainingBalanceAmount(df.format(Double.parseDouble(inv.getRemainingBalanceAmount()) - Double.parseDouble(bPay.getPaymentAmount())));
													inv.setPreviousBalanceAmount(bPay.getRemainingBalanceAmount());
													inv.setRemainingBalanceAmount("0");
												}else {
													bPay.setPreviousBalanceAmount(inv.getPreviousBalanceAmount());
													bPay.setRemainingBalanceAmount(df.format(Double.parseDouble(inv.getPreviousBalanceAmount()) - Double.parseDouble(bPay.getPaymentAmount())));
													inv.setPreviousBalanceAmount(bPay.getRemainingBalanceAmount());
													inv.setRemainingBalanceAmount("0");
												}
												
												String bank = bPay.getBeneficiaryAccount();
												bank = bank.substring(bank.length() -4);
												List<Udc> bList = udcService.searchBySystem(AppConstants.UDC_SYSTEM_ACCBANK);
												for(Udc bl: bList) {
													if(bl.getStrValue1().equals(r.getColumn11())) {
														if(bl.getUdcKey().contains(bank)) {
															bPay.setBeneficiaryAccount(bl.getUdcKey());
															break;
														}
													}
												}
											}							
										}else {//No hay pagos previos
											int con = 1;
											
											bPay.setPaymentType(AppConstants.PAYMENTS_CPAGO);
											bPay.setSerial(payList.getSerial());
											bPay.setFolio(payList.getFolio());
											bPay.setSerialRel(inv.getSerial());
											if(inv.getFolio().contains("-")) {
												bPay.setFolioRel(inv.getFolio().substring(0,inv.getFolio().indexOf("-")));
											}else {
												bPay.setFolioRel(inv.getFolio());
											}	
											bPay.setCreationDate(dateT);
											bPay.setPostalCode(inv.getCustomerZip());
											bPay.setRelationType(null);
											bPay.setBranch(inv.getBranch());
											bPay.setCompany(companyService.getCompanyByName(r.getColumn8()));
											bPay.setCountry(country);
											bPay.setCustomerName(r.getColumn0());
											bPay.setPartyNumber(inv.getCustomerPartyNumber());
											bPay.setCustomerEmail(inv.getCustomerEmail());
											bPay.setCurrency(inv.getInvoiceCurrency());
											bPay.setExchangeRate(r.getColumn7());
											bPay.setPaymentAmount(r.getColumn29());
											bPay.setBankReference("");//Cliente
											bPay.setAcountBankTaxIdentifier("");//Cliente
											bPay.setPayerAccount("");//Cliente
											bPay.setBeneficiaryAccount(r.getColumn10());
											bPay.setBenBankAccTaxIden("");	
											bPay.setReceiptId(r.getColumn22());
											bPay.setReceiptNumber(r.getColumn23());
											bPay.setPaymentNumber(String.valueOf(con));	
											bPay.setTaxIdentifier(r.getColumn1().replaceAll(" ", ""));//Utilizados para nacional o extranjero
											bPay.setPostalCode(r.getColumn4());
											bPay.setTransactionReference(r.getColumn12());	
											bPay.setPaymentMethod("PPD");
											bPay.setAdvanceApplied(false);
											bPay.setErrorActive(false);
											bPay.setPaymentStatus(AppConstants.STATUS_UPDUUID);
											if(r.getColumn37() != null && !r.getColumn37().isEmpty()) {
												bPay.setPaymentForm(r.getColumn37());
											}else {
												bPay.setPaymentError("SE HIZO UN PAGO PERO FORMA DE PAGO" );
												bPay.setErrorActive(true);
												bPay.setPaymentStatus(AppConstants.STATUS_ERROR_DATA_PAY);
											}									
											if(inv.getUUID()!=null && !inv.getUUID().isEmpty()) {
												bPay.setPaymentError(null);
												bPay.setUuidReference(inv.getUUID());
											}else {
												bPay.setPaymentError("SE HIZO UN PAGO PERO NO TIENE FOLIO FISCAL RELACIONADO: " );
												bPay.setErrorActive(true);
												pay.setPaymentStatus(AppConstants.STATUS_ERROR_DATA_PAY);
											}
											if(inv.getPreviousBalanceAmount() == null ) {
												bPay.setPreviousBalanceAmount(String.valueOf(inv.getInvoiceTotal()));
												bPay.setRemainingBalanceAmount(df.format(Double.parseDouble(inv.getRemainingBalanceAmount()) - Double.parseDouble(bPay.getPaymentAmount())));
												inv.setPreviousBalanceAmount(bPay.getRemainingBalanceAmount());
												inv.setRemainingBalanceAmount("0");
											}else {
												bPay.setPreviousBalanceAmount(inv.getPreviousBalanceAmount());
												bPay.setRemainingBalanceAmount(df.format(Double.parseDouble(inv.getPreviousBalanceAmount()) - Double.parseDouble(bPay.getPaymentAmount())));
												inv.setPreviousBalanceAmount(bPay.getRemainingBalanceAmount());
												inv.setRemainingBalanceAmount("0");
											}
											
											String bank = bPay.getBeneficiaryAccount();
											bank = bank.substring(bank.length() -4);
											List<Udc> bList = udcService.searchBySystem(AppConstants.UDC_SYSTEM_ACCBANK);
											for(Udc bl: bList) {
												if(bl.getStrValue1().equals(r.getColumn11())) {
													if(bl.getUdcKey().contains(bank)) {
														bPay.setBeneficiaryAccount(bl.getUdcKey());
														break;
													}
												}
											}
										}							
										
										if(bPay.getReceiptNumber() != null) {
											List<Payments> nnPay= new ArrayList<Payments>(payList.getPayments());
											nnPay.add(bPay);
											Set<Payments> realPayL = new HashSet<Payments>(nnPay);
											payList.setPayments(realPayL);
											paymentsListService.updatePaymentsList(payList);
											
											List<Payments> nPay= new ArrayList<Payments>();
											nPay.add(bPay);
											Set<Payments> realPay = new HashSet<Payments>(nPay);								
											inv.setPayments(realPay);
										}
									}
									
									return inv;
								}else {//Pago a varias facturas relacionadas diferente moneda
									PaymentsList p = paymentsListService.getByReceiptNumber(r.getColumn23());//Receipt Number
									if(p == null) {//se crea un registro 				
										PaymentsList  pList = new PaymentsList();									
										NextNumber nN = new NextNumber();							
										nN = nextNumberService.getNumberCon(AppConstants.ORDER_TYPE_CPAGO, inv.getBranch());
										
										if(r.getColumn7() != null) {//Cambio de moneda
											eRate = r.getColumn7();
										}
										//PaymentsList
										pList.setReceiptNumber(r.getColumn23());
										pList.setFolio(String.valueOf(nN.getFolio()));
										pList.setSerial(nN.getSerie());
										pList.setCustomerName(inv.getCustomerName());
										pList.setCustomerTaxId(inv.getCustomerTaxIdentifier());
										pList.setCustomerCountry(inv.getCustomerCountry());
										pList.setStatus(AppConstants.STATUS_PAYMENT_LIST_START);
										pList.setPaymentForm(NullValidator.isNull(r.getColumn37()));
										pList.setPaymentAmount(String.valueOf(pago));
										pList.setCurrency(r.getColumn16());
										pList.setExchangeRate(eRate);
										
										//Payments
										Payments bPay = new Payments();	
										
										List<Payments> payments = paymentsService.getPaymentsList(inv.getUUID());
										if(!payments.isEmpty()) {//Ya hay pago previos
											Payments getPay = paymentsService.getPayByUuidRNumber(r.getColumn23(), inv.getUUID());//Receipt Number & UUID
											if(getPay == null) {
												int con = payments.size() + 1;	
												
												bPay.setPaymentType(AppConstants.PAYMENTS_CPAGO);				
												bPay.setSerial(pList.getSerial());
												bPay.setFolio(pList.getFolio());
												bPay.setSerialRel(inv.getSerial());
												if(inv.getFolio().contains("-")) {
													bPay.setFolioRel(inv.getFolio().substring(0,inv.getFolio().indexOf("-")));
												}else {
													bPay.setFolioRel(inv.getFolio());
												}	
												bPay.setCreationDate(dateT);
												bPay.setPostalCode(inv.getCustomerZip());
												bPay.setRelationType(null);
												bPay.setBranch(inv.getBranch());
												bPay.setCompany(companyService.getCompanyByName(r.getColumn8()));
												bPay.setCountry(country);
												bPay.setCustomerName(r.getColumn0());
												bPay.setPartyNumber(inv.getCustomerPartyNumber());
												bPay.setCustomerEmail(inv.getCustomerEmail());
												bPay.setCurrency(inv.getInvoiceCurrency());
												bPay.setExchangeRate(r.getColumn7());
												bPay.setPaymentAmount(r.getColumn29());
												bPay.setBankReference("");//Cliente
												bPay.setAcountBankTaxIdentifier("");//Cliente
												bPay.setPayerAccount("");//Cliente
												bPay.setBeneficiaryAccount(r.getColumn10());
												bPay.setBenBankAccTaxIden("");	
												bPay.setReceiptId(r.getColumn22());
												bPay.setReceiptNumber(r.getColumn23());
												bPay.setPaymentNumber(String.valueOf(con));	
												bPay.setTaxIdentifier(r.getColumn1().replaceAll(" ", ""));//Utilizados para nacional o extranjero
												bPay.setPostalCode(r.getColumn4());
												bPay.setTransactionReference(r.getColumn12());	
												bPay.setPaymentMethod("PPD");
												bPay.setAdvanceApplied(false);
												bPay.setErrorActive(false);										
												bPay.setPaymentStatus(AppConstants.STATUS_UPDUUID);
												if(r.getColumn37() != null && !r.getColumn37().isEmpty()) {
													bPay.setPaymentForm(r.getColumn37());
												}else {
													bPay.setPaymentError("SE HIZO UN PAGO PERO FORMA DE PAGO" );
													bPay.setErrorActive(true);
													bPay.setPaymentStatus(AppConstants.STATUS_ERROR_DATA_PAY);
												}										
												if(inv.getUUID()!=null && !inv.getUUID().isEmpty()) {
													bPay.setPaymentError(null);
													bPay.setUuidReference(inv.getUUID());
												}else {
													bPay.setPaymentError("SE HIZO UN PAGO PERO NO TIENE FOLIO FISCAL RELACIONADO: " );
													bPay.setErrorActive(true);
													bPay.setPaymentStatus(AppConstants.STATUS_ERROR_DATA_PAY);
												}
												if(inv.getPreviousBalanceAmount() == null ) {
													bPay.setPreviousBalanceAmount(String.valueOf(inv.getInvoiceTotal()));
													bPay.setRemainingBalanceAmount(df.format(Double.parseDouble(inv.getRemainingBalanceAmount()) - Double.parseDouble(bPay.getPaymentAmount())));
													inv.setPreviousBalanceAmount(bPay.getRemainingBalanceAmount());
													inv.setRemainingBalanceAmount("0");
												}else {
													bPay.setPreviousBalanceAmount(inv.getPreviousBalanceAmount());
													bPay.setRemainingBalanceAmount(df.format(Double.parseDouble(inv.getPreviousBalanceAmount()) - Double.parseDouble(bPay.getPaymentAmount())));
													inv.setPreviousBalanceAmount(bPay.getRemainingBalanceAmount());
													inv.setRemainingBalanceAmount("0");
												}
												
												String bank = bPay.getBeneficiaryAccount();
												bank = bank.substring(bank.length() -4);
												List<Udc> bList = udcService.searchBySystem(AppConstants.UDC_SYSTEM_ACCBANK);
												for(Udc bl: bList) {
													if(bl.getStrValue1().equals(r.getColumn11())) {
														if(bl.getUdcKey().contains(bank)) {
															bPay.setBeneficiaryAccount(bl.getUdcKey());
															break;
														}
													}
												}
											}							
										}else {//No hay pagos previos
											int con = 1;
											
											bPay.setPaymentType(AppConstants.PAYMENTS_CPAGO);
											bPay.setSerial(pList.getSerial());
											bPay.setFolio(pList.getFolio());
											bPay.setSerialRel(inv.getSerial());
											if(inv.getFolio().contains("-")) {
												bPay.setFolioRel(inv.getFolio().substring(0,inv.getFolio().indexOf("-")));
											}else {
												bPay.setFolioRel(inv.getFolio());
											}	
											bPay.setCreationDate(dateT);
											bPay.setPostalCode(inv.getCustomerZip());
											bPay.setRelationType(null);
											bPay.setBranch(inv.getBranch());
											bPay.setCompany(companyService.getCompanyByName(r.getColumn8()));
											bPay.setCountry(country);
											bPay.setCustomerName(r.getColumn0());
											bPay.setPartyNumber(inv.getCustomerPartyNumber());
											bPay.setCustomerEmail(inv.getCustomerEmail());
											bPay.setCurrency(inv.getInvoiceCurrency());
											bPay.setExchangeRate(r.getColumn7());
											bPay.setPaymentAmount(r.getColumn29());
											bPay.setBankReference("");//Cliente
											bPay.setAcountBankTaxIdentifier("");//Cliente
											bPay.setPayerAccount("");//Cliente
											bPay.setBeneficiaryAccount(r.getColumn10());
											bPay.setBenBankAccTaxIden("");	
											bPay.setReceiptId(r.getColumn22());
											bPay.setReceiptNumber(r.getColumn23());
											bPay.setPaymentNumber(String.valueOf(con));	
											bPay.setTaxIdentifier(r.getColumn1().replaceAll(" ", ""));//Utilizados para nacional o extranjero
											bPay.setPostalCode(r.getColumn4());
											bPay.setTransactionReference(r.getColumn12());	
											bPay.setPaymentMethod("PPD");
											bPay.setAdvanceApplied(false);
											bPay.setErrorActive(false);									
											bPay.setPaymentStatus(AppConstants.STATUS_UPDUUID);
											if(r.getColumn37() != null && !r.getColumn37().isEmpty()) {
												bPay.setPaymentForm(r.getColumn37());
											}else {
												bPay.setPaymentError("SE HIZO UN PAGO PERO FORMA DE PAGO" );
												bPay.setErrorActive(true);
												bPay.setPaymentStatus(AppConstants.STATUS_ERROR_DATA_PAY);
											}
											if(inv.getUUID()!=null && !inv.getUUID().isEmpty()) {
												bPay.setPaymentError(null);
												bPay.setUuidReference(inv.getUUID());
											}else {
												bPay.setPaymentError("SE HIZO UN PAGO PERO NO TIENE FOLIO FISCAL RELACIONADO: " );
												bPay.setErrorActive(true);
												bPay.setPaymentStatus(AppConstants.STATUS_ERROR_DATA_PAY);
											}
											if(inv.getPreviousBalanceAmount() == null ) {
												bPay.setPreviousBalanceAmount(String.valueOf(inv.getInvoiceTotal()));
												bPay.setRemainingBalanceAmount(df.format(Double.parseDouble(inv.getRemainingBalanceAmount()) - Double.parseDouble(bPay.getPaymentAmount())));
												inv.setPreviousBalanceAmount(bPay.getRemainingBalanceAmount());
												inv.setRemainingBalanceAmount("0");
											}else {
												bPay.setPreviousBalanceAmount(inv.getPreviousBalanceAmount());
												bPay.setRemainingBalanceAmount(df.format(Double.parseDouble(inv.getPreviousBalanceAmount()) - Double.parseDouble(bPay.getPaymentAmount())));
												inv.setPreviousBalanceAmount(bPay.getRemainingBalanceAmount());
												inv.setRemainingBalanceAmount("0");
											}
											
											String bank = bPay.getBeneficiaryAccount();
											bank = bank.substring(bank.length() -4);
											List<Udc> bList = udcService.searchBySystem(AppConstants.UDC_SYSTEM_ACCBANK);
											for(Udc bl: bList) {
												if(bl.getStrValue1().equals(r.getColumn11())) {
													if(bl.getUdcKey().contains(bank)) {
														bPay.setBeneficiaryAccount(bl.getUdcKey());
														break;
													}
												}
											}
										}
										
										if(bPay != null) {
											List<Payments> nPay= new ArrayList<Payments>();
											nPay.add(bPay);
											Set<Payments> realPay = new HashSet<Payments>(nPay);
											pList.setPayments(realPay);
											paymentsListService.savePaymentsList(pList);
											inv.setPayments(realPay);											
										}
									}else {//se agregan datos al registro
										
										if(r.getColumn7() != null) {//Cambio de moneda
											eRate = r.getColumn7();
										}
										
										//Payments
										Payments bPay = new Payments();	
										List<Payments> payments = paymentsService.getPaymentsList(inv.getUUID());
										if(!payments.isEmpty()) {//Ya hay pago previos
											Payments getPay = paymentsService.getPayByUuidRNumber(r.getColumn23(), inv.getUUID());//Receipt Number
											if(getPay == null) {
												int con = payments.size() + 1;	
												
												bPay.setPaymentType(AppConstants.PAYMENTS_CPAGO);				
												bPay.setSerial(p.getSerial());
												bPay.setFolio(p.getFolio());
												bPay.setSerialRel(inv.getSerial());
												if(inv.getFolio().contains("-")) {
													bPay.setFolioRel(inv.getFolio().substring(0,inv.getFolio().indexOf("-")));
												}else {
													bPay.setFolioRel(inv.getFolio());
												}	
												bPay.setCreationDate(dateT);
												bPay.setPostalCode(inv.getCustomerZip());
												bPay.setRelationType(null);
												bPay.setBranch(inv.getBranch());
												bPay.setCompany(companyService.getCompanyByName(r.getColumn8()));
												bPay.setCountry(country);
												bPay.setCustomerName(r.getColumn0());
												bPay.setPartyNumber(inv.getCustomerPartyNumber());
												bPay.setCustomerEmail(inv.getCustomerEmail());
												bPay.setCurrency(inv.getInvoiceCurrency());
												bPay.setExchangeRate(r.getColumn7());
												bPay.setPaymentAmount(r.getColumn29());
												bPay.setBankReference("");//Cliente
												bPay.setAcountBankTaxIdentifier("");//Cliente
												bPay.setPayerAccount("");//Cliente
												bPay.setBeneficiaryAccount(r.getColumn10());
												bPay.setBenBankAccTaxIden("");	
												bPay.setReceiptId(r.getColumn22());
												bPay.setReceiptNumber(r.getColumn23());
												bPay.setPaymentNumber(String.valueOf(con));	
												bPay.setTaxIdentifier(r.getColumn1().replaceAll(" ", ""));//Utilizados para nacional o extranjero
												bPay.setPostalCode(r.getColumn4());
												bPay.setTransactionReference(r.getColumn12());	
												bPay.setPaymentMethod("PPD");
												bPay.setAdvanceApplied(false);
												bPay.setPaymentStatus(AppConstants.STATUS_UPDUUID);
												bPay.setErrorActive(false);
												if(r.getColumn37() != null && !r.getColumn37().isEmpty()) {
													bPay.setPaymentForm(r.getColumn37());
												}else {
													bPay.setPaymentError("SE HIZO UN PAGO PERO FORMA DE PAGO" );
													bPay.setErrorActive(true);
													bPay.setPaymentStatus(AppConstants.STATUS_ERROR_DATA_PAY);
												}										
												if(inv.getUUID()!=null && !inv.getUUID().isEmpty()) {
													bPay.setPaymentError(null);
													bPay.setUuidReference(inv.getUUID());
												}else {
													bPay.setPaymentError("SE HIZO UN PAGO PERO NO TIENE FOLIO FISCAL RELACIONADO: " );
													bPay.setErrorActive(true);	
													bPay.setPaymentStatus(AppConstants.STATUS_ERROR_DATA_PAY);
												}
												if(inv.getPreviousBalanceAmount() == null ) {
													bPay.setPreviousBalanceAmount(String.valueOf(inv.getInvoiceTotal()));
													bPay.setRemainingBalanceAmount(df.format(Double.parseDouble(inv.getRemainingBalanceAmount()) - Double.parseDouble(bPay.getPaymentAmount())));
													inv.setPreviousBalanceAmount(bPay.getRemainingBalanceAmount());
													inv.setRemainingBalanceAmount("0");
												}else {
													bPay.setPreviousBalanceAmount(inv.getPreviousBalanceAmount());
													bPay.setRemainingBalanceAmount(df.format(Double.parseDouble(inv.getPreviousBalanceAmount()) - Double.parseDouble(bPay.getPaymentAmount())));
													inv.setPreviousBalanceAmount(bPay.getRemainingBalanceAmount());
													inv.setRemainingBalanceAmount("0");
												}
												
												String bank = bPay.getBeneficiaryAccount();
												bank = bank.substring(bank.length() -4);
												List<Udc> bList = udcService.searchBySystem(AppConstants.UDC_SYSTEM_ACCBANK);
												for(Udc bl: bList) {
													if(bl.getStrValue1().equals(r.getColumn11())) {
														if(bl.getUdcKey().contains(bank)) {
															bPay.setBeneficiaryAccount(bl.getUdcKey());
															break;
														}
													}
												}
											}							
										}else {//No hay pagos previos
											int con = 1;
											
											bPay.setPaymentType(AppConstants.PAYMENTS_CPAGO);
											bPay.setSerial(p.getSerial());
											bPay.setFolio(p.getFolio());
											bPay.setSerialRel(inv.getSerial());
											if(inv.getFolio().contains("-")) {
												bPay.setFolioRel(inv.getFolio().substring(0,inv.getFolio().indexOf("-")));
											}else {
												bPay.setFolioRel(inv.getFolio());
											}	
											bPay.setCreationDate(dateT);
											bPay.setPostalCode(inv.getCustomerZip());
											bPay.setRelationType(null);
											bPay.setBranch(inv.getBranch());
											bPay.setCompany(companyService.getCompanyByName(r.getColumn8()));
											bPay.setCountry(country);
											bPay.setCustomerName(r.getColumn0());
											bPay.setPartyNumber(inv.getCustomerPartyNumber());
											bPay.setCustomerEmail(inv.getCustomerEmail());
											bPay.setCurrency(inv.getInvoiceCurrency());
											bPay.setExchangeRate(r.getColumn7());
											bPay.setPaymentAmount(r.getColumn29());
											bPay.setBankReference("");//Cliente
											bPay.setAcountBankTaxIdentifier("");//Cliente
											bPay.setPayerAccount("");//Cliente
											bPay.setBeneficiaryAccount(r.getColumn10());
											bPay.setBenBankAccTaxIden("");	
											bPay.setReceiptId(r.getColumn22());
											bPay.setReceiptNumber(r.getColumn23());
											bPay.setPaymentNumber(String.valueOf(con));	
											bPay.setTaxIdentifier(r.getColumn1().replaceAll(" ", ""));//Utilizados para nacional o extranjero
											bPay.setPostalCode(r.getColumn4());
											bPay.setTransactionReference(r.getColumn12());	
											bPay.setPaymentMethod("PPD");
											bPay.setAdvanceApplied(false);
											bPay.setErrorActive(false);
											bPay.setPaymentStatus(AppConstants.STATUS_UPDUUID);
											if(r.getColumn37() != null && !r.getColumn37().isEmpty()) {
												bPay.setPaymentForm(r.getColumn37());
											}else {
												bPay.setPaymentError("SE HIZO UN PAGO PERO FORMA DE PAGO" );
												bPay.setErrorActive(true);
												bPay.setPaymentStatus(AppConstants.STATUS_ERROR_DATA_PAY);
											}									
											if(inv.getUUID()!=null && !inv.getUUID().isEmpty()) {
												bPay.setPaymentError(null);
												bPay.setUuidReference(inv.getUUID());
											}else {
												bPay.setPaymentError("SE HIZO UN PAGO PERO NO TIENE FOLIO FISCAL RELACIONADO: " );
												bPay.setErrorActive(true);
												bPay.setPaymentStatus(AppConstants.STATUS_ERROR_DATA_PAY);
											}
											if(inv.getPreviousBalanceAmount() == null ) {
												bPay.setPreviousBalanceAmount(String.valueOf(inv.getInvoiceTotal()));
												bPay.setRemainingBalanceAmount(df.format(Double.parseDouble(inv.getRemainingBalanceAmount()) - Double.parseDouble(bPay.getPaymentAmount())));
												inv.setPreviousBalanceAmount(bPay.getRemainingBalanceAmount());
												inv.setRemainingBalanceAmount("0");
											}else {
												bPay.setPreviousBalanceAmount(inv.getPreviousBalanceAmount());
												bPay.setRemainingBalanceAmount(df.format(Double.parseDouble(inv.getPreviousBalanceAmount()) - Double.parseDouble(bPay.getPaymentAmount())));
												inv.setPreviousBalanceAmount(bPay.getRemainingBalanceAmount());
												inv.setRemainingBalanceAmount("0");
											}
											
											String bank = bPay.getBeneficiaryAccount();
											bank = bank.substring(bank.length() -4);
											List<Udc> bList = udcService.searchBySystem(AppConstants.UDC_SYSTEM_ACCBANK);
											for(Udc bl: bList) {
												if(bl.getStrValue1().equals(r.getColumn11())) {
													if(bl.getUdcKey().contains(bank)) {
														bPay.setBeneficiaryAccount(bl.getUdcKey());
														break;
													}
												}
											}
										}							
										
										if(bPay.getReceiptNumber() != null) {
											List<Payments> nnPay= new ArrayList<Payments>(p.getPayments());
											nnPay.add(bPay);
											Set<Payments> realPayL = new HashSet<Payments>(nnPay);
											p.setPayments(realPayL);
											paymentsListService.updatePaymentsList(p);
											
											List<Payments> nPay= new ArrayList<Payments>();
											nPay.add(bPay);
											Set<Payments> realPay = new HashSet<Payments>(nPay);								
											inv.setPayments(realPay);
										}							
									}
								}
								return inv;		
							}				
						}
					}
				}
			}else if(r.getColumn17().equals(AppConstants.IS_ADVANCE_PAYMENT)){//Saber si es anticipo
				if(r.getColumn17().equals(AppConstants.IS_ADVANCE_PAYMENT)) {
					Payments pSearch = paymentsService.getPayment(NullValidator.isNull(r.getColumn23())); 
					if(pSearch == null || (pSearch.getPaymentError() != null && !pSearch.getPaymentError().isEmpty())) {
						if(r.getColumn7() != null) {//Cambio de moneda
							eRate = r.getColumn7();
						}
						Udc udcI = udcService.searchBySystemAndKey(AppConstants.PAYMENTS_ADVPAY, AppConstants.INVOICE_SAT_TYPE_I);
						//Montos de pagos
						String subTotal = "0";
						String tax = "0";
						
						double st = Double.parseDouble(r.getColumn31())/(AppConstants.INVOICE_TAX_CODE_116);
						subTotal = df.format(st);
						tax = df.format(Double.parseDouble(r.getColumn31()) - Double.parseDouble(subTotal));
						
						if(NullValidator.isNull(r.getColumn38()).equals("SUCURSAL MEXICO")) {
							r.setColumn38("CEDIS");
						}
						Branch newBra = branchService.getBranchByName(NullValidator.isNull(r.getColumn38()));
						NextNumber nN = new NextNumber();
						nN = nextNumberService.getNumberCon(AppConstants.ORDER_TYPE_ADV, newBra);
						if(nN != null) {
							invoice.setCustomerName(NullValidator.isNull(r.getColumn0()));
							invoice.setCustomerTaxIdentifier(NullValidator.isNull(r.getColumn1()).replaceAll(" ", ""));
							invoice.setCustomerCity(NullValidator.isNull(r.getColumn2()));
							invoice.setCustomerCountry(country);
							invoice.setCustomerZip(NullValidator.isNull(r.getColumn4()));
							invoice.setCustomerState(NullValidator.isNull(r.getColumn5()));
							invoice.setCustomerAddress1(NullValidator.isNull(r.getColumn6()));
							invoice.setInvoiceExchangeRate(Double.parseDouble(eRate));
							invoice.setCompany(companyService.getCompanyByName(r.getColumn8()));
							invoice.setInvoiceCurrency(NullValidator.isNull(r.getColumn16()));
							invoice.setPaymentMethod(AppConstants.PAY_METHOD);
							invoice.setInvoiceType(AppConstants.ORDER_TYPE_ADV);
							invoice.setCreationDate(formatterUTC.parse(dateT));
							invoice.setFromSalesOrder(NullValidator.isNull(r.getColumn23()));
							invoice.setFolio(String.valueOf(nN.getFolio()));
							invoice.setSerial(nN.getSerie());
							invoice.setErrorActive(false);
							if(r.getColumn37() != null && !r.getColumn37().isEmpty()) {
								invoice.setPaymentType(r.getColumn37());
							}else {
								invoice.setErrorMsg("SE HIZO UN PAGO PERO FORMA DE PAGO" );
								invoice.setErrorActive(true);
								invoice.setStatus(AppConstants.STATUS_ERROR_DATA_PAY);
							}
							invoice.setPaymentTerms("");
							invoice.setInvoiceTotal(Double.parseDouble(subTotal) + Double.parseDouble(tax));
							invoice.setInvoiceSubTotal(Double.parseDouble(subTotal));
							invoice.setInvoiceTaxAmount(Double.parseDouble(tax));
							invoice.setCustomerEmail(NullValidator.isNull(r.getColumn35()));//-------------se tomará del reporte
							invoice.setRemainingBalanceAmount(String.valueOf(Double.parseDouble(subTotal) + Double.parseDouble(tax)));
							invoice.setCFDIUse(udcI.getNote());
							invoice.setProductType("ANTICIPO");
							invoice.setBranch(newBra);
							invoice.setPreviousBalanceAmount("0");
							invoice.setCreatedBy(AppConstants.USER_DEFAULT);
							invoice.setFromSalesOrder(null);
							invoice.setStatus(AppConstants.STATUS_PENDING);//------------------------
							invoice.setInvoiceDiscount(0);
							invoice.setInvoice(true);
							invoice.setCustomerPartyNumber(NullValidator.isNull(r.getColumn36()));
							invoice.setOrderSource(AppConstants.ORDER_TYPE_ADV);
							invoice.setOrderType(AppConstants.ORDER_TYPE_ADV);
							invoice.setSetName("");
							invoice.setAdvanceAplied(false);
							InvoiceDetails iD = new InvoiceDetails();
							//Consulta de la dirreción del cliente de correo electrónico
							if(invoice.getCustomerEmail() == null || "".contains(NullValidator.isNull(invoice.getCustomerEmail()))) {
								CustomerInformationDTO ciDTO = soapService.getEmaiAdress(invoice.getCustomerName(), invoice.getCustomerPartyNumber());
								if(ciDTO != null) {
									if(ciDTO.getEmailAdress() != null) {
										for(EmailAdressDTO eA: ciDTO.getEmailAdress()) {
											if(!eA.getPartyName().equals("COSME MONGE")) {
												invoice.setCustomerEmail(eA.getObjectEmailAddress());
												break;
											}
										}
									}									
								}
							}
							
							List<TaxCodes> tcs = new ArrayList<TaxCodes>();
							tcs = taxCodesService.getTCList(0, 10);
							for(TaxCodes tc: tcs) {
								if(tc.getTaxValue() == AppConstants.INVOICE_TAX_CODE_016) {
									List<TaxCodes> taxCodes = new ArrayList<TaxCodes>();
									taxCodes.add(tc);
									Set<TaxCodes> tCodes = new HashSet<TaxCodes>(taxCodes);
									iD.setTaxCodes(tCodes);
									break;
								}
							}
							iD.setItemNumber("");
							iD.setItemDescription(udcI.getStrValue1());
							iD.setUnitProdServ(String.valueOf(udcI.getIntValue()));
							iD.setUnitPrice(Double.parseDouble(subTotal));
							iD.setTotalTaxAmount(Double.parseDouble(tax));
							iD.setTotalAmount(Double.parseDouble(subTotal));
							iD.setTotalDiscount(0);
							iD.setUomName("");
							iD.setUomCode(udcI.getStrValue2());
							iD.setCurrency(NullValidator.isNull(r.getColumn16()));
							iD.setExchangeRate(Double.parseDouble(eRate));
							iD.setImport(false);
							iD.setLineType(AppConstants.REPORT_LINE_TYPE_NOR);
							iD.setQuantity(AppConstants.INVOICE_ADVPAY_DEFAULT_QUANTITY);
							iD.setTransactionLineNumber(AppConstants.INVOICE_ADVPAY_DEFAULT_TRANSLINNUMBER);
							iD.setRetailComplements(null);
							iD.setIsVehicleControl("0");
							iD.setEquipmentReference("N");
							//COLOCAR QUE LA LÍNEA SE DEBE DE TIMBRAR
							iD.setIsInvoiceLine("D");
							
							List<InvoiceDetails> idList = new ArrayList<InvoiceDetails>();
							idList.add(iD);
							Set<InvoiceDetails> sId = new HashSet<InvoiceDetails>(idList);
							invoice.setInvoiceDetails(sId);
							
							pay.setPaymentType(AppConstants.PAYMENTS_ADVPAY);
							pay.setCustomerName(NullValidator.isNull(r.getColumn0()));
							pay.setTaxIdentifier(NullValidator.isNull(r.getColumn1()));
							pay.setCountry(country);
							pay.setPostalCode(NullValidator.isNull(r.getColumn4()));
							pay.setExchangeRate(eRate);
							pay.setCompany(companyService.getCompanyByName(r.getColumn8()));
							pay.setBeneficiaryAccount(r.getColumn10());
							pay.setCurrency(NullValidator.isNull(r.getColumn16()));
							pay.setPaymentMethod(AppConstants.PAY_METHOD);
							pay.setCreationDate(dateT);
							pay.setReceiptId(NullValidator.isNull(r.getColumn22()));
							pay.setReceiptNumber(NullValidator.isNull(r.getColumn23()));
							pay.setPaymentAmount(r.getColumn31());
							pay.setRemainingBalanceAmount(r.getColumn31());
							pay.setPaymentStatus("");
							pay.setCustomerEmail(invoice.getCustomerEmail());
							pay.setPartyNumber(invoice.getCustomerPartyNumber());
							
							pay.setFolio(String.valueOf(nN.getFolio()));
							pay.setSerial(nN.getSerie());
							pay.setRelationType(null);
							pay.setBranch(invoice.getBranch());							
							pay.setPartyNumber(invoice.getCustomerPartyNumber());
							pay.setCustomerEmail(invoice.getCustomerEmail());
							pay.setAdvanceApplied(false);													
							String bank = pay.getBeneficiaryAccount();
							bank = bank.substring(bank.length() -4);
							List<Udc> bList = udcService.searchBySystem(AppConstants.UDC_SYSTEM_ACCBANK);
							for(Udc bl: bList) {
								if(bl.getStrValue1().equals(r.getColumn11())) {
									if(bl.getUdcKey().contains(bank)) {
										pay.setBeneficiaryAccount(bl.getUdcKey());
										break;
									}
								}
							}
							
							List<Payments> nPay= new ArrayList<Payments>();
							nPay.add(pay);
							Set<Payments> realPay = new HashSet<Payments>(nPay);
							invoice.setPayments(realPay);
							if(!invoiceDao.saveInvoice(invoice)) {
								return null;
							}
							return invoice;
						}else {
							return null;
						}
					}
				}		
			}else if(r.getColumn39() != null) {//Carga inicial
				if(!r.getColumn39().isEmpty()) {
					if(r.getColumn17().equals("N")){
						Payments pSearch = paymentsService.getPayment(NullValidator.isNull(r.getColumn23())); 
						if(pSearch == null) {
							if(!r.getColumn16().equals(AppConstants.DEFAUL_CURRENCY)) {
								if(r.getColumn7() != null) {//Cambio de moneda
									eRate = r.getColumn7();
								}
							}
							
							Udc udcI = udcService.searchBySystemAndKey(AppConstants.PAYMENTS_ADVPAY, AppConstants.INVOICE_SAT_TYPE_I);
							//Montos de pagos
							String subTotal = "0";
							String tax = "0";
							

							double st = Double.parseDouble(r.getColumn31())/(AppConstants.INVOICE_TAX_CODE_116);
							subTotal = df.format(st);
							tax = df.format(Double.parseDouble(r.getColumn31()) - Double.parseDouble(subTotal));
							if(NullValidator.isNull(r.getColumn38()).equals("SUCURSAL MEXICO")) {
								r.setColumn38("CEDIS");
							}
							Branch branch = branchService.getBranchByName(r.getColumn38());
							NextNumber nN = nextNumberService.getNumber(AppConstants.ORDER_TYPE_ADV, branch);
							
							invoice.setUUID(r.getColumn39());
							invoice.setCustomerName(NullValidator.isNull(r.getColumn0()));
							invoice.setCustomerTaxIdentifier(NullValidator.isNull(r.getColumn1()).replaceAll(" ", ""));
							invoice.setCustomerCity(NullValidator.isNull(r.getColumn2()));
							invoice.setCustomerCountry(country);
							invoice.setCustomerZip(NullValidator.isNull(r.getColumn4()));
							invoice.setCustomerState(NullValidator.isNull(r.getColumn5()));
							invoice.setCustomerAddress1(NullValidator.isNull(r.getColumn6()));
							invoice.setInvoiceExchangeRate(Double.parseDouble(eRate));
							invoice.setCompany(companyService.getCompanyByName(r.getColumn8()));
							invoice.setInvoiceCurrency(NullValidator.isNull(r.getColumn16()));
							invoice.setPaymentMethod(AppConstants.PAY_METHOD);
							invoice.setInvoiceType(AppConstants.ORDER_TYPE_ADV);
							invoice.setCreationDate(formatterUTC.parse(dateT));
							invoice.setFromSalesOrder(NullValidator.isNull(r.getColumn23()));
							invoice.setFolio("0");
							invoice.setSerial("CARGA INICIAL");
							invoice.setPaymentType(NullValidator.isNull(r.getColumn37()));
							invoice.setPaymentTerms("");
							invoice.setInvoiceTotal(Double.parseDouble(subTotal) + Double.parseDouble(tax));
							invoice.setInvoiceSubTotal(Double.parseDouble(subTotal));
							invoice.setInvoiceTaxAmount(Double.parseDouble(tax));
							invoice.setCustomerEmail(NullValidator.isNull(r.getColumn35()));//-------------se tomará del reporte
							invoice.setCFDIUse(udcI.getNote());
							invoice.setProductType("ANTICIPO");
							invoice.setBranch(branch);
							invoice.setRemainingBalanceAmount("0");
							invoice.setPreviousBalanceAmount("0");
							invoice.setCreatedBy(AppConstants.USER_DEFAULT);
							invoice.setFromSalesOrder(null);
							invoice.setStatus(AppConstants.STATUS_FINISHED);//------------------------
							invoice.setInvoiceDiscount(0);
							invoice.setInvoice(true);
							invoice.setCustomerPartyNumber(NullValidator.isNull(r.getColumn36()));
							invoice.setOrderSource(AppConstants.ORDER_TYPE_ADV);
							invoice.setOrderType(AppConstants.ORDER_TYPE_ADV);
							invoice.setSetName("");
							invoice.setAdvanceAplied(false);
							InvoiceDetails iD = new InvoiceDetails();
							List<TaxCodes> tcs = new ArrayList<TaxCodes>();
							tcs = taxCodesService.getTCList(0, 10);

							for(TaxCodes tc: tcs) {
								if(tc.getTaxValue() == AppConstants.INVOICE_TAX_CODE_016) {
									List<TaxCodes> taxCodes = new ArrayList<TaxCodes>();
									taxCodes.add(tc);
									Set<TaxCodes> tCodes = new HashSet<TaxCodes>(taxCodes);
									iD.setTaxCodes(tCodes);
									break;
								}
							}
							
							iD.setItemNumber("");
							iD.setItemDescription(udcI.getStrValue1());
							iD.setUnitProdServ(String.valueOf(udcI.getIntValue()));
							iD.setUnitPrice(Double.parseDouble(subTotal));
							iD.setTotalTaxAmount(Double.parseDouble(tax));
							iD.setTotalAmount(Double.parseDouble(subTotal));
							iD.setTotalDiscount(0);
							iD.setUomName(AppConstants.INVOICE_ADVPAY_DEFAULT_UOM);
							iD.setUomCode(udcI.getStrValue2());
							iD.setCurrency(NullValidator.isNull(r.getColumn16()));
							iD.setExchangeRate(Double.parseDouble(eRate));
							iD.setImport(false);
							iD.setLineType(AppConstants.REPORT_LINE_TYPE_NOR);
							iD.setQuantity(AppConstants.INVOICE_ADVPAY_DEFAULT_QUANTITY);
							iD.setTransactionLineNumber(AppConstants.INVOICE_ADVPAY_DEFAULT_TRANSLINNUMBER);
							iD.setRetailComplements(null);
							iD.setIsVehicleControl("0");
							
							List<InvoiceDetails> idList = new ArrayList<InvoiceDetails>();
							idList.add(iD);
							Set<InvoiceDetails> sId = new HashSet<InvoiceDetails>(idList);
							invoice.setInvoiceDetails(sId);
							
							pay.setUUID(r.getColumn39());
							pay.setPaymentType(AppConstants.PAYMENTS_ADVPAY);
							pay.setCustomerName(NullValidator.isNull(r.getColumn0()));
							pay.setTaxIdentifier(NullValidator.isNull(r.getColumn1()));
							pay.setCountry(country);
							pay.setPostalCode(NullValidator.isNull(r.getColumn4()));
							pay.setExchangeRate(eRate);
							pay.setCompany(companyService.getCompanyByName(r.getColumn8()));
							pay.setBeneficiaryAccount(r.getColumn10());
							pay.setCurrency(NullValidator.isNull(r.getColumn16()));
							pay.setPaymentMethod(AppConstants.PAY_METHOD);
							pay.setCreationDate(dateT);
							pay.setReceiptId(NullValidator.isNull(r.getColumn22()));
							pay.setReceiptNumber(NullValidator.isNull(r.getColumn23()));
							pay.setPaymentAmount(r.getColumn31());
							pay.setPaymentStatus("");
							pay.setCustomerEmail(invoice.getCustomerEmail());
							pay.setPartyNumber(invoice.getCustomerPartyNumber());
							
							pay.setFolio("0");
							pay.setSerial("CARGA INICIAL");
							pay.setRelationType(null);
							pay.setBranch(null);							
							pay.setPartyNumber(invoice.getCustomerPartyNumber());
							pay.setCustomerEmail(invoice.getCustomerEmail());
							pay.setAdvanceApplied(false);
							
							String bank = pay.getBeneficiaryAccount();
							bank = bank.substring(bank.length() -4);
							List<Udc> bList = udcService.searchBySystem(AppConstants.UDC_SYSTEM_ACCBANK);
							for(Udc bl: bList) {
								if(bl.getStrValue1().equals(r.getColumn11())) {
									if(bl.getUdcKey().contains(bank)) {
										pay.setBeneficiaryAccount(bl.getUdcKey());
										break;
									}
								}
							}
							
							List<Payments> nPay= new ArrayList<Payments>();
							nPay.add(pay);
							Set<Payments> realPay = new HashSet<Payments>(nPay);
							invoice.setPayments(realPay);
							if(!invoiceDao.saveInvoice(invoice)) {
								return null;
							}
							return invoice;
						}
					}
					
				}
			}
			return null;
		}catch(Exception e) {
			log.warn("ERROR EN EL PROCESO DE PAGOS", e);
			return null;
		}
	}

	@Override
	public boolean createTransferInvoice(List<Row> r) {
		String timeZone = "";
		try {
			List<Udc> tZone = udcService.searchBySystem(AppConstants.UDC_SYSTEM_TIMEZONE);
			for(Udc u: tZone) {
				if(u.getStrValue1().equals(AppConstants.UDC_STRVALUE1_TIMEZONE)) {
					timeZone = u.getUdcKey();
				}
			}
			sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
			formatterUTC.setTimeZone(TimeZone.getTimeZone(timeZone));
			List<String> arr = new ArrayList<String>();
			List<Invoice> invList = new ArrayList<Invoice>();
			//Llenado de objeto DTO de la respuesta del reporte de pagos	
			for(Row ro: r) {
				Invoice invReports = new Invoice();
				if(ro.getColumn3() != null && !ro.getColumn3().isEmpty()) {
					Invoice isExisting = invoiceDao.getInvoiceByOtFolio(AppConstants.ORDER_TYPE_TRANS, ro.getColumn3(), ro.getColumn13());
					if(isExisting == null) {
						invReports = fullTrasnferDTO(ro);
						if(invReports != null) {		
							invList.add(invReports);
						}					
						
					}
				}
			}
			List<Invoice> pendingList = new ArrayList<Invoice>();
			//Llenar registros del cabecero
			for(Invoice inv: invList) {
				if(!arr.contains(inv.getFolio())) {	
					pendingList.add(inv);
					arr.add(inv.getFolio());
				}
			}
			//Llenado de las lineas
			for(Invoice inv: pendingList) {
				Set<InvoiceDetails> invDList = new HashSet<InvoiceDetails>();
				String productTypes = "";
				for(Invoice invDetail: invList) {
					if(inv.getFromSalesOrder().equals(invDetail.getFromSalesOrder())) {	
						if(invList.size() > 1) {
							if(inv.getProductType() != null) {
								if(productTypes.contains(inv.getProductType())) {
									productTypes = productTypes + "," + invDetail.getProductType();
								}								
							}
						}else {
							productTypes = invDetail.getProductType();
						}
						invDList.addAll(invDetail.getInvoiceDetails());						
					}
				}
				inv.setProductType(productTypes);
				inv.setInvoiceDetails(invDList);
				Invoice isExisting = invoiceDao.getInvoiceByOtFolio(AppConstants.ORDER_TYPE_TRANS, inv.getFromSalesOrder(), inv.getCustomerName());
				if(isExisting == null) {
					NextNumber nNumber = nextNumberService.getNumberCon(AppConstants.ORDER_TYPE_TRANS, inv.getBranch());
					inv.setFolio(String.valueOf(nNumber.getFolio()));
					inv.setSerial(nNumber.getSerie());
					if(!invoiceDao.saveInvoice(inv)) {
						log.error("EXISTIO ALGÚN ERROR EN EL PROCESO DE TRASLADOS CON LA TRANSACCIÓN + " + inv.getFolio());
					}
				}				
			}
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			log.error("ERROR AL CREAR LA FACTURA DE TRASLADOS: " + e);
			return false;
		}
	}
	
	public Invoice fullTrasnferDTO(Row r) {
		Invoice inv = new Invoice();
		InvoiceDetails invD = new InvoiceDetails();
		String timeZone = "";
		String msgError = null;
		String productsType = "";
		try {
			if(r != null) {
				//FECHAS
				List<Udc> tZone = udcService.searchBySystem(AppConstants.UDC_SYSTEM_TIMEZONE);
				for(Udc u: tZone) {
					if(u.getStrValue1().equals(AppConstants.UDC_STRVALUE1_TIMEZONE)) {
						timeZone = u.getUdcKey();
					}
				}				
				sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
				formatterUTC.setTimeZone(TimeZone.getTimeZone(timeZone));
				Date date = sdf.parse(r.getColumn7());
				//CFDI TRASLAODS
				Udc traslados = udcService.searchBySystemAndKey(AppConstants.ORDER_TYPE_TRANS, AppConstantsUtil.VOUCHER_T);
				
				//OBTENCIÓN DE LAS SUCURSALES
				Branch branchCede = branchService.getBranchByName(NullValidator.isNull(r.getColumn10()));
				Branch branchRec = branchService.getBranchByName(NullValidator.isNull(r.getColumn13()));
				if(branchRec == null){
					branchRec = branchService.getBranchByName("CEDIS");
				}
				inv.setExtCom(false);
				inv.setPayments(null);
				inv.setCreatedBy(AppConstants.USER_DEFAULT);
				inv.setCreationDate(date);
				inv.setUpdatedBy(AppConstants.USER_DEFAULT);
				inv.setUpdatedDate(new Date());
				inv.setFolio(r.getColumn3());
				inv.setPaymentTerms(AppConstants.PTERMS_CONTADO);
				inv.setPaymentMethod(AppConstantsUtil.PAYMENT_METHOD);
				inv.setPaymentType("99");
				inv.setInvoiceTotal(0);
				inv.setInvoiceSubTotal(0);
				inv.setInvoiceDiscount(0);
				inv.setInvoiceTaxAmount(0);
				inv.setInvoiceExchangeRate(AppConstants.INVOICE_EXCHANGE_RATE);
				inv.setInvoiceCurrency(AppConstants.DEFAUL_CURRENCY);
				inv.setInvoiceType(AppConstants.ORDER_TYPE_TRANS);
				inv.setBranch(branchCede);
				inv.setCompany(branchCede.getCompany());
				inv.setInvoice(false);				
				inv.setInvoiceRelationType("");
				inv.setCFDIUse(traslados.getStrValue2());
				//Datos de la sucursal de envío
				inv.setCustomerPartyNumber(NullValidator.isNull(branchRec.getInvOrganizationId()));
				inv.setCustomerName(branchRec.getName());
				inv.setCustomerAddress1(branchRec.getAddress());
				inv.setCustomerCity(branchRec.getCity());
				inv.setCustomerCountry(branchRec.getCountry());
				inv.setCustomerState(branchRec.getState());
				inv.setCustomerZip(branchRec.getZip());
				inv.setCustomerTaxIdentifier(branchRec.getCompany().getTaxIdentifier());
				inv.setOrderSource("");
				inv.setStatus(AppConstants.STATUS_PENDING);
				inv.setOrderType(AppConstants.ORDER_TYPE_TRANS);
				inv.setSetName("");
				inv.setFromSalesOrder(NullValidator.isNull(r.getColumn3()));
				inv.setAdvanceAplied(false);
				
				invD.setRetailComplements(null);
				invD.setTaxCodes(null);
				
				invD.setItemDescription(NullValidator.isNull(r.getColumn0()));
				invD.setItemNumber(NullValidator.isNull(r.getColumn1()));
				invD.setItemSerial(NullValidator.isNull(r.getColumn6()));
				if(Double.parseDouble(NullValidator.isNull(r.getColumn8())) < 0) {
					invD.setQuantity((Double.parseDouble(NullValidator.isNull(r.getColumn8())))*(-1));
				}else {
					invD.setQuantity((Double.parseDouble(NullValidator.isNull(r.getColumn8()))));
				}
				
				//Unidad de medida
				if(r.getColumn14() != null && !r.getColumn14().isEmpty()) {
					Udc satUOM = udcService.searchBySystemAndKey(AppConstants.UDC_SYSTEM_UOMSAT, NullValidator.isNull(r.getColumn14()));
					if(satUOM != null && satUOM.getStrValue1() != null && !"".contains(satUOM.getStrValue1())) {
						//UOM del SAT
						invD.setUomCode(satUOM.getStrValue1());
						invD.setUomName(satUOM.getStrValue2().toUpperCase());
						//UOM de Aduana
						invD.setItemUomCustoms(String.valueOf(satUOM.getIntValue()));
					}else {
						msgError = msgError + ";UOMSAT-No existe la Unidad de Medida SAT -" + invD.getUomName() + " en UDC";
						inv.setStatus(AppConstants.STATUS_ERROR_DATA_TRANSFER);
						log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL OBTENER UDC UOMSAT de la linea "+ invD.getUomName() + ":" + inv.getFolio());
					}
				}else {
					msgError = msgError + ";UOMSAT-No existe la Unidad de Medida SAT -" + invD.getUomName() + " en UDC";
					inv.setStatus(AppConstants.STATUS_ERROR_DATA_TRANSFER);
					log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL OBTENER UDC UOMSAT de la linea "+ invD.getUomName() + ":" + inv.getFolio());
					
				}
				
				ItemsDTO itemSat = soapService.getItemDataByItemNumberOrgCode(invD.getItemNumber(), AppConstants.ORACLE_ITEMMASTER);
				if(itemSat != null) {
					//Clave Producto Servicio
					if(itemSat.getItemDFFClavProdServ() != null && !"".contains(itemSat.getItemDFFClavProdServ())) {
						invD.setUnitProdServ(itemSat.getItemDFFClavProdServ());
					}else {
						msgError = msgError + ";PRODSERVSAT-No existe la Clave ProdServ SAT -" + invD.getItemNumber() + " en ItemMaster";
						inv.setStatus(AppConstants.STATUS_ERROR_DATA_TRANSFER);
						log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL OBTENER CLAVPRODSER de la linea "+ invD.getTransactionLineNumber() + ":" + inv.getFolio());
					}
					//Saber si es artículo importado
					invD.setImport(itemSat.isItemDFFIsImported());
					
					productsType = itemSat.getItemCategory().get(0).getCategoryName();
				}

				//Tipo de cambio diario
				CurrencyRates cRates = restService.getDailyCurrency(sdfNoTime.format(new Date()), "USD", "MXN");
				if(cRates != null) {
					float eRate = 0;
					if(cRates.getItems() != null ) {
						if(cRates.getItems().size() > 0) {
							eRate = cRates.getItems().get(0).getConversionRate();
						}else {
							eRate = Float.parseFloat(String.valueOf(inv.getInvoiceExchangeRate()));													
						}
					}
					invD.setExchangeDailyRate(String.valueOf(eRate));
				}
				//referencia de equipo
				if(invD.getItemSerial() != null && !invD.getItemSerial().isEmpty()) {
					invD.setEquipmentReference("E");
				}else {
					invD.setEquipmentReference("R");
				}
				//tipo de producto código
				ItemCategory iCat = restService.getCategoryCode(productsType);
				if(iCat != null) {
					invD.setProductTypeCode(String.valueOf(iCat.getItems().get(0).getDff().get(0).getTipoProducto()));
				}else {
					log.error("ERROR AL TRAER EL CODIGO DEL TIPO DE PRODUCTO PARA CONTROL VEHICULAR");
				}
				//Costo unitario
				String unitCost = NullValidator.isNullUnitCost(r.getColumn15());
				invD.setUnitCost(NullValidator.isNull(unitCost));
				//Precio producto venta sin iva
				String priceList = this.getPriceListByWs(inv, invD);
				invD.setPriceListWTax(NullValidator.isNull(priceList));
				//Saber si va para control vehicular
				invD.setIsVehicleControl("1");
				//COLOCAR QUE LA LÍNEA SE DEBE DE TIMBRAR
				invD.setIsInvoiceLine("D");
				if(msgError == null){
					inv.setErrorMsg(null);
					inv.setErrorActive(false);
				}else {
					inv.setErrorMsg(msgError);
					inv.setErrorActive(true);
				}
				
				Set<InvoiceDetails> invDList = new HashSet<InvoiceDetails>();
				invDList.add(invD);
				inv.setInvoiceDetails(invDList);
				
				return inv;
			}else {
				return null;
			}
		}catch(Exception e) {
			e.printStackTrace();
			log.error("ERROR AL LLENAR EL REGISTRO INVOICE" + e);
			return null;
		}
	}
	
	@Override
	public void sendAllErrors() {
		try {
			Map<String, byte[]> attached= new HashMap<String, byte[]>();
			List<ErrorLog> listError = errorLogService.getAllError(true);
			List<Invoice> invoicesError = invoiceDao.getAllError(true);
			List<Payments> paymentsError = paymentsService.getAllError(true);
			if(listError != null && invoicesError != null && paymentsError != null) {
				String error = null;
				for(ErrorLog elog: listError) {
					error = elog.getOrderNumber() + " " + elog.getErrorMsg() + "\r\n" + error;
					elog.setNew(false);
					errorLogService.updateError(elog);
				}
				for(Invoice i: invoicesError) {
					error = i.getFromSalesOrder() + " " + i.getErrorMsg() + "\r\n" + error;
					i.setErrorActive(false);
					invoiceDao.updateInvoice(i);
				}
				for(Payments p: paymentsError) {
					error = p.getReceiptNumber() + " " + p.getPaymentError() + "\r\n" + error;
					p.setErrorActive(false);
					paymentsService.updatePayment(p);
				}
				if(error != null) {
					byte[] bytes = error.getBytes();
					attached.put("Errores.txt", bytes);
					List<Udc> emails = udcService.searchBySystem("EMAILSERRORS");
					List<String> email = new ArrayList<String>();
					for(Udc u: emails) {
						email.add(u.getUdcKey());
					}
					mailService.sendMail(email,
							"ERRORES PARA EL TIMBRADO AMBIENTE PRODUCTIVO",
							"ERRORES PARA EL TIMBRADO AMBIENTE PRODUCTIVO",
							attached);
				}
			}
		}catch(Exception e) {
			log.error("ERROR AL MANDAR LOS ERRORES", e);
		}
	}

	@Override
	public void recolectListPayments() {
		try {
			//Lista de pagos
			List<PaymentsList> payListList = paymentsListService.getAllPayList(AppConstants.STATUS_PAYMENT_LIST_START);
			List<PaymentsList> pl = new ArrayList<PaymentsList>();
			List<String> arrPayList = new ArrayList<String>();
			if(!payListList.isEmpty()) {
				for(PaymentsList p: payListList) {
					if(!arrPayList.contains(String.valueOf(p.getId()))) {				
						pl.add(p);
						arrPayList.add(String.valueOf(p.getId()));
					}
				}
				for(PaymentsList plist: pl) {
					List<Payments> pListVer = new ArrayList<Payments>(plist.getPayments());
					double total = 0.00;
					double totalList = Double.parseDouble(NullValidator.isNull(plist.getPaymentAmount()));
					for(Payments pa: pListVer) {
						if(plist.getCurrency().equals(pa.getCurrency())) {
							String val = df.format(Double.parseDouble(pa.getPaymentAmount()));
							total = total + Math.floor(Double.parseDouble(val));
						}else if(pa.getCurrency().equals(AppConstants.DEFAUL_CURRENCY)) {//Pago en dlls
							String pago = df.format(Double.parseDouble(pa.getPaymentAmount()) / Double.parseDouble(plist.getExchangeRate()));
							total = total + Double.parseDouble(df.format(Double.parseDouble(pago)));
							pa.setExchangeRate(plist.getExchangeRate());
						}else {//Pago en MXN
							String pagoConversion = df.format(Double.parseDouble(pa.getPaymentAmount()) * (Double.parseDouble(plist.getExchangeRate())));
							float pago = Float.parseFloat(pagoConversion);
							total = total + Math.floor(pago);
							String eRate = dfM.format(1 / (Double.parseDouble(plist.getExchangeRate())+0.05));
							pa.setExchangeRate(eRate);
						}
						paymentsService.updatePayment(pa);
						
					}
					double totalVerification =(totalList/100)*98;
					if(total <= totalList && total >= totalVerification) {
						plist.setStatus(AppConstants.STATUS_PENDING);
						paymentsListService.updatePaymentsList(plist);
					}
//					if(total == totalList) {
//						plist.setStatus(AppConstants.STATUS_PENDING);
//						paymentsListService.updatePaymentsList(plist);
//					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unused")
	public String getUnitCostByWsForSalesOrders(Invoice i, InvoiceDetails iDet, String SalesOrder) {
		log.warn("INCIO DE PROCESO PARA LA OBTENCIÓN DE COSTOS UNITARIOS");
		String unitCost = null;
		String itemId = null;
		AnalyticsDTO analytics = new AnalyticsDTO();
		try {			
			itemId = soapService.getItemId(iDet.getItemNumber());
			if(itemId != null) {
				analytics.setItemId(itemId);
				analytics.setSalesOrder(SalesOrder);
				analytics.setOrgCode(AppConstants.ORACLE_ITEMMASTER);;
				Rowset r = analyticsService.executeAnalyticsWS(AppConstants.ORACLE_USER, AppConstants.ORACLE_PASS, 
						AppConstants.SERVICE_ITEM_COST_FOR_SO, analytics);
				if(!r.getRow().isEmpty()) {
					if(iDet.getItemSerial() != null) {//Cuando se maneja el numero de serie						
						if(iDet.getItemSerial().contains(",")) {
							String [] itemSerials = null;
							itemSerials = iDet.getItemSerial().split(",");
							for(String serial: itemSerials) {
								for(Row row: r.getRow()) {
									if(serial.equals(NullValidator.isNull(row.getColumn5()).replace(" ", ""))) {
										if(unitCost == null) {
											unitCost = row.getColumn3();
											break;
										}else {
											unitCost = unitCost + "," + row.getColumn3();
											break;
										}
									}
								}								
							}
						}else {
							for(Row row: r.getRow()) {
								if(iDet.getItemSerial().equals(NullValidator.isNull(row.getColumn5()).replace(" ", ""))) {
									unitCost = row.getColumn3();
									break;
								}							
							}
						}					
					}else {
						for(Row row: r.getRow()) {//cuando no tiene numero de serie
							unitCost = row.getColumn3();
						}
						if(unitCost == null) {
							ItemCosts iCosts = restService.getItemCostWitoutSerialNumber(iDet.getItemNumber());
							if(iCosts != null) {
								if(iCosts.getItems() != null) {
									for(CostDetails cDet: iCosts.getItems().get(0).getCostDetails()) {
										if(cDet.getCostElement().equals("Material Directo")) {
											unitCost = df.format(cDet.getUnitCostAverage());
											break;
										}
									}
								}
								return unitCost;
							}
						}
					}

				}
			}	
			if(NullValidator.isNull(iDet.getItemSerial()).contains(",")) {
				String [] costos = unitCost.split(",");
				String [] itemSerials = iDet.getItemSerial().split(",");
				if(itemSerials.length != costos.length) {
					if(unitCost == null) {
						for(String iing: itemSerials) {
							if(unitCost == null) {
								unitCost = "0";
								break;
							}else {
								unitCost = unitCost + ",0";
								break;
							}
						}
					}else {
						int unit = itemSerials.length - costos.length;
						for(int var=0; var<unit; var++) {
							unitCost = unitCost + ",0";
						}
					}
				}
			}else if(iDet.getItemSerial() != null){
				unitCost = "0";				
			}else {
				if(unitCost == null) {
					ItemCosts iCosts = restService.getItemCostWitoutSerialNumber(iDet.getItemNumber());
					if(iCosts != null) {
						if(iCosts.getItems() != null) {
							if(iCosts.getItems().size() > 0) {
								for(CostDetails cDet: iCosts.getItems().get(0).getCostDetails()) {
									if(cDet.getCostElement().equals("Costos Integrales")) {
										unitCost = df.format(cDet.getUnitCostAverage());
										break;
									}
								}
							}							
						}
						return unitCost;
					}
				}
			}
			return unitCost;
			
		}catch(Exception e) {
			e.printStackTrace();
			log.warn("INCIO DE PROCESO PARA LA OBTENCIÓN DE COSTOS UNITARIOS", e);
		}
		return unitCost;
	}
	
	public String getPriceListByWs(Invoice i, InvoiceDetails iDet) {
		String priceList = null;
		String eRate = null;
		try {
			eRate = iDet.getExchangeDailyRate();
			PriceLists price = restService.getPriceList(i);
			for(Item p: price.getItems()) {
				iDet.getExchangeDailyRate();
				PriceListByItem pcItem = restService.getPrice(iDet.getItemNumber(), p.getPriceListId());
				if(pcItem != null && pcItem.getItems().size() > 0){
					if(p.getCurrencyCode().equals(AppConstants.DEFAUL_CURRENCY)) {
						priceList = pcItem.getItems().get(0).getCharges().get(0).getBasePrice();
						break;
					}else {
						String priceUsd = pcItem.getItems().get(0).getCharges().get(0).getBasePrice();
						if(eRate != null && !eRate.isEmpty()) {
							priceList = df.format(Double.parseDouble(eRate) * Double.parseDouble(priceUsd));
							break;
						}else {
							priceList = df.format(Double.parseDouble("1.00") * Double.parseDouble(priceUsd));
							break;
						}						
					}
				}
			}
			if(priceList != null) {
				return priceList;
			}else {
				return "0";
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return priceList;
	}
	
	public boolean createAdvPayNC(Invoice invoice, InvoicesByReportsDTO inv) {
		Invoice newInv = new Invoice();
		try {
			Invoice i = invoiceDao.getInvoiceByUuid(invoice.getUUIDReference());
			NextNumber nNumber = nextNumberService.getNumberCon(AppConstants.ORDER_TYPE_NC, invoice.getBranch());
			Udc creditNote = udcService.searchBySystemAndKey(AppConstants.PAYMENTS_ADVPAY,
					AppConstants.INVOICE_SAT_TYPE_E);
			if (creditNote != null) {
				newInv.setCustomerName(invoice.getCustomerName());
				newInv.setCustomerPartyNumber(invoice.getCustomerPartyNumber());
				newInv.setCustomerTaxIdentifier(invoice.getCustomerTaxIdentifier());
				newInv.setCustomerAddress1(invoice.getCustomerAddress1());
				newInv.setCustomerCity(invoice.getCustomerCity());
				newInv.setCustomerCountry(invoice.getCustomerCountry());
				newInv.setCustomerEmail(invoice.getCustomerEmail());
				newInv.setCustomerState(invoice.getCustomerState());
				newInv.setCustomerZip(invoice.getCustomerZip());
				
				newInv.setShipToName(NullValidator.isNull(invoice.getShipToName()));
				newInv.setShipToaddress(NullValidator.isNull(invoice.getShipToaddress()));
				newInv.setShipToCity(NullValidator.isNull(invoice.getShipToCity()));
				newInv.setShipToCountry(NullValidator.isNull(invoice.getShipToCountry()));
				newInv.setShipToState(NullValidator.isNull(invoice.getShipToState()));
				newInv.setShipToZip(NullValidator.isNull(invoice.getShipToZip()));
				
				newInv.setCFDIUse("G02");
				newInv.setBranch(invoice.getBranch());
				newInv.setCompany(invoice.getCompany());
				newInv.setCreatedBy(invoice.getCreatedBy());
				newInv.setCreationDate(dateFormat.parse(dateFormat.format(new Date())));
				newInv.setUpdatedBy(invoice.getUpdatedBy());
				newInv.setUpdatedDate(newInv.getCreationDate());
				newInv.setInvoiceRelationType("07");
				newInv.setUUIDReference(invoice.getUUID());
				newInv.setPayments(null);
				
				newInv.setInvoiceTotal(i.getInvoiceTotal());
				newInv.setInvoiceSubTotal(i.getInvoiceSubTotal());
				newInv.setInvoiceTaxAmount(i.getInvoiceTaxAmount());
				newInv.setInvoiceDiscount(0);
				
				newInv.setSetName(invoice.getSetName());
				newInv.setPaymentTerms("");
				newInv.setFolio(inv.getTransactionNumber());
				newInv.setSerial(nNumber.getSerie());
				newInv.setStatus(AppConstants.STATUS_PENDING);
				newInv.setInvoice(false);
				newInv.setInvoiceType(AppConstants.ORDER_TYPE_NC);
				newInv.setFromSalesOrder(null);
				newInv.setInvoiceCurrency(invoice.getInvoiceCurrency());
				newInv.setInvoiceExchangeRate(invoice.getInvoiceExchangeRate());
				newInv.setOrderSource(NullValidator.isNull(inv.getTransactionSource()));
				newInv.setOrderType(AppConstants.ORDER_TYPE_NC);
				newInv.setProductType("");
				newInv.setExtCom(false);
				newInv.setPaymentMethod(AppConstants.PAY_METHOD);
				newInv.setPaymentType(creditNote.getDescription());
				
				for(InvoiceDetails idetails: i.getInvoiceDetails()) {
					InvoiceDetails iD = new InvoiceDetails();
					iD.setItemNumber("");
					iD.setItemDescription(creditNote.getStrValue1());
					iD.setUnitProdServ(String.valueOf(creditNote.getIntValue()));
					iD.setUnitPrice(idetails.getUnitPrice());
					iD.setTotalTaxAmount(idetails.getTotalTaxAmount());
					iD.setTotalAmount(idetails.getTotalAmount());
					iD.setTotalDiscount(0);
					iD.setUomName(AppConstants.INVOICE_ADVPAY_DEFAULT_UOM);
					iD.setUomCode(creditNote.getStrValue2());
					iD.setCurrency(idetails.getCurrency());
					iD.setExchangeRate(idetails.getExchangeRate());
					iD.setImport(false);
					iD.setLineType(AppConstants.REPORT_LINE_TYPE_NOR);
					iD.setQuantity(AppConstants.INVOICE_ADVPAY_DEFAULT_QUANTITY);
					iD.setTransactionLineNumber(AppConstants.INVOICE_ADVPAY_DEFAULT_TRANSLINNUMBER);
					iD.setRetailComplements(null);
					iD.setIsInvoiceLine("D");
					iD.setTaxCodes(idetails.getTaxCodes());
					iD.setIsVehicleControl("0");
					
					List<InvoiceDetails> idList = new ArrayList<InvoiceDetails>();
					idList.add(iD);
					Set<InvoiceDetails> sId = new HashSet<InvoiceDetails>(idList);
					newInv.setInvoiceDetails(sId);
				}
				
				if(!invoiceDao.saveInvoice(newInv)){
					log.error("ERROR AL CREAR LA NOTA DE CREDITO RELACIONADA CON LA ORDEN: " + invoice.getFromSalesOrder()
					+ " Y EL UUID CORRESPONDIENTE: " + invoice.getUUID());
				}
				
			} else {
				log.error("ERROR AL CREAR LA NOTA DE CREDITO RELACIONADA CON LA ORDEN: " + invoice.getFromSalesOrder()
						+ " Y EL UUID CORRESPONDIENTE: " + invoice.getUUID());
			}
			return true;
		}catch(Exception e) {
			log.error("ERROR AL CREAR LA NOTA DE CREDITO RELACIONADA CON LA ORDEN: " + invoice.getFromSalesOrder() + " Y EL UUID CORRESPONDIENTE: " + invoice.getUUID());
			return false;
		}
	}
	
	public boolean fixedAssetInvoice(InvoicesByReportsDTO inv) {
		Invoice newInv = new Invoice();
		String country = null;
		String pTerms = null;
		try {
			if(inv.getPaymentTerms().equals(AppConstants.PTERMS_CONTADO)) {
				pTerms = AppConstants.PTERMS_CONTADO;
			}else {
				List<Udc> payTerms = udcService.searchBySystem(AppConstants.UDC_SYSTEM_PAYTERMS);
				for(Udc u: payTerms) {
					if(u.getStrValue1().equals(inv.getPaymentTerms()) || u.getUdcKey().equals(inv.getPaymentTerms())) {
						pTerms = String.valueOf(u.getIntValue());
						break;
					}
				}
			}
			Branch branch = branchService.getBranchByCode("CEDIS");
			NextNumber nNumber = nextNumberService.getNumberCon(AppConstants.ORDER_TYPE_FACTURA, branch);
			if (nNumber != null) {
				List<Udc> udc = udcService.searchBySystem(AppConstants.UDC_SYSTEM_COUNTRY);
				for(Udc u: udc) {
					if(u.getStrValue1().equals(inv.getCustomerCountry())) {
						country = u.getUdcKey();
					}
				}
				
				newInv.setCustomerName(inv.getCustomerName());
				newInv.setCustomerPartyNumber(inv.getCustomerPartyNumber());
				newInv.setCustomerTaxIdentifier(inv.getCustomerTaxIdentifier());
				newInv.setCustomerAddress1(inv.getCustomerAddress1());
				newInv.setCustomerCountry(country);
				newInv.setCustomerEmail(inv.getCustomerEmail());
				newInv.setCustomerState(inv.getCustomerState());
				newInv.setCustomerZip(inv.getCustomerPostalCode());
				
				newInv.setShipToName(NullValidator.isNull(inv.getShipToName()));
				newInv.setShipToaddress(NullValidator.isNull(inv.getShipToAddress()));
				newInv.setShipToCity(NullValidator.isNull(inv.getShipToCity()));
				newInv.setShipToCountry(NullValidator.isNull(country));
				newInv.setShipToState(NullValidator.isNull(inv.getShipToState()));
				newInv.setShipToZip(NullValidator.isNull(inv.getShipToZip()));
				
				newInv.setCFDIUse(inv.getFausoCFDI());
				newInv.setBranch(branch);
				newInv.setCompany(branch.getCompany());
				newInv.setCreatedBy("SYSTEM");
				newInv.setCreationDate(dateFormat.parse(dateFormat.format(new Date())));
				newInv.setUpdatedBy("SYSTEM");
				newInv.setUpdatedDate(newInv.getCreationDate());
				newInv.setInvoiceRelationType(null);
				newInv.setUUIDReference(null);
				newInv.setPayments(null);
				
				double total = Double.parseDouble(inv.getTransactionLineUnitSellingPrice()) + Double.parseDouble(inv.getTaxRecoverableAmount());
				newInv.setInvoiceTotal(Double.parseDouble(df.format(total)));
				newInv.setInvoiceSubTotal(Double.parseDouble(inv.getTransactionLineUnitSellingPrice()));
				newInv.setInvoiceTaxAmount(Double.parseDouble(inv.getTaxRecoverableAmount()));
				newInv.setInvoiceDiscount(0);
				
				newInv.setSetName(inv.getSetName());
				newInv.setPaymentTerms(pTerms);
				newInv.setFolio(inv.getTransactionNumber());
				newInv.setSerial(nNumber.getSerie());
				newInv.setStatus(AppConstants.STATUS_PENDING);
				newInv.setInvoice(true);
				newInv.setInvoiceType(AppConstants.ORDER_TYPE_FACTURA);
				newInv.setFromSalesOrder(null);
				newInv.setInvoiceCurrency(inv.getCurrency());
				if(inv.getExchangeRate() != null && !inv.getExchangeRate().isEmpty()) {
					newInv.setInvoiceExchangeRate(Double.parseDouble(inv.getExchangeRate()));
				}else {
					newInv.setInvoiceExchangeRate(AppConstants.INVOICE_EXCHANGE_RATE);
				}				
				newInv.setOrderSource(NullValidator.isNull(inv.getTransactionSource()));
				newInv.setOrderType(AppConstants.ORDER_TYPE_NC);
				newInv.setProductType("");
				newInv.setExtCom(false);
				newInv.setPaymentMethod(inv.getFaPaymentMethod());
				newInv.setPaymentType(inv.getFaPaymentForm());
				
				Udc satUOM = udcService.searchBySystemAndKey(AppConstants.UDC_SYSTEM_UOMSAT, inv.getUomCode());
				InvoiceDetails iD = new InvoiceDetails();
				iD.setItemNumber("");
				if(inv.getItemDescription() != null && !inv.getItemDescription().isEmpty()) {
					iD.setItemDescription(inv.getItemDescription());
				}else {
					iD.setItemDescription("Venta de activo fijo");
				}
				iD.setUnitProdServ(inv.getFaCodigoSat());
				iD.setUnitPrice(Double.parseDouble(inv.getTransactionLineUnitSellingPrice()));
				iD.setTotalTaxAmount(Double.parseDouble(inv.getTaxRecoverableAmount()));
				iD.setTotalAmount(Double.parseDouble(inv.getTransactionLineUnitSellingPrice()) * Double.parseDouble(inv.getQuantityInvoiced()));
				iD.setTotalDiscount(0);
				iD.setUomName(inv.getUomCode());
				iD.setUomName(satUOM.getStrValue2().toUpperCase());
				iD.setUomCode(satUOM.getStrValue1());
				iD.setItemUomCustoms(String.valueOf(satUOM.getIntValue()));
				iD.setCurrency(inv.getCurrency());
				if(inv.getExchangeRate() != null && !inv.getExchangeRate().isEmpty()) {
					iD.setExchangeRate(Double.parseDouble(inv.getExchangeRate()));
				}else {
					iD.setExchangeRate(AppConstants.INVOICE_EXCHANGE_RATE);
				}				
				iD.setImport(false);
				iD.setLineType(AppConstants.REPORT_LINE_TYPE_NOR);
				iD.setQuantity(Double.parseDouble(inv.getQuantityInvoiced()));
				iD.setTransactionLineNumber(AppConstants.INVOICE_ADVPAY_DEFAULT_TRANSLINNUMBER);
				iD.setRetailComplements(null);
				iD.setIsInvoiceLine("D");
				iD.setIsVehicleControl("0");
				Set<TaxCodes> tcList = new HashSet<TaxCodes>();
				List<TaxCodes> tclConsult = taxCodesService.getTCList(0, 10);
				Set<TaxCodes> tcl = new HashSet<TaxCodes>(tclConsult);
				for(TaxCodes tc: tcl) {
					if(tc.getTaxName().equals(inv.getTaxClassificationCode())) {
						tcList.add(tc);
					}
				}
				if(tcList.size() == 0) {
					tcList.add(taxCodesService.getTCById(2));
				}
				iD.setTaxCodes(tcList);
				
				List<InvoiceDetails> idList = new ArrayList<InvoiceDetails>();
				idList.add(iD);
				Set<InvoiceDetails> sId = new HashSet<InvoiceDetails>(idList);
				newInv.setInvoiceDetails(sId);
			
				
				if(!invoiceDao.saveInvoice(newInv)){
					log.error("ERROR AL CREAR LA FACTURA DE ACTVIVO FIJOS CON LA ORDEN: " + inv.getTransactionNumber());
				}
				
			} else {
				log.error("ERROR AL CREAR LA FACTURA DE ACTVIVO FIJOS CON LA ORDEN: " + inv.getTransactionNumber());
			}
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			log.error("Error al crear la factura para ACTIVOS FIJOS", e);
			return false;
		}
	}

	@Override
	public boolean createInvoiceByInitialCharge(List<Row> r) {
		List<Udc> udc = new ArrayList<Udc>();
		String timeZone = "";
		String pTerms = "";
		String country = "";
		String shipCountry = "";
		try {
			//Fechas
			List<Udc> tZone = udcService.searchBySystem(AppConstants.UDC_SYSTEM_TIMEZONE);
			for(Udc u: tZone) {
				if(u.getStrValue1().equals(AppConstants.UDC_STRVALUE1_TIMEZONE)) {
					timeZone = u.getUdcKey();
				}
			}
			sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
			sdfNoTime.setTimeZone(TimeZone.getTimeZone("UTC"));
			formatterUTC.setTimeZone(TimeZone.getTimeZone(timeZone));
			//Llenado de objeto DTO de la respuesta del reporte de facturas
			List<String> arr = new ArrayList<String>();
			List<InvoicesByReportsDTO> invlist = new ArrayList<InvoicesByReportsDTO>();	
			List<Invoice> invList = new ArrayList<Invoice>();
			for(Row ro: r) {
				InvoicesByReportsDTO invReports = new InvoicesByReportsDTO();
				invReports = fullDTO(ro);
				if(invReports != null) {			
					invlist.add(invReports);
					
				}			
			}
			//llenar header---------------------------------------------------------------------------------------------------
			for(InvoicesByReportsDTO inv: invlist) {	
				Invoice searchExistingInvoice = invoiceDao.getSingleInvoiceByFolio(inv.getTransactionNumber());
				if(searchExistingInvoice == null) {
						if(!arr.contains(inv.getTransactionNumber())) {					
							udc = udcService.searchBySystem(AppConstants.UDC_SYSTEM_COUNTRY);
							for(Udc u: udc) {
								if(u.getStrValue1().equals(inv.getCustomerCountry())) {
									country = u.getUdcKey();
									shipCountry = u.getUdcKey();
								}
							}
							if(inv.getPaymentTerms().equals(AppConstants.PTERMS_CONTADO)) {
								pTerms = AppConstants.PTERMS_CONTADO;
							}else {
								List<Udc> payTerms = udcService.searchBySystem(AppConstants.UDC_SYSTEM_PAYTERMS);
								for(Udc u: payTerms) {
									if(u.getStrValue1().equals(inv.getPaymentTerms()) || u.getUdcKey().equals(inv.getPaymentTerms())) {
										pTerms = String.valueOf(u.getIntValue());
										break;
									}
								}
							}
							Invoice invoice = new Invoice();
							invoice.setUUID(inv.getUuidInitialCharge());
							invoice.setPaymentMethod("PPD");
							invoice.setPaymentType("99");
							invoice.setCFDIUse("G01");
							//Datos del cliente facturacion---------------------------------------------------------------------------------------
							invoice.setCustomerName(inv.getCustomerName());
							invoice.setCustomerZip(inv.getCustomerPostalCode());
							invoice.setCustomerAddress1(inv.getCustomerAddress1());
							invoice.setCustomerState(inv.getCustomerState());
							invoice.setCustomerCountry(country);
							invoice.setCustomerTaxIdentifier(inv.getCustomerTaxIdentifier().replaceAll(" ", ""));
							invoice.setCustomerEmail(inv.getCustomerEmail());
							invoice.setCustomerPartyNumber(inv.getCustomerPartyNumber());
							invoice.setCustomerClass(inv.getCustomerClass());
							
							//Datos del cliente envío---------------------------------------------------------------------------------------
							invoice.setShipToName(inv.getShipToName());
							invoice.setShipToaddress(inv.getShipToAddress());
							invoice.setShipToCity(inv.getShipToCity());
							invoice.setShipToState(inv.getShipToState());
							invoice.setShipToCountry(shipCountry);
							invoice.setShipToZip(inv.getShipToZip());
							
							//Datos de la unidad de negocio---------------------------------------------------------------------------
							invoice.setCompany(companyService.getCompanyByName(inv.getBusisinesUnitName()));
							invoice.setBranch(null);
							invoice.setPayments(null);
							invoice.setAdvanceAplied(false);
							
							//Datos generales---------------------------------------------------------------------------------------
							invoice.setSetName(inv.getSetName());						
							invoice.setPaymentTerms(pTerms);
							invoice.setFolio(inv.getTransactionNumber());
							invoice.setInvoiceDetails(null);
							invoice.setStatus(AppConstants.STATUS_FINISHED);
//							if(facturas.toString().contains(inv.getTransactionTypeName())) {
								invoice.setInvoice(true);
								invoice.setInvoiceType("CARGA INICIAL");
								invoice.setFromSalesOrder(inv.getTransactionNumber());
								invoice.setSerial(inv.getTransactionNumber());
//							}else {
//								continue;
//							}
							invoice.setInvoiceCurrency(inv.getCurrency());
							if(inv.getExchangeRate().isEmpty()) {
								invoice.setInvoiceExchangeRate(AppConstants.INVOICE_EXCHANGE_RATE);
							}else {
								invoice.setInvoiceExchangeRate(Double.parseDouble(df.format(Double.parseDouble(inv.getExchangeRate()))));
							}
							
							invoice.setOrderSource(inv.getTransactionSource());
							invoice.setOrderType(inv.getTransactionTypeName());
							
							//Datos extras------------------------------------------------------------------------------------------------
							Date date = sdfNoTime.parse(inv.getTransactionDate());
							String dateT = formatterUTC.format(date);					
							invoice.setCreatedBy(AppConstants.USER_DEFAULT);
							invoice.setCreationDate(sdfNoTime.parse(dateT));
							invoice.setUpdatedBy(AppConstants.USER_DEFAULT);
							invoice.setUpdatedDate(new Date());
							invoice.setExtCom(false);					
							
							
							InvoiceDetails invDetails = new InvoiceDetails();
							Set<TaxCodes> tcList = new HashSet<TaxCodes>();
							
							invDetails.setItemNumber(inv.getItemName());
							invDetails.setItemDescription(inv.getItemDescription());
							invDetails.setCurrency(inv.getCurrency());
							if(inv.getExchangeRate().isEmpty()) {
								invDetails.setExchangeRate(AppConstants.INVOICE_EXCHANGE_RATE);
							}else {
								invDetails.setExchangeRate(Double.parseDouble(df.format(Double.parseDouble(inv.getExchangeRate()))));
							}
							if(NullValidator.isNull(Double.parseDouble(inv.getTransactionLineUnitSellingPrice())) > 0) {
								invDetails.setUnitPrice(NullValidator.isNull(Double.parseDouble(df.format(Double.parseDouble(inv.getTransactionLineUnitSellingPrice())))));
								invDetails.setLineType(AppConstants.REPORT_LINE_TYPE_NOR);
							}
							
							invDetails.setTransactionLineNumber(inv.getTransactionLineNumber());
							if(inv.getQuantityInvoiced() != null && !inv.getQuantityInvoiced().isEmpty()) {
								invDetails.setQuantity(Double.parseDouble(df.format(Double.parseDouble(NullValidator.isNull(inv.getQuantityInvoiced())))));
								inv.setInvoicedQuantity(String.valueOf((invDetails.getQuantity())));
							}else {
								continue;
							}

							invDetails.setUomName(inv.getUomCode());
							if(inv.getTaxRecoverableAmount().isEmpty()) {
								invDetails.setTotalTaxAmount(0.00);
							}else {
								invDetails.setTotalTaxAmount(NullValidator.isNull(Math.abs(Double.parseDouble(df.format(Double.parseDouble(inv.getTaxRecoverableAmount()))))));
							}						
							invDetails.setTotalAmount(Double.parseDouble(df.format(Double.parseDouble(inv.getTotal()))));
							//COLOCAR QUE LA LÍNEA ES TRANSACCIONABLE
							invDetails.setIsInvoiceLine("D");
							List<TaxCodes> tclConsult = taxCodesService.getTCList(0, 10);
							Set<TaxCodes> tcl = new HashSet<TaxCodes>(tclConsult);
							for(TaxCodes tc: tcl) {
								if(tc.getTaxName().equals(inv.getTaxClassificationCode())) {
									tcList.add(tc);
								}
							}
							if(tcList.size() == 0) {
								tcList.add(taxCodesService.getTCById(2));
							}
							//Añadir registro a la lista facturas
							invList.add(invoice);
							arr.add(inv.getTransactionNumber());
							Set<InvoiceDetails> invDListNormal = new HashSet<InvoiceDetails>();
							invDListNormal.add(invDetails);
							invoice.setInvoiceDetails(invDListNormal);
							
							invoice.setInvoiceTaxAmount(Double.parseDouble(df.format(invDetails.getTotalTaxAmount())));
							invoice.setInvoiceTotal(Double.parseDouble(df.format(invDetails.getTotalAmount() + invDetails.getTotalTaxAmount())));
							invoice.setInvoiceSubTotal(Double.parseDouble(df.format(invDetails.getTotalAmount())));
							invoice.setInvoiceDiscount(0);

							invoice.setRemainingBalanceAmount(String.valueOf(invoice.getInvoiceTotal()));
							//Guarda los datos en la base de datos pero antes valida si ya existe esa factura
							if(invoiceDao.getSingleInvoiceByFolio(invoice.getFolio()) == null) {
								if(invoice.getInvoiceDetails().size() > 0) {
									if(!invoiceService.createInvoice(invoice)) {
										System.out.println(false);
									}
								}							
							}
						}
					}
				}
			return true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
