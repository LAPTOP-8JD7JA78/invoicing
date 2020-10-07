package com.smartech.invoicing.integration.service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartech.invoicing.dao.InvoiceDao;
import com.smartech.invoicing.dto.InvoicesByReportsDTO;
import com.smartech.invoicing.dto.ItemsDTO;
import com.smartech.invoicing.dto.SalesLineLotSerDTO;
import com.smartech.invoicing.dto.SalesOrderDTO;
import com.smartech.invoicing.dto.SalesOrderLinesDTO;
import com.smartech.invoicing.integration.RESTService;
import com.smartech.invoicing.integration.SOAPService;
import com.smartech.invoicing.integration.json.salesorder.SalesOrder;
import com.smartech.invoicing.integration.json.salesorderai.SalesOrderAI;
import com.smartech.invoicing.integration.util.AppConstants;
import com.smartech.invoicing.integration.xml.rowset.Row;
import com.smartech.invoicing.model.Branch;
import com.smartech.invoicing.model.Invoice;
import com.smartech.invoicing.model.InvoiceDetails;
import com.smartech.invoicing.model.NextNumber;
import com.smartech.invoicing.model.TaxCodes;
import com.smartech.invoicing.model.Udc;
import com.smartech.invoicing.service.BranchService;
import com.smartech.invoicing.service.CompanyService;
import com.smartech.invoicing.service.InvoiceService;
import com.smartech.invoicing.service.NextNumberService;
import com.smartech.invoicing.service.TaxCodesService;
import com.smartech.invoicing.service.UdcService;
import com.smartech.invoicing.util.NullValidator;

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
	RESTService restService;
	
	@Autowired
	SOAPService soapService;
	
	static Logger log = Logger.getLogger(InvoicingServiceImpl.class.getName());
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat sdfNoTime = new SimpleDateFormat("yyyy-MM-dd");
	DecimalFormat df = new DecimalFormat("#.00");
	
	@Override
	public boolean createStampInvoice(List<Row> r) {
		try {
			//Llenado de objeto DTO de la respuesta del reporte
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
					Invoice invoice = new Invoice();
					//Datos del cliente facturacion---------------------------------------------------------------------------------------
					invoice.setCustomerName(inv.getCustomerName());
					invoice.setCustomerZip(inv.getCustomerPostalCode());
					invoice.setCustomerAddress1(inv.getCustomerAddress1());
					invoice.setCustomerCountry(inv.getCustomerCountry());
					invoice.setCustomerTaxIdentifier(inv.getCustomerTaxIdentifier());
					invoice.setCustomerEmail("llopez@smartech.com.mx");
					
					//Datos del cliente envío---------------------------------------------------------------------------------------
					invoice.setShipToName(inv.getShipToName());
					invoice.setShipToaddress(inv.getShipToAddress());
					invoice.setShipToCity(inv.getShipToCity());
					invoice.setShipToState(inv.getShipToState());
					invoice.setShipToCountry(inv.getShipToCountry());
					invoice.setShipToZip(inv.getShipToZip());
					
					//Datos de la unidad de negocio---------------------------------------------------------------------------
					invoice.setCompany(companyService.getCompanyByName(inv.getBusisinesUnitName()));
					invoice.setBranch(null);
					
					//Datos generales---------------------------------------------------------------------------------------
					invoice.setSetName(inv.getSetName());
					invoice.setFromSalesOrder(inv.getSalesOrderNumber());
					invoice.setPaymentTerms(inv.getPaymentTerms());
					invoice.setFolio(inv.getTransactionNumber());
					invoice.setInvoiceDetails(null);
					invoice.setStatus(AppConstants.STATUS_START);
					invoice.setInvoice(true);
					invoice.setInvoiceType(AppConstants.ORDER_TYPE_FACTURA);
					if(!inv.getTransactionTypeName().contains(AppConstants.STATUS_REPORTS_ESP)//Es nota de credito
						&& !inv.getTransactionTypeName().contains(AppConstants.STATUS_REPORTS_ING)) {
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
					
					//Datos extras-------------------------------------------------------------------------------------------------
					invoice.setCreatedBy("llopez");
					invoice.setCreationDate(sdfNoTime.parse(inv.getTransactionDate()));
					invoice.setUpdatedBy("llopez");
					invoice.setUpdatedDate(new Date());
					
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
						}else if(i.getInvoiceType().equals(AppConstants.ORDER_TYPE_NC)) {
							if(NullValidator.isNull(Double.parseDouble(in.getTransactionLineUnitSellingPrice())) < 0) {
								invDetails.setUnitPrice(NullValidator.isNull(Math.abs(Double.parseDouble(df.format(Double.parseDouble(in.getTransactionLineUnitSellingPrice()))))));
								invDetails.setLineType(AppConstants.REPORT_LINE_TYPE_NOR);
							}else {
								invDetails.setUnitPrice(Math.abs(Double.parseDouble(df.format(Double.parseDouble(in.getTransactionLineUnitSellingPrice())))));
								invDetails.setLineType(AppConstants.REPORT_LINE_TYPE_DIS);
							}	
						}
						
						invDetails.setTransactionLineNumber(in.getTransactionLineNumber());
						if(i.isInvoice()) {
							invDetails.setQuantity(NullValidator.isNull(Double.parseDouble(df.format(Double.parseDouble(in.getQuantityInvoiced())))));
						}else {
							invDetails.setQuantity(NullValidator.isNull(Double.parseDouble(df.format(Double.parseDouble(in.getQuantityCredited())))));
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
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		return invoice;
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
	public void updateStartInvoiceList() {
		List<String> otList = new ArrayList<String>();
		otList.add(AppConstants.STATUS_REPORTS_ING);
		otList.add(AppConstants.STATUS_REPORTS_ESP);
		otList.add("Credit Memo");
		otList.add("Nota de Crédito");
		
		List<String> sList = new ArrayList<String>();
		sList.add(AppConstants.STATUS_START);
		sList.add(AppConstants.STATUS_ERROR_DATA);
		
		List<Invoice> invoiceList = invoiceDao.getInvoiceListByStatusCode(sList, otList);
		if(invoiceList != null && !invoiceList.isEmpty()) {
			for(Invoice inv : invoiceList) {
				String msgError = "";
				boolean invStatus = true;
				//Obtención de Datos de OM
				SalesOrder so = restService.getSalesOrderByOrderNumber(inv.getFromSalesOrder());
				if(so != null && !so.getItems().isEmpty()) {
					SalesOrderAI soai = restService.getAddInfoBySalesNumber(so);
					if(soai != null && !soai.getItems().isEmpty()) {
						//Proceso de llenado con los datos de OM
						//CABECERO
						Branch br = branchService.getBranchByCode(so.getItems().get(0).getRequestedFulfillmentOrganizationCode());
						if(br != null) {
							inv.setBranch(br);
						}else {
							invStatus = false;
							msgError = msgError + ";BRANCH-Error al obtener la sucursal";
							log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL TRAER LA SUCURSAL");
						}
						//Uso CFDI
						if(!soai.getItems().get(0).getHeaderEffBUSOCFDIprivateVO().isEmpty()) {
							inv.setCFDIUse(soai.getItems().get(0).getHeaderEffBUSOCFDIprivateVO().get(0).getUsocfdi());
						}else {
							invStatus = false;
							msgError = msgError + ";USOCFDI-Error al obtener el Uso CFDI";
							log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL TRAER EL USO CFDI");
						}
						//Método de pago
						if(!soai.getItems().get(0).getHeaderEffBMETODOPAGOprivateVO().isEmpty()) {
							inv.setPaymentMethod(soai.getItems().get(0).getHeaderEffBMETODOPAGOprivateVO().get(0).getMetodopago());
						}else {
							invStatus = false;
							msgError = msgError + ";METODOPAGO-Error al obtener el Método de Pago";
							log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL TRAER EL MÉTODO DE PAGO");
						}
						//Forma de pago
						if(!soai.getItems().get(0).getHeaderEffBMETODOPAGOprivateVO().isEmpty()) {
							inv.setPaymentType(soai.getItems().get(0).getHeaderEffBFORMAPAGOprivateVO().get(0).getFormapago());
						}else {
							invStatus = false;
							msgError = msgError + ";FORMAPAGO-Error al obtener la Forma de Pago";
							log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL TRAER LA FORMA DE PAGO");
						}
						//SI ES NC
						if(!inv.isInvoice()) {
							Invoice invRef = invoiceDao.getSingleInvoiceById(inv.getId());
							if(invRef != null) {
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
							for(com.smartech.invoicing.integration.json.salesorder.Line line: so.getItems().get(0).getLines()) {
								if(line.getProductNumber().contains(invLine.getItemNumber()) 
										&& line.getOrderedQuantity().equals(invLine.getQuantity()) && line.getOrderedUOMCode().contains(invLine.getUomName())) {
									count++;
									//Clave ProdSer
									//obtener
									invLine.setUnitProdServ("25111802");
									invLine.setUomCode("H87");
									
									//Serie y lote (Datos Opcionales)
									if(!line.getLotSerials().isEmpty()) {
										String lots = "";
										String serials = "";
										for(com.smartech.invoicing.integration.json.salesorder.LotSerials lotSer : line.getLotSerials()) {
											if(lotSer.getLotNumber() != null && !"".contains(lotSer.getLotNumber())) {
												lots = lots + invLine.getItemLot() + ",";
											}
											
											if(lotSer.getItemSerialNumberFrom() != null && !"".contains(lotSer.getItemSerialNumberFrom())) {
												if(lotSer.getItemSerialNumberFrom().contains(lotSer.getItemSerialNumberTo())) {
													serials = serials + lotSer.getItemSerialNumberFrom() + ",";
												}else {
													serials = serials + lotSer.getItemSerialNumberFrom() + "-" + lotSer.getItemSerialNumberTo() + ",";
												}
											}
										}
										lots=lots!=""?lots.substring(0, lots.length() - 1):"";
										serials=serials!=""?serials.substring(0, serials.length() - 1):"";
										
										invLine.setItemLot(lots);
										invLine.setItemSerial(serials);
									}
									
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
						msgError = msgError + ";OMSALESORDER-AI-Error al obtener la inf. add. Order en OM (La factura puede no tener DFF asignados)";
						log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL TRAER LA INFO. ADI. ORDEN EN OM");
					}
				}else {
					invStatus = false;
					msgError = msgError + ";OMSALESORDER-Error al obtener la Order en OM (La factura puede no haberse cerrado)";
					log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL TRAER LA ORDEN EN OM");
				}
				
				if(invStatus) {
					inv.setStatus(AppConstants.STATUS_PENDING);
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
								//Clave ProdSer
								ItemsDTO itemSat = soapService.getItemDataByItemNumberOrgCode(line.getProductNumber(), AppConstants.ORACLE_ITEMMASTER);
								if(itemSat != null && itemSat.getItemDFFClavProdServ() != null && !"".contains(itemSat.getItemDFFClavProdServ())) {
									invLine.setUnitProdServ(itemSat.getItemDFFClavProdServ());
								}else {
									invStatus = false;
									msgError = msgError + ";PRODSERVSAT-No existe la Clave ProdServ SAT -" + invLine.getUomName() + " en ItemMaster";
									log.warn("PARA LA ORDEN " + inv.getFolio() + " ERROR AL OBTENER CLAVPRODSER de la linea "+ invLine.getUomName() + ":" + inv.getFolio());
								}
								
								Udc satUOM = udcService.searchBySystemAndKey(AppConstants.UDC_SYSTEM_UOMSAT, invLine.getUomName());
								if(satUOM != null) {
									invLine.setUomCode(satUOM.getStrValue1());
								}else {
									invStatus = false;
									msgError = msgError + ";UOMSAT-No existe la Unidad de Medida SAT -" + invLine.getUomName() + " en UDC";
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
											lots = lots + invLine.getItemLot() + ",";
										}
										
										if(lotSer.getSerialNumberFrom() != null && !"".contains(lotSer.getSerialNumberFrom())) {
											if(lotSer.getSerialNumberFrom().contains(lotSer.getSerialNumberTo())) {
												serials = serials + lotSer.getSerialNumberFrom() + ",";
											}else {
												serials = serials + lotSer.getSerialNumberFrom() + "-" + lotSer.getSerialNumberTo() + ",";
											}
										}
									}
									lots=lots!=""?lots.substring(0, lots.length() - 1):"";
									serials=serials!=""?serials.substring(0, serials.length() - 1):"";
									
									invLine.setItemLot(lots);
									invLine.setItemSerial(serials);
								}
								
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
					inv.setStatus(AppConstants.STATUS_PENDING);
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
						inv = soapService.updateUUIDToOracleERP(inv);
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
	}

//	@Override
//	public List<RESTInvoiceRespDTO> createInvoiceByREST(Invoice i) {
//		return null;
//	}
}
