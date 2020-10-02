package com.smartech.invoicing.integration.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartech.invoicing.dao.InvoiceDao;
import com.smartech.invoicing.model.Invoice;
import com.smartech.invoicing.model.InvoiceDetails;
import com.smartech.invoicing.model.TaxCodes;
import com.smartech.invoicing.model.Udc;
import com.smartech.invoicing.service.UdcService;
import com.smartech.invoicing.util.AppConstantsUtil;
import com.smartech.invoicing.util.NullValidator;

@Service("stampedService")
public class StampedServiceImpl implements StampedService{
	
	@Autowired
	UdcService udcService;
	
	@Autowired
	InvoiceDao invoiceDao;
	
	static Logger log = Logger.getLogger(StampedServiceImpl.class.getName());
	final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	public String[ ] impH = new String[30];
	
	@Override
	public boolean createFileFac(Invoice i) {
		String fileRuta = "";
		String fileName = "";
		String content = "";
		String voucherType = "";
		String relationType = "";
		String paymentTerms = "";
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
            }
			if(i.isInvoice()) {
				voucherType = AppConstantsUtil.VOUCHER_I;
				relationType = "";
			}else {
				voucherType = AppConstantsUtil.VOUCHER_E;
				relationType = i.getInvoiceRelationType();
			}
			if(i.getPaymentMethod().equals(AppConstantsUtil.PAYMENT_METHOD)) {
				paymentTerms = i.getPaymentTerms();
			}else {
				paymentTerms = "";
			}
			String taxes = getTaxes(i.getInvoiceDetails());
			String date = dateFormat.format(i.getCreationDate());
			//Cabecero txt
			content = AppConstantsUtil.FILES_HEADER + AppConstantsUtil.FILES_SEPARATOR +
					i.getFolio() + AppConstantsUtil.FILES_SEPARATOR +
					i.getInvoiceTotal() + AppConstantsUtil.FILES_SEPARATOR + 
					i.getInvoiceSubTotal() + AppConstantsUtil.FILES_SEPARATOR +
					voucherType + AppConstantsUtil.FILES_SEPARATOR +
					i.getPaymentType() + AppConstantsUtil.FILES_SEPARATOR +
					paymentTerms + AppConstantsUtil.FILES_SEPARATOR +
					i.getPaymentMethod() + AppConstantsUtil.FILES_SEPARATOR +
					i.getFolio() + AppConstantsUtil.FILES_SEPARATOR +
					date + AppConstantsUtil.FILES_SEPARATOR +
					i.getInvoiceDiscount() + AppConstantsUtil.FILES_SEPARATOR +
					""  + AppConstantsUtil.FILES_SEPARATOR +
					i.getCompany().getTaxRegime() + AppConstantsUtil.FILES_SEPARATOR +
					i.getCompany().getZip() + AppConstantsUtil.FILES_SEPARATOR +
					i.getCompany().getTaxIdentifier() + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
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
					i.getAddress1() + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					i.getCustomerZip() + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(i.getCity()) + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(i.getState()) + AppConstantsUtil.FILES_SEPARATOR +
					i.getCountry() + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					i.getInvoiceCurrency() + AppConstantsUtil.FILES_SEPARATOR +
					i.getInvoiceExchangeRate() + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +//Total Con letra
					i.getCustomerEmail() + AppConstantsUtil.FILES_SEPARATOR +//40
					"" + AppConstantsUtil.FILES_SEPARATOR +//orden de compra
					"" + AppConstantsUtil.FILES_SEPARATOR +//Número de copias
					"" + AppConstantsUtil.FILES_SEPARATOR +//JDE
					"" + AppConstantsUtil.FILES_SEPARATOR +//JDE
					"" + AppConstantsUtil.FILES_SEPARATOR +//JDE
					"" + AppConstantsUtil.FILES_SEPARATOR +//Número de cuenta del cliente
					i.getCustomerName() + AppConstantsUtil.FILES_SEPARATOR +
					i.getCustomerTaxIdentifier() + AppConstantsUtil.FILES_SEPARATOR +
					""  + AppConstantsUtil.FILES_SEPARATOR +
					i.getAddress1() + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					i.getCustomerZip() + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(i.getCity()) + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(i.getState()) + AppConstantsUtil.FILES_SEPARATOR +
					i.getCountry() + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR;//57
		            //Valores de los impuestos
		            /*for(int h=0; h<impH.length; h++)
		            {
		            	content = content + NullValidator.isNull(impH[h]) + "|";
		            }*/
					content = content +
					"" + AppConstantsUtil.FILES_SEPARATOR +//58
					"" + AppConstantsUtil.FILES_SEPARATOR +//59
					"" + AppConstantsUtil.FILES_SEPARATOR +//60
					"" + AppConstantsUtil.FILES_SEPARATOR +//61
					"" + AppConstantsUtil.FILES_SEPARATOR +//62
					"" + AppConstantsUtil.FILES_SEPARATOR +//63
					"" + AppConstantsUtil.FILES_SEPARATOR +//64
					"" + AppConstantsUtil.FILES_SEPARATOR +//65
					"" + AppConstantsUtil.FILES_SEPARATOR +//66
					"" + AppConstantsUtil.FILES_SEPARATOR +//67
					relationType + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(i.getUUIDReference()) + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(i.getSerial()) + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +//taxId Extranjero
					i.getCFDIUse() + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +//Notes					
					"\n";
			//Lineas txt
			for(InvoiceDetails id: i.getInvoiceDetails()) {
				if(id != null) {
					String lines = this.dataLines(id, i, n);
					content = content + lines;
					n=+1;
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
            /*i.setStatus(AppConstants.STATUS_INVOICED);
            invoiceDao.updateInvoice(i);*/
			
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			
			return false;
		}
	}

