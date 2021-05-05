package com.smartech.invoicing.controller;

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

import com.smartech.invoicing.integration.service.InvoicingService;
import com.smartech.invoicing.integration.service.MailService;
import com.smartech.invoicing.model.Invoice;
import com.smartech.invoicing.model.Udc;
import com.smartech.invoicing.service.UdcService;

@RestController
public class InvoiceController {
	
	@Autowired
	InvoicingService invoicingService;
	@Autowired
	UdcService udcService;
	@Autowired
	MailService mailService;
	
	@RequestMapping(value ="/integ/invoice/createInvoice", method = RequestMethod.POST)
	ResponseEntity<Map<String, Object>> createInvoice(@RequestBody (required=false) Invoice i ){
		Map<String,Object> modelMap = new HashMap<String,Object>(2);
//		if(i != null) {
//			List<RESTInvoiceRespDTO> list = invoicingService.createInvoiceByREST(i);
//			if(list != null && list.isEmpty()) {
//				modelMap.put("success", false);
//				modelMap.put("msg", "SOLICITUD EN PROCESO.");
//				return new ResponseEntity<>(modelMap, HttpStatus.CREATED);
//			}else {
//				modelMap.put("success", false);
//				modelMap.put("errorList", list);
//				return new ResponseEntity<>(modelMap, HttpStatus.BAD_REQUEST);
//			}
//		}else {
//			modelMap.put("success", false);
//			modelMap.put("errorMsg", "EL OBJECTO INVOICE NO PUEDE SER NULO O ESTAR VACIO.");
//			return new ResponseEntity<>(modelMap, HttpStatus.BAD_REQUEST);
//		}
		return null;
	}
	
	@RequestMapping(value ="/integ/invoice/getInvoice", method = RequestMethod.GET)
//	ResponseEntity<Map<String, Object>> getInvoice(@RequestParam String valor){
	ResponseEntity<Map<String, Object>> getInvoice(){
		Map<String,Object> modelMap = new HashMap<String,Object>(2);
//		if(i != null) {
//			List<RESTInvoiceRespDTO> list = invoicingService.createInvoiceByREST(i);
//			if(list != null && list.isEmpty()) {
//				modelMap.put("success", false);
//				modelMap.put("msg", "SOLICITUD EN PROCESO.");
//				return new ResponseEntity<>(modelMap, HttpStatus.CREATED);
//			}else {
//				modelMap.put("success", false);
//				modelMap.put("errorList", list);
//				return new ResponseEntity<>(modelMap, HttpStatus.BAD_REQUEST);
//			}
//		}else {
//			modelMap.put("success", false);
//			modelMap.put("errorMsg", "EL OBJECTO INVOICE NO PUEDE SER NULO O ESTAR VACIO.");
//			return new ResponseEntity<>(modelMap, HttpStatus.BAD_REQUEST);
//		}
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
