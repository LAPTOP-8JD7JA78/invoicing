package com.smartech.invoicing.integration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.smartech.invoicing.integration.dto.HeadersRestDTO;
import com.smartech.invoicing.integration.dto.ParamsRestDTO;
import com.smartech.invoicing.integration.json.invorg.InventoryOrganization;
import com.smartech.invoicing.integration.json.salesorder.SalesOrder;
import com.smartech.invoicing.integration.service.HTTPRequestService;
import com.smartech.invoicing.integration.util.AppConstants;

@Service("restService")
public class RESTServiceImpl implements RESTService {

	@Autowired
	HTTPRequestService httpRequestService;
	
	static Logger log = Logger.getLogger(RESTServiceImpl.class.getName());
	
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
			log.error("REST API SERVICE FAIL getInventoryOrganization ****************************", e);
			return null;
		}
	}

	@Override
	public SalesOrder getSalesOrderByOrderNumber(String orderNumber) {
		try {
			if(orderNumber != null && !"".contains(orderNumber)) {
				List<HeadersRestDTO> headers = new ArrayList<HeadersRestDTO>();
				headers.add(new HeadersRestDTO("Content-Type", "application/json"));
				headers.add(new HeadersRestDTO("Accept", "*/*"));
				headers.add(new HeadersRestDTO("User-Agent", "Java Client"));
				List<ParamsRestDTO> params = new ArrayList<ParamsRestDTO>();
				params.add(new ParamsRestDTO("finder", "findByOrderNumber;OrderNumber=" + orderNumber));
				params.add(new ParamsRestDTO("q", "StatusCode=CLOSED"));
				params.add(new ParamsRestDTO("expand", "lines,lines.lotSerials,lines.charges.chargeComponents,totals"));
				params.add(new ParamsRestDTO("fields", "HeaderId,OrderNumber,SourceTransactionNumber,BusinessUnitId,BusinessUnitName,RequestedFulfillmentOrganizationId,RequestedFulfillmentOrganizationCode,PaymentTermsCode,PaymentTerms,TransactionalCurrencyCode,TransactionalCurrencyName,CurrencyConversionRate,CurrencyConversionType,StatusCode;totals:TotalCode,TotalAmount,CurrencyCode;lines:SourceTransactionLineNumber,AssessableValue,FulfilledQuantity,ProductId,ProductNumber,ProductDescription,InventoryOrganizationCode,OrderedQuantity,OrderedUOMCode,OrderedUOM,StatusCode,TaxClassificationCode,TaxClassification,LineNumber;lines.charges:ChargeTypeCode;lines.charges.chargeComponents:PriceElementCode,PriceElement,HeaderCurrencyUnitPrice,HeaderCurrencyExtendedAmount,PercentOfComparisonElement;lines.lotSerials:ItemSerialNumberFrom,ItemSerialNumberTo,LotNumber"));
				params.add(new ParamsRestDTO("onlyData", true));
				params.add(new ParamsRestDTO("offset", 0));
				params.add(new ParamsRestDTO("limit", 1));
				
				Map<String, Object> response = httpRequestService.httpRESTRequest(AppConstants.ORACLE_USER, AppConstants.ORACLE_PASS,
						AppConstants.URL_REST_SALESORDER, HttpMethod.GET, headers, params, null, AppConstants.SERVICE_SALES_ORDER_1);
				
				int statusCode;
				SalesOrder responseRest;
				
				if(response != null) {
					statusCode = (int) response.get("code");
					responseRest = (SalesOrder) response.get("response");
					if(statusCode >= 200 && statusCode < 300) {
						return responseRest;
					}
				}
			}
			
			return null;
		}catch(Exception e) {
			e.printStackTrace();
			log.error("REST API SERVICE FAIL getSalesOrderByOrderNumber ****************************", e);
			return null;
		}
	}

}
