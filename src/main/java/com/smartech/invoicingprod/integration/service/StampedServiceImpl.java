package com.smartech.invoicingprod.integration.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.smartech.invoicingprod.dao.InvoiceDao;
import com.smartech.invoicingprod.distribuitorportal.dto.HeadersRestDTO;
import com.smartech.invoicingprod.distribuitorportal.json.InvoiceJSON;
import com.smartech.invoicingprod.distribuitorportal.services.HTTPRequestDistribuitorsService;
import com.smartech.invoicingprod.integration.AnalyticsService;
import com.smartech.invoicingprod.integration.SOAPService;
import com.smartech.invoicingprod.integration.dto.AnalyticsDTO;
import com.smartech.invoicingprod.integration.util.AppConstants;
import com.smartech.invoicingprod.integration.xml.rowset.Rowset;
import com.smartech.invoicingprod.model.Invoice;
import com.smartech.invoicingprod.model.InvoiceDetails;
import com.smartech.invoicingprod.model.NextNumber;
import com.smartech.invoicingprod.model.Payments;
import com.smartech.invoicingprod.model.PaymentsList;
import com.smartech.invoicingprod.model.TaxCodes;
import com.smartech.invoicingprod.model.Udc;
import com.smartech.invoicingprod.service.NextNumberService;
import com.smartech.invoicingprod.service.PaymentsListService;
import com.smartech.invoicingprod.service.PaymentsService;
import com.smartech.invoicingprod.service.TaxCodesService;
import com.smartech.invoicingprod.service.UdcService;
import com.smartech.invoicingprod.util.AppConstantsUtil;
import com.smartech.invoicingprod.util.NullValidator;

@Service("stampedService")
public class StampedServiceImpl implements StampedService{
	
	@Autowired
	UdcService udcService;
	
	@Autowired
	InvoiceDao invoiceDao;
	
	@Autowired
	NumberLetterService numberLetterService;
	
	@Autowired
	PaymentsService paymentsService;
	
	@Autowired
	NextNumberService nextNumberService;
	
	@Autowired
	TaxCodesService taxCodesService;
	
	@Autowired
	PaymentsListService paymentsListService;
	
	@Autowired
	HTTPRequestDistribuitorsService hTTPRequestDistribuitorsService;
	
	@Autowired
	AnalyticsService analyticsService;
	
	@Autowired
	SOAPService soapService;
	
	static Logger log = Logger.getLogger(StampedServiceImpl.class.getName());
	final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	DecimalFormat numberFormat = new DecimalFormat("#.00");
	DecimalFormat numberFormat6 = new DecimalFormat("#.000000");
	public String[ ] impH = new String[10];
	public String[ ] impH2 = new String[5];
	public String[ ] impD = new String[10];
	
	@Override
	public boolean createFileFac(Invoice i) {
		String fileRuta = "";
		String fileName = "";
		String content = "";
		String voucherType = "";
		String relationType = "";
		String paymentTerms = "";
		String UUIDRelated = "";
		String foreignTax = "";
		String folio = "";
		String catExpor = "01";
		impH = new String[10];
		impH2 = new String[5];
		impD = new String[10];
		int n = 1;
		try {
			if(i.getFolio() == null || i.getFolio().isEmpty()) {
				i.setErrorMsg(i.getErrorMsg() + " NO TIENE FOLIO PARA TIMBRE");
				invoiceDao.updateInvoice(i);
				return false;
			}
			if(i.getCatExportacion() != null && !i.getCatExportacion().isEmpty()) {
				catExpor = i.getCatExportacion();
			}
			//Obtener ruta para dejar los archivos
			List<Udc> u = udcService.searchBySystem(AppConstantsUtil.RUTA_FILES);
			for(Udc ud: u) {
				if(ud.getStrValue1().equals(AppConstantsUtil.RUTA_FILES_STAMPED)) {
					fileRuta = ud.getUdcKey();
				}
			}
			//Saber tipo de factura
			if(i.getFolio().contains("-")) {
				folio = i.getFolio().substring(0, i.getFolio().indexOf("-"));
			}else {
				folio = i.getFolio();
			}
			if(i.getInvoiceType().equals(AppConstants.ORDER_TYPE_FACTURA)) {
				voucherType = AppConstantsUtil.VOUCHER_I;
				relationType = NullValidator.isNull(i.getInvoiceRelationType());
				UUIDRelated = NullValidator.isNull(i.getUUIDReference());
			}else if(i.getInvoiceType().equals(AppConstants.ORDER_TYPE_ADV)) {
				voucherType = AppConstantsUtil.VOUCHER_I;
				relationType = NullValidator.isNull(i.getInvoiceRelationType());
				UUIDRelated = NullValidator.isNull(i.getUUIDReference());
			}else if(i.getInvoiceType().equals(AppConstants.ORDER_TYPE_TRANS)) {
				voucherType = AppConstantsUtil.VOUCHER_T;
				relationType = "";
				UUIDRelated = "";
			}else {
				voucherType = AppConstantsUtil.VOUCHER_E;
				relationType = i.getInvoiceRelationType();
				UUIDRelated = i.getUUIDReference();
			}
			//Terminos de pago
			if(i.getPaymentMethod().equals(AppConstantsUtil.PAYMENT_METHOD)) {
				paymentTerms = "CONTADO";
			}else if(i.getPaymentMethod() == null){
				paymentTerms = "CONTADO";
			}else {
				paymentTerms = i.getPaymentTerms();
			}
			if(!i.getCustomerCountry().equals(AppConstants.COUNTRY_DEFAULT)) {
				foreignTax = i.getCustomerTaxIdentifier().replaceAll(" ", "");
			}
			//Llenar los impuestos
			getTaxes(i.getInvoiceDetails());
			//Formato de fecha
			String date = dateFormat.format(new Date());
			if(i.getSerial().equals("MOFAC") || i.getSerial().equals("MONCR") || i.getSerial().equals("MOANT") || i.getSerial().equals("MOTR")) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				Date tempDate = cal.getTime();
				cal.set(Calendar.HOUR, cal.get(Calendar.HOUR)- 1);
				cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE)- 1);
				tempDate = cal.getTime();
				date = dateFormat.format(tempDate);
			}
			//Cabecero txt
			content = AppConstantsUtil.FILES_HEADER + AppConstantsUtil.FILES_SEPARATOR +
					folio + AppConstantsUtil.FILES_SEPARATOR +
					numberFormat.format(i.getInvoiceTotal()) + AppConstantsUtil.FILES_SEPARATOR + 
					numberFormat.format(i.getInvoiceSubTotal()) + AppConstantsUtil.FILES_SEPARATOR +
					voucherType + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(i.getPaymentType()) + AppConstantsUtil.FILES_SEPARATOR +
					paymentTerms + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(i.getPaymentMethod()) + AppConstantsUtil.FILES_SEPARATOR +
					folio + AppConstantsUtil.FILES_SEPARATOR +
					date + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +//i.getInvoiceDiscount() + AppConstantsUtil.FILES_SEPARATOR +//Descuento
					""  + AppConstantsUtil.FILES_SEPARATOR +
					i.getCompany().getTaxRegime() + AppConstantsUtil.FILES_SEPARATOR +
					i.getBranch().getZip() + AppConstantsUtil.FILES_SEPARATOR +//i.getCompany().getZip() + AppConstantsUtil.FILES_SEPARATOR +
					i.getCompany().getTaxIdentifier() + AppConstantsUtil.FILES_SEPARATOR +
					i.getCompany().getBusinessUnitName() + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(i.getCompany().getColony()) + AppConstantsUtil.FILES_SEPARATOR +
					i.getCompany().getAddress() + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +//20
					i.getCompany().getZip() + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					i.getCompany().getCity() + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(i.getCompany().getState()) + AppConstantsUtil.FILES_SEPARATOR +
					i.getCompany().getCountry() + AppConstantsUtil.FILES_SEPARATOR +
					i.getCustomerName() + AppConstantsUtil.FILES_SEPARATOR +
					i.getCustomerTaxIdentifier() + AppConstantsUtil.FILES_SEPARATOR +
					""  + AppConstantsUtil.FILES_SEPARATOR +
					i.getCustomerAddress1() + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					i.getCustomerZip() + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(i.getCustomerCity()) + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(i.getCustomerState()) + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(i.getCustomerCountry()) + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(i.getInvoiceCurrency()) + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(i.getInvoiceExchangeRate()) + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(numberLetterService.getNumberLetter(String.valueOf(i.getInvoiceTotal()), true, i.getInvoiceCurrency())) + AppConstantsUtil.FILES_SEPARATOR +//Total Con letra
					NullValidator.isNull(i.getCustomerEmail()) + AppConstantsUtil.FILES_SEPARATOR +//40
					NullValidator.isNull(i.getPurchaseOrder()) + AppConstantsUtil.FILES_SEPARATOR +//orden de compra------------------------
					AppConstantsUtil.NUMBER_COPIES + AppConstantsUtil.FILES_SEPARATOR +//Número de copias
					NullValidator.isNull(i.getFromSalesOrder()) + AppConstantsUtil.FILES_SEPARATOR +//ORDEN DE VENTA
					NullValidator.isNull(i.getShippingMethod()) + AppConstantsUtil.FILES_SEPARATOR +//VIA DE EMBARQUE
					NullValidator.isNull(i.getProductType()) + AppConstantsUtil.FILES_SEPARATOR +//TIPO DE PRODUCTO
					NullValidator.isNull(i.getCustomerPartyNumber()) + AppConstantsUtil.FILES_SEPARATOR;//Número de cuenta del cliente
					if((i.getShipToaddress() != null && !i.getShipToaddress().isEmpty())) {
						content = content + 
								(i.getShipToName() == null ? i.getCustomerName() : i.getShipToName()) + AppConstantsUtil.FILES_SEPARATOR +
								i.getCustomerTaxIdentifier() + AppConstantsUtil.FILES_SEPARATOR +
								""  + AppConstantsUtil.FILES_SEPARATOR +
								i.getShipToaddress() + AppConstantsUtil.FILES_SEPARATOR +
								"" + AppConstantsUtil.FILES_SEPARATOR +//Interior Number
								"" + AppConstantsUtil.FILES_SEPARATOR +//Exterior Number
								NullValidator.isNull(i.getShipToZip()) + AppConstantsUtil.FILES_SEPARATOR +
								NullValidator.isNull(i.getShipToCity()) + AppConstantsUtil.FILES_SEPARATOR +
								NullValidator.isNull(i.getShipToState()) + AppConstantsUtil.FILES_SEPARATOR +
								NullValidator.isNull(i.getShipToCountry()) + AppConstantsUtil.FILES_SEPARATOR +
								"" + AppConstantsUtil.FILES_SEPARATOR;//Localidad-----57					
					}else {
						content = content +
							i.getCustomerName() + AppConstantsUtil.FILES_SEPARATOR +
							i.getCustomerTaxIdentifier() + AppConstantsUtil.FILES_SEPARATOR +
							""  + AppConstantsUtil.FILES_SEPARATOR +
							i.getCustomerAddress1() + AppConstantsUtil.FILES_SEPARATOR +
							"" + AppConstantsUtil.FILES_SEPARATOR +
							"" + AppConstantsUtil.FILES_SEPARATOR +
							i.getCustomerZip() + AppConstantsUtil.FILES_SEPARATOR +
							NullValidator.isNull(i.getCustomerCity()) + AppConstantsUtil.FILES_SEPARATOR +
							NullValidator.isNull(i.getCustomerState()) + AppConstantsUtil.FILES_SEPARATOR +
							i.getCustomerCountry() + AppConstantsUtil.FILES_SEPARATOR +
							"" + AppConstantsUtil.FILES_SEPARATOR;
					}
		            //Valores de los impuestos
		            for(int h=0; h<impH.length; h++){
		            	content = content + NullValidator.isNull(impH[h]) + "|";
		            }
					content = content +
					NullValidator.isNull(relationType) + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(UUIDRelated) + AppConstantsUtil.FILES_SEPARATOR +
					i.getSerial() + AppConstantsUtil.FILES_SEPARATOR +
					foreignTax + AppConstantsUtil.FILES_SEPARATOR +//taxId Extranjero
					i.getCFDIUse() + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(i.getNotes()) + AppConstantsUtil.FILES_SEPARATOR +//Notes	
					( i.getInvoiceType().equals(AppConstants.ORDER_TYPE_TRANS) ? NullValidator.isNull(i.getBranch().getCityTransfer()): NullValidator.isNull(i.getBranch().getCity()) ) + AppConstantsUtil.FILES_SEPARATOR +
					( i.getInvoiceType().equals(AppConstants.ORDER_TYPE_TRANS) ? NullValidator.isNull(i.getBranch().getAddressTransfer()): NullValidator.isNull(i.getBranch().getAddress()) ) + AppConstantsUtil.FILES_SEPARATOR +
					( i.getInvoiceType().equals(AppConstants.ORDER_TYPE_TRANS) ? NullValidator.isNull(i.getBranch().getColonyTransfer()): NullValidator.isNull(i.getBranch().getColony()) ) + AppConstantsUtil.FILES_SEPARATOR +
					( i.getInvoiceType().equals(AppConstants.ORDER_TYPE_TRANS) ? NullValidator.isNull(i.getBranch().getStateTransfer()): NullValidator.isNull(i.getBranch().getState()) ) + AppConstantsUtil.FILES_SEPARATOR +
					( i.getInvoiceType().equals(AppConstants.ORDER_TYPE_TRANS) ? NullValidator.isNull(i.getBranch().getZipAddressPdfTransfer()): NullValidator.isNull(i.getBranch().getZipAddressPdf()) ) + AppConstantsUtil.FILES_SEPARATOR +
					( i.getInvoiceType().equals(AppConstants.ORDER_TYPE_TRANS) ? NullValidator.isNull(i.getBranch().getCountryTransfer()): NullValidator.isNull(i.getBranch().getCountry()) ) + AppConstantsUtil.FILES_SEPARATOR +
					( i.getInvoiceType().equals(AppConstants.ORDER_TYPE_TRANS) ? NullValidator.isNull(i.getBranch().getCellPhoneNumberTransfer()): NullValidator.isNull(i.getBranch().getCellPhoneNumber()) ) + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(i.getLongDescription()).replaceAll("\n", "").replaceAll("\r", "") + AppConstantsUtil.FILES_SEPARATOR;
					for(int h=0; h<impH2.length; h++) {
						content = content + NullValidator.isNull(impH2[h]) + AppConstantsUtil.FILES_SEPARATOR;
					}
					String comercioExterior = "";
					boolean extCom = NullValidator.isNull(i.isExtCom()); 
					if(extCom) {
						comercioExterior = "1";
					}else {
						comercioExterior = "0";
					}
					content = content + 
							"" + AppConstantsUtil.FILES_SEPARATOR +
							comercioExterior + AppConstantsUtil.FILES_SEPARATOR +
							catExpor + AppConstantsUtil.FILES_SEPARATOR + // Fac 4.0
							NullValidator.isNull(i.getRegimenFiscal()) + AppConstantsUtil.FILES_SEPARATOR +// Fac 4.0
							NullValidator.isNull(i.getCustomerZip()) + AppConstantsUtil.FILES_SEPARATOR +// Fac 4.0
							NullValidator.isNull(i.getShippingInstruction()) + AppConstantsUtil.FILES_SEPARATOR +
							NullValidator.isNull(i.getCompany().getBusinessUnitNameExtensive()) + AppConstantsUtil.FILES_SEPARATOR +
							"\r\n";
