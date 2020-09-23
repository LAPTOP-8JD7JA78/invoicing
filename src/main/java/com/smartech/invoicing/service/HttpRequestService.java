package com.smartech.invoicing.service;

import java.io.IOException;
import java.io.InputStream;

public interface HttpRequestService {
	public String httpXmlRequest(String destUrl, String body, String authStr);
	
	public String httpRestRequest(String destUrl, String authStr);
	
	public String httpRestDeleteRequest(String destUrl, String authStr);
	
	public String readFullyAsString(InputStream inputStream, String encoding) throws IOException;
}
