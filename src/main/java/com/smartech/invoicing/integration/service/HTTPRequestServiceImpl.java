package com.smartech.invoicing.integration.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Base64;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.stereotype.Service;

@Service("hTTPRequestService")
public class HTTPRequestServiceImpl implements HTTPRequestService {

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
				String auth = Base64.getEncoder().encodeToString(authBytes);
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
