package com.smartech.invoicingprod.integration.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.smartech.invoicingprod.dao.InvoiceDao;
import com.smartech.invoicingprod.integration.util.AppConstants;
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
	
	static Logger log = Logger.getLogger(StampedServiceImpl.class.getName());
	final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	DecimalFormat numberFormat = new DecimalFormat("#.00");
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
					if(i.getShipToaddress() != null && !i.getShipToaddress().isEmpty()) {
						content = content + 
							i.getCustomerName() + AppConstantsUtil.FILES_SEPARATOR +
							i.getCustomerTaxIdentifier() + AppConstantsUtil.FILES_SEPARATOR +
							""  + AppConstantsUtil.FILES_SEPARATOR +
							i.getShipToaddress() + AppConstantsUtil.FILES_SEPARATOR +
							"" + AppConstantsUtil.FILES_SEPARATOR +//Interior Number
							"" + AppConstantsUtil.FILES_SEPARATOR +//Exterior Number
							i.getShipToZip() + AppConstantsUtil.FILES_SEPARATOR +
							NullValidator.isNull(i.getShipToCity()) + AppConstantsUtil.FILES_SEPARATOR +
							NullValidator.isNull(i.getShipToState()) + AppConstantsUtil.FILES_SEPARATOR +
							i.getShipToCountry() + AppConstantsUtil.FILES_SEPARATOR +
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
					NullValidator.isNull(i.getBranch().getCity()) + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(i.getBranch().getAddress()) + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(i.getBranch().getColony()) + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(i.getBranch().getState()) + AppConstantsUtil.FILES_SEPARATOR +
//					NullValidator.isNull(i.getBranch().getZip()) + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(i.getBranch().getZipAddressPdf()) + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(i.getBranch().getCountry()) + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(i.getBranch().getCellPhoneNumber()) + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(i.getLongDescription()).replaceAll("\n", "").replaceAll("\r", "") + AppConstantsUtil.FILES_SEPARATOR;
					for(int h=0; h<impH2.length; h++) {
						content = content + NullValidator.isNull(impH2[h]) + AppConstantsUtil.FILES_SEPARATOR;
					}
					content = content + 
							"\r\n";
			for(InvoiceDetails id: i.getInvoiceDetails()) {
				if(id != null) {
					String lines = this.dataLines(id, i, n);
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
		try {
			List<Udc> oType = udcService.searchBySystem(AppConstants.UDC_SYSTEM_OPERATIONTYPE);
			List<Udc> pKey = udcService.searchBySystem(AppConstants.UDC_SYSTEM_PETITIONKEY);
			for(Udc uoType: oType) {
				if(uoType.getStrValue1().equals(AppConstants.UDC_STRVALUE1_DEFAULT)) {
					operationType = uoType.getUdcKey();
					break;
				}
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
//				if(NullValidator.isNull(idet.getItemDescription()).contains("Content-Type: text/plain")) {
//					itemDescription = NullValidator.isNull(idet.getItemDescription().replaceAll("\n", "").replaceAll("\r", "").replace("?", "").replace("Content-Type: text/plain", ""));
//				}else {
					itemDescription = NullValidator.isNull(NullValidator.isNull(idet.getItemDescription()).replaceAll("\n", "").replaceAll("\r", "").replace("?", ""));
//				}
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
					NullValidator.isNull(idet.getExchangeRate()) + AppConstantsUtil.FILES_SEPARATOR +//10
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
							NullValidator.isNull(idet.getExchangeRate()) + AppConstantsUtil.FILES_SEPARATOR +//Tipo de cambio
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
		        		String refDate = df.format(idet.getRetailComplements().getReferenceDate());
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
									"\n";
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
									"\n";
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
									"\n";
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
									"\n";
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
				impH[3] = numberFormat.format(NullValidator.isNullD(impH[2]));
			}
			
			if(impH2[3] != null) {
				impH2[3] = numberFormat.format(NullValidator.isNullD(impH2[2]));
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
		try{
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
					String data = sb.toString();
					JSONObject xmlJSONObj = XML.toJSONObject(data, true);
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
								if(inv.getInvoiceRelationType() == null){
									log.warn(uuid + " " + inv.getFolio());
									inv.setUUID(uuid);
									inv.setErrorMsg(null);
									inv.setStatus(AppConstants.STATUS_INVOICED);
									invoiceDao.updateInvoice(inv);
								}else if(inv.getInvoiceRelationType().equals(advPay)){
									inv.setUUID(uuid);
									inv.setErrorMsg(null);
									inv.setStatus(AppConstants.STATUS_INVOICED);
//									this.createAdvPayNC(inv);
									invoiceDao.updateInvoice(inv);
								}
								break;
							case AppConstants.ORDER_TYPE_NC:
								inv.setUUID(uuid);
								inv.setErrorMsg(null);
								inv.setStatus(AppConstants.STATUS_INVOICED);
								invoiceDao.updateInvoice(inv);
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
					JSONObject xmlJSONObj = XML.toJSONObject(data, true);
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
					}					
				}catch(Exception e) {
					log.info("NOSE A ENCONTRADO NADA" + e);
				}
			}
			
			List<PaymentsList> pllist = new ArrayList<PaymentsList>();
			pllist = paymentsListService.getAllPayList(AppConstants.STATUS_UPDUUID);
			for(PaymentsList pl: pllist) {
				for(Payments p: pl.getPayments()) {
					if(p.getUUID()!= null && !p.getUUID().isEmpty()) {
						pl.setUuid(p.getUUID());
						pl.setStatus(AppConstants.STATUS_FINISHED);
						paymentsListService.updatePaymentsList(pl);
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
	public boolean createPaymentsFile(Payments i) {
		String fileRuta = "";
		String fileName = "";
		String content = "";		
		String rType ="";
		String country = "";
		try {
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
				country = "";
			}else {
				country = i.getCountry();
			}
			content = AppConstantsUtil.PAYMENT_HEADER + AppConstantsUtil.FILES_SEPARATOR +
					i.getSerial() + AppConstantsUtil.FILES_SEPARATOR +
					i.getFolio() + AppConstantsUtil.FILES_SEPARATOR +
					dateFormat.format(new Date()) + AppConstantsUtil.FILES_SEPARATOR +
					i.getBranch().getZip() + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					i.getCompany().getTaxIdentifier() + AppConstantsUtil.FILES_SEPARATOR +
					i.getCompany().getBusinessUnitName() + AppConstantsUtil.FILES_SEPARATOR +
					i.getCompany().getTaxRegime() + AppConstantsUtil.FILES_SEPARATOR +
					i.getTaxIdentifier() + AppConstantsUtil.FILES_SEPARATOR +
					i.getCustomerName() + AppConstantsUtil.FILES_SEPARATOR +
					country + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +//RFC Extranjero
					i.getPartyNumber() + AppConstantsUtil.FILES_SEPARATOR +
					i.getCustomerEmail() + AppConstantsUtil.FILES_SEPARATOR +					
					"\n"+
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
					NullValidator.isNull(i.getBenBankAccTaxIden()) + AppConstantsUtil.FILES_SEPARATOR +
					"\n"+
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
					"\n";
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

	@Override
	public boolean creatPaymentListFile(List<PaymentsList> pl) {
		String fileRuta = "";
		String fileName = "";
		String content = "";		
		String country = "";
		String rfc = "";
		try {
			//Obtener ruta para dejar los archivos
			List<Udc> u = udcService.searchBySystem(AppConstantsUtil.RUTA_FILES);
			for(Udc ud: u) {
				if(ud.getStrValue1().equals(AppConstantsUtil.RUTA_FILES_STAMPED)) {
					fileRuta = ud.getStrValue2();
				}
			}	
						
			for(PaymentsList plist: pl) {
				if(plist.getCustomerCountry().equals(AppConstantsUtil.COUNTRY_DEFAULT)) {
					country = "";
					rfc = "";
				}else {
					country = plist.getCustomerCountry();
					rfc = plist.getCustomerTaxId();
				}
				List<Payments> pay = new ArrayList<Payments>(plist.getPayments());
				content = AppConstantsUtil.PAYMENT_HEADER + AppConstantsUtil.FILES_SEPARATOR +
						plist.getSerial() + AppConstantsUtil.FILES_SEPARATOR +
						plist.getFolio() + AppConstantsUtil.FILES_SEPARATOR +
						dateFormat.format(new Date()) + AppConstantsUtil.FILES_SEPARATOR +
						pay.get(0).getBranch().getZip() + AppConstantsUtil.FILES_SEPARATOR +
						""  + AppConstantsUtil.FILES_SEPARATOR +
						"" + AppConstantsUtil.FILES_SEPARATOR +
						pay.get(0).getCompany().getTaxIdentifier() + AppConstantsUtil.FILES_SEPARATOR +
						pay.get(0).getCompany().getBusinessUnitName() + AppConstantsUtil.FILES_SEPARATOR +
						pay.get(0).getCompany().getTaxRegime() + AppConstantsUtil.FILES_SEPARATOR +
						plist.getCustomerTaxId() + AppConstantsUtil.FILES_SEPARATOR +
						plist.getCustomerName() + AppConstantsUtil.FILES_SEPARATOR +
						country + AppConstantsUtil.FILES_SEPARATOR +
						rfc + AppConstantsUtil.FILES_SEPARATOR +//RFC Extranjero
						pay.get(0).getPartyNumber() + AppConstantsUtil.FILES_SEPARATOR +
						pay.get(0).getCustomerEmail() + AppConstantsUtil.FILES_SEPARATOR +
						"\r\n" +
						AppConstantsUtil.PAYMENT_PAYMENT + AppConstantsUtil.FILES_SEPARATOR +
						pay.get(0).getCreationDate() + AppConstantsUtil.FILES_SEPARATOR +
						plist.getPaymentForm() + AppConstantsUtil.FILES_SEPARATOR +
						plist.getCurrency() + AppConstantsUtil.FILES_SEPARATOR +
						plist.getExchangeRate() + AppConstantsUtil.FILES_SEPARATOR +
						plist.getPaymentAmount() + AppConstantsUtil.FILES_SEPARATOR +
						"" + AppConstantsUtil.FILES_SEPARATOR +//Transaction Reference
						NullValidator.isNull(pay.get(0).getBankReference()) + AppConstantsUtil.FILES_SEPARATOR +
						NullValidator.isNull(pay.get(0).getAcountBankTaxIdentifier()) + AppConstantsUtil.FILES_SEPARATOR +
						NullValidator.isNull(pay.get(0).getPayerAccount()) + AppConstantsUtil.FILES_SEPARATOR +
						NullValidator.isNull(pay.get(0).getBeneficiaryAccount()) + AppConstantsUtil.FILES_SEPARATOR +
						NullValidator.isNull(pay.get(0).getBenBankAccTaxIden()) + AppConstantsUtil.FILES_SEPARATOR +
						"\r\n";
				for(Payments p: pay) {
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
							"\r\n";
					
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
	
}
