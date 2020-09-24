package com.smartech.invoicing.integration.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.stereotype.Service;

@Service("hTTPRequestService")
public class HTTPRequestServiceImpl implements HTTPRequestService {

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
				String authBasic = Base64.getEncoder().encodeToString(authBytes);
				conn.setRequestProperty("Authorization", "Basic " + authBasic);
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
			io.printStackTrace();
			modelMap.put("code", 406);
			modelMap.put("response", io.getMessage());
			modelMap.put("httpResponse", io.getCause());
			return modelMap;
			
		}catch(Exception e) {
			e.printStackTrace();
			modelMap.put("code", 500);
			modelMap.put("response", e.getMessage());
			modelMap.put("httpResponse", e.getCause());
			return modelMap;
		}
	}
}
