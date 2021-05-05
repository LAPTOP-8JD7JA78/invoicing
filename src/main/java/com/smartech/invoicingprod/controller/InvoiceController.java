package com.smartech.invoicingprod.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.smartech.invoicingprod.integration.service.InvoicingService;
import com.smartech.invoicingprod.integration.service.MailService;
import com.smartech.invoicingprod.model.Invoice;
import com.smartech.invoicingprod.model.Udc;
import com.smartech.invoicingprod.service.UdcService;

@RestController
public class InvoiceController {
	
	@Autowired
	InvoicingService invoicingService;
	@Autowired
	UdcService udcService;
	@Autowired
	MailService mailService;
	
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
}
