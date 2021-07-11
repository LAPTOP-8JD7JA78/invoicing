package com.smartech.invoicingprod.distribuitorportal.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpMethod;

import com.smartech.invoicingprod.distribuitorportal.dto.HeadersRestDTO;
import com.smartech.invoicingprod.distribuitorportal.dto.ParamsRestDTO;


public interface HTTPRequestDistribuitorsService {

	public Map<String, Object> httpXMLRequest(String url, String bdy, String auth);
	public String readFullyAsString(InputStream inputStream, String encoding) throws IOException;
	public Map<String, Object> httpRESTRequest(String user, String pass, String url, HttpMethod method, List<HeadersRestDTO> headers, List<ParamsRestDTO> params, Object body);
	public String getPlainCreds(String user, String pass);
}
