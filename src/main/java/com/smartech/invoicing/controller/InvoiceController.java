package com.smartech.invoicing.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InvoiceController {
	
	@RequestMapping(value ="/integ/invoice/createInvoice", method = RequestMethod.POST)
	ResponseEntity<Map<String, Object>> createInvoice(){
		
		
		return null;
	}
}
