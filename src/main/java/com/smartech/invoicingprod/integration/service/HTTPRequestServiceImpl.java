package com.smartech.invoicing.integration.service;

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
import com.smartech.invoicing.integration.dto.HeadersRestDTO;
import com.smartech.invoicing.integration.dto.ParamsRestDTO;
import com.smartech.invoicing.integration.json.dailyRates.CurrencyRates;
import com.smartech.invoicing.integration.json.invitemlot.InventoryItemLots;
import com.smartech.invoicing.integration.json.invorg.InventoryOrganization;
import com.smartech.invoicing.integration.json.salesorder.SalesOrder;
import com.smartech.invoicing.integration.json.salesorderai.SalesOrderAI;
import com.smartech.invoicing.integration.util.AppConstants;

@Service("hTTPRequestService")
public class HTTPRequestServiceImpl implements HTTPRequestService {
	
	static Logger log = Logger.getLogger(HTTPRequestServiceImpl.class.getName());

//	@Override
//	@SuppressWarnings({ "restriction", "static-access" })
//	public String httpXmlRequest(String destUrl, String body, String authStr) {
//		
//		try {
//			System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
//			URL url = new URL(null, destUrl, new sun.net.www.protocol.https.Handler());
//			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
//
//			if (conn == null) {
//				return null;
//			}
//			
//			conn.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
//			conn.setRequestProperty("Accept", "*/*");
//			conn.setRequestProperty("User-Agent", "Java Client");
//			conn.setDoOutput(true);
//			conn.setDoInput(true);
//			conn.setUseCaches(false);
//			conn.setFollowRedirects(true);
//			conn.setAllowUserInteraction(false);
//			conn.setConnectTimeout(600000);
//
//			if(!"".equals(authStr)) {
//				byte[] authBytes = authStr.getBytes("UTF-8");
//				String auth = Base64.getEncoder().encodeToString(authBytes);
//				conn.setRequestProperty("Authorization", "Basic " + auth);
//			}
//
//			OutputStream out = conn.getOutputStream();
//			OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8");
//			
//			if(body != null) {
//				writer.write(body);
//			}else {
//				return "Body is null";
//			}
//			writer.close();
//			out.close();
//			String response = readFullyAsString(conn.getInputStream(), "UTF-8");
//			conn.disconnect();
//			return response;
//		}catch(Exception e) {
//			e.printStackTrace();
//			return e.toString();
//		}
//		
//	}
	
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
//			io.printStackTrace();
			modelMap.put("code", 406);
			modelMap.put("response", io.getMessage());
			modelMap.put("httpResponse", io.getCause());
			return modelMap;
			
		}catch(Exception e) {
			log.error("XML REQUEST FAIL - " + url, e);
//			e.printStackTrace();
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
