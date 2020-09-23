package com.smartech.invoicing.integration;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.smartech.invoicing.integration.dto.AnalyticsDTO;
import com.smartech.invoicing.integration.service.HTTPRequestService;
import com.smartech.invoicing.integration.util.AppConstants;
import com.smartech.invoicing.integration.util.PayloadProducer;
import com.smartech.invoicing.integration.xml.rowset.Rowset;

@Service("analyticsService")
public class AnalyticsServiceImpl implements AnalyticsService {

	@Autowired
	HTTPRequestService httpRequestService;
	
	@Override
	public Rowset executeAnalyticsWS(String user, String pass, String service, AnalyticsDTO dto) {
		
		if((user != null && pass != null) && (!"".contains(user) && !"".contains(pass))) {
			String sessionId = "";
			Rowset response = new Rowset();
			JSONObject xmlJSONObj;
			JSONObject json;
			JsonElement jelement;
			JsonObject jobject;
			JsonElement result;	
			//getSessionId--------------------------------------------------------
			try {
				String request1 = httpRequestService.httpXmlRequest(AppConstants.URL_ANALYTICS_SESSION, 
																	PayloadProducer.getSessionIdSOAPXmlContent(user, pass), 
																	"");
				if(request1 != null && !"".contains(request1)) {
					xmlJSONObj = XML.toJSONObject(request1, true);
					jelement = new JsonParser().parse(xmlJSONObj.toString());
					jobject = jelement.getAsJsonObject();
					result = jobject.get("soap:Envelope").getAsJsonObject().get("soap:Body").getAsJsonObject()
							.get("sawsoap:logonResult").getAsJsonObject();
					String contentId = result.getAsJsonObject().get("sawsoap:sessionID").toString();
					json = new JSONObject(contentId);
					sessionId = json.getString("content");
				}
				
				httpRequestService.httpXmlRequest(AppConstants.URL_ANALYTICS_SESSION, 
												  PayloadProducer.keepSessionIdSOAPXmlContent(sessionId), 
												  "");
				
				if("".contains(sessionId)) {
					return null;
				}
				
				
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			String request = "";
			
			switch(service) {
				case AppConstants.SERVICE_TEST1:
					request = PayloadProducer.getTestAnalytics1(sessionId);
					break;
			}

			if(!"".contains(request)) {
				//ejecutar 
				try {
					String request2 = httpRequestService.httpXmlRequest(AppConstants.URL_ANALYTICS,
																		request, 
																		"");
					if(request2 != null && !"".contains(request2)) {
						xmlJSONObj = XML.toJSONObject(request2, true);
						jelement = new JsonParser().parse(xmlJSONObj.toString());
						jobject = jelement.getAsJsonObject();
						
						result = jobject.get("soap:Envelope").getAsJsonObject().get("soap:Body").getAsJsonObject()
								.get("sawsoap:executeSQLQueryResult").getAsJsonObject().get("sawsoap:return").getAsJsonObject();
						String rowSet = result.getAsJsonObject().get("sawsoap:rowset").toString();
						rowSet = rowSet.replace("\\\"", "\"");
						rowSet = rowSet.replace("\"<", "<");
						rowSet = rowSet.replace(">\"", ">");
						rowSet = rowSet.replace("xmlns=\"urn:schemas-microsoft-com:xml-analysis:rowset\"", "");
				
						StringReader sr = new StringReader(rowSet);
						JAXBContext jaxbContext = JAXBContext.newInstance(Rowset.class);
						Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
						Rowset rsList = (Rowset) unmarshaller.unmarshal(sr);
						response = rsList;
					}
					
					httpRequestService.httpXmlRequest(AppConstants.URL_ANALYTICS_SESSION,
													  PayloadProducer.getSessionLogOffSOAPXmlContent(sessionId), 
													  "");
					
					return response;
				}catch(Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		}
		
		return null;
	}

}
