package com.smartech.invoicing.integration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.smartech.invoicing.dto.CategoryDTO;
import com.smartech.invoicing.dto.ItemsDTO;
import com.smartech.invoicing.integration.service.HTTPRequestService;
import com.smartech.invoicing.integration.util.AppConstants;
import com.smartech.invoicing.integration.util.PayloadProducer;
import com.smartech.invoicing.util.NullValidator;

@Service("soapService")
public class SOAPServiceImpl implements SOAPService {

	@Autowired
	HTTPRequestService httpRequestService;
	
	static Logger log = Logger.getLogger(SOAPService.class.getName());
	
	@Override
	public ItemsDTO getItemDataByItemNumberOrgCode(String itemNumber, String orgCode) {
		ItemsDTO item = null;
		JSONObject xmlJSONObj;
		JSONObject json;
		JsonElement jelement;
		JsonObject jobject;
		if((itemNumber != null && !"".contains(itemNumber)) && (itemNumber != null && !"".contains(itemNumber))) {
			try {
				Map<String, Object> request1 = httpRequestService.httpXMLRequest(AppConstants.URL_SOAP_ITEMSV2, 
																	PayloadProducer.getItemDataByItemNumberOrgCode(itemNumber, orgCode),AppConstants.ORACLE_USER + ":" + AppConstants.ORACLE_PASS);;
				String strResponse1 = (String) request1.get("response");
				int codeResponse1 = (int) request1.get("code");
				String strHttpResponse1 = (String) request1.get("httpResponse");
				
				if(codeResponse1 >= 200 && codeResponse1 < 300) {
					if(strResponse1 != null && !"".contains(strResponse1)) {
						xmlJSONObj = XML.toJSONObject(strResponse1, true);
						jelement = new JsonParser().parse(xmlJSONObj.toString());
						jobject = jelement.getAsJsonObject();
						if(jobject.get("env:Envelope").getAsJsonObject().get("env:Body").getAsJsonObject().has("ns0:findItemResponse")) {
							if(jobject.get("env:Envelope").getAsJsonObject().get("env:Body").getAsJsonObject()
									.get("ns0:findItemResponse").getAsJsonObject().get("ns2:result").getAsJsonObject().has("ns0:Value")) {
								JsonObject result = jobject.get("env:Envelope").getAsJsonObject().get("env:Body").getAsJsonObject()
										.get("ns0:findItemResponse").getAsJsonObject().get("ns2:result").getAsJsonObject().get("ns0:Value").getAsJsonObject();
								
								if(!result.isJsonNull()) {
									item = new ItemsDTO();
									item.setItemNumber(result.get("ns1:ItemNumber").getAsString());
									item.setItemDescription(result.get("ns1:ItemDescription").getAsString());
									if(result.has("ns1:ItemCategory")) {
										List<CategoryDTO> catList = new ArrayList<CategoryDTO>();
										if(result.get("ns1:ItemCategory").isJsonArray()) {
											JsonArray jsonarray = result.get("ns1:ItemCategory").getAsJsonArray();
											for (int i = 0; i < jsonarray.size(); i++) {
												CategoryDTO c = new CategoryDTO();
												JsonElement op = jsonarray.get(i).getAsJsonObject();
												c.setCatalogCode(NullValidator.isNull(op.getAsJsonObject().get("ns1:ItemCatalog").toString()));
												c.setCategoryName(NullValidator.isNull(op.getAsJsonObject().get("ns1:CategoryName").toString()));
												if(!c.getCatalogCode().isEmpty()) {
													catList.add(c);
												}
											}
										}else {
											CategoryDTO c = new CategoryDTO();
											c.setCatalogCode(NullValidator.isNull(result.get("ns1:ItemCategory").getAsJsonObject().get("ns1:ItemCatalog").toString()));
											c.setCategoryName(NullValidator.isNull(result.get("ns1:ItemCategory").getAsJsonObject().get("ns1:CategoryName").toString()));
											if(!c.getCatalogCode().isEmpty()) {
												catList.add(c);
											}
										}
										item.setItemCategory(catList);
									}
								}
							}
															 
						}
					}
				}
			}catch(Exception e) {
				e.printStackTrace();
				log.error("ERROR AL OBTENER WS DE ITEMS_V2 - getItemDataByItemNumberOrgCode***************************", e);
			}
		}
		return item;
	}

	
	
}
