package com.smartech.invoicing.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.smartech.invoicing.dto.RESTInvoiceRespDTO;
import com.smartech.invoicing.integration.service.InvoicingService;
import com.smartech.invoicing.model.Invoice;

@RestController
public class InvoiceController {
	
	@Autowired
	InvoicingService invoicingService;
	
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
}
