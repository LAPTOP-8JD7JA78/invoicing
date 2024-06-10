package com.smartech.invoicingprod.integration.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.smartech.invoicingprod.dto.responseInsertInvoiceDTO;
import com.smartech.invoicingprod.integration.dto.HeadersRestDTO;
import com.smartech.invoicingprod.integration.dto.ParamsRestDTO;
import com.smartech.invoicingprod.integration.json.IncotermByRest.IncotermByRest;
import com.smartech.invoicingprod.integration.json.dailyRates.CurrencyRates;
import com.smartech.invoicingprod.integration.json.inventoryItemSerialNumbers.InventoryItemSerialNumbers;
import com.smartech.invoicingprod.integration.json.invitemlot.InventoryItemLots;
import com.smartech.invoicingprod.integration.json.invorg.InventoryOrganization;
import com.smartech.invoicingprod.integration.json.itemCategories.ItemCategory;
import com.smartech.invoicingprod.integration.json.priceList.PriceLists;
import com.smartech.invoicingprod.integration.json.priceListByItem.PriceListByItem;
import com.smartech.invoicingprod.integration.json.receivablesInvoices.ReceivablesInvoices;
import com.smartech.invoicingprod.integration.json.salesorder.SalesOrder;
import com.smartech.invoicingprod.integration.json.salesorderai.SalesOrderAI;
import com.smartech.invoicingprod.integration.json.standardReceipts.StandardReceipts;
import com.smartech.invoicingprod.integration.json.unitCost.ItemCosts;
import com.smartech.invoicingprod.integration.util.AppConstants;

@Service("hTTPRequestService")
public class HTTPRequestServiceImpl implements HTTPRequestService {
	
	static Logger log = Logger.getLogger(HTTPRequestServiceImpl.class.getName());
	
	@Override
	public String readFullyAsString(InputStream inputStream, String encoding) throws IOException {
        return readFully(inputStream).toString(encoding);
    }
	
	private ByteArrayOutputStream readFully(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = inputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        return baos;
    }

	@SuppressWarnings({ "restriction", "static-access" })
	@Override
	public Map<String, Object> httpXMLRequest(String url, String bdy, String auth) {
		Map<String,Object> modelMap = new HashMap<String,Object>(3);

		try {
			System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
			URL urlRequest = new URL(null, url, new sun.net.www.protocol.https.Handler());
			HttpsURLConnection conn = (HttpsURLConnection) urlRequest.openConnection();

			if (conn == null) {
				return null;
			}
			
			conn.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			conn.setRequestProperty("Accept", "*/*");
			conn.setRequestProperty("User-Agent", "Java Client");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setFollowRedirects(true);
			conn.setAllowUserInteraction(false);
			conn.setConnectTimeout(600000);

			if(!"".equals(auth)) {
				byte[] authBytes = auth.getBytes("UTF-8");
				byte[] base64Creds  = Base64.encodeBase64(authBytes);
				String base64CredsStr = new String(base64Creds);
				conn.setRequestProperty("Authorization", "Basic " + base64CredsStr);
			}

			OutputStream out = conn.getOutputStream();
			OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8");
			
			if(bdy != null) {
				writer.write(bdy);
			}else {
				modelMap.put("code", 400);
				modelMap.put("response", "");
				modelMap.put("httpResponse", "La solicitud no cuenta con XML en el Body.");
				return modelMap;
			}
			
			writer.close();
			out.close();
			String response = readFullyAsString(conn.getInputStream(), "UTF-8");
			
			int code = conn.getResponseCode();
			String httpResponse = conn.getResponseMessage();
			conn.disconnect();
			
			modelMap.put("code", code);
			modelMap.put("response", response);
			modelMap.put("httpResponse", httpResponse);
			
			return modelMap;
		}catch(IOException io) {
			log.error("XML REQUEST FAIL - " + url, io);
			modelMap.put("code", 406);
			modelMap.put("response", io.getMessage());
			modelMap.put("httpResponse", io.getCause());
			return modelMap;
			
		}catch(Exception e) {
			log.error("XML REQUEST FAIL - " + url, e);
			modelMap.put("code", 500);
			modelMap.put("response", e.getMessage());
			modelMap.put("httpResponse", e.getCause());
			return modelMap;
		}
	}

