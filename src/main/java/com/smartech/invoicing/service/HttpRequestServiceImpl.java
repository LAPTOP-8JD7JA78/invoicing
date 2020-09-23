package com.smartech.invoicing.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.stereotype.Service;

@Service("httpRequestService")
public class HttpRequestServiceImpl implements HttpRequestService{
	@Override
	@SuppressWarnings({ "restriction", "static-access" })
	public String httpXmlRequest(String destUrl, String body, String authStr) {
		
		try {
			System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
			URL url = new URL(null, destUrl, new sun.net.www.protocol.https.Handler());
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

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

			//J.Avila - 20Mar2019
			if(!"".equals(authStr)) {
				byte[] authBytes = authStr.getBytes("UTF-8");
				String auth = com.sun.org.apache.xml.internal.security.utils.Base64.encode(authBytes);
				conn.setRequestProperty("Authorization", "Basic " + auth);
			}

			OutputStream out = conn.getOutputStream();
			OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8");
			if(body != null) {
				writer.write(body);
			}else {
				return "Body is null";
			}
			writer.close();
			out.close();
			String response = readFullyAsString(conn.getInputStream(), "UTF-8");
			conn.disconnect();
			return response;
		}catch(Exception e) {
			e.printStackTrace();
			return e.toString();
		}
		
	}
	
	@Override
	public String httpRestRequest(String destUrl, String authStr) {
		
		try {
			URL url = new URL(destUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			if (conn == null) {
				return null;
			}
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "*/*");
			conn.setRequestProperty("User-Agent", "Java Client");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setAllowUserInteraction(false);
			
			//J.Avila - 20Mar2019
			if(!"".equals(authStr)) {
				byte[] authBytes = authStr.getBytes("UTF-8");
				String auth = com.sun.org.apache.xml.internal.security.utils.Base64.encode(authBytes);
				conn.setRequestProperty("Authorization", "Basic " + auth);
			}
			
			String response = readFullyAsString(conn.getInputStream(), "UTF-8");
			 conn.disconnect();			

			return response;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	@Override
	public String httpRestDeleteRequest(String destUrl, String authStr) {
		HttpURLConnection conn = null;
		try {
			URL url = new URL(destUrl);
			conn = (HttpURLConnection) url.openConnection();
			if (conn == null) {
				return null;
			}
			conn.setRequestMethod("DELETE");
			conn.setDoOutput(true);
			
			if(!"".equals(authStr)) {
				byte[] authBytes = authStr.getBytes("UTF-8");
				String auth = com.sun.org.apache.xml.internal.security.utils.Base64.encode(authBytes);
				conn.setRequestProperty("Authorization", "Basic " + auth);
			}
			int a = conn.getResponseCode();
			conn.disconnect();
			
			return String.valueOf(a);
		}catch(IOException fe) {
			fe.printStackTrace();
			return "404";
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
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

}
