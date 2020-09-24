package com.smartech.invoicing.integration.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public interface HTTPRequestService {
//	public String httpXmlRequest(String destUrl, String body, String authStr);
	public Map<String, Object> httpXMLRequest(String url, String bdy, String auth);
	public String readFullyAsString(InputStream inputStream, String encoding) throws IOException;
	
}
