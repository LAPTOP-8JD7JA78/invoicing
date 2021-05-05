package com.smartech.invoicing.integration.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpMethod;

import com.smartech.invoicing.integration.dto.HeadersRestDTO;
import com.smartech.invoicing.integration.dto.ParamsRestDTO;

public interface HTTPRequestService {
//	public String httpXmlRequest(String destUrl, String body, String authStr);
	public Map<String, Object> httpXMLRequest(String url, String bdy, String auth);
	public String readFullyAsString(InputStream inputStream, String encoding) throws IOException;
	public Map<String, Object> httpRESTRequest(String user, String pass, String url, HttpMethod method, List<HeadersRestDTO> headers, List<ParamsRestDTO> params, Object body, String service);
	public String getPlainCreds(String user, String pass);
}