	@Override
	public String getPlainCreds(String user, String pass) {
		String plainCreds = user + ":" + pass;
		byte[] plainCredsBytes = plainCreds.getBytes();
		byte[] base64Creds  = Base64.encodeBase64(plainCredsBytes);
		String base64CredsStr = new String(base64Creds);
		return base64CredsStr;
	}
	
	private RestTemplate restTemplateFactory() {
		RestTemplate rt = new RestTemplate();
		rt.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		rt.getMessageConverters().add(new StringHttpMessageConverter());
		return rt;
	}

	@Override
	public Map<String, Object> httpRESTRequest(String user, String pass, String url, HttpMethod method, List<HeadersRestDTO> headers,
			List<ParamsRestDTO> params, Object body, String service) {

		Map<String, Object> map = new HashMap<String, Object>(3);
		RestTemplate rt = restTemplateFactory();
		
		HttpHeaders h = new HttpHeaders();
		if(user != null && pass != null) {
			h.add("Authorization", "Basic " + getPlainCreds(user, pass));
		}
		if(headers != null && !headers.isEmpty()) {
			for(HeadersRestDTO head: headers) {
				h.add(head.getHeaderName(), head.getHeaderValue());
			}
		}
		
		if(params != null && !params.isEmpty()) {
			url = url + "?";
			for(ParamsRestDTO param: params) {
				url = url + param.getParamName() + "=" + param.getParamValue() + "&";
			}
			url = url.substring(0, url.length() - 1);
		}
	
		HttpEntity<String> re ;
		
		if(body != null) {
			String jsonInString = new Gson().toJson(body);
			JSONObject mJSONObject = new JSONObject(jsonInString);
			
			re = new HttpEntity<String>(mJSONObject.toString(), h);
		}else {
			re = new HttpEntity<String>(h);
		}
					
		try {
			switch(service) {
				case AppConstants.SERVICE_REST_TEST1:
					ResponseEntity<InventoryOrganization> resp = rt.exchange(url, method, re, InventoryOrganization.class);
					map.put("code", resp.getStatusCode().value());
					map.put("response", resp.hasBody()?resp.getBody():null);
					map.put("httpResponse", resp.getHeaders());
					break;
				case AppConstants.SERVICE_SALES_ORDER_1:
					ResponseEntity<SalesOrder> respSO1 = rt.exchange(url, method, re, SalesOrder.class);
					map.put("code", respSO1.getStatusCode().value());
					map.put("response", respSO1.hasBody()?respSO1.getBody():null);
					map.put("httpResponse", respSO1.getHeaders());
					break;
				case AppConstants.SERVICE_SALES_ORDER_AI_1:
					ResponseEntity<SalesOrderAI> respSO2 = rt.exchange(url, method, re, SalesOrderAI.class);
					map.put("code", respSO2.getStatusCode().value());
					map.put("response", respSO2.hasBody()?respSO2.getBody():null);
					map.put("httpResponse", respSO2.getHeaders());
					break;
				case AppConstants.SERVICE_REST_ITEMLOT:
					ResponseEntity<InventoryItemLots> respSO3 = rt.exchange(url, method, re, InventoryItemLots.class);
					map.put("code", respSO3.getStatusCode().value());
					map.put("response", respSO3.hasBody()?respSO3.getBody():null);
					map.put("httpResponse", respSO3.getHeaders());
					break;
				case AppConstants.SERVICE_REST_CURRENCY_RATES:
					ResponseEntity<CurrencyRates> respSO4 = rt.exchange(url, method, re, CurrencyRates.class);
					map.put("code", respSO4.getStatusCode().value());
					map.put("response", respSO4.hasBody()?respSO4.getBody():null);
					map.put("httpResponse", respSO4.getHeaders());
					break;
				case AppConstants.SERVICE_REST_PRICE_LIST:
					ResponseEntity<PriceLists> respSO5 = rt.exchange(url, method, re, PriceLists.class);
					map.put("code", respSO5.getStatusCode().value());
					map.put("response", respSO5.hasBody()?respSO5.getBody():null);
					map.put("httpResponse", respSO5.getHeaders());
					break;
				case AppConstants.SERVICE_REST_PRICE_LIST_BY_ITEM:
					ResponseEntity<PriceListByItem> respSO6 = rt.exchange(url, method, re, PriceListByItem.class);
					map.put("code", respSO6.getStatusCode().value());
					map.put("response", respSO6.hasBody()?respSO6.getBody():null);
					map.put("httpResponse", respSO6.getHeaders());
					break;
				case AppConstants.SERVICE_REST_SALES_ORDER_INCOTERM:
					ResponseEntity<IncotermByRest> respSO7 = rt.exchange(url, method, re, IncotermByRest.class);
					map.put("code", respSO7.getStatusCode().value());
					map.put("response", respSO7.hasBody()?respSO7.getBody():null);
					map.put("httpResponse", respSO7.getHeaders());
					break;
				case AppConstants.SERVICE_REST_ITEM_CATEGORY:
					ResponseEntity<ItemCategory> respSO8 = rt.exchange(url, method, re, ItemCategory.class);
					map.put("code", respSO8.getStatusCode().value());
					map.put("response", respSO8.hasBody()?respSO8.getBody():null);
					map.put("httpResponse", respSO8.getHeaders());
					break;
				case AppConstants.SERVICE_REST_ITEM_SERIAL_NUMBER:
					ResponseEntity<InventoryItemSerialNumbers> respSO9 = rt.exchange(url, method, re, InventoryItemSerialNumbers.class);
					map.put("code", respSO9.getStatusCode().value());
					map.put("response", respSO9.hasBody()?respSO9.getBody():null);
					map.put("httpResponse", respSO9.getHeaders());
					break;
				case AppConstants.SERVICE_REST_ITEM_COSTS:
					ResponseEntity<ItemCosts> respS10 = rt.exchange(url, method, re, ItemCosts.class);
					map.put("code", respS10.getStatusCode().value());
					map.put("response", respS10.hasBody()?respS10.getBody():null);
					map.put("httpResponse", respS10.getHeaders());
					break;
				case AppConstants.SERVICE_REST_RECEIVABLES_INVOICES:
					ResponseEntity<ReceivablesInvoices> respS11 = rt.exchange(url, method, re, ReceivablesInvoices.class);
					map.put("code", respS11.getStatusCode().value());
					map.put("response", respS11.hasBody()?respS11.getBody():null);
					map.put("httpResponse", respS11.getHeaders());
					break;
				case AppConstants.SERVICE_REST_STANDARD_RECEIPT:
					ResponseEntity<StandardReceipts> respS12 = rt.exchange(url, method, re, StandardReceipts.class);
					map.put("code", respS12.getStatusCode().value());
					map.put("response", respS12.hasBody()?respS12.getBody():null);
					map.put("httpResponse", respS12.getHeaders());
					break;
				case AppConstants.SERVICE_REST_INSERT_AR_INVOICE:
//					Object respS13 = rt.exchange(url, method, re, Object.class);
//					System.out.print("Hola");
					ResponseEntity<responseInsertInvoiceDTO> respS13 = rt.exchange(url, method, re, responseInsertInvoiceDTO.class);
//					String var1 = respS13.toString();
					map.put("code", respS13.getStatusCode().value());
					map.put("response", respS13.toString() != null?respS13.toString():null);
					map.put("httpResponse", respS13.getHeaders());
					break;
				case AppConstants.SERVICE_REST_RECEIVABLES_INVOICES2:
					ResponseEntity<responseInsertInvoiceDTO> respS14 = rt.exchange(url, method, re, responseInsertInvoiceDTO.class);
					map.put("code", respS14.getStatusCode().value());
					map.put("response", respS14.hasBody()?respS14.getBody():null);
					map.put("httpResponse",respS14.getHeaders());
					break;
			}		
		}catch(Exception e) {
			log.error("REST REQUEST FAIL - " + service + "********************");
			log.error("REST REQUEST FAIL - " + url, e);
			e.printStackTrace();
			map.put("code", 400);
			map.put("response", null);
			map.put("httpResponse", e.getCause());
		}
		return map;
	}
}
