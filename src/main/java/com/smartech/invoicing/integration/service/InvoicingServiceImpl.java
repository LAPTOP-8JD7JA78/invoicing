package com.smartech.invoicing.integration.service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartech.invoicing.dao.InvoiceDao;
import com.smartech.invoicing.dto.CatAttachmentDTO;
import com.smartech.invoicing.dto.CategoryDTO;
import com.smartech.invoicing.dto.InvoicesByReportsDTO;
import com.smartech.invoicing.dto.ItemGtinDTO;
import com.smartech.invoicing.dto.ItemsDTO;
import com.smartech.invoicing.dto.SalesLineLotSerDTO;
import com.smartech.invoicing.dto.SalesOrderDTO;
import com.smartech.invoicing.dto.SalesOrderLinesDTO;
import com.smartech.invoicing.integration.RESTService;
import com.smartech.invoicing.integration.SOAPService;
import com.smartech.invoicing.integration.json.invitemlot.InventoryItemLots;
import com.smartech.invoicing.integration.util.AppConstants;
import com.smartech.invoicing.integration.xml.rowset.Row;
import com.smartech.invoicing.model.Branch;
import com.smartech.invoicing.model.Invoice;
import com.smartech.invoicing.model.InvoiceDetails;
import com.smartech.invoicing.model.NextNumber;
import com.smartech.invoicing.model.Payments;
import com.smartech.invoicing.model.RetailComplement;
import com.smartech.invoicing.model.TaxCodes;
import com.smartech.invoicing.model.Udc;
import com.smartech.invoicing.service.BranchService;
import com.smartech.invoicing.service.CompanyService;
import com.smartech.invoicing.service.InvoiceService;
import com.smartech.invoicing.service.NextNumberService;
import com.smartech.invoicing.service.PaymentsService;
import com.smartech.invoicing.service.TaxCodesService;
import com.smartech.invoicing.service.UdcService;
import com.smartech.invoicing.util.AppConstantsUtil;
import com.smartech.invoicing.util.NullValidator;
import com.smartech.invoicing.util.StringUtils;

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
	
	static Logger log = Logger.getLogger(InvoicingServiceImpl.class.getName());
	
	final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");	
	final SimpleDateFormat formatterUTC = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	SimpleDateFormat sdfNoTime = new SimpleDateFormat("yyyy-MM-dd");
	DecimalFormat df = new DecimalFormat("#.00");
	
	@SuppressWarnings("null")
	@Override
	public boolean createStampInvoice(List<Row> r) {		
		List<Udc> udc = new ArrayList<Udc>();
		String country = "";
		String shipCountry = "";
		String timeZone = "";
		String pTerms = "";
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
//					System.out.println(invReports.getTransactionNumber());				
					invlist.add(invReports);
					
				}			
			}
			
			//llenar header---------------------------------------------------------------------------------------------------
			for(InvoicesByReportsDTO inv: invlist) {				
				if(!arr.contains(inv.getTransactionNumber())) {					
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
						Udc payTerms = udcService.searchBySystemAndKey(AppConstants.UDC_SYSTEM_PAYTERMS,inv.getPaymentTerms());
						pTerms = String.valueOf(payTerms.getIntValue());
					}
					if(country.isEmpty()) {
						return false;
					}
					if(shipCountry.isEmpty()) {
						return false;
					}
					Invoice invoice = new Invoice();
					//Datos del cliente facturacion---------------------------------------------------------------------------------------
					invoice.setCustomerName(inv.getCustomerName());
					invoice.setCustomerZip(inv.getCustomerPostalCode());
					invoice.setCustomerAddress1(inv.getCustomerAddress1());
					invoice.setCustomerCountry(country);
					invoice.setCustomerTaxIdentifier(inv.getCustomerTaxIdentifier());
					invoice.setCustomerEmail("llopez@smartech.com.mx");
					invoice.setCustomerPartyNumber(inv.getCustomerPartyNumber());
					
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
					
					//Datos generales---------------------------------------------------------------------------------------
					invoice.setSetName(inv.getSetName());
					invoice.setFromSalesOrder(inv.getSalesOrderNumber());
					invoice.setPaymentTerms(pTerms);
					invoice.setFolio(inv.getTransactionNumber());
					invoice.setInvoiceDetails(null);
					invoice.setStatus(AppConstants.STATUS_START);
					invoice.setInvoice(true);
					invoice.setInvoiceType(AppConstants.ORDER_TYPE_FACTURA);
					if(!inv.getTransactionTypeName().contains(AppConstants.STATUS_REPORTS_FCON16)//Es nota de credito
						&& !inv.getTransactionTypeName().contains(AppConstants.STATUS_REPORTS_FCRE16)
						&& !inv.getTransactionTypeName().contains(AppConstants.STATUS_REPORTS_FCON0)
						&& !inv.getTransactionTypeName().contains(AppConstants.STATUS_REPORTS_FCRE0)
						&& !inv.getTransactionTypeName().contains(AppConstants.STATUS_REPORTS_FECON)
						&& !inv.getTransactionTypeName().contains(AppConstants.STATUS_REPORTS_FECRE)
						&& !inv.getTransactionTypeName().contains(AppConstants.STATUS_REPORTS_FACTURA)
						&& !inv.getTransactionTypeName().contains(AppConstants.STATUS_REPORTS_INVOICE)
						&& !inv.getTransactionTypeName().contains(AppConstants.STATUS_REPORTS_FACCON16)
						&& !inv.getTransactionTypeName().contains(AppConstants.STATUS_REPORTS_FACCRE16)
						&& !inv.getTransactionTypeName().contains(AppConstants.STATUS_REPORTS_FACCRE0)
						&& !inv.getTransactionTypeName().contains(AppConstants.STATUS_REPORTS_FACCON0)) {
							invoice.setInvoice(false);
							invoice.setInvoiceReferenceTransactionNumber(inv.getPreviousTransactionNumber());
							invoice.setInvoiceType(AppConstants.ORDER_TYPE_NC);
							invoice.setFromSalesOrder(inv.getPreviousSalesOrder());						
					}
					invoice.setInvoiceCurrency(inv.getCurrency());
					if(inv.getExchangeRate().isEmpty()) {
						invoice.setInvoiceExchangeRate(AppConstants.INVOICE_EXCHANGE_RATE);
					}else {
						invoice.setInvoiceExchangeRate(Double.parseDouble(inv.getExchangeRate()));
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
					
					//Datos para anticipos
					List<Invoice> invPay = invoiceDao.getInvoiceListByStatusCode("", AppConstants.ORDER_TYPE_ADV);
					if(invPay != null && !invPay.isEmpty()) {
						for(Invoice in: invPay) {
							 if(in.getCustomerName().equals(invoice.getCustomerName()) &&
									 in.getCustomerPartyNumber().equals(invoice.getCustomerPartyNumber()) &&
									 in.getCustomerTaxIdentifier().equals(invoice.getCustomerTaxIdentifier())) {
								 List<Payments> payData = paymentsService.getPayByAdv(in.getUUID());
								 int sizePay = 0;
								 for(Payments p: payData) {								
									List<Udc> udcPay = udcService.searchBySystem(AppConstants.UDC_SYSTEM_RTYPE);
									for(Udc u: udcPay) {
										if(u.getStrValue1().equals(AppConstants.UDC_STRVALUE1_ADVANCE_PAYMENTS)) {
											invoice.setInvoiceRelationType(u.getUdcKey());
											break;
										}
									}			
									if(sizePay == 0) {
										invoice.setUUIDReference(p.getUUID());  
									}else {
										invoice.setUUIDReference("," + p.getUUID());
									}
									sizePay+=1;
								 }
							 }
						}
					}
					
					//Añadir registro a la lista facturas
					invList.add(invoice);
					arr.add(inv.getTransactionNumber());
				}
			}
			
			//Llenado de líneas---------------------------------------------------------------------------------------
			for(Invoice i: invList) {
				Set<InvoiceDetails> invDListNormal = new HashSet<InvoiceDetails>();
				Set<InvoiceDetails> invDListDiscount = new HashSet<InvoiceDetails>();
				int disc = 0;
				
				for(InvoicesByReportsDTO in : invlist) {
					if(i.getFolio().equals(in.getTransactionNumber())) {
						InvoiceDetails invDetails = new InvoiceDetails();
						Set<TaxCodes> tcList = new HashSet<TaxCodes>();
						
						invDetails.setItemNumber(in.getItemName());
						invDetails.setItemDescription(in.getItemDescription());
						invDetails.setCurrency(in.getCurrency());
						if(in.getExchangeRate().isEmpty()) {
							invDetails.setExchangeRate(AppConstants.INVOICE_EXCHANGE_RATE);
						}else {
							invDetails.setExchangeRate(Double.parseDouble(df.format(Double.parseDouble(in.getExchangeRate()))));
						}
						if(i.getInvoiceType().equals(AppConstants.ORDER_TYPE_FACTURA)){
							if(NullValidator.isNull(Double.parseDouble(in.getTransactionLineUnitSellingPrice())) > 0) {
								invDetails.setUnitPrice(NullValidator.isNull(Double.parseDouble(df.format(Double.parseDouble(in.getTransactionLineUnitSellingPrice())))));
								invDetails.setLineType(AppConstants.REPORT_LINE_TYPE_NOR);
							}else {
								invDetails.setUnitPrice(Math.abs(Double.parseDouble(df.format(Double.parseDouble(in.getTransactionLineUnitSellingPrice())))));
								invDetails.setLineType(AppConstants.REPORT_LINE_TYPE_DIS);
							}	
							in.setTransactionType(AppConstants.LIVERPOOL_INVOICE);
						}else if(i.getInvoiceType().equals(AppConstants.ORDER_TYPE_NC)) {
							if(NullValidator.isNull(Double.parseDouble(in.getTransactionLineUnitSellingPrice())) < 0) {
								invDetails.setUnitPrice(NullValidator.isNull(Math.abs(Double.parseDouble(df.format(Double.parseDouble(in.getTransactionLineUnitSellingPrice()))))));
								invDetails.setLineType(AppConstants.REPORT_LINE_TYPE_NOR);
							}else {
								invDetails.setUnitPrice(Math.abs(Double.parseDouble(df.format(Double.parseDouble(in.getTransactionLineUnitSellingPrice())))));
								invDetails.setLineType(AppConstants.REPORT_LINE_TYPE_DIS);
							}
							in.setTransactionType(AppConstants.LIVERPOOL_CREDIT_NOTE);
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
						invDetails.setUomName(in.getUomCode());
						if(in.getTaxRecoverableAmount().isEmpty()) {
							invDetails.setTotalTaxAmount(0.00);
						}else {
							invDetails.setTotalTaxAmount(NullValidator.isNull(Math.abs(Double.parseDouble(df.format(Double.parseDouble(in.getTaxRecoverableAmount()))))));
						}						
						invDetails.setTotalAmount(invDetails.getQuantity()*invDetails.getUnitPrice());
						
						//List<TaxCodes> tcL = taxCodesService.getTCList(0, 100);
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
							//rc.setReferenceId();
							//rc.setReferenceDate(referenceDate);
							rc.setAdicionalInformation("");
							rc.setAdicionalInformationNumber(in.getAdicionalInformationNumber());
							rc.setAdicionalInformationId(in.getAdicionalInformationId());
							rc.setDeliveryNote("");
							//rc.setBuyerNumberFolio("");
							//rc.setBuyerDateFolio("");
							rc.setGlobalLocationNumberBuyer(in.getGLNBuyer());
							rc.setPurchasingContact(in.getPurchasingContact());
							rc.setSeller("");
							//rc.getGlobalLocationNumberProvider("");
							//rc.setAlternativeId("");
							rc.setIdentificationType(in.getIdentificationType());
							rc.setElementOnline("");
							//rc.setType("");
							//rc.setNumber("");
							//rc.setgTin("");
							rc.setInovicedQuantity(in.getInvoicedQuantity());
							//rc.setUomCode("");
							rc.setPriceTotal(String.valueOf(invDetails.getTotalAmount()));
							rc.setTotal(String.valueOf(invDetails.getTotalAmount() + invDetails.getTotalTaxAmount()));
							
							invDetails.setRetailComplements(rc);
						}else {
							invDetails.setRetailComplements(null);
						}	
						
						invDetails.setTaxCodes(tcList);
						//invDetails.setTaxCodes(tcList);
						if(invDetails.getLineType().equals(AppConstants.REPORT_LINE_TYPE_NOR)) {							
							invDListNormal.add(invDetails);
						}else {	
							invDListDiscount.add(invDetails);
							disc+=1;
						}
					}					
				}
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
				for(InvoiceDetails id: i.getInvoiceDetails()) {
					taxAmount = taxAmount + id.getTotalTaxAmount();
					subtotal = subtotal + id.getTotalAmount();
					discount = discount + id.getTotalDiscount();
				}
				i.setInvoiceTaxAmount(taxAmount);
				i.setInvoiceTotal(subtotal + taxAmount);
				i.setInvoiceSubTotal(subtotal);
				i.setInvoiceDiscount(discount);
				//Guarda los datos en la base de datos pero antes valida si ya existe esa factura
				/*Invoice invoicetest = invoiceDao.getSingleInvoiceByFolio(i.getFolio());
				if(invoicetest == null) {
					System.out.println(true);
				}*/
				if(invoiceDao.getSingleInvoiceByFolio(i.getFolio()) == null) {
					if(!invoiceService.createInvoice(i)) {
						System.out.println(false);
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
		try {
			//Datos del cliente para facturación
			invoice.setCustomerName(NullValidator.isNull(r.getColumn0()));
			invoice.setCustomerNumber(NullValidator.isNull(r.getColumn1()));
			invoice.setCustomerTaxIdentifier(NullValidator.isNull(r.getColumn2()));
			invoice.setCustomerCountry(NullValidator.isNull(r.getColumn3()));
			invoice.setCustomerPostalCode(NullValidator.isNull(r.getColumn4()));
			invoice.setCustomerAddress1(NullValidator.isNull(r.getColumn5()));			
			invoice.setPaymentTerms(NullValidator.isNull(r.getColumn6()));			
			invoice.setTransactionDate(NullValidator.isNull(r.getColumn7()));
			invoice.setExchangeRate(NullValidator.isNull(r.getColumn8()));
			invoice.setTransactionNumber(NullValidator.isNull(r.getColumn9()));
			invoice.setTransactionSource(NullValidator.isNull(r.getColumn10()));
			invoice.setTransactionTypeName(NullValidator.isNull(r.getColumn11()));	
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
			//Datos del cliente para envío
			invoice.setShipToName(NullValidator.isNull(r.getColumn35()));
			invoice.setShipToAddress(NullValidator.isNull(r.getColumn36()));
			invoice.setShipToCity(NullValidator.isNull(r.getColumn37()));
			invoice.setShipToCountry(NullValidator.isNull(r.getColumn38()));
			invoice.setShipToZip(NullValidator.isNull(r.getColumn39()));
			invoice.setShipToState(NullValidator.isNull(r.getColumn40()));
			invoice.setCustomerPartyNumber(NullValidator.isNull(r.getColumn41()));
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
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		return invoice;
	}
	
	public boolean createAdvPayNC(Invoice invoice) {
		Invoice newInv = new Invoice();
		try {
			Invoice i = invoiceDao.getInvoiceByUuid(invoice.getUUID());
			if(invoice != null) {
				newInv.setCustomerName(invoice.getCustomerName());
				newInv.setCustomerPartyNumber(invoice.getCustomerPartyNumber());
				newInv.setCustomerTaxIdentifier(invoice.getCustomerTaxIdentifier());
				newInv.setCustomerAddress1(invoice.getCustomerAddress1());
				newInv.setCustomerCity(invoice.getCustomerCity());
				newInv.setCustomerCountry(invoice.getCustomerCountry());
				newInv.setCustomerEmail(invoice.getCustomerEmail());
				newInv.setCustomerState(invoice.getCustomerState());
				newInv.setCustomerZip(invoice.getCustomerZip());
				newInv.setShipToName(invoice.getShipToName());
				newInv.setShipToaddress(invoice.getShipToaddress());
				newInv.setShipToCity(invoice.getShipToCity());
				newInv.setShipToCountry(invoice.getShipToCountry());
				newInv.setShipToState(invoice.getShipToState());
				newInv.setShipToZip(invoice.getShipToZip());
				//Datos de la factura
				newInv.setCFDIUse(invoice.getCFDIUse());
				newInv.setBranch(invoice.getBranch());
				newInv.setCompany(invoice.getCompany());
				newInv.setCreatedBy(invoice.getCreatedBy());
				newInv.setCreationDate(sdf.parse(formatterUTC.format(new Date())));
				newInv.setUpdatedBy(invoice.getUpdatedBy());
				newInv.setUpdatedDate(newInv.getCreationDate());
				newInv.setInvoiceRelationType(invoice.getInvoiceRelationType());
				
				return true;
			}			
			return false;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
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
	public void updateStartInvoiceSOAPList() {
		List<String> otList = new ArrayList<String>();
		otList.add(AppConstants.ORDER_TYPE_FACTURA);
		otList.add(AppConstants.ORDER_TYPE_NC);
		
		List<String> sList = new ArrayList<String>();
		sList.add(AppConstants.STATUS_START);
		sList.add(AppConstants.STATUS_ERROR_DATA);
		
		List<Invoice> invoiceList = invoiceDao.getInvoiceListByStatusCode(sList, otList);
		if(invoiceList != null && !invoiceList.isEmpty()) {
			for(Invoice inv : invoiceList) {
				String msgError = "";
				boolean invStatus = true;
				boolean havePetition = false;
				//Obtención de Datos de OM
				SalesOrderDTO so = soapService.getSalesOrderInformation(inv.getFromSalesOrder());
				if(so != null && !so.getLines().isEmpty()) {
					//Proceso de llenado con los datos de OM
					//CABECERO
					Branch br = branchService.getBranchByCode(so.getRequestedFulfillmentOrganizationCode());
					if(br != null) {
						inv.setBranch(br);
					}else {
						invStatus = false;
						msgError = msgError + ";BRANCH-Error al obtener la sucursal";
						log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL TRAER LA SUCURSAL");
					}
					//Saber si tiene complemento exterior
					if(so.getOrderType().equals(AppConstants.INVOICE_EXTERIOR_COMPLEMENT)) {
						inv.setExtCom(true);
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
					//Método de pago
					if(so.getMetodoPago() != null && !"".contains(so.getMetodoPago())) {
						if(so.getMetodoPago().equals(AppConstants.PAY_METHOD)) {
							inv.setRemainingBalanceAmount(null);
							inv.setPreviousBalanceAmount(null);
						}else {
							inv.setRemainingBalanceAmount(String.valueOf(inv.getInvoiceTotal()));
							inv.setPreviousBalanceAmount(null);
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
					//SI ES NC
					if(!inv.isInvoice()) {
						Invoice invRef = invoiceDao.getSingleInvoiceByFolio(inv.getInvoiceReferenceTransactionNumber());
						if(invRef != null && (invRef.getUUID() != null && !"".contains(invRef.getUUID()))) {
							inv.setUUIDReference(invRef.getUUID());
						}else {
							invStatus = false;
							msgError = msgError + ";DATOSREF-Error al obtener la Factura de referencia";
							log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL TRAER LA FACTURA DE REFEENCIA");
						}
					}
					
					int count = 0;
					
					//Revisar las lineas
					for(InvoiceDetails invLine: inv.getInvoiceDetails()) {
						for(SalesOrderLinesDTO line: so.getLines()) {
							if(line.getProductNumber().contains(invLine.getItemNumber()) && Double.parseDouble(line.getOrderedQuantity()) == invLine.getQuantity() 
									&& line.getOrderedUOMCode().contains(invLine.getUomName()) && "CLOSED".contains(line.getStatusCode())) {
								count++;
								//Item Master
								ItemsDTO itemSat = soapService.getItemDataByItemNumberOrgCode(line.getProductNumber(), AppConstants.ORACLE_ITEMMASTER);							
								if(itemSat != null) {
									//Clave Producto Servicio
									if(itemSat.getItemDFFClavProdServ() != null && !"".contains(itemSat.getItemDFFClavProdServ())) {
										invLine.setUnitProdServ(itemSat.getItemDFFClavProdServ());
									}else {
										invStatus = false;
										msgError = msgError + ";PRODSERVSAT-No existe la Clave ProdServ SAT -" + invLine.getItemNumber() + " en ItemMaster";
										log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL OBTENER CLAVPRODSER de la linea "+ invLine.getTransactionLineNumber() + ":" + inv.getFolio());
									}
									
									//Flexfield no Obligatorios
									invLine.setFraccionArancelaria(itemSat.getItemDFFFraccionArancelaria());
									invLine.setItemBrand(itemSat.getItemDFFMarca());
									invLine.setItemModel(itemSat.getItemDFFModelo());
									
									//Importación
									invLine.setImport(itemSat.isItemDFFIsImported());
									if(invLine.isImport()) {
										havePetition = true;
										log.info("PARA LA ORDEN -" + inv.getFolio() + "El ITEM - " + invLine.getItemNumber() + " TIENE PEDIMENTOS");
									}
									
								}else {
									invStatus = false;
									msgError = msgError + ";ITEMMAST-Error al consultar los datos del IMA";
									log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL OBTENER LOS DATOS DEL ITEM MASTER de la linea "+ invLine.getTransactionLineNumber() + ":" + inv.getFolio());
								}
								
								Udc satUOM = udcService.searchBySystemAndKey(AppConstants.UDC_SYSTEM_UOMSAT, invLine.getUomName());
								if(satUOM != null && satUOM.getStrValue1() != null && !"".contains(satUOM.getStrValue1())) {
									//UOM del SAT
									invLine.setUomCode(satUOM.getStrValue1());
									//UOM de Aduana
									invLine.setItemUomCustoms(String.valueOf(satUOM.getIntValue()));
								}else {
									invStatus = false;
									msgError = msgError + ";UOMSAT-No existe la Unidad de Medida SAT -" + invLine.getUomName() + " en UDC";
									log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL OBTENER UDC UOMSAT de la linea "+ invLine.getUomName() + ":" + inv.getFolio());
								}
								
								if(line.getAdditionalInformation() != null && !"".contains(line.getAdditionalInformation())) {
									invLine.setAddtionalDescription(line.getAdditionalInformation());
								}
								
								//Comercio Exterior
								invLine.setIncotermKey(line.getFreightTermsCode());
								
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
								}
								
								//complemento detallista---------------------------------------------------------------
								if(invLine.getRetailComplements() != null) {
									
									//UOM 
									invLine.getRetailComplements().setUomCode(satUOM.getStrValue1());	
								
									//Complemento detallista Número y Type
									if(itemSat.getItemCategory() != null && itemSat.getItemCategory().size() >= 1) {
										String numbers = "";
										String names = "";
										for(CategoryDTO catItem : itemSat.getItemCategory()) {
											String catStr = catItem.getCategoryCode();
											names = names + catItem.getCategoryName() + ",";
											catItem = soapService.getCategoryDataFrom(catStr);
											if(catItem != null && (catItem.getAttachments() != null && !catItem.getAttachments().isEmpty())) {
												for(CatAttachmentDTO att : catItem.getAttachments()) {
													if(att.getTitle().contains(inv.getCustomerPartyNumber())) {
														numbers = numbers + att.getFileName() + ",";
														break;
													}
												}
												if(!"".contains(numbers)) {
													numbers = numbers.substring(0, numbers.length() - 1);
													invLine.getRetailComplements().setNumber(numbers);
												}else {
													invStatus = false;
													msgError = msgError + ";RETAILSNUMBERWS-NO SE ENCONTRARON ATT DEL CLIENTE " + inv.getCustomerPartyNumber();
													log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL OBTENER los attachment de la linea "+ invLine.getItemNumber() + ":" + inv.getFolio() + " del cliente " + inv.getCustomerPartyNumber());
												}
											}else {
												invStatus = false;
												msgError = msgError + ";RETAILSNUMBERWS-NO SE ENCONTRARON DATOS DEL CATÁLOGO " + catStr;
												log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL OBTENER las categorias de la linea "+ invLine.getItemNumber() + ":" + inv.getFolio() + " del categoria " + catStr);
											}
										}
										
										//Type 
										if(!"".contains(names)) {
											names = names.substring(0, names.length() - 1);
											invLine.getRetailComplements().setType(names);
										}else {
											invStatus = false;
											msgError = msgError + ";RETAILSNUMBERWS-NO SE ENCONTRARON CATEGORIAS DEL CLIENTE " + inv.getCustomerPartyNumber();
											log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL OBTENER Categorias de la linea "+ invLine.getItemNumber() + ":" + inv.getFolio() + " del cliente " + inv.getCustomerPartyNumber());
										}
									}else {
										invStatus = false;
										msgError = msgError + ";RETAILSNUMBER-NO TIENE CATEGORÍAS ASIGNADAS EL ARTÍCULO.";
										log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL OBTENER las categorias de la linea "+ invLine.getItemNumber() + ":" + inv.getFolio() + " No contiene catálogos asignados.");
									}
									
									//complemento Destallista GTIN
									ItemGtinDTO gtin = soapService.getItemGTINData(invLine.getItemNumber(), AppConstants.ORACLE_ITEMMASTER, inv.getCustomerPartyNumber());
									if(gtin != null) {
										if(gtin.getGtin() != null && !"".contains(gtin.getGtin())) {
											invLine.getRetailComplements().setgTin(gtin.getGtin());
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

								break;
							}
						}
					}
					if(count != inv.getInvoiceDetails().size()) {
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
					inv.setUpdatedBy("SYSTEM");
					inv.setUpdatedDate(new Date());
					inv.setErrorMsg("");
					invoiceDao.updateInvoice(inv);
				}else {
					inv.setStatus(AppConstants.STATUS_ERROR_DATA);
					inv.setUpdatedBy("SYSTEM");
					inv.setUpdatedDate(new Date());
					inv.setErrorMsg(msgError);
					invoiceDao.updateInvoice(inv);
				}
			}
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
									invLine.setCustomskey("24");
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
		try {
			List<Udc> tZone = udcService.searchBySystem(AppConstants.UDC_SYSTEM_TIMEZONE);
			for(Udc u: tZone) {
				if(u.getStrValue1().equals(AppConstants.UDC_STRVALUE1_TIMEZONE)) {
					timeZone = u.getUdcKey();
				}
			}
			sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
			formatterUTC.setTimeZone(TimeZone.getTimeZone(timeZone));
//			Date date = sdf.parse(r.getColumn19());
//			String dateT = formatterUTC.format(date);
			
			List<Udc> catPago = udcService.searchBySystem(AppConstants.UDC_SYSTEM_RTYPE);
			for(Udc cp: catPago) {
				if(cp.getStrValue1().equals(AppConstants.UDC_STRVALUE1_CPAGOS)) {
					cPago = cp.getUdcKey();
					break;
				}
			}
			//Llenado de objeto DTO de la respuesta del reporte de pagos	
			for(Row ro: r) {
				Invoice invReports = new Invoice();
				invReports = fullPaymentsDTO(ro);
				if(invReports != null) {		
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
			
			//Actualización del UUID en los complementos de pago
			List<Payments> listPayments = paymentsService.getPaymentsStatus(AppConstants.STATUS_ERROR_DATA_PAY);
			if(listPayments != null && !listPayments.isEmpty()) {
				for(Payments lPay: listPayments) {
					Invoice invoiceErrorPay = new Invoice();
					invoiceErrorPay = invoiceDao.getInvoiceWithOutUuid(String.valueOf(lPay.getId()));
					if(invoiceErrorPay.getUUID() != null && !invoiceErrorPay.getUUID().isEmpty()) {
						lPay.setUuidReference(invoiceErrorPay.getUUID());
						lPay.setPaymentError("");
						lPay.setPaymentStatus(AppConstants.STATUS_PENDING);
						paymentsService.updatePayment(lPay);
					}
				}
			}
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
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
				}
			}
			sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
			formatterUTC.setTimeZone(TimeZone.getTimeZone(timeZone));
			Date date = sdf.parse(r.getColumn19());
			String dateT = formatterUTC.format(date);
			
			Invoice inv = new Invoice();
			inv = invoiceDao.getSingleInvoiceByFolio(r.getColumn12());//TransactionReference
			if(inv != null) {
				List<Payments> payments = paymentsService.getPaymentsList(inv.getUUID());
				Payments p = paymentsService.getPayment(r.getColumn23());//Receipt Number
				if(payments != null && p == null && inv.getErrorMsg().isEmpty()) {
					NextNumber nN = new NextNumber();
					nN = nextNumberService.getNumberCon(AppConstants.ORDER_TYPE_CPAGO, inv.getBranch());
					if(r.getColumn7() != null) {//Cambio de moneda
						eRate = r.getColumn7();
					}
					
					int con = payments.size() + 1;	
					pay.setPaymentType(AppConstants.PAYMENTS_CPAGO);				
					pay.setSerial(nN.getSerie());
					pay.setFolio(String.valueOf(nN.getFolio()));
					pay.setCreationDate(dateT);
					pay.setPostalCode(inv.getBranch().getZip());
					pay.setRelationType(cPago);
					pay.setUuidReference(inv.getUUID());
					pay.setBranch(inv.getBranch());
					pay.setCompany(companyService.getCompanyByName(r.getColumn8()));
					pay.setCountry(country);
					pay.setTaxIdentifier(inv.getCustomerTaxIdentifier());//Utilizados para nacional o extranjero
					pay.setCustomerName(r.getColumn0());
					pay.setPartyNumber(inv.getCustomerPartyNumber());
					pay.setCustomerEmail(inv.getCustomerEmail());
					pay.setCurrency(r.getColumn16());
					pay.setExchangeRate(eRate);
					pay.setPaymentAmount(r.getColumn31());
					pay.setTransactionReference("");
					pay.setBankReference("");//Cliente
					pay.setAcountBankTaxIdentifier("");//Cliente
					pay.setPayerAccount("");//Cliente
					pay.setBeneficiaryAccount(r.getColumn10());
					pay.setBenBankAccTaxIden("");	
					pay.setReceiptId(r.getColumn22());
					pay.setReceiptNumber(r.getColumn23());
					pay.setPaymentNumber(String.valueOf(con));	
					pay.setTaxIdentifier(r.getColumn1());
					pay.setPostalCode(r.getColumn4());
					pay.setTransactionReference(r.getColumn12());	
					
//					p.setPreviousBalanceAmount(r.getColumn31());
//					p.setRemainingBalanceAmount(String.valueOf(Double.parseDouble(p.getPreviousBalanceAmount()) - Double.parseDouble(p.getPaymentAmount())));				
					pay.setPaymentStatus(AppConstants.STATUS_PENDING);
					
					if(inv.getUUID()!=null && !inv.getUUID().isEmpty()) {
						pay.setPaymentError("");
					}else {
						pay.setPaymentError("SE HIZO UN PAGO PERO NO TIENE UUID RELACIONADO: " );
						pay.setPaymentStatus(AppConstants.STATUS_ERROR_DATA_PAY);
					}
					
					if(inv.getPreviousBalanceAmount() == null ) {
						pay.setPreviousBalanceAmount(String.valueOf(inv.getInvoiceTotal()));
						pay.setRemainingBalanceAmount(String.valueOf(Double.parseDouble(inv.getRemainingBalanceAmount()) - Double.parseDouble(pay.getPaymentAmount())));
						inv.setPreviousBalanceAmount(pay.getRemainingBalanceAmount());
						inv.setRemainingBalanceAmount("0");
					}else {
						pay.setPreviousBalanceAmount(inv.getPreviousBalanceAmount());
						pay.setRemainingBalanceAmount(String.valueOf(Double.parseDouble(inv.getPreviousBalanceAmount()) - Double.parseDouble(pay.getPaymentAmount())));
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
					
					List<Payments> nPay= new ArrayList<Payments>();
					nPay.add(pay);
					Set<Payments> realPay = new HashSet<Payments>(nPay);
					inv.setPayments(realPay);
				}	
				return inv;
			}else if(r.getColumn17() != null){//Saber si es anticipo
				if(r.getColumn17().equals(AppConstants.IS_ADVANCE_PAYMENT)) {
					Payments pSearch = paymentsService.getPayment(NullValidator.isNull(r.getColumn23())); 
					if(pSearch == null) {
						if(r.getColumn7() != null) {//Cambio de moneda
							eRate = r.getColumn7();
						}
						Udc udcI = udcService.searchBySystemAndKey(AppConstants.PAYMENTS_ADVPAY, AppConstants.INVOICE_SAT_TYPE_I);
						//Montos de pagos
						double tax = Double.parseDouble(r.getColumn31())*(AppConstants.INVOICE_TAX_CODE_016);
						double subTotal = Double.parseDouble(r.getColumn31()) - tax;				
						
						invoice.setBranch(branchService.getBranchByCode(AppConstants.BRANCH_DEFUALT));
						NextNumber nN = new NextNumber();
						nN = nextNumberService.getNumberCon(AppConstants.ORDER_TYPE_ADV, invoice.getBranch());
						if(nN != null) {
							invoice.setCustomerName(NullValidator.isNull(r.getColumn0()));
							invoice.setCustomerTaxIdentifier(NullValidator.isNull(r.getColumn1()));
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
							invoice.setPaymentType(NullValidator.isNull(r.getColumn38()));//----------------Se tomará del reporte forma de pago
							invoice.setPaymentTerms("");
							invoice.setInvoiceTotal(subTotal + tax);
							invoice.setInvoiceSubTotal(subTotal);
							invoice.setInvoiceTaxAmount(tax);
							invoice.setCustomerEmail(NullValidator.isNull(r.getColumn35()));//-------------se tomará del reporte
							invoice.setCFDIUse(udcI.getNote());
							if(branchService.getBranchByCode(NullValidator.isNull(r.getColumn37())) != null) {
								invoice.setBranch(branchService.getBranchByCode(NullValidator.isNull(r.getColumn37())));
							}else {
								invoice.setBranch(branchService.getBranchByCode(AppConstants.BRANCH_DEFUALT));
							}
							invoice.setRemainingBalanceAmount("0");
							invoice.setPreviousBalanceAmount("0");
							invoice.setCreatedBy(AppConstants.USER_DEFAULT);
							invoice.setFolio(String.valueOf(nN.getFolio()));
							invoice.setSerial(nN.getSerie());
							invoice.setFromSalesOrder(null);
							invoice.setStatus(AppConstants.STATUS_PENDING);//------------------------
							invoice.setInvoiceDiscount(0);
							invoice.setInvoice(true);
							invoice.setCustomerPartyNumber(NullValidator.isNull(r.getColumn36()));
							invoice.setOrderSource(AppConstants.ORDER_TYPE_ADV);
							invoice.setOrderType(AppConstants.ORDER_TYPE_ADV);
							invoice.setSetName("");
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
							iD.setUnitPrice(subTotal);
							iD.setTotalTaxAmount(tax);
							iD.setTotalAmount(subTotal + tax);
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
							pay.setPaymentStatus("");
							pay.setCustomerEmail(invoice.getCustomerEmail());
							pay.setPartyNumber(invoice.getCustomerPartyNumber());
							
							pay.setFolio(String.valueOf(nN.getFolio()));
							pay.setSerial(nN.getSerie());
							pay.setRelationType(null);
							pay.setBranch(invoice.getBranch());
							pay.setPartyNumber(invoice.getCustomerPartyNumber());
							pay.setCustomerEmail(invoice.getCustomerEmail());
							
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
			e.printStackTrace();
			return null;
		}
//		return invoice;
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
			
			//Llenado de objeto DTO de la respuesta del reporte de pagos	
			for(Row ro: r) {
				Invoice invReports = new Invoice();
				invReports = fullTrasnferDTO(ro);
				if(invReports != null) {		
					if(!invoiceDao.updateInvoice(invReports)) {
						log.error("EXISTIO ALGÚN ERROR EN EL PROCESO DE TRASLADOS");
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
		String msgError = "";
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
//				String dateT = formatterUTC.format(date);
				//CFDI TRASLAODS
				Udc traslados = udcService.searchBySystemAndKey(AppConstants.ORDER_TYPE_TRANS, AppConstantsUtil.VOUCHER_T);
				
				//OBTENCIÓN DE LAS SUCURSALES
				Branch branchCede = branchService.getBranchByCode(NullValidator.isNull(r.getColumn10()));
				Branch branchRec = branchService.getBranchByCode(NullValidator.isNull(r.getColumn13()));
				
				inv.setExtCom(false);
				inv.setPayments(null);
				inv.setCreatedBy(AppConstants.USER_DEFAULT);
				inv.setCreationDate(date);
				inv.setUpdatedBy(AppConstants.USER_DEFAULT);
				inv.setUpdatedDate(new Date());
				inv.setFolio(NullValidator.isNull(r.getColumn3()));
				inv.setPaymentTerms(null);
				inv.setPaymentMethod(null);
				inv.setPaymentType(null);
				inv.setInvoiceTotal(0);
				inv.setInvoiceSubTotal(0);
				inv.setInvoiceDiscount(0);
				inv.setInvoiceTaxAmount(0);
				inv.setBranch(branchCede);
				inv.setCompany(branchCede.getCompany());
				inv.setInvoice(false);
				inv.setInvoiceRelationType(traslados.getStrValue1());
				inv.setCFDIUse(traslados.getStrValue2());
				//Datos de la sucursal de envío
				inv.setCustomerName(branchRec.getName());
				inv.setCustomerAddress1(branchRec.getAddress());
				inv.setCustomerCity(branchRec.getCity());
				inv.setCustomerCountry(branchRec.getCountry());
				inv.setCustomerState(branchRec.getState());
				inv.setCustomerZip(branchRec.getZip());
				
				invD.setRetailComplements(null);
				invD.setTaxCodes(null);
				invD.setItemDescription(NullValidator.isNull(r.getColumn0()));
				invD.setItemNumber(NullValidator.isNull(r.getColumn1()));
				invD.setItemLot(NullValidator.isNull(r.getColumn5()));
				invD.setItemSerial(NullValidator.isNull(r.getColumn6()));
				invD.setQuantity((Double.parseDouble(NullValidator.isNull(r.getColumn8())))*(-1));
				//Unidad de medida
				Udc satUOM = udcService.searchBySystemAndKey(AppConstants.UDC_SYSTEM_UOMSAT, NullValidator.isNull(r.getColumn4()));
				if(satUOM != null && satUOM.getStrValue1() != null && !"".contains(satUOM.getStrValue1())) {
					//UOM del SAT
					invD.setUomCode(satUOM.getStrValue1());
					//UOM de Aduana
					invD.setItemUomCustoms(String.valueOf(satUOM.getIntValue()));
				}else {
					msgError = msgError + ";UOMSAT-No existe la Unidad de Medida SAT -" + invD.getUomName() + " en UDC";
					log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL OBTENER UDC UOMSAT de la linea "+ invD.getUomName() + ":" + inv.getFolio());
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
}
