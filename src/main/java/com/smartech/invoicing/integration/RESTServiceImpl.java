package com.smartech.invoicing.integration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.smartech.invoicing.integration.dto.HeadersRestDTO;
import com.smartech.invoicing.integration.dto.ParamsRestDTO;
import com.smartech.invoicing.integration.json.invorg.InventoryOrganization;
import com.smartech.invoicing.integration.service.HTTPRequestService;
import com.smartech.invoicing.integration.util.AppConstants;

@Service("restService")
public class RESTServiceImpl implements RESTService {

	@Autowired
	HTTPRequestService httpRequestService;
	
	@Override
	public InventoryOrganization getInventoryOrganization() {
		try {
			List<HeadersRestDTO> headers = new ArrayList<HeadersRestDTO>();
			headers.add(new HeadersRestDTO("Content-Type", "application/json"));
			headers.add(new HeadersRestDTO("Accept", "*/*"));
			headers.add(new HeadersRestDTO("User-Agent", "Java Client"));
			List<ParamsRestDTO> params = new ArrayList<ParamsRestDTO>();
			params.add(new ParamsRestDTO("q", "InventoryFlag=true"));
			params.add(new ParamsRestDTO("onlyData", true));
			params.add(new ParamsRestDTO("offset", 0));
			params.add(new ParamsRestDTO("limit", 100));
			
			Map<String, Object> response = httpRequestService.httpRESTRequest(AppConstants.ORACLE_USER, AppConstants.ORACLE_PASS,
					AppConstants.URL_REST_INVORG, HttpMethod.GET, headers, params, null, AppConstants.SERVICE_REST_TEST1);
			
			int statusCode;
			InventoryOrganization responseRest;
			
			if(response != null) {
				statusCode = (int) response.get("code");
				responseRest = (InventoryOrganization) response.get("response");
				if(statusCode >= 200 && statusCode < 300) {
					return responseRest;
				}
			}
			
			return null;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
