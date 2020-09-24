package com.smartech.invoicing.integration;

import java.io.StringReader;
import java.util.Map;

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
				Map<String, Object> request1 = httpRequestService.httpXMLRequest(AppConstants.URL_ANALYTICS_SESSION, 
																	PayloadProducer.getSessionIdSOAPXmlContent(user, pass), 
																	"");
				
				String strResponse1 = (String) request1.get("response");
				int codeResponse1 = (int) request1.get("code");
				String strHttpResponse1 = (String) request1.get("httpResponse");
				
				if(codeResponse1 >= 200 && codeResponse1 < 300) {
					if(strResponse1 != null && !"".contains(strResponse1)) {
						xmlJSONObj = XML.toJSONObject(strResponse1, true);
						jelement = new JsonParser().parse(xmlJSONObj.toString());
						jobject = jelement.getAsJsonObject();
						result = jobject.get("soap:Envelope").getAsJsonObject().get("soap:Body").getAsJsonObject()
								.get("sawsoap:logonResult").getAsJsonObject();
						String contentId = result.getAsJsonObject().get("sawsoap:sessionID").toString();
						json = new JSONObject(contentId);
						sessionId = json.getString("content");
					}
				
					httpRequestService.httpXMLRequest(AppConstants.URL_ANALYTICS_SESSION, 
													  PayloadProducer.keepSessionIdSOAPXmlContent(sessionId), 
													  "");
				}
				
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
				case AppConstants.SERVICE_AR_REPORT_INVOICES:
					request = PayloadProducer.getArReportInvoice(sessionId, dto.getAr_Report_date());
					break;
			}

			if(!"".contains(request)) {
				//ejecutar 
				try {
					Map<String, Object> request2 = httpRequestService.httpXMLRequest(AppConstants.URL_ANALYTICS,
																					request, 
																					"");
					
					String strResponse2 = (String) request2.get("response");
					int codeResponse2 = (int) request2.get("code");
					String strHttpResponse2 = (String) request2.get("httpResponse");
					
					if(codeResponse2 >= 200 && codeResponse2 < 300) {
						if(strResponse2 != null && !"".contains(strResponse2)) {
							xmlJSONObj = XML.toJSONObject(strResponse2, true);
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
					}
					
					httpRequestService.httpXMLRequest(AppConstants.URL_ANALYTICS_SESSION,
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
