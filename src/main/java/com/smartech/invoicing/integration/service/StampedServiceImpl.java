package com.smartech.invoicing.integration.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartech.invoicing.dao.InvoiceDao;
import com.smartech.invoicing.integration.util.AppConstants;
import com.smartech.invoicing.model.Invoice;
import com.smartech.invoicing.model.InvoiceDetails;
import com.smartech.invoicing.model.Payments;
import com.smartech.invoicing.model.TaxCodes;
import com.smartech.invoicing.model.Udc;
import com.smartech.invoicing.service.PaymentsService;
import com.smartech.invoicing.service.UdcService;
import com.smartech.invoicing.util.AppConstantsUtil;
import com.smartech.invoicing.util.NullValidator;

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
	
	static Logger log = Logger.getLogger(StampedServiceImpl.class.getName());
	final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	public String[ ] impH = new String[10];
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
		impH = new String[10];
		impD = new String[10];
		int n = 1;
		try {
			//Obtener ruta para dejar los archivos
			List<Udc> u = udcService.searchBySystem(AppConstantsUtil.RUTA_FILES);
			for(Udc ud: u) {
				if(ud.getStrValue1().equals(AppConstantsUtil.RUTA_FILES_STAMPED)) {
					fileRuta = ud.getUdcKey();
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
			//Saber tipo de factura
//			if(i.isInvoice()) {
//				voucherType = AppConstantsUtil.VOUCHER_I;
//				relationType = "";
//				UUIDRelated = "";
//			}else {
//				voucherType = AppConstantsUtil.VOUCHER_E;
//				relationType = i.getInvoiceRelationType();
//				UUIDRelated = i.getUUIDReference();
//			}			
			if(i.getInvoiceType().equals(AppConstants.ORDER_TYPE_FACTURA)) {
				voucherType = AppConstantsUtil.VOUCHER_I;
				relationType = "";
				UUIDRelated = "";
			}else if(i.getInvoiceType().equals(AppConstants.ORDER_TYPE_ADV)) {
				voucherType = AppConstantsUtil.VOUCHER_P;
				relationType = i.getInvoiceRelationType();
				UUIDRelated = i.getUUIDReference();
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
				paymentTerms = "";
			}else if(i.getPaymentMethod() == null){
				paymentTerms = "";
			}else {
				paymentTerms = i.getPaymentTerms();
			}
			//Llenar los impuestos
			getTaxes(i.getInvoiceDetails());
			//Formato de fecha
			String date = dateFormat.format(i.getCreationDate());
			//Cabecero txt
			content = AppConstantsUtil.FILES_HEADER + AppConstantsUtil.FILES_SEPARATOR +
					i.getFolio() + AppConstantsUtil.FILES_SEPARATOR +
					i.getInvoiceTotal() + AppConstantsUtil.FILES_SEPARATOR + 
					i.getInvoiceSubTotal() + AppConstantsUtil.FILES_SEPARATOR +
					voucherType + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(i.getPaymentType()) + AppConstantsUtil.FILES_SEPARATOR +
					paymentTerms + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(i.getPaymentMethod()) + AppConstantsUtil.FILES_SEPARATOR +
					i.getFolio() + AppConstantsUtil.FILES_SEPARATOR +
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
					i.getCustomerCountry() + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					i.getInvoiceCurrency() + AppConstantsUtil.FILES_SEPARATOR +
					i.getInvoiceExchangeRate() + AppConstantsUtil.FILES_SEPARATOR +
					numberLetterService.getNumberLetter(String.valueOf(i.getInvoiceTotal()), true, i.getInvoiceCurrency()) + AppConstantsUtil.FILES_SEPARATOR +//Total Con letra
					i.getCustomerEmail() + AppConstantsUtil.FILES_SEPARATOR +//40
					"" + AppConstantsUtil.FILES_SEPARATOR +//orden de compra
					AppConstantsUtil.NUMBER_COPIES + AppConstantsUtil.FILES_SEPARATOR +//Número de copias
					"" + AppConstantsUtil.FILES_SEPARATOR +//JDE ORDEN DE VENTA
					"" + AppConstantsUtil.FILES_SEPARATOR +//JDE
					"" + AppConstantsUtil.FILES_SEPARATOR +//JDE
					i.getCustomerPartyNumber() + AppConstantsUtil.FILES_SEPARATOR;//Número de cuenta del cliente
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
					/*"" + AppConstantsUtil.FILES_SEPARATOR +//58
					"" + AppConstantsUtil.FILES_SEPARATOR +//59
					"" + AppConstantsUtil.FILES_SEPARATOR +//60
					"" + AppConstantsUtil.FILES_SEPARATOR +//61
					"" + AppConstantsUtil.FILES_SEPARATOR +//62
					"" + AppConstantsUtil.FILES_SEPARATOR +//63
					"" + AppConstantsUtil.FILES_SEPARATOR +//64
					"" + AppConstantsUtil.FILES_SEPARATOR +//65
					"" + AppConstantsUtil.FILES_SEPARATOR +//66
					"" + AppConstantsUtil.FILES_SEPARATOR +//67  */
					NullValidator.isNull(relationType) + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(UUIDRelated) + AppConstantsUtil.FILES_SEPARATOR +
					i.getSerial() + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +//taxId Extranjero
					i.getCFDIUse() + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +//Notes	
					"" + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +				
					"\r\n";
			//Lineas txt
			for(InvoiceDetails id: i.getInvoiceDetails()) {
				if(id != null) {
					String lines = this.dataLines(id, i, n);
					content = content + lines;
					impD = new String[10];
					n = n + 1;
				}
			}
//			Contenido en base64
//			String encodedString = Base64.getEncoder().encodeToString(content.getBytes());
			//Escribir contenido en el archivo creado
			FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
            //Actualizar estado de la factura
            i.setStatus(AppConstants.STATUS_UPDUUID);
            invoiceDao.updateInvoice(i);
			
			return true;
		}catch(Exception e) {
			log.error("ERROR AL CREAR EL ARCHIVO" + e);
			e.printStackTrace();			
			return false;
		}
	}
	
	public String dataLines(InvoiceDetails idet, Invoice i, int nL) {
		String detail = "";
		getTaxesDetails(idet);
		String operationType = "";
		String petitionKey  = "";
		try {
			Udc isComboOrMarina = udcService.searchBySystemAndKey(AppConstants.UDC_SYSTEM_MARINA, AppConstants.UDC_KEY_MARINA);
			List<Udc> oType = udcService.searchBySystem(AppConstants.UDC_SYSTEM_OPERATIONTYPE);
			List<Udc> pKey = udcService.searchBySystem(AppConstants.UDC_SYSTEM_PETITIONKEY);
			for(Udc uoType: oType) {
				if(uoType.getStrValue1().equals(AppConstants.UDC_STRVALUE1_DEFAULT)) {
					operationType = uoType.getUdcKey();
					break;
				}
			}
			for(Udc upKey: pKey) {
				if(upKey.getStrValue1().equals(AppConstants.UDC_STRVALUE1_DEFAULT)) {
					petitionKey = upKey.getUdcKey();
					break;
				}				
			}
			
			detail = AppConstantsUtil.FILES_DETAILS + AppConstantsUtil.FILES_SEPARATOR +
					i.getFolio() + AppConstantsUtil.FILES_SEPARATOR +
					nL + AppConstantsUtil.FILES_SEPARATOR +
					idet.getTotalAmount() + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					idet.getItemDescription() + AppConstantsUtil.FILES_SEPARATOR +
					idet.getUomName() + AppConstantsUtil.FILES_SEPARATOR +
					idet.getUnitPrice() + AppConstantsUtil.FILES_SEPARATOR +
					idet.getQuantity() + AppConstantsUtil.FILES_SEPARATOR +
					idet.getExchangeRate() + AppConstantsUtil.FILES_SEPARATOR +//10
					idet.getItemNumber() + AppConstantsUtil.FILES_SEPARATOR +
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
					/*"" + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					idet.getTotalDiscount() + AppConstantsUtil.FILES_SEPARATOR +//28*/
		        	"" + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(idet.getItemNotes()) + AppConstantsUtil.FILES_SEPARATOR +//Notes
					"" + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR;
		        	if(isComboOrMarina.getUdcKey().equals(i.getCustomerTaxIdentifier()) &&
		        			isComboOrMarina.getStrValue1().equals(i.getCustomerName())) {
		        		detail = detail +
	        				idet.getAddtionalDescription().replaceAll("\r\n", " ") + AppConstantsUtil.FILES_SEPARATOR +//Campo para marina
	    					"" + AppConstantsUtil.FILES_SEPARATOR;//Campo para combo
		        	}else {
		        		detail = detail +
	        				"" + AppConstantsUtil.FILES_SEPARATOR +//Campo para marina
	    					NullValidator.isNull(idet.getAddtionalDescription()).replaceAll("\r\n", " ") + AppConstantsUtil.FILES_SEPARATOR;//Campo para combo
		        	}
					

					//Datos del complemento exterior
					if(i.isExtCom()) {
						detail = detail +
							"" + AppConstantsUtil.FILES_SEPARATOR +//c_Motivo Traslado
							operationType + AppConstantsUtil.FILES_SEPARATOR +//c_Tipo de operacion
							petitionKey + AppConstantsUtil.FILES_SEPARATOR +//c_Clave pedimento
							"" + AppConstantsUtil.FILES_SEPARATOR +//Cert origen
							"" + AppConstantsUtil.FILES_SEPARATOR +
							"" + AppConstantsUtil.FILES_SEPARATOR +
							NullValidator.isNull(idet.getIncotermKey()) + AppConstantsUtil.FILES_SEPARATOR +//c_Incoterm 40
							"" + AppConstantsUtil.FILES_SEPARATOR +//Subdivison	
							NullValidator.isNull(idet.getItemNotes()) + AppConstantsUtil.FILES_SEPARATOR +//Observaciones	
							idet.getExchangeRate() + AppConstantsUtil.FILES_SEPARATOR +//Tipo de cambio
							idet.getTotalAmount() + AppConstantsUtil.FILES_SEPARATOR +//Total venta moneda extranjera
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
//							NullValidator.isNull(idet.getFraccionArancelaria()) + AppConstantsUtil.FILES_SEPARATOR +//c_FraccionArancelaria 60
							"89039999" + AppConstantsUtil.FILES_SEPARATOR +
							idet.getQuantity() + AppConstantsUtil.FILES_SEPARATOR +//Cantidad Aduana
							idet.getItemUomCustoms() + AppConstantsUtil.FILES_SEPARATOR +//c_Unidad de medida aduana
							idet.getUnitPrice() + AppConstantsUtil.FILES_SEPARATOR +//Valor unitario de aduana
							idet.getTotalAmount() + AppConstantsUtil.FILES_SEPARATOR +//Valor total aduana
							NullValidator.isNull(idet.getItemBrand())+ AppConstantsUtil.FILES_SEPARATOR +//Marca
							NullValidator.isNull(idet.getItemModel()) + AppConstantsUtil.FILES_SEPARATOR +//Modelo
							"" + AppConstantsUtil.FILES_SEPARATOR +//Submodelo
							NullValidator.isNull(idet.getItemSerial()) + AppConstantsUtil.FILES_SEPARATOR;//No. de seríe
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
							"" + AppConstantsUtil.FILES_SEPARATOR +
							"" + AppConstantsUtil.FILES_SEPARATOR +
							"" + AppConstantsUtil.FILES_SEPARATOR +
							"" + AppConstantsUtil.FILES_SEPARATOR;
					}

		        	//Datos del complemento detallista 
		        	if(idet.getRetailComplements() != null) {
		        		if(idet.getRetailComplements().getBuyerDateFolio() == null) {
		        			
		        		}
			        	detail = detail +								
						idet.getRetailComplements().getDocumentStatus() + AppConstantsUtil.FILES_SEPARATOR +
						idet.getRetailComplements().getTransactionType() + AppConstantsUtil.FILES_SEPARATOR +
						idet.getRetailComplements().getInstructionCode() + AppConstantsUtil.FILES_SEPARATOR +
						idet.getRetailComplements().getTextNote() + AppConstantsUtil.FILES_SEPARATOR +
						NullValidator.isNull(idet.getRetailComplements().getReferenceId()) + AppConstantsUtil.FILES_SEPARATOR +
//						NullValidator.isNull(idet.getRetailComplements().getReferenceDate().toString()) + AppConstantsUtil.FILES_SEPARATOR +
						"" + AppConstantsUtil.FILES_SEPARATOR +
						NullValidator.isNull(idet.getRetailComplements().getAdicionalInformation()) + AppConstantsUtil.FILES_SEPARATOR +
						NullValidator.isNull(idet.getRetailComplements().getAdicionalInformationNumber()) + AppConstantsUtil.FILES_SEPARATOR +
						NullValidator.isNull(idet.getRetailComplements().getAdicionalInformationId()) + AppConstantsUtil.FILES_SEPARATOR +
						NullValidator.isNull(idet.getRetailComplements().getDeliveryNote()) + AppConstantsUtil.FILES_SEPARATOR +//10
//						NullValidator.isNull(idet.getRetailComplements().getBuyerNumberFolio()) + AppConstantsUtil.FILES_SEPARATOR +
						"2085157632" + AppConstantsUtil.FILES_SEPARATOR +
//						NullValidator.isNull(idet.getRetailComplements().getBuyerDateFolio().toString()) + AppConstantsUtil.FILES_SEPARATOR +
						"" + AppConstantsUtil.FILES_SEPARATOR +
						"" + AppConstantsUtil.FILES_SEPARATOR +
						idet.getRetailComplements().getGlobalLocationNumberBuyer() + AppConstantsUtil.FILES_SEPARATOR +
						idet.getRetailComplements().getPurchasingContact() + AppConstantsUtil.FILES_SEPARATOR +
						idet.getRetailComplements().getSeller() + AppConstantsUtil.FILES_SEPARATOR +
						i.getCompany().getGlobalLocationNumberProvider() + AppConstantsUtil.FILES_SEPARATOR +
						i.getCompany().getAlternativeId() + AppConstantsUtil.FILES_SEPARATOR +
						idet.getRetailComplements().getIdentificationType() + AppConstantsUtil.FILES_SEPARATOR +
						idet.getRetailComplements().getElementOnline() + AppConstantsUtil.FILES_SEPARATOR +
						NullValidator.isNull(idet.getRetailComplements().getType()) + AppConstantsUtil.FILES_SEPARATOR +//10
						NullValidator.isNull(idet.getRetailComplements().getNumber()) + AppConstantsUtil.FILES_SEPARATOR +
						idet.getRetailComplements().getgTin() + AppConstantsUtil.FILES_SEPARATOR +
						idet.getRetailComplements().getInovicedQuantity() + AppConstantsUtil.FILES_SEPARATOR +
						idet.getRetailComplements().getUomCode() + AppConstantsUtil.FILES_SEPARATOR +
						idet.getRetailComplements().getPriceTotal() + AppConstantsUtil.FILES_SEPARATOR +
						idet.getRetailComplements().getTotal() + AppConstantsUtil.FILES_SEPARATOR +
						"\n";
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
						"\r\n";
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
								impH[5] = tax.getTax();	
								impH[6] = tax.getFactor();
								if(impH[7] != null) {
									impH[7] = String.valueOf(id.getTotalTaxAmount() + Double.parseDouble(impH[7])) ;
								}else {
									impH[7] = String.valueOf(id.getTotalTaxAmount());	
								}
								if(impH[8] != null) {
									impH[8] = String.valueOf(id.getTotalAmount() + Double.parseDouble(impH[8]));
								}else {
									impH[8] = String.valueOf(id.getTotalAmount());									
								}
								impH[9] = String.valueOf(tax.getTaxValue());
								break;
							default:
								break;
						}
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getTaxesDetails(InvoiceDetails set) {
		int op = 0;
		try {
			Set<TaxCodes> tc = new HashSet<TaxCodes>(set.getTaxCodes());
			for(TaxCodes tax: tc) {
				op = Integer.parseInt(tax.getPosition());
				if(tax.getTax().equals(AppConstantsUtil.TAX_CODE)) {
					switch(op) {
						case 1:
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
						case 2:
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

	@Override
	public boolean readDataFromTxt() {
		String filePathResponse = "";
		String filePathSuccess = "";
		String fileName = "";
		String contentFile = "";
		String c = "";
		try{
			//Ruta donde estan guardados los archivos timbrados
			List<Udc> success = udcService.searchBySystem(AppConstantsUtil.RUTA_FILES);
			for(Udc u: success) {
				if(u.getStrValue1().equals(AppConstantsUtil.FILE_RESPONSE)) {
					filePathResponse = u.getUdcKey();
				}
				if(u.getStrValue1().equals(AppConstantsUtil.FILE_SUCCESS)) {
					filePathSuccess = u.getUdcKey();
				}
			}
			File f = new File(filePathResponse);
			if(f.isDirectory()) {
				File[] resFiles = f.listFiles();
				for(File file: resFiles) {
					String[] content = new String[3];
					fileName = file.getName().substring(0, file.getName().length()-4);
					BufferedReader objReader = new BufferedReader(new FileReader(file));  
		            while ((contentFile = objReader.readLine()) != null) {
		                c = contentFile;
		            }
		            content = c.split(Pattern.quote("|"));
		            objReader.close();
		            Invoice inv = new Invoice();
		            Payments payments = new Payments();
		            //Modificar valor cuando se setean los nextNumbersCorrespondientes y se tengan los archivos reales
		            //fileName = "MEFAC10001";
		            Invoice getId = invoiceDao.getSingleInvoiceByFolioSerial(fileName);
		            Payments pay = paymentsService.getPaymentByName(fileName);
		            if(getId != null) {
			            inv = invoiceDao.getSingleInvoiceById(getId.getId());
			            if(inv != null && !inv.getInvoiceType().equals(AppConstants.ORDER_TYPE_ADV)) {//FACTURAS, NC
			            	if(!AppConstantsUtil.STAMPED_CODES.toString().contains(content[0])) {
			            		inv.setUUID(content[1]);
			            		inv.setErrorMsg("");
			            		inv.setStatus(AppConstants.STATUS_INVOICED);
			            		if(invoiceDao.updateInvoice(inv)) {
			            			log.info("Se guardo el UUID correspondiente satisfactoriamente: " + file.getName());
			    		            //Pasar el archivo a otra carpeta
			    		            File newArrive = new File(filePathSuccess + fileName + AppConstantsUtil.RUTA_FILES_EXTENSION);
			    					FileWriter fw = new FileWriter(newArrive);
			    		            BufferedWriter bw = new BufferedWriter(fw);
			    		            bw.write(c);
			    		            bw.close();	  
			    		            fw.close();
			    		            if(file.exists()) {
			    		             	file.setExecutable(true);
			    		             	file.setReadable(true);
			    		             	file.setWritable(true);		    		            	
			    		            	if(file.delete()){
			    		            		log.info("El fichero " + file.getName() + " ha sido borrado satisfactoriamente");
			    			            }else {
			    			            	log.error("El fichero " + file.getName() + " no ha sido borrado" + "|" + file.delete());
			    			            }
			    		            }	
			            		}else {
			            			log.info("No se actualizo la factura: " + file.getName());
			            		}
			            	}else {
			            		inv.setUUID("");
			            		inv.setErrorMsg(content[1].substring(0, 250));
			            		inv.setStatus(AppConstants.STATUS_ERROR_PAC);
			            		if(invoiceDao.updateInvoice(inv)) {
			            			log.info("Se obtuvo el error en la factura " + file.getName());
			            		}else {
			            			log.error("No se actualizo la factura, update: " + file.getName());
			            		}
			            	}
			            }else if(inv.getInvoiceType().equals(AppConstants.ORDER_TYPE_ADV)) {//ANTICIPOS
			            	Payments payment = paymentsService.getPaymentByName(fileName);			            	
			            	if(!AppConstantsUtil.STAMPED_CODES.toString().contains(content[0])) {
			            		inv.setUUID(content[1]);
			            		inv.setErrorMsg("");
			            		inv.setStatus(AppConstants.STATUS_FINISHED);
			            		if(invoiceDao.updateInvoice(inv)) {
			            			log.info("Se guardo el UUID correspondiente satisfactoriamente: " + file.getName());
			    		            //Pasar el archivo a otra carpeta
			    		            File newArrive = new File(filePathSuccess + fileName + AppConstantsUtil.RUTA_FILES_EXTENSION);
			    					FileWriter fw = new FileWriter(newArrive);
			    		            BufferedWriter bw = new BufferedWriter(fw);
			    		            bw.write(c);
			    		            bw.close();	  
			    		            fw.close();
			    		            if(file.exists()) {
			    		            	if(file.delete()){
			    		            		log.info("El fichero " + file.getName() + " ha sido borrado satisfactoriamente");
			    			            }
			    			            else {
			    			            	log.error("El fichero " + file.getName() + " no ha sido borrado");
			    			            }
			    		            }
			    		            if(payment != null){
			    		            	Payments paym = paymentsService.getPaymentsById(String.valueOf(payment.getId()));
			    		            	paym.setUUID(content[1]);
			    		            	paym.setPaymentError("");
			    		            	paym.setPaymentStatus(AppConstants.STATUS_INVOICED);
			    		            	if(paymentsService.updatePayment(paym)) {
					            			log.info("Se guardo el UUID correspondiente satisfactoriamente: " + file.getName());
			    		            	}
			    		            }
			            		}else {
			            			log.error("No se actualizo la factura: " + file.getName());
			            		}
			            	}else {
			            		inv.setUUID("");
			            		inv.setErrorMsg(content[1].substring(0, 250));
			            		inv.setStatus(AppConstants.STATUS_ERROR_PAC);
			            		if(invoiceDao.updateInvoice(inv)) {
			            			log.info("Se obtuvo el error en la factura " + file.getName());
			            		}else {
			            			log.error("No se actualizo la factura, update: " + file.getName());
			            		}
			            	}
			            }
		            }else if(pay != null) {//COMPLEMENTO DE PAGO
		            	payments = paymentsService.getPaymentsById(String.valueOf(pay.getId()));
		            	if(payments != null) {
		            		if(!AppConstantsUtil.STAMPED_CODES.toString().contains(content[0])) {
		            			payments.setUUID(content[1]);
		            			payments.setPaymentError("");
		            			payments.setPaymentStatus(AppConstants.STATUS_INVOICED);
			            		if(paymentsService.updatePayment(payments)) {
			            			log.info("Se guardo el UUID correspondiente satisfactoriamente: " + file.getName());
			    		            //Pasar el archivo a otra carpeta
			    		            File newArrive = new File(filePathSuccess + fileName + AppConstantsUtil.RUTA_FILES_EXTENSION);
			    					FileWriter fw = new FileWriter(newArrive);
			    		            BufferedWriter bw = new BufferedWriter(fw);
			    		            bw.write(c);
			    		            bw.close();	  
			    		            fw.close();
			    		            if(file.exists()) {
			    		             	file.setExecutable(true);
			    		             	file.setReadable(true);
			    		             	file.setWritable(true);
			    		            	if(file.delete()){
			    		            		log.info("El fichero" + file.getName() + " ha sido borrado satisfactoriamente");
			    			            }
			    			            else {
			    			            	log.info("El fichero" + file.getName() + " no ha sido borrado");
			    			            }
			    		            }	
			            		}else {
			            			log.info("No se actualizo el COMPLEMENTO DE PAGO: " + file.getName());
			            		}
		            		}else {
			            		payments.setUUID("");
			            		payments.setPaymentError(content[1].substring(0, 250));
			            		payments.setPaymentStatus(AppConstants.STATUS_ERROR_PAC);
			            		if(paymentsService.updatePayment(payments)) {
			            			log.info("Se obtuvo el error en el COMPLEMENTO DE PAGO " + file.getName());
			            		}else {
			            			log.info("No se actualizo el COMPLEMENTO DE PAGO, update: " + file.getName());
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
					fileRuta = ud.getUdcKey();
				}
			}
			//Nombrar archivo
			fileName = NullValidator.isNull(i.getSerial()) + i.getFolio();
			//Crear archivo en la ruta deseada
			File file = new File(fileRuta + fileName + AppConstantsUtil.RUTA_FILES_EXTENSION);
			if (!file.exists()) {
             	file.createNewFile();
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
					i.getCreationDate() + AppConstantsUtil.FILES_SEPARATOR +
					i.getBranch().getZip() + AppConstantsUtil.FILES_SEPARATOR +
					rType + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(i.getUuidReference()) + AppConstantsUtil.FILES_SEPARATOR +
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
					"" + AppConstantsUtil.FILES_SEPARATOR +//Forma de pago
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
					i.getSerial() + AppConstantsUtil.FILES_SEPARATOR +
					i.getFolio() + AppConstantsUtil.FILES_SEPARATOR +
					i.getCurrency() + AppConstantsUtil.FILES_SEPARATOR +
					i.getExchangeRate() + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +//Método de pago PUE
					i.getPaymentNumber() + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(i.getPreviousBalanceAmount()) + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(i.getPaymentAmount()) + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(i.getRemainingBalanceAmount()) + AppConstantsUtil.FILES_SEPARATOR +
					"\n";
			
			FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
            
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