	@Override
	public boolean createPaymentsFile(Invoice i) {
		try {
			
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public String getTaxes(Set<InvoiceDetails> set) {
		String t = "";
		String factor = "";
		try {
			for(InvoiceDetails id: set) {
				Set<TaxCodes> tc = new HashSet<TaxCodes>(id.getTaxCodes());
				for(TaxCodes tax: tc) {
					
				}
			}
			if(t != null) {
				return t;
			}
			return null;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String dataLines(InvoiceDetails idet, Invoice i, int nL) {
		String detail = "";
		try {
			detail = AppConstantsUtil.FILES_DETAILS + AppConstantsUtil.FILES_SEPARATOR +
					i.getFolio() + AppConstantsUtil.FILES_SEPARATOR +
					nL + AppConstantsUtil.FILES_SEPARATOR +
					idet.getTotalAmount() + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(idet.getItemLot()) + AppConstantsUtil.FILES_SEPARATOR +
					idet.getItemDescription() + AppConstantsUtil.FILES_SEPARATOR +
					idet.getUomName() + AppConstantsUtil.FILES_SEPARATOR +
					idet.getUnitPrice() + AppConstantsUtil.FILES_SEPARATOR +
					idet.getQuantity() + AppConstantsUtil.FILES_SEPARATOR +
					idet.getExchangeRate() + AppConstantsUtil.FILES_SEPARATOR +//10
					idet.getItemNumber() + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +//Fecha de pedimento
					"" + AppConstantsUtil.FILES_SEPARATOR +//Aduana
					"" + AppConstantsUtil.FILES_SEPARATOR +//Número de pedimento
					"" + AppConstantsUtil.FILES_SEPARATOR +//Fecha caducidad lote
					NullValidator.isNull(idet.getUnitProdServ()) + AppConstantsUtil.FILES_SEPARATOR +
					NullValidator.isNull(idet.getUomCode()) + AppConstantsUtil.FILES_SEPARATOR +//17
					"" + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					"" + AppConstantsUtil.FILES_SEPARATOR +
					idet.getTotalDiscount() + AppConstantsUtil.FILES_SEPARATOR +//28
					"" + AppConstantsUtil.FILES_SEPARATOR +//Notes				
					"\n";
			return detail;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
