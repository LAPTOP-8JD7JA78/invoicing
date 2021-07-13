package com.smartech.invoicingprod.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.smartech.invoicingprod.distribuitorportal.services.DistribuitorServices;
import com.smartech.invoicingprod.integration.dto.InsertDataDTO;
import com.smartech.invoicingprod.integration.dto.WarrantyDataDTO;
import com.smartech.invoicingprod.integration.service.InvoicingService;
import com.smartech.invoicingprod.integration.service.MailService;
import com.smartech.invoicingprod.model.Invoice;
import com.smartech.invoicingprod.model.Udc;
import com.smartech.invoicingprod.service.UdcService;

@RestController
@RequestMapping("/rest")
public class InvoiceController {
	
	@Autowired
	InvoicingService invoicingService;
	@Autowired
	UdcService udcService;
	@Autowired
	MailService mailService;
	@Autowired
	ServletContext servletContext;
	@Autowired
	DistribuitorServices distribuitorServices;
	
	@SuppressWarnings("unused")
	@RequestMapping(value ="/integ/invoice/createInvoice", method = RequestMethod.POST)
	ResponseEntity<Map<String, Object>> createInvoice(@RequestBody (required=false) Invoice i ){
		Map<String,Object> modelMap = new HashMap<String,Object>(2);
		return null;
	}
	
	@SuppressWarnings("unused")
	@RequestMapping(value ="/integ/invoice/getInvoice", method = RequestMethod.GET)
	ResponseEntity<Map<String, Object>> getInvoice(){
		Map<String,Object> modelMap = new HashMap<String,Object>(2);
		List<Udc> emails = udcService.searchBySystem("EMAILS");
		String e = "lopluis98@gmail.com";
		List<String> email = new ArrayList<String>();
		email.add(e);
		for(Udc u: emails) {
			email.add(u.getUdcKey());
		}
		mailService.sendMail(email,
				"ERROR EN PROCESO DE REPORTE (INVOICE)",
				"SE HAN HECHO 5 INTENTOS DE PROCESAR LA INFORMACION PERO SE HAN OBTENIDO ERRORES",
				null);
		return null;
	}
	
	@RequestMapping(value = "/testMethod", method = RequestMethod.GET)
	 public boolean oracleCustomers(@RequestParam String request) {
		System.out.print(true);
		return true;
	}
	
	@RequestMapping(value = "/distribuitors/warranty", method = RequestMethod.GET)
	 public @ResponseBody Map<String, Object> getDataForWarranty(@RequestParam String invoiceNumber, String itemNumber, String itemSerial, String customerName) {
		
		if(itemSerial == null || itemSerial.isEmpty() || customerName == null || customerName.isEmpty()) {
			return mapError("Porfavor de agregar el valor del número de la factura");
		}
		WarrantyDataDTO array = new WarrantyDataDTO();
		try {
			array = distribuitorServices.getDataInvoice(invoiceNumber, itemNumber, itemSerial, customerName);
			if(array != null ) {
				if(array.getLinesWarranty() != null) {
					if(array.getLinesWarranty().size()>0) {
						return mapOK(array, 1);	
					}else {
						return mapError("Esta factura no tiene líneas que se le puede generar garantía");
					}
				}else {
					return mapError("No se encuentra el registro búscado");
				}
			}else {
				return mapError("No se encuentra el registro búscado");
			}
		}catch(Exception e) {
			e.printStackTrace();
			return mapError(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/warranty/insertData", method = RequestMethod.GET)
	 public InsertDataDTO insertData(@RequestParam String invoiceNumber, String itemNumber, String itemSerial, String productTypeCode) {
		
		InsertDataDTO inData = new InsertDataDTO();
		if((invoiceNumber == null || invoiceNumber.isEmpty()) ||
				(itemNumber == null || itemNumber.isEmpty()) ||
				(itemSerial == null || itemSerial.isEmpty() )) {
			inData.setData(false);
			return inData;
		}
		inData.setData(distribuitorServices.insertData(invoiceNumber, itemNumber, itemSerial, productTypeCode));
		return inData;
	}
	
	@RequestMapping(value = "/warranty/retreiveAllData", method = RequestMethod.GET)
	 public List<WarrantyDataDTO> retrieveDataWithoutWarranty(@RequestParam String dataSearch) {	
		if(dataSearch == null || dataSearch.isEmpty()) {
			return null;
		}
		return distribuitorServices.retrieveAllData(dataSearch); 		
	}	
	
	public Map<String, Object> mapOK(WarrantyDataDTO list, int total) {
		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", total);
		modelMap.put("data", list);
		modelMap.put("success", true);
		return modelMap;
	}
	
	public Map<String, Object> mapOKInsertData(WarrantyDataDTO list, int total) {
		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", total);
		modelMap.put("data", list);
		modelMap.put("success", true);
		return modelMap;
	}
	
	public Map<String, Object> mapOKList(WarrantyDataDTO list, int total) {
		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", total);
		modelMap.put("data", list);
		modelMap.put("success", true);
		return modelMap;
	}
	
	public Map<String, Object> mapError(String msg) {
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		modelMap.put("message", msg);
		modelMap.put("success", false);
		return modelMap;
	}
}
