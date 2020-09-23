package com.smartech.invoicing.integration.service;

import java.io.IOException;
import java.io.InputStream;

public interface HTTPRequestService {
	public String httpXmlRequest(String destUrl, String body, String authStr);
	public String readFullyAsString(InputStream inputStream, String encoding) throws IOException;
	
}