//					log.warn("ARCHIVO CREADO " + content);
//					log.warn("ARCHIVO CREADO " + i.getInvoiceDetails().size());
			for(InvoiceDetails id: i.getInvoiceDetails()) {
				if(id != null) {
					String lines = this.dataLines(id, i, n);
//					log.warn("ARCHIVO CREADO " + lines);
					if(lines == null) {
						i.setStatus(AppConstants.STATUS_ERROR_CREATE_FILE);
						invoiceDao.updateInvoice(i);
						return false;
					}
					content = content + lines;
					impD = new String[10];
					n = n + 1;
				}
			}
			//Nombrar archivo
			fileName = NullValidator.isNull(i.getSerial()) + i.getFolio();
			//Crear archivo en la ruta deseada
			File file = new File(fileRuta + fileName + AppConstantsUtil.RUTA_FILES_EXTENSION);
			if (!file.exists()) {
             	file.createNewFile();
             	file.setExecutable(true);
             	file.setReadable(true);
             	file.setWritable(true);             	
            }
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
            out.write(content);
            out.close();
            //Actualizar estado de la factura
            i.setStatus(AppConstants.STATUS_UPDUUID);
            invoiceDao.updateInvoice(i);
			
			return true;
		}catch(Exception e) {
			log.error("ERROR AL CREAR EL ARCHIVO" + e);
			e.printStackTrace();	
			i.setStatus(AppConstants.STATUS_ERROR_CREATE_FILE);
			invoiceDao.updateInvoice(i);
			return false;
		}
	}
	
	public String dataLines(InvoiceDetails idet, Invoice i, int nL) {
		String detail = "";
		getTaxesDetails(idet);
		String operationType = "";
		String petitionKey  = "";
		String itemDescription = "";
		String itemExtraDescription = "";
		String folio = "";
//		String obImp = "02";
		String obImp = "";
		String tipoCambio = String.valueOf(i.getInvoiceExchangeRate());
		try {
			List<Udc> oType = udcService.searchBySystem(AppConstants.UDC_SYSTEM_OPERATIONTYPE);
			List<Udc> pKey = udcService.searchBySystem(AppConstants.UDC_SYSTEM_PETITIONKEY);
			for(Udc uoType: oType) {
				if(uoType.getStrValue1().equals(AppConstants.UDC_STRVALUE1_DEFAULT)) {
					operationType = uoType.getUdcKey();
					break;
				}
			}
			if(idet.getCatObjImp() != null && !idet.getCatObjImp().isEmpty()) {
				obImp = idet.getCatObjImp();
			}
			if(i.getFolio().contains("-")) {
				folio = i.getFolio().substring(0, i.getFolio().indexOf("-"));
			}else {
				folio = i.getFolio();
			}
			for(Udc upKey: pKey) {
				if(upKey.getStrValue1().equals(AppConstants.UDC_STRVALUE1_DEFAULT)) {
					petitionKey = upKey.getUdcKey();
					break;
				}				
			}
			if(idet.getAddtionalDescription() != null) {
				if(idet.getAddtionalDescription().replaceAll("\n", "").replaceAll("\r", "").length() > 1000) {
					if(idet.getAddtionalDescription().contains("Content-Type: text/plain")) {
						itemDescription = idet.getAddtionalDescription().replaceAll("\n", "").replaceAll("\r", "").substring(0,1000).replace("?", "").replace("Content-Type: text/plain", "");
					}else {
						itemDescription = idet.getAddtionalDescription().replaceAll("\n", "").replaceAll("\r", "").substring(0,1000).replace("?", "");
					}
					itemExtraDescription = idet.getAddtionalDescription().replaceAll("\n", "").replaceAll("\r", "").substring(1000);
				}else {
					if(idet.getAddtionalDescription().contains("Content-Type: text/plain")) {
						itemDescription = NullValidator.isNull(idet.getAddtionalDescription().replaceAll("\n", "").replaceAll("\r", "").replace("?", "").replace("Content-Type: text/plain", ""));
					}else {
						itemDescription = NullValidator.isNull(idet.getAddtionalDescription().replaceAll("\n", "").replaceAll("\r", "").replace("?", ""));
					}
					itemExtraDescription = "";
				}
			}else {
				itemDescription = NullValidator.isNull(NullValidator.isNull(idet.getItemDescription()).replaceAll("\n", "").replaceAll("\r", "").replace("?", ""));
			}
			
			detail = NullValidator.isNull(idet.getIsInvoiceLine()) + AppConstantsUtil.FILES_SEPARATOR +
					folio + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(idet.getTransactionLineNumber()) + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(numberFormat.format(idet.getTotalAmount())) + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(itemDescription) + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(idet.getUomName()) + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(numberFormat.format(idet.getUnitPrice())) + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(idet.getQuantity()) + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(tipoCambio) + AppConstantsUtil.FILES_SEPARATOR +//10
					//NullValidator.isNull(idet.getExchangeRate()) + AppConstantsUtil.FILES_SEPARATOR +//10
					NullValidator.isNull(idet.getItemNumber()) + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(idet.getDatePetition()) + AppConstantsUtil.FILES_SEPARATOR +//Fecha de pedimento
					NullValidator.isNull(idet.getCustomskey()) + AppConstantsUtil.FILES_SEPARATOR +//Aduana
					NullValidator.isNull(idet.getNumberPetiton()) + AppConstantsUtil.FILES_SEPARATOR +//Número de pedimento
					"" + AppConstantsUtil.FILES_SEPARATOR +//Fecha caducidad lote
					NullValidator.isNull(idet.getUnitProdServ()) + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(idet.getUomCode()) + AppConstantsUtil.FILES_SEPARATOR;//17
		        	for(int j=0; j < impD.length; j++) {
		        		detail = detail + NullValidator.isNull(impD[j]) + "|";
		        	}
		        	detail = detail +
		        	"" + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(idet.getItemNotes()) + AppConstantsUtil.FILES_SEPARATOR +//Notes
					"" + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(itemExtraDescription) + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR;					

					//Datos del complemento exterior
					if(i.isExtCom()) {
						detail = detail +
							"" + AppConstantsUtil.FILES_SEPARATOR +//c_Motivo Traslado
							operationType + AppConstantsUtil.FILES_SEPARATOR +//c_Tipo de operacion
							petitionKey + AppConstantsUtil.FILES_SEPARATOR +//c_Clave pedimento
							"0" + AppConstantsUtil.FILES_SEPARATOR +//Cert origen
							"" + AppConstantsUtil.FILES_SEPARATOR +
							"" + AppConstantsUtil.FILES_SEPARATOR +
							NullValidator.isNull(idet.getIncotermKey()) + AppConstantsUtil.FILES_SEPARATOR +//c_Incoterm 40
							"" + AppConstantsUtil.FILES_SEPARATOR +//Subdivison	
							NullValidator.isNull(idet.getItemNotes()) + AppConstantsUtil.FILES_SEPARATOR +//Observaciones								
							NullValidator.isNull(tipoCambio) + AppConstantsUtil.FILES_SEPARATOR +//Tipo de cambio
							//NullValidator.isNull(idet.getExchangeRate()) + AppConstantsUtil.FILES_SEPARATOR +//Tipo de cambio
							NullValidator.isNull(idet.getTotalAmount()) + AppConstantsUtil.FILES_SEPARATOR +//Total venta moneda extranjera
							"" + AppConstantsUtil.FILES_SEPARATOR +//Curp del emisor
							"" + AppConstantsUtil.FILES_SEPARATOR +//Número del registro fiscal
							"" + AppConstantsUtil.FILES_SEPARATOR +//Recidencia fiscal
							"" + AppConstantsUtil.FILES_SEPARATOR +//ShipTo Número de identificacion fiscal
							i.getCustomerName() + AppConstantsUtil.FILES_SEPARATOR +//shipTo Nombre
							i.getShipToaddress() + AppConstantsUtil.FILES_SEPARATOR +//shipTo Calle 50
							"" + AppConstantsUtil.FILES_SEPARATOR +//ShipTo # Exterior 
							"" + AppConstantsUtil.FILES_SEPARATOR +//shipTo # Interior
							"" + AppConstantsUtil.FILES_SEPARATOR +//ShipTo Colony
							"" + AppConstantsUtil.FILES_SEPARATOR +//ShipTo Localidad
							i.getShipToCity() + AppConstantsUtil.FILES_SEPARATOR +//ShipTo Municipio
							NullValidator.isNull(i.getShipToState()) + AppConstantsUtil.FILES_SEPARATOR +//shipTo Estado
							NullValidator.isNull(i.getShipToCountry()) + AppConstantsUtil.FILES_SEPARATOR +//ShipTo Country
							i.getShipToZip() + AppConstantsUtil.FILES_SEPARATOR +//shipTo Zip
							idet.getItemNumber() + AppConstantsUtil.FILES_SEPARATOR +//Número del artículo, sku
							NullValidator.isNull(idet.getFraccionArancelaria()) + AppConstantsUtil.FILES_SEPARATOR +//c_FraccionArancelaria 60
							NullValidator.isNull(idet.getQuantity()) + AppConstantsUtil.FILES_SEPARATOR +//Cantidad Aduana
							NullValidator.isNull(idet.getItemUomCustoms()) + AppConstantsUtil.FILES_SEPARATOR +//c_Unidad de medida aduana
							NullValidator.isNull(idet.getUnitPrice()) + AppConstantsUtil.FILES_SEPARATOR +//Valor unitario de aduana
							NullValidator.isNull(idet.getTotalAmount()) + AppConstantsUtil.FILES_SEPARATOR +//Valor total aduana
							NullValidator.isNull(idet.getItemBrand()) + AppConstantsUtil.FILES_SEPARATOR;//Marca
						if(NullValidator.isNull(idet.getEquipmentReference()).equals("E")) {
							detail = detail + 
									NullValidator.isNull(idet.getItemNumber()) + AppConstantsUtil.FILES_SEPARATOR +//Modelo
									"" + AppConstantsUtil.FILES_SEPARATOR;//Submodelo	
						}else {
							detail = detail + 
							NullValidator.isNull(idet.getItemNumber()) + AppConstantsUtil.FILES_SEPARATOR +//Modelo
							"" + AppConstantsUtil.FILES_SEPARATOR;//Submodelo	
						}
													
					}else {
						detail = detail +
							"" + AppConstantsUtil.FILES_SEPARATOR +
							"" + AppConstantsUtil.FILES_SEPARATOR +
							"" + AppConstantsUtil.FILES_SEPARATOR +
							"" + AppConstantsUtil.FILES_SEPARATOR +
							"" + AppConstantsUtil.FILES_SEPARATOR +
							"" + AppConstantsUtil.FILES_SEPARATOR +
							"" + AppConstantsUtil.FILES_SEPARATOR +
							"" + AppConstantsUtil.FILES_SEPARATOR +
							"" + AppConstantsUtil.FILES_SEPARATOR +
							"" + AppConstantsUtil.FILES_SEPARATOR +//10
							"" + AppConstantsUtil.FILES_SEPARATOR +
							"" + AppConstantsUtil.FILES_SEPARATOR +
							"" + AppConstantsUtil.FILES_SEPARATOR +
							"" + AppConstantsUtil.FILES_SEPARATOR +
							"" + AppConstantsUtil.FILES_SEPARATOR +
							"" + AppConstantsUtil.FILES_SEPARATOR +
							"" + AppConstantsUtil.FILES_SEPARATOR +
							"" + AppConstantsUtil.FILES_SEPARATOR +
							"" + AppConstantsUtil.FILES_SEPARATOR +
							"" + AppConstantsUtil.FILES_SEPARATOR +//10
							"" + AppConstantsUtil.FILES_SEPARATOR +
							"" + AppConstantsUtil.FILES_SEPARATOR +
							"" + AppConstantsUtil.FILES_SEPARATOR +
							"" + AppConstantsUtil.FILES_SEPARATOR +
							"" + AppConstantsUtil.FILES_SEPARATOR +
							"" + AppConstantsUtil.FILES_SEPARATOR +
							"" + AppConstantsUtil.FILES_SEPARATOR +
							"" + AppConstantsUtil.FILES_SEPARATOR +
							"" + AppConstantsUtil.FILES_SEPARATOR +
							"" + AppConstantsUtil.FILES_SEPARATOR +//10
							"" + AppConstantsUtil.FILES_SEPARATOR +
							"" + AppConstantsUtil.FILES_SEPARATOR;
						if(NullValidator.isNull(idet.getEquipmentReference()).equals("E")) {
							detail = detail + 
									NullValidator.isNull(idet.getItemNumber()) + AppConstantsUtil.FILES_SEPARATOR +//Modelo
									"" + AppConstantsUtil.FILES_SEPARATOR;//Submodelo	
						}else {
							detail = detail + 
							NullValidator.isNull(idet.getItemNumber()) + AppConstantsUtil.FILES_SEPARATOR +//Modelo
							"" + AppConstantsUtil.FILES_SEPARATOR;//Submodelo	
						}
					}
					detail = detail + 
							NullValidator.isNull(idet.getItemSerial()) + AppConstantsUtil.FILES_SEPARATOR;//No. de seríe

		        	//Datos del complemento detallista 
		        	if(idet.getRetailComplements() != null) {
		        		if(idet.getRetailComplements().getReferenceDate() == null) {
		        			idet.getRetailComplements().setReferenceDate(new Date());
		        		}
		        		String refDate = df.format(NullValidator.isNull(idet.getRetailComplements().getReferenceDate()));
			        	detail = detail +								
			        	NullValidator.isNull(idet.getRetailComplements().getDocumentStatus()) + AppConstantsUtil.FILES_SEPARATOR +
			        	NullValidator.isNull(idet.getRetailComplements().getTransactionType()) + AppConstantsUtil.FILES_SEPARATOR +
			        	NullValidator.isNull(idet.getRetailComplements().getInstructionCode()) + AppConstantsUtil.FILES_SEPARATOR +
			        	NullValidator.isNull(idet.getRetailComplements().getTextNote()) + AppConstantsUtil.FILES_SEPARATOR +
						NullValidator.isNull(idet.getRetailComplements().getReferenceId()) + AppConstantsUtil.FILES_SEPARATOR +
						NullValidator.isNull(refDate) + AppConstantsUtil.FILES_SEPARATOR +
						NullValidator.isNull(idet.getRetailComplements().getAdicionalInformation()) + AppConstantsUtil.FILES_SEPARATOR +
						NullValidator.isNull(idet.getRetailComplements().getAdicionalInformationNumber()) + AppConstantsUtil.FILES_SEPARATOR +
						NullValidator.isNull(idet.getRetailComplements().getAdicionalInformationId()) + AppConstantsUtil.FILES_SEPARATOR +
						NullValidator.isNull(idet.getRetailComplements().getDeliveryNote()) + AppConstantsUtil.FILES_SEPARATOR +//10
						NullValidator.isNull(idet.getRetailComplements().getBuyerNumberFolio()) + AppConstantsUtil.FILES_SEPARATOR +//"2085157632" + AppConstantsUtil.FILES_SEPARATOR +
						"" + AppConstantsUtil.FILES_SEPARATOR +//	NullValidator.isNull(idet.getRetailComplements().getBuyerDateFolio().toString()) + AppConstantsUtil.FILES_SEPARATOR +					
						NullValidator.isNull(idet.getRetailComplements().getGlobalLocationNumberBuyer()) + AppConstantsUtil.FILES_SEPARATOR +
						NullValidator.isNull(idet.getRetailComplements().getPurchasingContact()) + AppConstantsUtil.FILES_SEPARATOR +
						NullValidator.isNull(idet.getRetailComplements().getSeller()) + AppConstantsUtil.FILES_SEPARATOR +
						i.getCompany().getGlobalLocationNumberProvider() + AppConstantsUtil.FILES_SEPARATOR +
						i.getCompany().getAlternativeId() + AppConstantsUtil.FILES_SEPARATOR +
						NullValidator.isNull(idet.getRetailComplements().getIdentificationType()) + AppConstantsUtil.FILES_SEPARATOR +
						NullValidator.isNull(idet.getRetailComplements().getElementOnline()) + AppConstantsUtil.FILES_SEPARATOR +
						NullValidator.isNull(idet.getRetailComplements().getType()) + AppConstantsUtil.FILES_SEPARATOR +//10
						NullValidator.isNull(idet.getRetailComplements().getNumber()) + AppConstantsUtil.FILES_SEPARATOR +
						NullValidator.isNull(idet.getRetailComplements().getgTin()) + AppConstantsUtil.FILES_SEPARATOR +
						NullValidator.isNull(idet.getRetailComplements().getInovicedQuantity()) + AppConstantsUtil.FILES_SEPARATOR +
						NullValidator.isNull(idet.getRetailComplements().getUomCode()) + AppConstantsUtil.FILES_SEPARATOR +
						NullValidator.isNull(idet.getRetailComplements().getPriceTotal()) + AppConstantsUtil.FILES_SEPARATOR +
						NullValidator.isNull(idet.getRetailComplements().getTotal()) + AppConstantsUtil.FILES_SEPARATOR +
						"" + AppConstantsUtil.FILES_SEPARATOR +
						"" + AppConstantsUtil.FILES_SEPARATOR +
						"TN" + AppConstantsUtil.FILES_SEPARATOR;
			        	String isImported = null;
			        	if(idet.isImport()) {
		        			isImported = "1";
		        		}else {
		        			isImported = "0";
		        		}
			        	if(i.getInvoiceType().equals(AppConstants.ORDER_TYPE_TRANS)) {
				        	detail = detail +
				        			isImported + AppConstantsUtil.FILES_SEPARATOR +
				        			i.getBranch().getInvOrganizationId() + AppConstantsUtil.FILES_SEPARATOR +//sucursal
				        			NullValidator.isNull(idet.getProductTypeCode()) + AppConstantsUtil.FILES_SEPARATOR +//tipo de producto
				        			NullValidator.isNull(idet.getUnitCost()) + AppConstantsUtil.FILES_SEPARATOR +//total venta costo unitario
				        			NullValidator.isNull(idet.getExchangeDailyRate()) + AppConstantsUtil.FILES_SEPARATOR +//tipo de cambio
				        			NullValidator.isNull(idet.getEquipmentReference()) + AppConstantsUtil.FILES_SEPARATOR +//referencia equipo
									i.getFromSalesOrder() + AppConstantsUtil.FILES_SEPARATOR +//Número de envío
									NullValidator.isNull(idet.getPriceListWTax()) + AppConstantsUtil.FILES_SEPARATOR +//Precio Unitario sin iva
									NullValidator.isNull(idet.getIsVehicleControl()) + AppConstantsUtil.FILES_SEPARATOR +
									NullValidator.isNull(idet.getSerialPdf()) + AppConstantsUtil.FILES_SEPARATOR +
									NullValidator.isNull(idet.getCatObjImp()) + AppConstantsUtil.FILES_SEPARATOR +// Fac 4.0
									"\r\n";
			        	}else {
				        	detail = detail +
				        			isImported + AppConstantsUtil.FILES_SEPARATOR +
				        			i.getBranch().getInvOrganizationId() + AppConstantsUtil.FILES_SEPARATOR +//sucursal
				        			NullValidator.isNull(idet.getProductTypeCode()) + AppConstantsUtil.FILES_SEPARATOR +//tipo de producto
				        			NullValidator.isNull(idet.getUnitCost()) + AppConstantsUtil.FILES_SEPARATOR +//total venta costo unitario
				        			NullValidator.isNull(idet.getExchangeDailyRate()) + AppConstantsUtil.FILES_SEPARATOR +//tipo de cambio
				        			NullValidator.isNull(idet.getEquipmentReference()) + AppConstantsUtil.FILES_SEPARATOR +//referencia equipo
									"" + AppConstantsUtil.FILES_SEPARATOR +//Número de envío
									NullValidator.isNull(idet.getPriceListWTax()) + AppConstantsUtil.FILES_SEPARATOR +//Precio Unitario sin iva
									NullValidator.isNull(idet.getIsVehicleControl()) + AppConstantsUtil.FILES_SEPARATOR +
									NullValidator.isNull(idet.getSerialPdf()) + AppConstantsUtil.FILES_SEPARATOR +
									NullValidator.isNull(idet.getCatObjImp()) + AppConstantsUtil.FILES_SEPARATOR +// Fac 4.0
									"\r\n";
			        	}
		        	}else {
			        	detail = detail +
						"" + AppConstantsUtil.FILES_SEPARATOR +
						"" + AppConstantsUtil.FILES_SEPARATOR +
						"" + AppConstantsUtil.FILES_SEPARATOR +
						"" + AppConstantsUtil.FILES_SEPARATOR +
						"" + AppConstantsUtil.FILES_SEPARATOR +
						"" + AppConstantsUtil.FILES_SEPARATOR +
						"" + AppConstantsUtil.FILES_SEPARATOR +
						"" + AppConstantsUtil.FILES_SEPARATOR +
						"" + AppConstantsUtil.FILES_SEPARATOR +
						"" + AppConstantsUtil.FILES_SEPARATOR +//10
						"" + AppConstantsUtil.FILES_SEPARATOR +
						"" + AppConstantsUtil.FILES_SEPARATOR +
						"" + AppConstantsUtil.FILES_SEPARATOR +
						"" + AppConstantsUtil.FILES_SEPARATOR +
						"" + AppConstantsUtil.FILES_SEPARATOR +
						"" + AppConstantsUtil.FILES_SEPARATOR +
						"" + AppConstantsUtil.FILES_SEPARATOR +
						"" + AppConstantsUtil.FILES_SEPARATOR +
						"" + AppConstantsUtil.FILES_SEPARATOR +
						"" + AppConstantsUtil.FILES_SEPARATOR +//10
						"" + AppConstantsUtil.FILES_SEPARATOR +
						"" + AppConstantsUtil.FILES_SEPARATOR +
						"" + AppConstantsUtil.FILES_SEPARATOR +
						"" + AppConstantsUtil.FILES_SEPARATOR +
						"" + AppConstantsUtil.FILES_SEPARATOR +
						"" + AppConstantsUtil.FILES_SEPARATOR +
						"" + AppConstantsUtil.FILES_SEPARATOR +
						"" + AppConstantsUtil.FILES_SEPARATOR +
						"" + AppConstantsUtil.FILES_SEPARATOR;
			        	String isImported = "";
			        	if(idet.isImport()) {
		        			isImported = "1";
		        		}else {
		        			isImported = "0";
		        		}
			        	if(i.getInvoiceType().equals(AppConstants.ORDER_TYPE_TRANS)) {
				        	detail = detail +
				        			isImported + AppConstantsUtil.FILES_SEPARATOR + 
				        			i.getBranch().getInvOrganizationId() + AppConstantsUtil.FILES_SEPARATOR +//sucursal
				        			NullValidator.isNull(idet.getProductTypeCode()) + AppConstantsUtil.FILES_SEPARATOR +//tipo de producto
				        			NullValidator.isNull(idet.getUnitCost()) + AppConstantsUtil.FILES_SEPARATOR +//total venta costo unitario
				        			NullValidator.isNull(idet.getExchangeDailyRate()) + AppConstantsUtil.FILES_SEPARATOR +//tipo de cambio
				        			NullValidator.isNull(idet.getEquipmentReference()) + AppConstantsUtil.FILES_SEPARATOR +//referencia equipo
									i.getFromSalesOrder() + AppConstantsUtil.FILES_SEPARATOR +//Número de envío
									NullValidator.isNull(idet.getPriceListWTax()) + AppConstantsUtil.FILES_SEPARATOR +//Precio Unitario sin iva
									NullValidator.isNull(idet.getIsVehicleControl()) + AppConstantsUtil.FILES_SEPARATOR +
									NullValidator.isNull(idet.getSerialPdf()) + AppConstantsUtil.FILES_SEPARATOR +
									NullValidator.isNull(idet.getCatObjImp()) + AppConstantsUtil.FILES_SEPARATOR +// Fac 4.0
//									obImp + AppConstantsUtil.FILES_SEPARATOR +
									"\r\n";
			        	}else {
				        	detail = detail +
				        			isImported + AppConstantsUtil.FILES_SEPARATOR +
				        			i.getBranch().getInvOrganizationId() + AppConstantsUtil.FILES_SEPARATOR +//sucursal
				        			NullValidator.isNull(idet.getProductTypeCode()) + AppConstantsUtil.FILES_SEPARATOR +//tipo de producto
				        			NullValidator.isNull(idet.getUnitCost()) + AppConstantsUtil.FILES_SEPARATOR +//total venta costo unitario
				        			NullValidator.isNull(idet.getExchangeDailyRate()) + AppConstantsUtil.FILES_SEPARATOR +//tipo de cambio
				        			NullValidator.isNull(idet.getEquipmentReference()) + AppConstantsUtil.FILES_SEPARATOR +//referencia equipo
									"" + AppConstantsUtil.FILES_SEPARATOR +//Número de envío
									NullValidator.isNull(idet.getPriceListWTax()) + AppConstantsUtil.FILES_SEPARATOR +//Precio Unitario sin iva
									NullValidator.isNull(idet.getIsVehicleControl()) + AppConstantsUtil.FILES_SEPARATOR +
									NullValidator.isNull(idet.getSerialPdf()) + AppConstantsUtil.FILES_SEPARATOR +
									NullValidator.isNull(idet.getCatObjImp()) + AppConstantsUtil.FILES_SEPARATOR +// Fac 4.0
//									obImp + AppConstantsUtil.FILES_SEPARATOR +
									"\r\n";
			        	}
		        	}

			return detail;
		}catch(Exception e) {
			e.printStackTrace();
			log.error("ERROR AL LLENAR EL DETALLE DEL ARCHIVO TXT " + i.getFolio() + e);
			return null;
		}
	}
	
	public void getTaxes(Set<InvoiceDetails> set) {
		int op = 0;
		try {
			for(InvoiceDetails id: set) {
				Set<TaxCodes> tc = new HashSet<TaxCodes>(id.getTaxCodes());
				for(TaxCodes tax: tc) {
					op = Integer.parseInt(tax.getPosition());
					if(tax.getTax().equals(AppConstantsUtil.TAX_CODE)) {
						switch(op) {
							case 1:
								impH[0] = tax.getTax();	
								impH[1] = tax.getFactor();
								if(impH[2] != null) {
									impH[2] = String.valueOf(id.getTotalTaxAmount() + Double.parseDouble(impH[2])) ;
								}else {
									impH[2] = String.valueOf(id.getTotalTaxAmount());	
								}
								if(impH[3] != null) {
									impH[3] = String.valueOf(id.getTotalAmount() + Double.parseDouble(impH[3]));
								}else {
									impH[3] = String.valueOf(id.getTotalAmount());									
								}
								impH[4] = String.valueOf(tax.getTaxValue());
								break;
							case 2:
								impH2[0] = tax.getTax();	
								impH2[1] = tax.getFactor();
								if(impH2[2] != null) {
									impH2[2] = String.valueOf(id.getTotalTaxAmount() + Double.parseDouble(impH2[2])) ;
								}else {
									impH2[2] = String.valueOf(id.getTotalTaxAmount());	
								}
								if(impH2[3] != null) {
									impH2[3] = String.valueOf(id.getTotalAmount() + Double.parseDouble(impH2[3]));
								}else {
									impH2[3] = String.valueOf(id.getTotalAmount());									
								}
								impH2[4] = String.valueOf(tax.getTaxValue());
								break;
							default:
								break;
						}
					}
				}
			}
			
			if(impH[3] != null) {
				impH[2] = numberFormat.format(NullValidator.isNullD(impH[2]));
				impH[3] = numberFormat.format(NullValidator.isNullD(impH[3]));
			}
			
			if(impH2[3] != null) {
				impH2[3] = numberFormat.format(NullValidator.isNullD(impH2[3]));
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getTaxesDetails(InvoiceDetails set) {
		String op = "000";
		try {
			Set<TaxCodes> tc = new HashSet<TaxCodes>(set.getTaxCodes());
			for(TaxCodes tax: tc) {
//				op = Integer.parseInt(tax.getPosition());
				op = tax.getTax();
				if(tax.getTax().equals(AppConstantsUtil.TAX_CODE)) {
					switch(op) {
						case "002":
							impD[0] = tax.getTax();
							if(impD[1] != null) {
								impD[1] = String.valueOf(set.getTotalAmount() + Double.parseDouble(impD[1]));
							}else {
								impD[1] = String.valueOf(set.getTotalAmount());									
							}
							impD[2] = tax.getFactor();
							impD[3] = String.valueOf(tax.getTaxValue());
							if(impD[4] != null) {
								impD[4] = String.valueOf(set.getTotalTaxAmount() + Double.parseDouble(impD[4]));
							}else {
								impD[4] = String.valueOf(set.getTotalTaxAmount());	
							}
							break;
						case "003":
							impD[5] = tax.getTax();
							if(impD[6] != null) {
								impD[6] = String.valueOf(set.getTotalAmount() + Double.parseDouble(impD[6]));
							}else {
								impD[6] = String.valueOf(set.getTotalAmount());									
							}
							impD[7] = tax.getFactor();
							impD[8] = String.valueOf(tax.getTaxValue());
							if(impD[9] != null) {
								impD[9] = String.valueOf(set.getTotalTaxAmount() + Double.parseDouble(impD[9]));
							}else {
								impD[9] = String.valueOf(set.getTotalTaxAmount());	
							}
							break;
						default:
							break;
					}
				}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "resource", "unused" })
	@Override
	public boolean readDataFromTxt() {
		String filePathResponse = "";
		String filePathPay = "";
		String fileName = "";
		String contentFile = "";
		String c = "";
		boolean proceso = this.procesoAlejandro();
		return proceso;
//		return this.procesoFernando();
		/*
		try{
			//Proceso Alejandro
			List<Invoice> updateInvAle = invoiceDao.getInvoiceListByStatusCode(AppConstants.STATUS_UPDUUID, "");
			List<Payments> updatePayAle = paymentsService.getPaymentsStatus(AppConstants.STATUS_UPDUUID);
			List<Invoice> updateNCAle = invoiceDao.getInvoiceListByStatusCode(AppConstants.STATUS_PENDING_UUID_NC, AppConstants.ORDER_TYPE_NC);
			List<Invoice> lastestInvAle = new ArrayList<Invoice>();
			List<String> arrAle = new ArrayList<String>();
			for(Invoice i: updateInvAle) {
				if(!arrAle.contains(i.getFolio())) {
					lastestInvAle.add(i);
					arrAle.add(i.getFolio());
				}
			}
			for(Invoice invD: lastestInvAle) {
				Invoice inv = new Invoice();
				inv = invD;
				log.info(inv.getId() + " " + inv.getFolio());
				String fName = inv.getSerial() + inv.getFolio() + ".txt";
				String uuid = null;
				//Obtener UUID
				uuid = this.getUuidAlejandro(fName, inv.getCustomerTaxIdentifier());
				//Obtener UUID
				String advPay = "";
				List<Udc> udcPay = udcService.searchBySystem(AppConstants.UDC_SYSTEM_RTYPE);
				for(Udc u: udcPay) {
					if(u.getStrValue1().equals(AppConstants.UDC_STRVALUE1_ADVANCE_PAYMENTS)) {
						advPay = u.getUdcKey();
						break;
					}
				}
				if(uuid != null && !uuid.isEmpty()) {
					uuid = uuid.replaceAll("\"", "");
					String option =  inv.getInvoiceType();
					switch(option) {
						case AppConstants.ORDER_TYPE_FACTURA:
							log.warn(uuid + " " + inv.getFolio());
							if(inv.getInvoiceRelationType() == null || inv.getInvoiceRelationType().equals("04")){
								log.warn(uuid + " " + inv.getFolio());
								inv.setUUID(uuid);
								inv.setErrorMsg(null);
								inv.setStatus(AppConstants.STATUS_INVOICED);
								invoiceDao.updateInvoice(inv);
								
								//Enviar a Portal de Distribuidores
								createDistPortalInvoice(inv);
							}else if(inv.getInvoiceRelationType().equals(advPay)){
								inv.setUUID(uuid);
								inv.setErrorMsg(null);
								inv.setStatus(AppConstants.STATUS_INVOICED);
								//this.createAdvPayNC(inv);
								invoiceDao.updateInvoice(inv);
								
								//Actualiza NC generadas a partir de anticipos
								for(Invoice nc : updateNCAle) {
									if(inv.getFromSalesOrder().equals(nc.getFromSalesOrder()) && nc.getUUIDReference() == null) {
										nc.setUUIDReference(uuid);
										nc.setStatus(AppConstants.STATUS_PENDING);
										invoiceDao.updateInvoice(nc);
									}
								}
								
								//Enviar a Portal de Distribuidores
								createDistPortalInvoice(inv);
							}
							break;
						case AppConstants.ORDER_TYPE_NC:
							inv.setUUID(uuid);
							inv.setErrorMsg(null);
							if(NullValidator.isNull(inv.getInvoiceRelationType()).equals("07")){
								inv.setStatus(AppConstants.STATUS_INVOICED);
							}else{
								inv.setStatus(AppConstants.STATUS_INVOICED);
							}
							invoiceDao.updateInvoice(inv);
							//Enviar a Portal de Distribuidores
							createDistPortalInvoice(inv);
							break;
						case AppConstants.ORDER_TYPE_ADV:
							List<Payments> advpay = new ArrayList<Payments>(inv.getPayments());
							if(advPay != null && !advpay.isEmpty()) {
								for(Payments pay: advpay) {
									pay.setUUID(uuid);
									pay.setPaymentError(null);
									pay.setPaymentStatus(AppConstants.STATUS_INVOICED);
									paymentsService.updatePayment(pay);
								}
							}
							inv.setUUID(uuid);
							inv.setErrorMsg(null);
							inv.setStatus(AppConstants.STATUS_FINISHED);
							invoiceDao.updateInvoice(inv);
							//Enviar a Portal de Distribuidores
							createDistPortalInvoice(inv);
							break;
						case AppConstants.ORDER_TYPE_TRANS:
							inv.setUUID(uuid);
							inv.setErrorMsg(null);
							inv.setStatus(AppConstants.STATUS_FINISHED);
							invoiceDao.updateInvoice(inv);
							break;								
					}
				}
			}
			for(Payments payS: updatePayAle) {
				Payments pay = new Payments();
				pay = payS;
				String fName = pay.getSerial() + pay.getFolio() + ".txt";
				log.warn(fName + " " + filePathPay );
				String uuid = null;
				uuid = this.getUuidAlejandro(fName, payS.getTaxIdentifier());
				if(uuid != null && !uuid.isEmpty()) {
					uuid = uuid.replaceAll("\"", "");
					pay.setUUID(uuid);
					pay.setPaymentError(null);
					pay.setPaymentStatus(AppConstants.STATUS_INVOICED);
					paymentsService.updatePayment(pay);
					
					PaymentsList pl = paymentsListService.getByReceiptNumber(pay.getReceiptNumber());
					if(pl == null) {
						//Enviar a Portal de Distribuidores
						if(AppConstants.PAYMENTS_CPAGO.equals(pay.getPaymentType())) {
							createDistPortalPayment(pay, pl, true);
						}							
					}
				}
			}
			List<String> sentPaymentsAle = new ArrayList<String>();
			List<PaymentsList> pllistAle = new ArrayList<PaymentsList>();
			pllistAle = paymentsListService.getAllPayList(AppConstants.STATUS_UPDUUID);
			
			for(PaymentsList pl: pllistAle) {
				for(Payments p: pl.getPayments()) {
					if(p.getUUID()!= null && !p.getUUID().isEmpty()) {
						pl.setUuid(p.getUUID());
						pl.setStatus(AppConstants.STATUS_FINISHED);
						paymentsListService.updatePaymentsList(pl);
						
						//Enviar a Portal de Distribuidores
						if(!sentPaymentsAle.contains(pl.getFolio())) {					
							Payments firstPayment = (Payments)pl.getPayments().toArray()[0];
							if(AppConstants.PAYMENTS_CPAGO.equals(firstPayment.getPaymentType())) {						
								createDistPortalPayment(firstPayment, pl, false);
								sentPaymentsAle.add(pl.getFolio());
							}
						}
						break;
					}
				}
			}
			//Proceso Tellez
			//Ruta donde estan guardados los archivos timbrados
			List<Udc> success = udcService.searchBySystem(AppConstantsUtil.RUTA_FILES);
			for(Udc u: success) {
				if(u.getStrValue1().equals(AppConstantsUtil.FILE_RESPONSE)) {
					filePathResponse = u.getUdcKey();
					filePathPay = u.getStrValue2();
				}
			}
			
			List<Invoice> updateInv = invoiceDao.getInvoiceListByStatusCode(AppConstants.STATUS_UPDUUID, "");
			List<Payments> updatePay = paymentsService.getPaymentsStatus(AppConstants.STATUS_UPDUUID);
			List<Invoice> updateNC = invoiceDao.getInvoiceListByStatusCode(AppConstants.STATUS_PENDING_UUID_NC, AppConstants.ORDER_TYPE_NC);
			List<Invoice> lastestInv = new ArrayList<Invoice>();
			List<String> arr = new ArrayList<String>();
			for(Invoice i: updateInv) {
				if(!arr.contains(i.getFolio())) {
					lastestInv.add(i);
					arr.add(i.getFolio());
				}
			}
			for(Invoice invD: lastestInv) {
				Invoice inv = new Invoice();
				inv = invD;
				log.error(inv.getId() + " " + inv.getFolio());
				String fName = inv.getSerial() + inv.getFolio();
				File file = new File(filePathResponse + fName + ".XML");
				String uuid = null;
				try {
					Reader fileReader = new FileReader(file);
					BufferedReader bufReader = new BufferedReader(fileReader); 
					StringBuilder sb = new StringBuilder(); 
					String line = bufReader.readLine(); 
					while( line != null){ 
						sb.append(line).append("\n"); 
						line = bufReader.readLine(); 
					}
					String data = "";
					data = sb.toString();
					
					JSONObject xmlJSONObj = new JSONObject();
					xmlJSONObj = XML.toJSONObject(data, true);
					JsonElement jelement = new JsonParser().parse(xmlJSONObj.toString());
					JsonObject jobject = jelement.getAsJsonObject();
					JsonElement soapEnvelope = jobject.get("cfdi:Comprobante").getAsJsonObject().get("cfdi:Complemento");
					JsonElement bdy = soapEnvelope.getAsJsonObject().get("tfd:TimbreFiscalDigital");
					if(bdy != null) {
						if(bdy instanceof JsonArray) {
							JsonArray jsonarray = bdy.getAsJsonArray();
							for (int i = 0; i < jsonarray.size(); i++) {
								JsonElement op = jsonarray.get(i).getAsJsonObject();
								uuid = String.valueOf(op.getAsJsonObject().get("UUID"));
							}
						}else {							
							uuid = String.valueOf(bdy.getAsJsonObject().get("UUID"));	
						}
					}
					String advPay = "";
					List<Udc> udcPay = udcService.searchBySystem(AppConstants.UDC_SYSTEM_RTYPE);
					for(Udc u: udcPay) {
						if(u.getStrValue1().equals(AppConstants.UDC_STRVALUE1_ADVANCE_PAYMENTS)) {
							advPay = u.getUdcKey();
							break;
						}
					}	
					if(uuid != null && !uuid.isEmpty()) {
						uuid = uuid.replaceAll("\"", "");
						String option =  inv.getInvoiceType();
						switch(option) {
							case AppConstants.ORDER_TYPE_FACTURA:
								log.warn(uuid + " " + inv.getFolio());
								if(inv.getInvoiceRelationType() == null || inv.getInvoiceRelationType().equals("04")){
									log.warn(uuid + " " + inv.getFolio());
									inv.setUUID(uuid);
									inv.setErrorMsg(null);
									inv.setStatus(AppConstants.STATUS_INVOICED);
									invoiceDao.updateInvoice(inv);
									
									//Enviar a Portal de Distribuidores
									createDistPortalInvoice(inv);
								}else if(inv.getInvoiceRelationType().equals(advPay)){
									inv.setUUID(uuid);
									inv.setErrorMsg(null);
									inv.setStatus(AppConstants.STATUS_INVOICED);
									//this.createAdvPayNC(inv);
									invoiceDao.updateInvoice(inv);
									
									//Actualiza NC generadas a partir de anticipos
									for(Invoice nc : updateNC) {
										if(inv.getFromSalesOrder().equals(nc.getFromSalesOrder()) && nc.getUUIDReference() == null) {
											nc.setUUIDReference(uuid);
											nc.setStatus(AppConstants.STATUS_PENDING);
											invoiceDao.updateInvoice(nc);
										}
									}
									
									//Enviar a Portal de Distribuidores
									createDistPortalInvoice(inv);
								}
								break;
							case AppConstants.ORDER_TYPE_NC:
								inv.setUUID(uuid);
								inv.setErrorMsg(null);
								if(NullValidator.isNull(inv.getInvoiceRelationType()).equals("07")){
									inv.setStatus(AppConstants.STATUS_INVOICED);
								}else{
									inv.setStatus(AppConstants.STATUS_INVOICED);
								}
								invoiceDao.updateInvoice(inv);
								//Enviar a Portal de Distribuidores
								createDistPortalInvoice(inv);
								break;
							case AppConstants.ORDER_TYPE_ADV:
								List<Payments> advpay = new ArrayList<Payments>(inv.getPayments());
								if(advPay != null && !advpay.isEmpty()) {
									for(Payments pay: advpay) {
										pay.setUUID(uuid);
										pay.setPaymentError(null);
										pay.setPaymentStatus(AppConstants.STATUS_INVOICED);
										paymentsService.updatePayment(pay);
									}
								}
								inv.setUUID(uuid);
								inv.setErrorMsg(null);
								inv.setStatus(AppConstants.STATUS_FINISHED);
								invoiceDao.updateInvoice(inv);
								//Enviar a Portal de Distribuidores
								createDistPortalInvoice(inv);
								break;
							case AppConstants.ORDER_TYPE_TRANS:
								inv.setUUID(uuid);
								inv.setErrorMsg(null);
								inv.setStatus(AppConstants.STATUS_FINISHED);
								invoiceDao.updateInvoice(inv);
								break;								
						}
					}
				}catch(Exception e) {
					log.info("NO SE AH ENCONTRADO NADA " + inv.getFolio() + e);
				}
			}
			
			for(Payments payS: updatePay) {
				Payments pay = new Payments();
				pay = payS;
				String fName = "P_" + pay.getSerial() + pay.getFolio();
				File file = new File(filePathPay + fName + ".XML");
				log.warn(fName + " " + filePathPay );
				String uuid = null;
				try {
					Reader fileReader = new FileReader(file);
					BufferedReader bufReader = new BufferedReader(fileReader); 
					StringBuilder sb = new StringBuilder(); 
					String line = bufReader.readLine(); 
					while( line != null){ 
						sb.append(line).append("\n"); 
						line = bufReader.readLine(); 
					}
					String data = sb.toString();
//					JSONObject xmlJSONObj = XML.toJSONObject(data, true);
					JSONObject xmlJSONObj = XML.toJSONObject(data);
					JsonElement jelement = new JsonParser().parse(xmlJSONObj.toString());
					JsonObject jobject = jelement.getAsJsonObject();
					JsonElement soapEnvelope = jobject.get("cfdi:Comprobante").getAsJsonObject().get("cfdi:Complemento");
					JsonElement bdy = soapEnvelope.getAsJsonObject().get("tfd:TimbreFiscalDigital");
					if(bdy != null) {
						if(bdy instanceof JsonArray) {
							JsonArray jsonarray = bdy.getAsJsonArray();
							for (int i = 0; i < jsonarray.size(); i++) {
								JsonElement op = jsonarray.get(i).getAsJsonObject();
								uuid = String.valueOf(op.getAsJsonObject().get("UUID"));
							}
						}else {							
							uuid = String.valueOf(bdy.getAsJsonObject().get("UUID"));	
						}
					}
					
					if(uuid != null && !uuid.isEmpty()) {
						uuid = uuid.replaceAll("\"", "");
						pay.setUUID(uuid);
						pay.setPaymentError(null);
						pay.setPaymentStatus(AppConstants.STATUS_INVOICED);
						paymentsService.updatePayment(pay);
						
						PaymentsList pl = paymentsListService.getByReceiptNumber(pay.getReceiptNumber());
						if(pl == null) {
							//Enviar a Portal de Distribuidores
							if(AppConstants.PAYMENTS_CPAGO.equals(pay.getPaymentType())) {
								createDistPortalPayment(pay, pl, true);
							}							
						}
					}					
				}catch(Exception e) {
					log.info("NO SE A ENCONTRADO NADA" + e);
				}
			}
			
			List<String> sentPayments = new ArrayList<String>();
			List<PaymentsList> pllist = new ArrayList<PaymentsList>();
			pllist = paymentsListService.getAllPayList(AppConstants.STATUS_UPDUUID);
			
			for(PaymentsList pl: pllist) {
				for(Payments p: pl.getPayments()) {
					if(p.getUUID()!= null && !p.getUUID().isEmpty()) {
						pl.setUuid(p.getUUID());
						pl.setStatus(AppConstants.STATUS_FINISHED);
						paymentsListService.updatePaymentsList(pl);
						
						//Enviar a Portal de Distribuidores
						if(!sentPayments.contains(pl.getFolio())) {					
							Payments firstPayment = (Payments)pl.getPayments().toArray()[0];
							if(AppConstants.PAYMENTS_CPAGO.equals(firstPayment.getPaymentType())) {						
								createDistPortalPayment(firstPayment, pl, false);
								sentPayments.add(pl.getFolio());
							}
						}
						break;
					}
				}
				
//				//Enviar a Portal de Distribuidores
//				if(!sentPayments.contains(pl.getFolio())) {					
//					Payments firstPayment = (Payments)pl.getPayments().toArray()[0];
//					if(AppConstants.PAYMENTS_CPAGO.equals(firstPayment.getPaymentType())) {						
//						createDistPortalPayment(firstPayment, pl, false);
//						sentPayments.add(pl.getFolio());
//					}
//				}
			}
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}*/
	}
	
	@SuppressWarnings("resource")
	public boolean procesoFernando() {
		String filePathResponse = "";
		String filePathPay = "";
		String fileName = "";
		String contentFile = "";
		String c = "";
		try{
			//Proceso Tellez
			//Ruta donde estan guardados los archivos timbrados
			List<Udc> success = udcService.searchBySystem(AppConstantsUtil.RUTA_FILES);
			for(Udc u: success) {
				if(u.getStrValue1().equals(AppConstantsUtil.FILE_RESPONSE)) {
					filePathResponse = u.getUdcKey();
					filePathPay = u.getStrValue2();
				}
			}
			
			List<Invoice> updateInv = invoiceDao.getInvoiceListByStatusCode(AppConstants.STATUS_UPDUUID, "");
			List<Payments> updatePay = paymentsService.getPaymentsStatus(AppConstants.STATUS_UPDUUID);
			List<Invoice> updateNC = invoiceDao.getInvoiceListByStatusCode(AppConstants.STATUS_PENDING_UUID_NC, AppConstants.ORDER_TYPE_NC);
			List<Invoice> lastestInv = new ArrayList<Invoice>();
			List<String> arr = new ArrayList<String>();
			for(Invoice i: updateInv) {
				if(!arr.contains(i.getFolio())) {
					lastestInv.add(i);
					arr.add(i.getFolio());
				}
			}
			for(Invoice invD: lastestInv) {
				Invoice inv = new Invoice();
				inv = invD;
				log.error(inv.getId() + " " + inv.getFolio());
				String fName = inv.getSerial() + inv.getFolio();
				File file = new File(filePathResponse + fName + ".XML");
				String uuid = null;
				try {
					Reader fileReader = new FileReader(file);
					BufferedReader bufReader = new BufferedReader(fileReader); 
					StringBuilder sb = new StringBuilder(); 
					String line = bufReader.readLine(); 
					while( line != null){ 
						sb.append(line).append("\n"); 
						line = bufReader.readLine(); 
					}
					String data = "";
					data = sb.toString();
					
					JSONObject xmlJSONObj = new JSONObject();
					xmlJSONObj = XML.toJSONObject(data, true);
					JsonElement jelement = new JsonParser().parse(xmlJSONObj.toString());
					JsonObject jobject = jelement.getAsJsonObject();
					JsonElement soapEnvelope = jobject.get("cfdi:Comprobante").getAsJsonObject().get("cfdi:Complemento");
					JsonElement bdy = soapEnvelope.getAsJsonObject().get("tfd:TimbreFiscalDigital");
					if(bdy != null) {
						if(bdy instanceof JsonArray) {
							JsonArray jsonarray = bdy.getAsJsonArray();
							for (int i = 0; i < jsonarray.size(); i++) {
								JsonElement op = jsonarray.get(i).getAsJsonObject();
								uuid = String.valueOf(op.getAsJsonObject().get("UUID"));
							}
						}else {							
							uuid = String.valueOf(bdy.getAsJsonObject().get("UUID"));	
						}
					}
					String advPay = "";
					List<Udc> udcPay = udcService.searchBySystem(AppConstants.UDC_SYSTEM_RTYPE);
					for(Udc u: udcPay) {
						if(u.getStrValue1().equals(AppConstants.UDC_STRVALUE1_ADVANCE_PAYMENTS)) {
							advPay = u.getUdcKey();
							break;
						}
					}	
					if(uuid != null && !uuid.isEmpty()) {
						uuid = uuid.replaceAll("\"", "");
						String option =  inv.getInvoiceType();
						switch(option) {
							case AppConstants.ORDER_TYPE_FACTURA:
								log.warn(uuid + " " + inv.getFolio());
								if(inv.getInvoiceRelationType() == null || inv.getInvoiceRelationType().equals("04")){
									log.warn(uuid + " " + inv.getFolio());
									inv.setUUID(uuid);
									inv.setErrorMsg(null);
									inv.setStatus(AppConstants.STATUS_INVOICED);
									invoiceDao.updateInvoice(inv);
									
									//Enviar a Portal de Distribuidores
									createDistPortalInvoice(inv);
								}else if(inv.getInvoiceRelationType().equals(advPay)){
									inv.setUUID(uuid);
									inv.setErrorMsg(null);
									inv.setStatus(AppConstants.STATUS_INVOICED);
									//this.createAdvPayNC(inv);
									invoiceDao.updateInvoice(inv);
									
									//Actualiza NC generadas a partir de anticipos
									for(Invoice nc : updateNC) {
										if(inv.getFromSalesOrder().equals(nc.getFromSalesOrder()) && nc.getUUIDReference() == null) {
											nc.setUUIDReference(uuid);
											nc.setStatus(AppConstants.STATUS_PENDING);
											invoiceDao.updateInvoice(nc);
										}
									}
									
									//Enviar a Portal de Distribuidores
									createDistPortalInvoice(inv);
								}
								break;
							case AppConstants.ORDER_TYPE_NC:
								inv.setUUID(uuid);
								inv.setErrorMsg(null);
								if(NullValidator.isNull(inv.getInvoiceRelationType()).equals("07")){
									inv.setStatus(AppConstants.STATUS_INVOICED);
								}else{
									inv.setStatus(AppConstants.STATUS_INVOICED);
								}
								invoiceDao.updateInvoice(inv);
								//Enviar a Portal de Distribuidores
								createDistPortalInvoice(inv);
								break;
							case AppConstants.ORDER_TYPE_ADV:
								List<Payments> advpay = new ArrayList<Payments>(inv.getPayments());
								if(advPay != null && !advpay.isEmpty()) {
									for(Payments pay: advpay) {
										pay.setUUID(uuid);
										pay.setPaymentError(null);
										pay.setPaymentStatus(AppConstants.STATUS_INVOICED);
										paymentsService.updatePayment(pay);
									}
								}
								inv.setUUID(uuid);
								inv.setErrorMsg(null);
								inv.setStatus(AppConstants.STATUS_FINISHED);
								invoiceDao.updateInvoice(inv);
								//Enviar a Portal de Distribuidores
								createDistPortalInvoice(inv);
								break;
							case AppConstants.ORDER_TYPE_TRANS:
								inv.setUUID(uuid);
								inv.setErrorMsg(null);
								inv.setStatus(AppConstants.STATUS_FINISHED);
								invoiceDao.updateInvoice(inv);
								break;								
						}
					}
				}catch(Exception e) {
					log.info("NO SE AH ENCONTRADO NADA " + inv.getFolio() + e);
				}
			}
			
			for(Payments payS: updatePay) {
				Payments pay = new Payments();
				pay = payS;
				String fName = "P_" + pay.getSerial() + pay.getFolio();
				File file = new File(filePathPay + fName + ".XML");
				log.warn(fName + " " + filePathPay );
				String uuid = null;
				try {
					Reader fileReader = new FileReader(file);
					BufferedReader bufReader = new BufferedReader(fileReader); 
					StringBuilder sb = new StringBuilder(); 
					String line = bufReader.readLine(); 
					while( line != null){ 
						sb.append(line).append("\n"); 
						line = bufReader.readLine(); 
					}
					String data = sb.toString();
//					JSONObject xmlJSONObj = XML.toJSONObject(data, true);
					JSONObject xmlJSONObj = XML.toJSONObject(data);
					JsonElement jelement = new JsonParser().parse(xmlJSONObj.toString());
					JsonObject jobject = jelement.getAsJsonObject();
					JsonElement soapEnvelope = jobject.get("cfdi:Comprobante").getAsJsonObject().get("cfdi:Complemento");
					JsonElement bdy = soapEnvelope.getAsJsonObject().get("tfd:TimbreFiscalDigital");
					if(bdy != null) {
						if(bdy instanceof JsonArray) {
							JsonArray jsonarray = bdy.getAsJsonArray();
							for (int i = 0; i < jsonarray.size(); i++) {
								JsonElement op = jsonarray.get(i).getAsJsonObject();
								uuid = String.valueOf(op.getAsJsonObject().get("UUID"));
							}
						}else {							
							uuid = String.valueOf(bdy.getAsJsonObject().get("UUID"));	
						}
					}
					
					if(uuid != null && !uuid.isEmpty()) {
						uuid = uuid.replaceAll("\"", "");
						pay.setUUID(uuid);
						pay.setPaymentError(null);
						pay.setPaymentStatus(AppConstants.STATUS_INVOICED);
						paymentsService.updatePayment(pay);
						
						PaymentsList pl = paymentsListService.getByReceiptNumber(pay.getReceiptNumber());
						if(pl == null) {
							//Enviar a Portal de Distribuidores
							if(AppConstants.PAYMENTS_CPAGO.equals(pay.getPaymentType())) {
								createDistPortalPayment(pay, pl, true);
							}							
						}
					}					
				}catch(Exception e) {
					log.info("NO SE A ENCONTRADO NADA" + e);
				}
			}
			
			List<String> sentPayments = new ArrayList<String>();
			List<PaymentsList> pllist = new ArrayList<PaymentsList>();
			pllist = paymentsListService.getAllPayList(AppConstants.STATUS_UPDUUID);
			
			for(PaymentsList pl: pllist) {
				for(Payments p: pl.getPayments()) {
					if(p.getUUID()!= null && !p.getUUID().isEmpty()) {
						pl.setUuid(p.getUUID());
						pl.setStatus(AppConstants.STATUS_FINISHED);
						paymentsListService.updatePaymentsList(pl);
						
						//Enviar a Portal de Distribuidores
						if(!sentPayments.contains(pl.getFolio())) {					
							Payments firstPayment = (Payments)pl.getPayments().toArray()[0];
							if(AppConstants.PAYMENTS_CPAGO.equals(firstPayment.getPaymentType())) {						
								createDistPortalPayment(firstPayment, pl, false);
								sentPayments.add(pl.getFolio());
							}
						}
						break;
					}
				}
				
//				//Enviar a Portal de Distribuidores
//				if(!sentPayments.contains(pl.getFolio())) {					
//					Payments firstPayment = (Payments)pl.getPayments().toArray()[0];
//					if(AppConstants.PAYMENTS_CPAGO.equals(firstPayment.getPaymentType())) {						
//						createDistPortalPayment(firstPayment, pl, false);
//						sentPayments.add(pl.getFolio());
//					}
//				}
			}
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean procesoAlejandro() {
		try{
			//Proceso Alejandro
			List<Invoice> updateInvAle = invoiceDao.getInvoiceListByStatusCode(AppConstants.STATUS_UPDUUID, "");
			List<Payments> updatePayAle = paymentsService.getPaymentsStatus(AppConstants.STATUS_UPDUUID);
			List<Invoice> updateNCAle = invoiceDao.getInvoiceListByStatusCode(AppConstants.STATUS_PENDING_UUID_NC, AppConstants.ORDER_TYPE_NC);
			List<Invoice> lastestInvAle = new ArrayList<Invoice>();
			List<String> arrAle = new ArrayList<String>();
			for(Invoice i: updateInvAle) {
				if(!arrAle.contains(i.getFolio())) {
					lastestInvAle.add(i);
					arrAle.add(i.getFolio());
				}
			}
			for(Invoice invD: lastestInvAle) {
				Invoice inv = new Invoice();
				inv = invD;
				log.info(inv.getId() + " " + inv.getFolio());
				String fName = inv.getSerial() + inv.getFolio();
				String uuid = null;
				//Obtener UUID
				uuid = this.getUuidAlejandro(fName, inv.getCustomerTaxIdentifier());
				//Obtener UUID
				String advPay = "";
				List<Udc> udcPay = udcService.searchBySystem(AppConstants.UDC_SYSTEM_RTYPE);
				for(Udc u: udcPay) {
					if(u.getStrValue1().equals(AppConstants.UDC_STRVALUE1_ADVANCE_PAYMENTS)) {
						advPay = u.getUdcKey();
						break;
					}
				}
				if(uuid != null && !uuid.isEmpty()) {
					uuid = uuid.replaceAll("\"", "");
					String option =  inv.getInvoiceType();
					switch(option) {
						case AppConstants.ORDER_TYPE_FACTURA:
							log.warn(uuid + " " + inv.getFolio());
							if(inv.getInvoiceRelationType() == null || inv.getInvoiceRelationType().equals("04")){
								log.warn(uuid + " " + inv.getFolio());
								inv.setUUID(uuid);
								inv.setErrorMsg(null);
								inv.setStatus(AppConstants.STATUS_INVOICED);
								invoiceDao.updateInvoice(inv);
								
								//Enviar a Portal de Distribuidores
								createDistPortalInvoice(inv);
							}else if(inv.getInvoiceRelationType().equals(advPay)){
								inv.setUUID(uuid);
								inv.setErrorMsg(null);
								inv.setStatus(AppConstants.STATUS_INVOICED);
								//this.createAdvPayNC(inv);
								invoiceDao.updateInvoice(inv);
								
								//Actualiza NC generadas a partir de anticipos
								for(Invoice nc : updateNCAle) {
									if(inv.getFromSalesOrder().equals(nc.getFromSalesOrder()) && nc.getUUIDReference() == null) {
										nc.setUUIDReference(uuid);
										nc.setStatus(AppConstants.STATUS_PENDING);
										invoiceDao.updateInvoice(nc);
									}
								}
								
								//Enviar a Portal de Distribuidores
								createDistPortalInvoice(inv);
							}else if(inv.getInvoiceRelationType().contains(advPay)){
								inv.setUUID(uuid);
								inv.setErrorMsg(null);
								inv.setStatus(AppConstants.STATUS_INVOICED);
								//this.createAdvPayNC(inv);
								invoiceDao.updateInvoice(inv);
								
								//Actualiza NC generadas a partir de anticipos
								for(Invoice nc : updateNCAle) {
									if(inv.getFromSalesOrder().equals(nc.getFromSalesOrder()) && nc.getUUIDReference() == null) {
										nc.setUUIDReference(uuid);
										nc.setStatus(AppConstants.STATUS_PENDING);
										invoiceDao.updateInvoice(nc);
									}
								}
								
								//Enviar a Portal de Distribuidores
								createDistPortalInvoice(inv);
								
							}else {
								log.warn(uuid + " " + inv.getFolio());
								inv.setUUID(uuid);
								inv.setErrorMsg(null);
								inv.setStatus(AppConstants.STATUS_INVOICED);
								invoiceDao.updateInvoice(inv);
								
								//Enviar a Portal de Distribuidores
								createDistPortalInvoice(inv);
							}
							break;
						case AppConstants.ORDER_TYPE_NC:
							inv.setUUID(uuid);
							inv.setErrorMsg(null);
							if(NullValidator.isNull(inv.getInvoiceRelationType()).equals("07")){
								inv.setStatus(AppConstants.STATUS_INVOICED);
							}else{
								inv.setStatus(AppConstants.STATUS_INVOICED);
							}
							invoiceDao.updateInvoice(inv);
							//Enviar a Portal de Distribuidores
							createDistPortalInvoice(inv);
							break;
						case AppConstants.ORDER_TYPE_ADV:
							List<Payments> advpay = new ArrayList<Payments>(inv.getPayments());
							if(advPay != null && !advpay.isEmpty()) {
								for(Payments pay: advpay) {
									pay.setUUID(uuid);
									pay.setPaymentError(null);
									pay.setPaymentStatus(AppConstants.STATUS_INVOICED);
									paymentsService.updatePayment(pay);
								}
							}
							inv.setUUID(uuid);
							inv.setErrorMsg(null);
							inv.setStatus(AppConstants.STATUS_FINISHED);
							invoiceDao.updateInvoice(inv);
							//Enviar a Portal de Distribuidores
							createDistPortalInvoice(inv);
							break;
						case AppConstants.ORDER_TYPE_TRANS:
							inv.setUUID(uuid);
							inv.setErrorMsg(null);
							inv.setStatus(AppConstants.STATUS_FINISHED);
							invoiceDao.updateInvoice(inv);
							break;								
					}
				}
			}
			for(Payments payS: updatePayAle) {
				Payments pay = new Payments();
				pay = payS;
				String fName = pay.getSerial() + pay.getFolio();
				//log.warn(fName + " " + filePathPay );
				String uuid = null;
				uuid = this.getUuidAlejandro(fName, payS.getTaxIdentifier());
				if(uuid != null && !uuid.isEmpty()) {
					uuid = uuid.replaceAll("\"", "");
					pay.setUUID(uuid);
					pay.setPaymentError(null);
					pay.setPaymentStatus(AppConstants.STATUS_INVOICED);
					paymentsService.updatePayment(pay);
					
					PaymentsList pl = paymentsListService.getByReceiptNumber(pay.getReceiptNumber());
					if(pl == null) {
						//Enviar a Portal de Distribuidores
						if(AppConstants.PAYMENTS_CPAGO.equals(pay.getPaymentType())) {
							createDistPortalPayment(pay, pl, true);
						}							
					}
				}
			}
			List<String> sentPaymentsAle = new ArrayList<String>();
			List<PaymentsList> pllistAle = new ArrayList<PaymentsList>();
			pllistAle = paymentsListService.getAllPayList(AppConstants.STATUS_UPDUUID);
			
			for(PaymentsList pl: pllistAle) {
				for(Payments p: pl.getPayments()) {
					if(p.getUUID()!= null && !p.getUUID().isEmpty()) {
						pl.setUuid(p.getUUID());
						pl.setStatus(AppConstants.STATUS_FINISHED);
						paymentsListService.updatePaymentsList(pl);
						
						//Enviar a Portal de Distribuidores
						if(!sentPaymentsAle.contains(pl.getFolio())) {					
							Payments firstPayment = (Payments)pl.getPayments().toArray()[0];
							if(AppConstants.PAYMENTS_CPAGO.equals(firstPayment.getPaymentType())) {						
								createDistPortalPayment(firstPayment, pl, false);
								sentPaymentsAle.add(pl.getFolio());
							}
						}
						break;
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
	@Override
	public boolean createDistPortalInvoice(Invoice inv) {
		try {
			
			String invoiceType;
			
			if(AppConstants.ORDER_TYPE_ADV.equals(inv.getInvoiceType())) {
				invoiceType = AppConstants.DIST_ORDER_TYPE_ANTICIPO;
			} else {
				invoiceType = inv.getInvoiceType();
			}
			
			InvoiceJSON invoiceJSON = new InvoiceJSON();
			invoiceJSON.setBranch(inv.getBranch().getName());
			invoiceJSON.setCompany(inv.getCompany().getName());
			invoiceJSON.setCustomerName(inv.getCustomerName());
			invoiceJSON.setCustomerTaxId(inv.getCustomerTaxIdentifier());
			invoiceJSON.setFolio(inv.getFolio());
			invoiceJSON.setInvoiceCurrency(inv.getInvoiceCurrency());
			invoiceJSON.setInvoiceSubTotal(inv.getInvoiceSubTotal());
			invoiceJSON.setInvoiceTaxAmount(inv.getInvoiceTaxAmount());
			invoiceJSON.setInvoiceTotal(inv.getInvoiceTotal());
			invoiceJSON.setInvoiceType(invoiceType);
			invoiceJSON.setSalesOrder(inv.getFromSalesOrder());
			invoiceJSON.setSerial(inv.getSerial());
			invoiceJSON.setStampDate(df.format(new Date()));
			invoiceJSON.setUuid(inv.getUUID());
			
			String portalUsr = "";
			String portalPwd = "";
			List<Udc> udcCredentials = udcService.searchBySystem(AppConstants.UDC_SYSTEM_PORTALDIST);
			
			for(Udc udc : udcCredentials) {
				if(AppConstants.UDC_KEY_PORTALDIST_USER.equals(udc.getUdcKey())) {
					portalUsr = udc.getStrValue1();
				}
				if(AppConstants.UDC_KEY_PORTALDIST_PWD.equals(udc.getUdcKey())) {
					portalPwd = udc.getStrValue1();
				}
			}
			
			String url = AppConstants.URL_ENDPOINT_INVOICE;
			List<HeadersRestDTO> headers = new ArrayList<HeadersRestDTO>();
			headers.add(new HeadersRestDTO("Content-Type", "application/vnd.oracle.adf.resourceitem+json"));
			
			Map<String, Object> response = hTTPRequestDistribuitorsService.httpRESTRequest(portalUsr, portalPwd,
					url, HttpMethod.POST, headers, null, invoiceJSON);

			int statusCode;
			InvoiceJSON responseRest;
			
			if(response != null) {
				statusCode = (int) response.get("code");
				responseRest = (InvoiceJSON) response.get("response");
				if(statusCode >= 200 && statusCode < 300) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@SuppressWarnings("unused")
	@Override
	public boolean createDistPortalPayment(Payments pay, PaymentsList payList, boolean isUniquePayment) {
		try {		
			double paymentAmount;
			
			if(isUniquePayment) {
				paymentAmount = Double.valueOf(pay.getPaymentAmount()).doubleValue(); 
			} else {
				paymentAmount = Double.valueOf(payList.getPaymentAmount()).doubleValue(); 
			}
			
			InvoiceJSON invoiceJSON = new InvoiceJSON();
			invoiceJSON.setBranch(pay.getBranch().getName());
			invoiceJSON.setCompany(pay.getCompany().getName());
			invoiceJSON.setCustomerName(pay.getCustomerName());
			invoiceJSON.setCustomerTaxId(pay.getTaxIdentifier());
			invoiceJSON.setFolio(pay.getFolio());
			invoiceJSON.setInvoiceCurrency(pay.getCurrency());
			invoiceJSON.setInvoiceSubTotal(paymentAmount);
			invoiceJSON.setInvoiceTaxAmount(0);
			invoiceJSON.setInvoiceTotal(paymentAmount);
			invoiceJSON.setInvoiceType(AppConstants.PAYMENTS_CPAGO);
			invoiceJSON.setSalesOrder("");
			invoiceJSON.setSerial(pay.getSerial());
			invoiceJSON.setStampDate(df.format(new Date()));
			invoiceJSON.setUuid(pay.getUUID());
			
			String portalUsr = "";
			String portalPwd = "";
			List<Udc> udcCredentials = udcService.searchBySystem(AppConstants.UDC_SYSTEM_PORTALDIST);
			
			for(Udc udc : udcCredentials) {
				if(AppConstants.UDC_KEY_PORTALDIST_USER.equals(udc.getUdcKey())) {
					portalUsr = udc.getStrValue1();
				}
				if(AppConstants.UDC_KEY_PORTALDIST_PWD.equals(udc.getUdcKey())) {
					portalPwd = udc.getStrValue1();
				}
			}
			
			//Búsqueda de información
//			String urlSearch = AppConstants.URL_ENDPOINT_INVOICE;
//			List<HeadersRestDTO> headersSearch = new ArrayList<HeadersRestDTO>();
//			headersSearch.add(new HeadersRestDTO("Content-Type", "application/vnd.oracle.adf.resourceitem+json"));
//			
//			List<ParamsRestDTO> params = new ArrayList<ParamsRestDTO>();
//			params.add(new ParamsRestDTO("q", "invoiceType like '" + AppConstants.PAYMENTS_CPAGO + "' and serial like '" + invoiceJSON.getSerial() +"' and folio like '" + invoiceJSON.getFolio() + "'"));
//			params.add(new ParamsRestDTO("expand", "totals"));
//			params.add(new ParamsRestDTO("onlyData", true));
//			
//			Map<String, Object> responseSearch = hTTPRequestDistribuitorsService.httpRESTRequest(portalUsr, portalPwd,
//					urlSearch, HttpMethod.GET, headersSearch, params, null);
			
			//Inserción de datos
			String url = AppConstants.URL_ENDPOINT_INVOICE;
			List<HeadersRestDTO> headers = new ArrayList<HeadersRestDTO>();
			headers.add(new HeadersRestDTO("Content-Type", "application/vnd.oracle.adf.resourceitem+json"));
			
			Map<String, Object> response = hTTPRequestDistribuitorsService.httpRESTRequest(portalUsr, portalPwd,
					url, HttpMethod.POST, headers, null, invoiceJSON);

			int statusCode;
			InvoiceJSON responseRest;
			
			if(response != null) {
				statusCode = (int) response.get("code");
				responseRest = (InvoiceJSON) response.get("response");
				if(statusCode >= 200 && statusCode < 300) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@SuppressWarnings("unused")
	@Override
	public boolean createPaymentsFile(Payments i) {
		String fileRuta = "";
		String fileName = "";
		String content = "";		
		String rType ="";
		String country = "";
		String rfcNac = "";
		String rfcExt = "";
		String pagos16Base = "0";
		String pagos16Iva = "0";
		String pagos8Base = "0";
		String pagos8Iva = "0";
		String pagos0Base = "0";
		String pagos0Iva = "0";
		String pagosExento = "0";
		double valorBase = 0;
		String baseTotal = "0";
		String ivaTotal = "0";
		String tasa = "";
		String catExport = i.getCatExportacion();
		try {
			i = reviewData(i);
			//Obtener ruta para dejar los archivos
			List<Udc> u = udcService.searchBySystem(AppConstantsUtil.RUTA_FILES);
			for(Udc ud: u) {
				if(ud.getStrValue1().equals(AppConstantsUtil.RUTA_FILES_STAMPED)) {
					fileRuta = ud.getStrValue2();
				}
			}			
			List<Udc> uList = udcService.searchBySystem(AppConstants.UDC_SYSTEM_RTYPE);
			for(Udc ud: uList) {
				if(ud.getStrValue1().equals(AppConstants.UDC_STRVALUE1_CPAGOS)) {
					rType = ud.getUdcKey();
					break;
				}
			}
			if(i.getCountry().equals(AppConstantsUtil.COUNTRY_DEFAULT)) {
				country = i.getCountry();
				rfcNac = i.getTaxIdentifier();
			}else {
				List<Udc> rfcList = udcService.searchBySystem(AppConstants.UDC_SYSTEM_RFC_EXT);
				for(Udc rf: rfcList) {
					if(rf.getStrValue1().equals("EXTRANJERO")) {
						rfcNac = rf.getUdcKey();
					}
				}
				country = i.getCountry();
				rfcExt = i.getTaxIdentifier();
			}
			if(i.getCatExportacion() == null) {
				catExport = "01";
			}
			//Metodo para obtener el regimen fiscal
			if(i.getCustomerTaxRegime().length() == 0) {
				String regimenFiscal = soapService.getRegimenFiscal(i.getPartyNumber());
				i.setCustomerTaxRegime(regimenFiscal);
			}				
//			float erateBase = Math.round(Double.parseDouble(i.getExchangeRate())*100.00);
//			valorBase = Math.round((Math.round(Double.parseDouble(i.getPaymentAmount())*100.00)/100.00) * (Math.round(Double.parseDouble(i.getExchangeRate())*100.00)/100.00)*100.00)/100.00;
			valorBase = Math.round((Math.round(Double.parseDouble(i.getPaymentAmount())*100.00)/100.00) * (Double.parseDouble(i.getExchangeRate()))*100.00)/100.00;
			if(i.getTaxCode().equals(AppConstants.COMP_PAGOS_FAC_16)) {
//				pagos16Base = String.valueOf(Math.round((valorBase/1.16)*100.00)/100.00);
//				pagos16Iva = String.valueOf(Math.round((valorBase - (Double.parseDouble(pagos16Base)))*100.00)/100.00);
//				double balue = Math.round((i.getBase() * (Math.round(Double.parseDouble(i.getExchangeRate())*100.00)/100.00)*100.00))/100.00;
				pagos16Base = String.valueOf(Math.round((i.getBase() * (Double.parseDouble(i.getExchangeRate()))*100.00))/100.00);
				pagos16Iva = String.valueOf(Math.round((i.getTaxAmount() * (Double.parseDouble(i.getExchangeRate()))*100.00))/100.00); 
				baseTotal = String.valueOf((Math.round(i.getBase()*100.00))/100.00);
				ivaTotal = String.valueOf(Math.round(i.getTaxAmount()*100.00)/100.00);
				tasa = "0.160000";
			}else if(i.getTaxCode().equals(AppConstants.COMP_PAGOS_FAC_8)) {
				pagos8Base = String.valueOf(Math.round((valorBase/1.08)*100.00)/100.00);
				pagos8Iva = String.valueOf(Math.round((valorBase - (Double.parseDouble(pagos8Base)))*100.00)/100.00);
				baseTotal = String.valueOf((Math.round(i.getBase()*100.00))/100.00);
				ivaTotal = String.valueOf(Math.round(i.getTaxAmount()*100.00)/100.00);
				tasa = "0.080000";
			}else if(i.getTaxCode().equals(AppConstants.COMP_PAGOS_FAC_0)) {
				pagos0Base = String.valueOf(valorBase);
				pagos0Iva = "0";
				baseTotal = String.valueOf((Math.round(i.getBase()*100.00))/100.00);
				ivaTotal = "0";
				tasa = "0.000000";
			}else if(i.getTaxCode().equals(AppConstants.COMP_PAGOS_FAC_EXENTO)) {
				pagosExento = String.valueOf(valorBase);
			}
			
			content = AppConstantsUtil.PAYMENT_HEADER + AppConstantsUtil.FILES_SEPARATOR +
					i.getSerial() + AppConstantsUtil.FILES_SEPARATOR +
					i.getFolio() + AppConstantsUtil.FILES_SEPARATOR +
					dateFormat.format(new Date()) + AppConstantsUtil.FILES_SEPARATOR +
					i.getBranch().getZip() + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(i.getRelationType()) + AppConstantsUtil.FILES_SEPARATOR +//CFDI tipo relacion
					NullValidator.isNull(i.getRelationTypeUUID()) + AppConstantsUtil.FILES_SEPARATOR +//UUID Relacion
					i.getCompany().getTaxIdentifier() + AppConstantsUtil.FILES_SEPARATOR +
					i.getCompany().getBusinessUnitName() + AppConstantsUtil.FILES_SEPARATOR +
					i.getCompany().getTaxRegime() + AppConstantsUtil.FILES_SEPARATOR +
					rfcNac + AppConstantsUtil.FILES_SEPARATOR +
					i.getCustomerName() + AppConstantsUtil.FILES_SEPARATOR +
					country + AppConstantsUtil.FILES_SEPARATOR +
					rfcExt + AppConstantsUtil.FILES_SEPARATOR +//RFC Extranjero
					i.getPartyNumber() + AppConstantsUtil.FILES_SEPARATOR +
					i.getCustomerEmail() + AppConstantsUtil.FILES_SEPARATOR +	
					catExport + AppConstantsUtil.FILES_SEPARATOR + //Pagos 2.0 Exportacion
//					i.getCatExportacion()  + AppConstantsUtil.FILES_SEPARATOR + //Pagos 2.0 Exportacion
					i.getCustomerTaxRegime()  + AppConstantsUtil.FILES_SEPARATOR + //Pagos 2.0 Regimen Fiscal
					i.getCustomerZipCode() + AppConstantsUtil.FILES_SEPARATOR + //Pagos 2.0 Domicilio Fiscal
					"0"  + AppConstantsUtil.FILES_SEPARATOR + //Pagos 2.0 TotalRetencionesIVA Importe MXN
					"0"  + AppConstantsUtil.FILES_SEPARATOR + //Pagos 2.0 TotalRetencionesISR Importe MXN
					"0"  + AppConstantsUtil.FILES_SEPARATOR + //Pagos 2.0 TotalRetencionesIEPS Importe MXN
					pagos16Base  + AppConstantsUtil.FILES_SEPARATOR + //Pagos 2.0 TotalTrasladosBaseIVA16 Importe MXN
					pagos16Iva  + AppConstantsUtil.FILES_SEPARATOR + //Pagos 2.0 TotalTrasladosImpuestoIVA16 Importe MXN
					pagos8Base  + AppConstantsUtil.FILES_SEPARATOR + //Pagos 2.0 TotalTrasladosBaseIVA8 Importe MXN
					pagos8Iva  + AppConstantsUtil.FILES_SEPARATOR + //Pagos 2.0 TotalTrasladosImpuestoIVA8 Importe MXN
					pagos0Base  + AppConstantsUtil.FILES_SEPARATOR + //Pagos 2.0 TotalTrasladosBaseIVA0 Importe MXN
					pagos0Iva  + AppConstantsUtil.FILES_SEPARATOR + //Pagos 2.0 TotalTrasladosImpuestoIVA0 Importe MXN
					pagosExento  + AppConstantsUtil.FILES_SEPARATOR + //Pagos 2.0 TotalTrasladosBaseIVAExento Importe MXN
					valorBase  + AppConstantsUtil.FILES_SEPARATOR + //Pagos 2.0 MontoTotalPagos Importe MXN
					"\r\n"+
					AppConstantsUtil.PAYMENT_PAYMENT + AppConstantsUtil.FILES_SEPARATOR +
					i.getCreationDate() + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(i.getPaymentForm()) + AppConstantsUtil.FILES_SEPARATOR +//Forma de pago
					i.getCurrency() + AppConstantsUtil.FILES_SEPARATOR +
					i.getExchangeRate() + AppConstantsUtil.FILES_SEPARATOR +
					i.getPaymentAmount() + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +//Transaction Reference
					NullValidator.isNull(i.getBankReference()) + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(i.getAcountBankTaxIdentifier()) + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(i.getPayerAccount()) + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(i.getBeneficiaryAccount()) + AppConstantsUtil.FILES_SEPARATOR +
//					NullValidator.isNull(i.getBenBankAccTaxIden()) + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(i.getReceiptNumber()) + AppConstantsUtil.FILES_SEPARATOR +					
					"\r\n"+// Pagos 2.0
					AppConstantsUtil.PAYMENT_PAYMENT_M + AppConstantsUtil.FILES_SEPARATOR +
					baseTotal + AppConstantsUtil.FILES_SEPARATOR +
					AppConstantsUtil.TAX_CODE + AppConstantsUtil.FILES_SEPARATOR +
					AppConstantsUtil.TAX_CODE_NAME + AppConstantsUtil.FILES_SEPARATOR +
					tasa + AppConstantsUtil.FILES_SEPARATOR +
					ivaTotal + AppConstantsUtil.FILES_SEPARATOR +
					"\r\n"+
					AppConstantsUtil.PAYMENT_PAYMENT_N + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					"\r\n"+// Pagos 2.0
					AppConstantsUtil.PAYMENT_DETAILS + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(i.getUuidReference()) + AppConstantsUtil.FILES_SEPARATOR +
					i.getSerialRel() + AppConstantsUtil.FILES_SEPARATOR +
					i.getFolioRel() + AppConstantsUtil.FILES_SEPARATOR +
					i.getCurrency() + AppConstantsUtil.FILES_SEPARATOR +
					i.getExchangeRate() + AppConstantsUtil.FILES_SEPARATOR +
					i.getPaymentMethod() + AppConstantsUtil.FILES_SEPARATOR +//Método de pago PUE
					i.getPaymentNumber() + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(i.getPreviousBalanceAmount()) + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(i.getPaymentAmount()) + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(i.getRemainingBalanceAmount()) + AppConstantsUtil.FILES_SEPARATOR +
					"02" + AppConstantsUtil.FILES_SEPARATOR +
					"\r\n"+//Pagos 2.0
					AppConstantsUtil.PAYMENT_DETAILS_Y + AppConstantsUtil.FILES_SEPARATOR +
					baseTotal + AppConstantsUtil.FILES_SEPARATOR +
					AppConstantsUtil.TAX_CODE + AppConstantsUtil.FILES_SEPARATOR +
					AppConstantsUtil.TAX_CODE_NAME + AppConstantsUtil.FILES_SEPARATOR +
					tasa + AppConstantsUtil.FILES_SEPARATOR +
					ivaTotal + AppConstantsUtil.FILES_SEPARATOR +
					"\r\n"+
					AppConstantsUtil.PAYMENT_DETAILS_X + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					"\r\n";//Pagos 2.0
			//Nombrar archivo
			fileName = NullValidator.isNull(i.getSerial()) + i.getFolio();
			//Crear archivo en la ruta deseada
			File file = new File(fileRuta + fileName + AppConstantsUtil.RUTA_FILES_EXTENSION);
			if (!file.exists()) {
             	file.createNewFile();
            }
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
            out.write(content);
            out.close();
            i.setPaymentStatus(AppConstants.STATUS_UPDUUID);
            paymentsService.updatePayment(i);
            
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public Payments reviewData(Payments Pay) {
		Double remaining = Double.parseDouble(Pay.getRemainingBalanceAmount());
		Double amount = Double.parseDouble(Pay.getPaymentAmount());
		if(remaining <= 0) {
			Pay.setRemainingBalanceAmount("0.0");
			Pay.setPreviousBalanceAmount(Pay.getPaymentAmount());			
		}else if (remaining > 0) {
			Pay.setPreviousBalanceAmount(String.valueOf(remaining + amount));
		}
		return Pay;
	}
	
	public boolean createAdvPayNC(Invoice invoice) {
		Invoice newInv = new Invoice();
		try {
			if(invoice.getUUIDReference().contains(",")) {
				String[ ] invoicesRef = invoice.getUUIDReference().split(",");
				for(String iR: invoicesRef) {
					Invoice i = invoiceDao.getInvoiceByUuid(iR);
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
						
						newInv.setInvoiceTotal(Double.parseDouble(numberFormat.format(i.getInvoiceTotal())));
						newInv.setInvoiceSubTotal(Double.parseDouble(numberFormat.format(i.getInvoiceSubTotal())));
						newInv.setInvoiceTaxAmount(Double.parseDouble(numberFormat.format(i.getInvoiceTaxAmount())));
						newInv.setInvoiceDiscount(0);
						
						newInv.setSetName(invoice.getSetName());
						newInv.setPaymentTerms("CONTADO");
						newInv.setFolio(String.valueOf(nNumber.getFolio()));
						newInv.setSerial(nNumber.getSerie());
						newInv.setStatus(AppConstants.STATUS_PENDING);
						newInv.setInvoice(false);
						newInv.setInvoiceType(AppConstants.ORDER_TYPE_NC);
						newInv.setFromSalesOrder(null);
						newInv.setInvoiceCurrency(invoice.getInvoiceCurrency());
						newInv.setInvoiceExchangeRate(invoice.getInvoiceExchangeRate());
						newInv.setOrderSource(NullValidator.isNull(invoice.getOrderSource()));
						newInv.setOrderType(AppConstants.ORDER_TYPE_NC);
						newInv.setProductType("");
						newInv.setExtCom(invoice.isExtCom());
						newInv.setPaymentMethod(AppConstants.PAY_METHOD);
						newInv.setPaymentType(creditNote.getDescription());
						
						for(InvoiceDetails idetails: i.getInvoiceDetails()) {
							InvoiceDetails iD = new InvoiceDetails();
							iD.setIsInvoiceLine("D");
							iD.setItemNumber("");
							iD.setItemDescription(creditNote.getStrValue1());
							iD.setUnitProdServ(String.valueOf(creditNote.getIntValue()));
							iD.setUnitPrice(Double.parseDouble(numberFormat.format(idetails.getUnitPrice())));
							iD.setTotalTaxAmount(Double.parseDouble(numberFormat.format(idetails.getTotalTaxAmount())));
							iD.setTotalAmount(Double.parseDouble(numberFormat.format(idetails.getTotalAmount())));
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
						}
						
						if(!invoiceDao.saveInvoice(newInv)){
							log.error("ERROR AL CREAR LA NOTA DE CREDITO RELACIONADA CON LA ORDEN: " + invoice.getFromSalesOrder()
							+ " Y EL UUID CORRESPONDIENTE: " + invoice.getUUID());
						}
						
					} else {
						log.error("ERROR AL CREAR LA NOTA DE CREDITO RELACIONADA CON LA ORDEN: " + invoice.getFromSalesOrder()
								+ " Y EL UUID CORRESPONDIENTE: " + invoice.getUUID());
					}
				}
			}else {
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
					
					newInv.setCFDIUse(invoice.getCFDIUse());
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
					newInv.setPaymentTerms("CONTADO");
					newInv.setFolio(String.valueOf(nNumber.getFolio()));
					newInv.setSerial(nNumber.getSerie());
					newInv.setStatus(AppConstants.STATUS_PENDING);
					newInv.setInvoice(false);
					newInv.setInvoiceType(AppConstants.ORDER_TYPE_NC);
					newInv.setFromSalesOrder(null);
					newInv.setInvoiceCurrency(invoice.getInvoiceCurrency());
					newInv.setInvoiceExchangeRate(invoice.getInvoiceExchangeRate());
					newInv.setOrderSource(NullValidator.isNull(invoice.getOrderSource()));
					newInv.setOrderType(AppConstants.ORDER_TYPE_NC);
					newInv.setProductType("");
					newInv.setExtCom(invoice.isExtCom());
					newInv.setPaymentMethod(AppConstants.PAY_METHOD);
					newInv.setPaymentType(creditNote.getDescription());
					
					for(InvoiceDetails idetails: i.getInvoiceDetails()) {
						InvoiceDetails iD = new InvoiceDetails();
						iD.setIsInvoiceLine("D");
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
					}
					
					if(!invoiceDao.saveInvoice(newInv)){
						log.error("ERROR AL CREAR LA NOTA DE CREDITO RELACIONADA CON LA ORDEN: " + invoice.getFromSalesOrder()
						+ " Y EL UUID CORRESPONDIENTE: " + invoice.getUUID());
					}
					
				} else {
					log.error("ERROR AL CREAR LA NOTA DE CREDITO RELACIONADA CON LA ORDEN: " + invoice.getFromSalesOrder()
							+ " Y EL UUID CORRESPONDIENTE: " + invoice.getUUID());
				}
			}			
			return true;
		}catch(Exception e) {
			log.error("ERROR AL CREAR LA NOTA DE CREDITO RELACIONADA CON LA ORDEN: " + invoice.getFromSalesOrder() + " Y EL UUID CORRESPONDIENTE: " + invoice.getUUID());
			return false;
		}
	}

	@SuppressWarnings("unused")
	@Override
	public boolean creatPaymentListFile(List<PaymentsList> pl) {
		String fileRuta = "";		String fileName = "";			String content = "";		String country = "";
		String rfc = "";			String pagos16Base = "";		String pagos16Iva = "";		String pagos8Base = "";
		String pagos8Iva = "";		String pagos0Base = "";			String pagos0Iva = "";		String pagosExento = "";
		double valorBase = 0;		String baseTotal16 = "";		String ivaTotal16 = "";		String tasa16 = "";
		String baseTotal08 = "";	String ivaTotal08 = "";			String tasa08 = "";			String baseTotal00 = "";
		String ivaTotal00 = "";		String tasa00 = "";				String baseTotal = "";		String ivaTotal = "";
		String tasa = "";			boolean changeCurrency = false;	String currency = "";		String excRate = "";
		String catExport = "";		String regFiscal = "";
		try {
			//Obtener ruta para dejar los archivos
			List<Udc> u = udcService.searchBySystem(AppConstantsUtil.RUTA_FILES);
			for(Udc ud: u) {
				if(ud.getStrValue1().equals(AppConstantsUtil.RUTA_FILES_STAMPED)) {
					fileRuta = ud.getStrValue2();
					break;
				}
			}
						
			for(PaymentsList plist: pl) {
				if(plist.getCatExportacion() == null) {
					catExport = "01";
				}else {
					catExport = plist.getCatExportacion();
				}
				if(plist.getCustomerCountry().equals(AppConstantsUtil.COUNTRY_DEFAULT)) {
					country = plist.getCustomerCountry();
					rfc = "";
				}else {
					country = plist.getCustomerCountry();
					rfc = plist.getCustomerTaxId();
				}
				List<Payments> pay = new ArrayList<Payments>(plist.getPayments());
				//PAGOS 2.0
				pagos16Base = "0"; pagos16Iva = "0";  pagos8Base = "0";  pagos8Iva = "0";	pagos0Base = "0";
				pagos0Iva = "0";	  pagosExento = "0"; valorBase = 0;    baseTotal16 = "0"; ivaTotal = "0";
				ivaTotal16 = "0";  tasa16 = "0";      baseTotal08 = "0"; ivaTotal08 = "0";  tasa = "";
				tasa08 = "0"; 	  baseTotal00 = "0"; ivaTotal00 = "0";  tasa00 = "0";      baseTotal = "0";	regFiscal = "";

				if(plist.getCurrency().equals(AppConstants.DEFAUL_CURRENCY)) {
					valorBase = Math.round(Double.parseDouble(plist.getPaymentAmount())*100.00)/100.00;
					excRate = "1.0";
				}else {
					valorBase = (Math.round(Double.parseDouble(plist.getPaymentAmount()) * Double.parseDouble(plist.getExchangeRate())*100.00)/100.00);
					excRate = plist.getExchangeRate();
				}
				changeCurrency = false;
				currency = pay.get(0).getCurrency();
				for(Payments bp: plist.getPayments()) {
					if(!bp.getCurrency().equals(currency)) {
						changeCurrency = true;
					}
				}
				for(Payments bp: plist.getPayments()) {
					//Metodo para obtener el regimen fiscal
					if(bp.getCustomerTaxRegime().length() == 0) {
						regFiscal = soapService.getRegimenFiscal(bp.getPartyNumber());
						bp.setCustomerTaxRegime(regFiscal);
					}else {
						regFiscal = bp.getCustomerTaxRegime();
					}
					bp = reviewData(bp);
					if(!changeCurrency) {//FACTURAS EN MISMA MONEDA
						if(plist.getCurrency().equals(AppConstants.DEFAUL_CURRENCY)) {//PAGO CABECERO EN MXN; VALORES DE IMPUESTOS DEL PAGO EN MXN
							switch(bp.getCurrency()){//MONEDA DE LAS FACTURAS
								case AppConstants.DEFAUL_CURRENCY://MONEDA MXN
									switch(bp.getTaxCode()) {
										case AppConstants.COMP_PAGOS_FAC_16:
											baseTotal16 = (baseTotal16.isEmpty()) ? String.valueOf((Math.round(bp.getBase()*100.00))/100.00) : (String.valueOf(Math.round((Double.parseDouble(baseTotal16) + (bp.getBase()))*100.00)/100.00));
											ivaTotal16 = (ivaTotal16.isEmpty()) ? String.valueOf(Math.round(bp.getTaxAmount()*100.00)/100.00): (String.valueOf(Math.round((Double.parseDouble(ivaTotal16) + (bp.getTaxAmount()))*100.00)/100.00));	
											tasa16 = "0.160000";
											break;
										case AppConstants.COMP_PAGOS_FAC_8:
											baseTotal08 = (baseTotal08.isEmpty()) ? String.valueOf((Math.round(bp.getBase()*100.00))/100.00) : (String.valueOf(Math.round((Double.parseDouble(baseTotal08) + (bp.getBase()))*100.00)/100.00));
											ivaTotal08 = (ivaTotal08.isEmpty()) ? String.valueOf(Math.round(bp.getTaxAmount()*100.00)/100.00) : (String.valueOf(Math.round((Double.parseDouble(ivaTotal08) + (bp.getTaxAmount()))*100.00)/100.00));							
											tasa08 = "0.080000";
											break;
										case AppConstants.COMP_PAGOS_FAC_0:
											baseTotal00 = (baseTotal00.isEmpty()) ? String.valueOf((Math.round(bp.getBase()*100.00))/100.00) : (String.valueOf(Math.round((Double.parseDouble(baseTotal00) + (bp.getBase()))*100.00)/100.00));
											ivaTotal00 = "0";
											tasa00 = "0.000000";
											break;
										default:
											pagosExento = String.valueOf(valorBase);
											break;
									}
									break;
								default://MONEDA EXTRANJERA
									switch(bp.getTaxCode()) {//Revisado
										case AppConstants.COMP_PAGOS_FAC_16:
//											baseTotal16 = String.valueOf(Double.parseDouble(baseTotal16) + (bp.getBase() * Double.parseDouble(bp.getExchangeRate())));
											baseTotal16 = String.valueOf(this.createAmountWithDecimal(Double.parseDouble(baseTotal16),6) + this.createAmountWithDecimal((bp.getBase() / Double.parseDouble(bp.getExchangeRate())), 6));
											int valorConteo = baseTotal16.substring(baseTotal16.indexOf(".")).length();
											baseTotal16 = (valorConteo >= 7) ? baseTotal16.substring(0, baseTotal16.indexOf(".") + 7) : baseTotal16.substring(0, baseTotal16.indexOf(".") + valorConteo);
											ivaTotal16 = String.valueOf(this.createAmountWithDecimal(Double.parseDouble(ivaTotal16),6) + this.createAmountWithDecimal((bp.getTaxAmount() / Double.parseDouble(bp.getExchangeRate())), 6));
											int valorConteoIva = ivaTotal16.substring(ivaTotal16.indexOf(".")).length();
											//ivaTotal16 = String.valueOf(this.createAmountWithDecimal(Double.parseDouble(ivaTotal16),6) + this.createAmountWithDecimal((bp.getTaxAmount() / Double.parseDouble(bp.getExchangeRate())), 6));
											ivaTotal16 = (valorConteoIva >= 7) ? ivaTotal16.substring(0, ivaTotal16.indexOf(".") + 7) : ivaTotal16.substring(0, ivaTotal16.indexOf(".") + valorConteoIva);
											tasa16 = "0.160000";
											break;
										case AppConstants.COMP_PAGOS_FAC_8:
											double baseTasa08 = this.createAmountWithDecimal(bp.getBase(), 2);
											double taxTasa08 = this.createAmountWithDecimal(bp.getTaxAmount(), 2);
											baseTotal08 = String.valueOf(this.createAmountWithDecimal(Double.parseDouble(baseTotal08),6) + this.createAmountWithDecimal((baseTasa08 / Double.parseDouble(bp.getExchangeRate())), 6));
											ivaTotal08 = String.valueOf(this.createAmountWithDecimal(Double.parseDouble(ivaTotal08),6) + this.createAmountWithDecimal((taxTasa08 / Double.parseDouble(bp.getExchangeRate())), 6));							
											tasa08 = "0.080000";
											break;
										case AppConstants.COMP_PAGOS_FAC_0:
											baseTotal00 = String.valueOf(this.createAmountWithDecimal(Double.parseDouble(baseTotal00),6) + this.createAmountWithDecimal((bp.getBase() / Double.parseDouble(bp.getExchangeRate())), 6));
											ivaTotal00 = "0";
											tasa00 = "0.000000";
											break;
										default:
											pagosExento = String.valueOf(valorBase);
											break;
									}
									break;
							}
						}else {//PAGO CABECERO USD; VALORES DE IMPUESTOS DEL PAGO EN USD
							switch(bp.getCurrency()){//MONEDA DE LAS FACTURAS
								case AppConstants.DEFAUL_CURRENCY://MONEDA MXN
									switch(bp.getTaxCode()) {//Revisado
										case AppConstants.COMP_PAGOS_FAC_16:
											baseTotal16 = String.valueOf(this.createAmountWithDecimal(Double.parseDouble(baseTotal16),6) + this.createAmountWithDecimal((bp.getBase() / Double.parseDouble(bp.getExchangeRate())), 6));
											ivaTotal16 = String.valueOf(this.createAmountWithDecimal(Double.parseDouble(ivaTotal16),6) + this.createAmountWithDecimal((bp.getTaxAmount() / Double.parseDouble(bp.getExchangeRate())), 6));	
											tasa16 = "0.160000";
											break;
										case AppConstants.COMP_PAGOS_FAC_8:
											baseTotal08 = String.valueOf(this.createAmountWithDecimal(Double.parseDouble(baseTotal08),6) + this.createAmountWithDecimal((bp.getBase() / Double.parseDouble(bp.getExchangeRate())), 6));
											ivaTotal08 = String.valueOf(this.createAmountWithDecimal(Double.parseDouble(ivaTotal08),6) + this.createAmountWithDecimal((bp.getTaxAmount() / Double.parseDouble(bp.getExchangeRate())), 6));							
											tasa08 = "0.080000";
											break;
										case AppConstants.COMP_PAGOS_FAC_0:
											baseTotal00 = String.valueOf(this.createAmountWithDecimal(Double.parseDouble(baseTotal00),6) + this.createAmountWithDecimal((bp.getBase() / Double.parseDouble(bp.getExchangeRate())), 6));
											ivaTotal00 = "0";
											tasa00 = "0.000000";
											break;
										default:
											pagosExento = String.valueOf(valorBase);
											break;
									}
									break;
								default://MONEDA EXTRANJERA
									switch(bp.getTaxCode()) {
										case AppConstants.COMP_PAGOS_FAC_16:
											if(plist.getCurrency().equals(bp.getCurrency())) {
												baseTotal16 = (baseTotal16.isEmpty()) ? String.valueOf(this.createAmountWithDecimal(bp.getBase(), 6)) : String.valueOf(this.createAmountWithDecimal(Double.parseDouble(baseTotal16) + bp.getBase(),6));
//												baseTotal16 = (baseTotal16.isEmpty()) ? String.valueOf(this.createAmountWithDecimal(bp.getBase(), 6)) : String.valueOf(Double.parseDouble(baseTotal16) + ((Math.round(bp.getBase()*100.00))/100.00));
												ivaTotal16 = (ivaTotal16.isEmpty()) ? String.valueOf(this.createAmountWithDecimal(bp.getTaxAmount(),6)): String.valueOf(this.createAmountWithDecimal(Double.parseDouble(ivaTotal16) + bp.getTaxAmount(),6));
//												ivaTotal16 = (ivaTotal16.isEmpty()) ? String.valueOf(Math.round(bp.getTaxAmount()*100.00)/100.00): String.valueOf(Math.round((Double.parseDouble(ivaTotal16) + bp.getTaxAmount())*100.00)/100.00);
												tasa16 = "0.160000";
											}else {
												baseTotal16 = (baseTotal16.isEmpty()) ? String.valueOf(this.createAmountWithDecimal(bp.getBase() * Double.parseDouble(bp.getExchangeRate()),6)) : String.valueOf(this.createAmountWithDecimal(Double.parseDouble(baseTotal16) + (bp.getBase() * Double.parseDouble(bp.getExchangeRate())),6));
//												baseTotal16 = (baseTotal16.isEmpty()) ? String.valueOf(((Math.round(bp.getBase() * Double.parseDouble(bp.getExchangeRate())*100.000000))/100.000000)) : String.valueOf(Double.parseDouble(baseTotal16) + ((Math.round(bp.getBase() * Double.parseDouble(bp.getExchangeRate())*100.000000))/100.000000));
												ivaTotal16 = (ivaTotal16.isEmpty()) ? String.valueOf(this.createAmountWithDecimal(bp.getTaxAmount() * Double.parseDouble(bp.getExchangeRate()),6)): String.valueOf(this.createAmountWithDecimal(Double.parseDouble(ivaTotal16) + (bp.getTaxAmount()* Double.parseDouble(bp.getExchangeRate())),6));
//												ivaTotal16 = (ivaTotal16.isEmpty()) ? String.valueOf(Math.round(bp.getTaxAmount() * Double.parseDouble(bp.getExchangeRate())*100.000000)/100.000000): String.valueOf(Double.parseDouble(ivaTotal16) + Math.round(bp.getTaxAmount()* Double.parseDouble(bp.getExchangeRate())*100.000000)/100.000000);
												tasa16 = "0.160000";
											}
											break;
										case AppConstants.COMP_PAGOS_FAC_8:
											if(plist.getCurrency().equals(bp.getCurrency())) {
												baseTotal08 = (baseTotal08.isEmpty()) ? String.valueOf((Math.round(bp.getBase()*100.00))/100.00) : String.valueOf(Double.parseDouble(baseTotal08) + ((Math.round(bp.getBase()*100.00))/100.00));
												ivaTotal08 = (ivaTotal08.isEmpty()) ? String.valueOf(Math.round(bp.getTaxAmount()*100.00)/100.00) : String.valueOf((Double.parseDouble(ivaTotal08) + Math.round(bp.getTaxAmount())*100.00)/100.00);							
												tasa08 = "0.080000";
											}else {
												baseTotal08 = (baseTotal08.isEmpty()) ? String.valueOf(((Math.round(bp.getBase() * Double.parseDouble(bp.getExchangeRate())*100.000000))/100.000000)) : String.valueOf(Double.parseDouble(baseTotal08) + ((Math.round(bp.getBase() * Double.parseDouble(bp.getExchangeRate())*100.000000))/100.000000));
												ivaTotal08 = (ivaTotal08.isEmpty()) ? String.valueOf(Math.round(bp.getTaxAmount() * Double.parseDouble(bp.getExchangeRate())*100.000000)/100.000000) : String.valueOf(Double.parseDouble(ivaTotal08) + Math.round(bp.getTaxAmount()* Double.parseDouble(bp.getExchangeRate())*100.000000)/100.000000);							
												tasa08 = "0.080000";
											}											
											break;
										case AppConstants.COMP_PAGOS_FAC_0:
											if(plist.getCurrency().equals(bp.getCurrency())) {
												baseTotal00 = (baseTotal00.isEmpty()) ? String.valueOf(this.createAmountWithDecimal(bp.getBase(),6)) : String.valueOf(this.createAmountWithDecimal((Double.parseDouble(baseTotal00) + bp.getBase()), 6));
//												baseTotal00 = (baseTotal00.isEmpty()) ? String.valueOf(Math.round(bp.getBase()*100.00)/100.00) : String.valueOf(Math.round((Double.parseDouble(baseTotal00) + bp.getBase())*100.00)/100.00);
												ivaTotal00 = "0";
												tasa00 = "0.000000";
											}else {
												baseTotal00 = (baseTotal00.isEmpty()) ? String.valueOf(this.createAmountWithDecimal((bp.getBase() * Double.parseDouble(bp.getExchangeRate())),6)) : String.valueOf(this.createAmountWithDecimal(Double.parseDouble(baseTotal00) + (bp.getBase() * Double.parseDouble(bp.getExchangeRate())),6));
//												baseTotal00 = (baseTotal00.isEmpty()) ? String.valueOf(((Math.round(bp.getBase() * Double.parseDouble(bp.getExchangeRate())*100.000000))/100.000000)) : String.valueOf(Math.round(Double.parseDouble(baseTotal00) + (bp.getBase() * Double.parseDouble(bp.getExchangeRate())*100.000000))/100.000000);
												ivaTotal00 = "0";
												tasa00 = "0.000000";
											}
											break;
										default:
											pagosExento = String.valueOf(valorBase);
											break;
									}
									break;
							}							
						}
					}else {//FACTURAS DIFERENTES MONEDAS
						double ba = 0;
						double iva = 0;
						if(plist.getCurrency().equals(AppConstants.DEFAUL_CURRENCY)) {
							if(bp.getCurrency().equals(AppConstants.DEFAUL_CURRENCY)) {//Factura # 1 MXN y Factura # N MXN
								ba =this.createAmountWithDecimal(bp.getBase(), 6);
								iva = this.createAmountWithDecimal(bp.getTaxAmount(), 6);
							}else {
								double eRate = (Double.parseDouble(bp.getExchangeRate()) < 1.00) ? Math.round((1/Double.parseDouble(bp.getExchangeRate())) * 100.00)/100.00 : Math.round(Double.parseDouble(bp.getExchangeRate()) * 100.00)/100.00;							
								ba = this.createAmountWithDecimal((bp.getBase() / Double.parseDouble(bp.getExchangeRate())), 6);
								iva = this.createAmountWithDecimal((bp.getTaxAmount() / Double.parseDouble(bp.getExchangeRate())), 6);
							}
						}else {
							if(bp.getCurrency().equals(AppConstants.DEFAUL_CURRENCY)) {//Factura # 1 MXN y Factura # N MXN
								double eRate = (Double.parseDouble(bp.getExchangeRate()) < 1.00) ? Math.round((1/Double.parseDouble(bp.getExchangeRate())) * 100.00)/100.00 : Math.round(Double.parseDouble(bp.getExchangeRate()) * 100.00)/100.00;
								ba =this.createAmountWithDecimal(bp.getBase() / eRate, 6);
								iva = this.createAmountWithDecimal(bp.getTaxAmount() / eRate, 6);
							}else {
								double eRate = (Double.parseDouble(bp.getExchangeRate()) < 1.00) ? Math.round((1/Double.parseDouble(bp.getExchangeRate())) * 100.00)/100.00 : Math.round(Double.parseDouble(bp.getExchangeRate()) * 100.00)/100.00;							
								ba = this.createAmountWithDecimal((bp.getBase()), 6);
								iva = this.createAmountWithDecimal((bp.getTaxAmount()), 6);
							}
						}
						
						switch(bp.getTaxCode()) {
							case AppConstants.COMP_PAGOS_FAC_16:
								baseTotal16 = String.valueOf(this.createAmountWithDecimal((Double.parseDouble(baseTotal16) + ba), 6));
								ivaTotal16 = String.valueOf(this.createAmountWithDecimal((Double.parseDouble(ivaTotal16) + iva), 6));	
								tasa16 = "0.160000";
								break;
							case AppConstants.COMP_PAGOS_FAC_8:
								baseTotal08 = String.valueOf(this.createAmountWithDecimal((Double.parseDouble(baseTotal08) + ba), 6));
								ivaTotal08 = String.valueOf(this.createAmountWithDecimal((Double.parseDouble(ivaTotal08) + iva), 6));							
								tasa08 = "0.080000";
								break;
							case AppConstants.COMP_PAGOS_FAC_0:
								baseTotal00 = String.valueOf(this.createAmountWithDecimal((Double.parseDouble(baseTotal00) + ba), 6));
								ivaTotal00 = "0";
								tasa00 = "0.000000";
								break;
							default:
								pagosExento = String.valueOf(valorBase);
								break;
						}						
					}
				}
				if(!changeCurrency) {
					if(plist.getCurrency().equals(AppConstants.DEFAUL_CURRENCY) && pay.get(0).getCurrency().equals(AppConstants.DEFAUL_CURRENCY)) {
						pagos16Base = (baseTotal16.isEmpty()) ? "" : String.valueOf(Math.round((Double.parseDouble(baseTotal16))*100.00)/100.00);
						pagos16Iva = (ivaTotal16.isEmpty()) ? "" : String.valueOf(Math.round((Double.parseDouble(ivaTotal16))*100.00)/100.00);
						pagos8Base = (baseTotal08.isEmpty()) ? "" : String.valueOf(Math.round((Double.parseDouble(baseTotal08))*100.00)/100.00);
						pagos8Iva = (ivaTotal08.isEmpty()) ? "" : String.valueOf(Math.round((Double.parseDouble(ivaTotal08))*100.00)/100.00);
						pagos0Base = (baseTotal00.isEmpty()) ? "" : String.valueOf(Math.round((Double.parseDouble(baseTotal00))*100.00)/100.00);
						pagos0Iva = ivaTotal00;
					}else if(!plist.getCurrency().equals(AppConstants.DEFAUL_CURRENCY) && pay.get(0).getCurrency().equals(AppConstants.DEFAUL_CURRENCY)){
						pagos16Base = (baseTotal16.isEmpty()) ? "" : String.valueOf(Math.round((Double.parseDouble(baseTotal16)*Double.parseDouble(excRate))*100.00)/100.00);
						pagos16Iva = (ivaTotal16.isEmpty()) ? "" : String.valueOf(Math.round((Double.parseDouble(ivaTotal16)*Double.parseDouble(excRate))*100.00)/100.00);
						pagos8Base = (baseTotal08.isEmpty()) ? "" : String.valueOf(Math.round((Double.parseDouble(baseTotal08)*Double.parseDouble(excRate))*100.00)/100.00);
						pagos8Iva = (ivaTotal08.isEmpty()) ? "" : String.valueOf(Math.round((Double.parseDouble(ivaTotal08)*Double.parseDouble(excRate))*100.00)/100.00);
						pagos0Base = (baseTotal00.isEmpty()) ? "" : String.valueOf(Math.round((Double.parseDouble(baseTotal00)*Double.parseDouble(excRate))*100.00)/100.00);
						pagos0Iva = ivaTotal00;
					}else if(!plist.getCurrency().equals(AppConstants.DEFAUL_CURRENCY) && !pay.get(0).getCurrency().equals(AppConstants.DEFAUL_CURRENCY)){
						pagos16Base = (baseTotal16.isEmpty()) ? "" : String.valueOf(Math.round((Double.parseDouble(baseTotal16)*Double.parseDouble(excRate))*100.00)/100.00);
						pagos16Iva = (ivaTotal16.isEmpty()) ? "" : String.valueOf(Math.round((Double.parseDouble(ivaTotal16)*Double.parseDouble(excRate))*100.00)/100.00);
						pagos8Base = (baseTotal08.isEmpty()) ? "" : String.valueOf(Math.round((Double.parseDouble(baseTotal08)*Double.parseDouble(excRate))*100.00)/100.00);
						pagos8Iva = (ivaTotal08.isEmpty()) ? "" : String.valueOf(Math.round((Double.parseDouble(ivaTotal08)*Double.parseDouble(excRate))*100.00)/100.00);
						pagos0Base = (baseTotal00.isEmpty()) ? "" : String.valueOf(Math.round((Double.parseDouble(baseTotal00)*Double.parseDouble(excRate))*100.00)/100.00);
						pagos0Iva = ivaTotal00;
					}else {
						pagos16Base = (baseTotal16.isEmpty()) ? "" : String.valueOf(Math.round((Double.parseDouble(baseTotal16))*100.00)/100.00);
						pagos16Iva = (ivaTotal16.isEmpty()) ? "" : String.valueOf(Math.round((Double.parseDouble(ivaTotal16))*100.00)/100.00);
						pagos8Base = (baseTotal08.isEmpty()) ? "" : String.valueOf(Math.round((Double.parseDouble(baseTotal08))*100.00)/100.00);
						pagos8Iva = (ivaTotal08.isEmpty()) ? "" : String.valueOf(Math.round((Double.parseDouble(ivaTotal08))*100.00)/100.00);
						pagos0Base = (baseTotal00.isEmpty()) ? "" : String.valueOf(Math.round((Double.parseDouble(baseTotal00))*100.00)/100.00);
						pagos0Iva = ivaTotal00;
					}
				}else {
					if(plist.getCurrency().equals(AppConstants.DEFAUL_CURRENCY)) {
						pagos16Base = (baseTotal16.isEmpty()) ? "" : String.valueOf(Math.round((Double.parseDouble(baseTotal16))*100.00)/100.00);
						pagos16Iva = (ivaTotal16.isEmpty()) ? "" : String.valueOf(Math.round((Double.parseDouble(ivaTotal16))*100.00)/100.00);
						pagos8Base = (baseTotal08.isEmpty()) ? "" : String.valueOf(Math.round((Double.parseDouble(baseTotal08))*100.00)/100.00);
						pagos8Iva = (ivaTotal08.isEmpty()) ? "" : String.valueOf(Math.round((Double.parseDouble(ivaTotal08))*100.00)/100.00);
						pagos0Base = (baseTotal00.isEmpty()) ? "" : String.valueOf(Math.round((Double.parseDouble(baseTotal00))*100.00)/100.00);
						pagos0Iva = ivaTotal00;
					}else {
						pagos16Base = (baseTotal16.isEmpty()) ? "" : String.valueOf(Math.round((Double.parseDouble(baseTotal16) * Double.parseDouble(plist.getExchangeRate()))*100.00)/100.00);
						pagos16Iva = (ivaTotal16.isEmpty()) ? "" : String.valueOf(Math.round((Double.parseDouble(ivaTotal16) * Double.parseDouble(plist.getExchangeRate()))*100.00)/100.00);
						pagos8Base = (baseTotal08.isEmpty()) ? "" : String.valueOf(Math.round((Double.parseDouble(baseTotal08) * Double.parseDouble(plist.getExchangeRate()))*100.00)/100.00);
						pagos8Iva = (ivaTotal08.isEmpty()) ? "" : String.valueOf(Math.round((Double.parseDouble(ivaTotal08) * Double.parseDouble(plist.getExchangeRate()))*100.00)/100.00);
						pagos0Base = (baseTotal00.isEmpty()) ? "" : String.valueOf(Math.round((Double.parseDouble(baseTotal00) * Double.parseDouble(plist.getExchangeRate()))*100.00)/100.00);
						pagos0Iva = ivaTotal00;
					}
					
				}
				plist.setPaymentAmount(String.valueOf(this.paymentList(Double.parseDouble(plist.getPaymentAmount()), (Double.parseDouble(baseTotal16) + Double.parseDouble(ivaTotal16) + Double.parseDouble(baseTotal08) + Double.parseDouble(ivaTotal08) + Double.parseDouble(baseTotal00)))));
				
				//PAGOS 2.0
				content = AppConstantsUtil.PAYMENT_HEADER + AppConstantsUtil.FILES_SEPARATOR +
						plist.getSerial() + AppConstantsUtil.FILES_SEPARATOR +
						plist.getFolio() + AppConstantsUtil.FILES_SEPARATOR +
						dateFormat.format(new Date()) + AppConstantsUtil.FILES_SEPARATOR +
						pay.get(0).getBranch().getZip() + AppConstantsUtil.FILES_SEPARATOR +
						NullValidator.isNull(plist.getRelationType())  + AppConstantsUtil.FILES_SEPARATOR +//CFDI Tipo Relación
						NullValidator.isNull(plist.getRelationTypeUUID()) + AppConstantsUtil.FILES_SEPARATOR +//UUID Relación
						pay.get(0).getCompany().getTaxIdentifier() + AppConstantsUtil.FILES_SEPARATOR +
						pay.get(0).getCompany().getBusinessUnitName() + AppConstantsUtil.FILES_SEPARATOR +
						pay.get(0).getCompany().getTaxRegime() + AppConstantsUtil.FILES_SEPARATOR +
						plist.getCustomerTaxId() + AppConstantsUtil.FILES_SEPARATOR +
						plist.getCustomerName() + AppConstantsUtil.FILES_SEPARATOR +
						country + AppConstantsUtil.FILES_SEPARATOR +
						rfc + AppConstantsUtil.FILES_SEPARATOR +//RFC Extranjero
						pay.get(0).getPartyNumber() + AppConstantsUtil.FILES_SEPARATOR +
						pay.get(0).getCustomerEmail() + AppConstantsUtil.FILES_SEPARATOR +	
						catExport  + AppConstantsUtil.FILES_SEPARATOR + //Pagos 2.0 Exportacion
						regFiscal + AppConstantsUtil.FILES_SEPARATOR + //Pagos 2.0 Regimen Fiscal
						//plist.getTaxRegime() + AppConstantsUtil.FILES_SEPARATOR + //Pagos 2.0 Regimen Fiscal
						plist.getCustomerZipCode() + AppConstantsUtil.FILES_SEPARATOR + //Pagos 2.0 Domicilio Fiscal
						"0"  + AppConstantsUtil.FILES_SEPARATOR + //Pagos 2.0 TotalRetencionesIVA Importe MXN
						"0"  + AppConstantsUtil.FILES_SEPARATOR + //Pagos 2.0 TotalRetencionesISR Importe MXN
						"0"  + AppConstantsUtil.FILES_SEPARATOR + //Pagos 2.0 TotalRetencionesIEPS Importe MXN
						pagos16Base  + AppConstantsUtil.FILES_SEPARATOR + //Pagos 2.0 TotalTrasladosBaseIVA16 Importe MXN
						pagos16Iva  + AppConstantsUtil.FILES_SEPARATOR + //Pagos 2.0 TotalTrasladosImpuestoIVA16 Importe MXN
						pagos8Base  + AppConstantsUtil.FILES_SEPARATOR + //Pagos 2.0 TotalTrasladosBaseIVA8 Importe MXN
						pagos8Iva  + AppConstantsUtil.FILES_SEPARATOR + //Pagos 2.0 TotalTrasladosImpuestoIVA8 Importe MXN
						pagos0Base  + AppConstantsUtil.FILES_SEPARATOR + //Pagos 2.0 TotalTrasladosBaseIVA0 Importe MXN
						pagos0Iva  + AppConstantsUtil.FILES_SEPARATOR + //Pagos 2.0 TotalTrasladosImpuestoIVA0 Importe MXN
						pagosExento  + AppConstantsUtil.FILES_SEPARATOR + //Pagos 2.0 TotalTrasladosBaseIVAExento Importe MXN
						valorBase  + AppConstantsUtil.FILES_SEPARATOR + //Pagos 2.0 MontoTotalPagos Importe MXN						
						"\r\n" +
						AppConstantsUtil.PAYMENT_PAYMENT + AppConstantsUtil.FILES_SEPARATOR +
						pay.get(0).getCreationDate() + AppConstantsUtil.FILES_SEPARATOR +
						plist.getPaymentForm() + AppConstantsUtil.FILES_SEPARATOR +
						plist.getCurrency() + AppConstantsUtil.FILES_SEPARATOR +
						excRate + AppConstantsUtil.FILES_SEPARATOR +
						plist.getPaymentAmount() + AppConstantsUtil.FILES_SEPARATOR +
						"" + AppConstantsUtil.FILES_SEPARATOR +//Transaction Reference
						NullValidator.isNull(pay.get(0).getBankReference()) + AppConstantsUtil.FILES_SEPARATOR +
						NullValidator.isNull(pay.get(0).getAcountBankTaxIdentifier()) + AppConstantsUtil.FILES_SEPARATOR +
						NullValidator.isNull(pay.get(0).getPayerAccount()) + AppConstantsUtil.FILES_SEPARATOR +
						NullValidator.isNull(pay.get(0).getBeneficiaryAccount()) + AppConstantsUtil.FILES_SEPARATOR +
//						NullValidator.isNull(pay.get(0).getBenBankAccTaxIden()) + AppConstantsUtil.FILES_SEPARATOR +
						NullValidator.isNull(plist.getReceiptNumber()) + AppConstantsUtil.FILES_SEPARATOR +
						"\r\n" + // Pagos 2.0 SET PARA IVA 16
						AppConstantsUtil.PAYMENT_PAYMENT_M + AppConstantsUtil.FILES_SEPARATOR +
						((baseTotal16.equals("0")) ? "" : baseTotal16) + AppConstantsUtil.FILES_SEPARATOR +
						((baseTotal16.equals("0")) ? "" : AppConstantsUtil.TAX_CODE) + AppConstantsUtil.FILES_SEPARATOR +
						((baseTotal16.equals("0")) ? "" : AppConstantsUtil.TAX_CODE_NAME) + AppConstantsUtil.FILES_SEPARATOR +
						((baseTotal16.equals("0")) ? "" : tasa16) + AppConstantsUtil.FILES_SEPARATOR +
						((baseTotal16.equals("0")) ? "" : ivaTotal16) + AppConstantsUtil.FILES_SEPARATOR +
						"\r\n"+ // Pagos 2.0 SET PARA IVA 8
						AppConstantsUtil.PAYMENT_PAYMENT_M + AppConstantsUtil.FILES_SEPARATOR +
						((baseTotal08.equals("0")) ? "" : baseTotal08) + AppConstantsUtil.FILES_SEPARATOR +
						((baseTotal08.equals("0")) ? "" : AppConstantsUtil.TAX_CODE) + AppConstantsUtil.FILES_SEPARATOR +
						((baseTotal08.equals("0")) ? "" : AppConstantsUtil.TAX_CODE_NAME) + AppConstantsUtil.FILES_SEPARATOR +
						((baseTotal08.equals("0")) ? "" : tasa08) + AppConstantsUtil.FILES_SEPARATOR +
						((baseTotal08.equals("0")) ? "" : ivaTotal08) + AppConstantsUtil.FILES_SEPARATOR +
						"\r\n"+ // Pagos 2.0 SET PARA IVA 0
						AppConstantsUtil.PAYMENT_PAYMENT_M + AppConstantsUtil.FILES_SEPARATOR +
						((baseTotal00.equals("0")) ? "" : baseTotal00) + AppConstantsUtil.FILES_SEPARATOR +
						((baseTotal00.equals("0")) ? "" : AppConstantsUtil.TAX_CODE) + AppConstantsUtil.FILES_SEPARATOR +
						((baseTotal00.equals("0")) ? "" : AppConstantsUtil.TAX_CODE_NAME) + AppConstantsUtil.FILES_SEPARATOR +
						((baseTotal00.equals("0")) ? "" : tasa00) + AppConstantsUtil.FILES_SEPARATOR +
						((baseTotal00.equals("0")) ? "" : ivaTotal00) + AppConstantsUtil.FILES_SEPARATOR +
						"\r\n"+
						AppConstantsUtil.PAYMENT_PAYMENT_N + AppConstantsUtil.FILES_SEPARATOR +
						"" + AppConstantsUtil.FILES_SEPARATOR +
						"" + AppConstantsUtil.FILES_SEPARATOR +
						"" + AppConstantsUtil.FILES_SEPARATOR +
						"" + AppConstantsUtil.FILES_SEPARATOR +
						"" + AppConstantsUtil.FILES_SEPARATOR +
						"\r\n";// Pagos 2.0
				for(Payments p: pay) {
					//PAGOS 2.0
					if(p.getTaxCode().equals(AppConstants.COMP_PAGOS_FAC_16)) {
						baseTotal = String.valueOf((Math.round(p.getBase()*100.00))/100.00);
						ivaTotal = String.valueOf(Math.round(p.getTaxAmount()*100.00)/100.00);
						tasa = "0.160000";
					}else if(p.getTaxCode().equals(AppConstants.COMP_PAGOS_FAC_8)) {
						baseTotal = String.valueOf((Math.round(p.getBase()*100.00))/100.00);
						ivaTotal = String.valueOf(Math.round(p.getTaxAmount()*100.00)/100.00);
						tasa = "0.080000";
					}else if(p.getTaxCode().equals(AppConstants.COMP_PAGOS_FAC_0)) {
						baseTotal = String.valueOf((Math.round(p.getBase()*100.00))/100.00);
						ivaTotal = "0";
						tasa = "0.000000";
					}else if(p.getTaxCode().equals(AppConstants.COMP_PAGOS_FAC_EXENTO)) {
						pagosExento = String.valueOf(valorBase);
					}
					//PAGOS 2.0
					content = content +
							AppConstantsUtil.PAYMENT_DETAILS + AppConstantsUtil.FILES_SEPARATOR +
							NullValidator.isNull(p.getUuidReference()) + AppConstantsUtil.FILES_SEPARATOR +
							NullValidator.isNull(p.getSerialRel()) + AppConstantsUtil.FILES_SEPARATOR +
							p.getFolioRel() + AppConstantsUtil.FILES_SEPARATOR +
							p.getCurrency() + AppConstantsUtil.FILES_SEPARATOR +
							p.getExchangeRate() + AppConstantsUtil.FILES_SEPARATOR +
							p.getPaymentMethod() + AppConstantsUtil.FILES_SEPARATOR +//Método de pago PUE
							p.getPaymentNumber() + AppConstantsUtil.FILES_SEPARATOR +
							NullValidator.isNull(p.getPreviousBalanceAmount()) + AppConstantsUtil.FILES_SEPARATOR +
							NullValidator.isNull(p.getPaymentAmount()) + AppConstantsUtil.FILES_SEPARATOR +
							NullValidator.isNull(p.getRemainingBalanceAmount()) + AppConstantsUtil.FILES_SEPARATOR +
							"02" + AppConstantsUtil.FILES_SEPARATOR +
							"\r\n" +// Pagos 2.0 SET IVA CORRESPONDIENTE
							AppConstantsUtil.PAYMENT_DETAILS_Y + AppConstantsUtil.FILES_SEPARATOR +
							baseTotal + AppConstantsUtil.FILES_SEPARATOR +
							AppConstantsUtil.TAX_CODE + AppConstantsUtil.FILES_SEPARATOR +
							AppConstantsUtil.TAX_CODE_NAME + AppConstantsUtil.FILES_SEPARATOR +
							tasa + AppConstantsUtil.FILES_SEPARATOR +
							ivaTotal + AppConstantsUtil.FILES_SEPARATOR +
							"\r\n"+
							AppConstantsUtil.PAYMENT_DETAILS_X + AppConstantsUtil.FILES_SEPARATOR +
							"" + AppConstantsUtil.FILES_SEPARATOR +
							"" + AppConstantsUtil.FILES_SEPARATOR +
							"" + AppConstantsUtil.FILES_SEPARATOR +
							"" + AppConstantsUtil.FILES_SEPARATOR +
							"" + AppConstantsUtil.FILES_SEPARATOR +
							"\r\n";// Pagos 2.0;
					
					p.setPaymentStatus(AppConstants.STATUS_UPDUUID);
					paymentsService.updatePayment(p);
				}
				
				//Nombrar archivo
				fileName = NullValidator.isNull(plist.getSerial()) + plist.getFolio();
				//Crear archivo en la ruta deseada
				File file = new File(fileRuta + fileName + AppConstantsUtil.RUTA_FILES_EXTENSION);
				if (!file.exists()) {
	             	file.createNewFile();
	            }
	            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
	            out.write(content);
	            out.close();
	            plist.setStatus(AppConstants.STATUS_UPDUUID);
	            paymentsListService.updatePaymentsList(plist);	 
			}
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public double createAmountWithDecimal(double value, int decimalpoint) {
		// Using the pow() method
        value = value * Math.pow(10, decimalpoint);
        value = Math.round(value);
        value = value / Math.pow(10, decimalpoint);
//      System.out.println(value);  
        return value;
	}
	
	public double paymentList (double plistAmount, double suma) {
		double value = 0.0;
		suma = this.createAmountWithDecimal(suma, 2);
		if(plistAmount > suma) {
			value = plistAmount;
		}else {
			value = suma;
		}
		
		return value;
	}
	
	public String getUuidAlejandro (String fileName, String customerRfc) {
		String uuid = "";
		Connection cn = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//			DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
//			String url = "jdbc:sqlserver://localhost:1433;databaseName=SCADB-P-IMEMSA;integratedSecurity=false;encrypt=true;trustServerCertificate=true";
//			cn = DriverManager.getConnection(url, "sa", "1234");
			
			String url = AppConstants.URL_DATABASE_ALEJANDRO;
			
//			String url = "jdbc:sqlserver://EC2AMAZ-MHT40UR:1433;databaseName=SCADB-D-IMEMSA;integratedSecurity=false;encrypt=true;trustServerCertificate=true";//AWS TEST
//			cn = DriverManager.getConnection(url, "sa", "ScG990720Rf1.$");//AWS TEST
//			
//			String url = "jdbc:sqlserver://EC2AMAZ-MHT40UR:1433;databaseName=SCADB-P-IMEMSA;integratedSecurity=false;encrypt=true;trustServerCertificate=true";//AWS PROD
//			cn = DriverManager.getConnection(url, "sa", "ScG990720Rf1.$");//AWS PROD
			
			cn = DriverManager.getConnection(url, "sa", "ScG990720Rf1.$");//Credenciales de conexión a la BD de SQL Server
			
			if(cn != null) {
				System.out.println("Conectado");
				String query = "Declare @Archivo varchar(100) = '" + fileName +".txt'\n" + 
						"						select ISNULL(Serie,'') + ISNULL(Folio,'') + '.txt', RutaPDF , RutaXML, UUID, emisor.Rfc RFCE, emisor.Nombre NombreE, receptor.Rfc RFCR, receptor.Nombre NombreR  \n" + 
						"						from Comprobante Txt\n" + 
						"						inner join FACT_Factura Archivos\n" + 
						"						on\n" + 
						"							txt.ClaveComprobante = Archivos.ClaveComprobante \n" + 
						"						inner join TimbreFiscalDigital timbre\n" + 
						"						on\n" + 
						"							txt.ClaveComprobante = timbre.ClaveComprobante \n" + 
						"						inner join ComprobanteEmisor emisor\n" + 
						"						on\n" + 
						"							txt.ClaveComprobante = emisor.ClaveComprobante \n" + 
						"						inner join ComprobanteReceptor receptor\n" + 
						"						on\n" + 
						"							txt.ClaveComprobante = receptor.ClaveComprobante \n" + 
						"						where (ISNULL(Serie,'') + ISNULL(Folio,'') + '.txt') =  @Archivo";
//				String query = "Declare @Archivo varchar (100) = '" + fileName + "'\n" + 
//						"select RutaArchivo, RutaPDF , RutaXML, UUID, emisor.Rfc RFCE, emisor.Nombre NombreE, receptor.Rfc RFCR, receptor.Nombre NombreR  \n" + 
//						"from FACT_ComprobanteTXT Txt\n" + 
//						"inner join FACT_Factura Archivos\n" + 
//						"on\n" + 
//						"	txt.ClaveComprobante = Archivos.ClaveComprobante \n" + 
//						"inner join TimbreFiscalDigital timbre\n" + 
//						"on\n" + 
//						"	txt.ClaveComprobante = timbre.ClaveComprobante \n" + 
//						"inner join ComprobanteEmisor emisor\n" + 
//						"on\n" + 
//						"	txt.ClaveComprobante = emisor.ClaveComprobante \n" + 
//						"inner join ComprobanteReceptor receptor\n" + 
//						"on\n" + 
//						"	txt.ClaveComprobante = receptor.ClaveComprobante \n" + 
//						"where RutaArchivo like '%' + @Archivo and txt.EstatusEntidad = 6";
				PreparedStatement pstmt = cn.prepareStatement(query);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					String rutaArchivo = rs.getString(1);
					String rutaPdf = rs.getString(2);
					String rutaXml = rs.getString(3);
					String uuidRs = rs.getString(4);
					String rfcEmisor = rs.getString(5);
					String nombreEmisor = rs.getString(6);
					String rfcReceptor = rs.getString(7);
					String nombreReceptor = rs.getString(8);
					System.out.println("Revisar valor");
					if(customerRfc.equals(rfcReceptor) && uuidRs != null && !uuidRs.isEmpty()) {
						return uuidRs;
					}
				}
				rs.close();
				System.out.print(rs);
			}
			return null;
		}catch(Exception e) {
			System.out.println(e);
		}finally {
            try {
                if (cn != null && !cn.isClosed()) {
                    cn.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
		return null;
	}

	@Override
	public boolean createFileForCancel(Invoice inv) {
		// TODO Auto-generated method stub
		try{
			String content = "";
			String fileRuta = "";
			
			List<Udc> u = udcService.searchBySystem(AppConstantsUtil.RUTA_FILES);
			for(Udc ud: u) {
				if(ud.getStrValue1().equals(AppConstantsUtil.RUTA_FILES_STAMPED_CANCEL)) {
					fileRuta = ud.getStrValue2();
				}
			}	
			content = inv.getUUID();
			String fileName = NullValidator.isNull(inv.getSerial()) + inv.getFolio();
			File file = new File(fileRuta + fileName + AppConstantsUtil.RUTA_FILES_EXTENSION);
			if (!file.exists()) {
             	file.createNewFile();
            }
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
            out.write(content);
            out.close();
            inv.setStatus(AppConstants.STATUS_FINISHED);
            if(invoiceDao.updateInvoice(inv)) {
            	return true;
            }
			return false;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}		
	}
	
}
