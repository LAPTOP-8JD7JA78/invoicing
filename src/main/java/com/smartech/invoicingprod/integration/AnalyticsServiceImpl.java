package com.smartech.invoicingprod.integration;

import java.io.StringReader;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.smartech.invoicingprod.integration.dto.AnalyticsDTO;
import com.smartech.invoicingprod.integration.service.HTTPRequestService;
import com.smartech.invoicingprod.integration.util.AppConstants;
import com.smartech.invoicingprod.integration.util.PayloadProducer;
import com.smartech.invoicingprod.integration.xml.rowset.Rowset;

@Service("analyticsService")
public class AnalyticsServiceImpl implements AnalyticsService {

	@Autowired
	HTTPRequestService httpRequestService;
	
	static Logger log = Logger.getLogger(AnalyticsServiceImpl.class.getName());
	
	@SuppressWarnings("unused")
	@Override
	public Rowset executeAnalyticsWS(String user, String pass, String service, AnalyticsDTO dto) {
		
		if((user != null && pass != null) && (!"".contains(user) && !"".contains(pass))) {
			String sessionId = "";
			Rowset response = new Rowset();
			JSONObject xmlJSONObj;
			JSONObject json;
			JsonElement jelement;
			JsonObject jobject;
			JsonElement result = null;	
			try {
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
							if(jobject.get("soap:Envelope").getAsJsonObject().get("soap:Body").getAsJsonObject().has("sawsoap:logonResult")) {
								result = jobject.get("soap:Envelope").getAsJsonObject().get("soap:Body").getAsJsonObject()
										.get("sawsoap:logonResult").getAsJsonObject();
								String contentId = result.getAsJsonObject().get("sawsoap:sessionID").toString();
								json = new JSONObject(contentId);
								sessionId = json.getString("content");
								httpRequestService.httpXMLRequest(AppConstants.URL_ANALYTICS_SESSION, 
																  PayloadProducer.keepSessionIdSOAPXmlContent(sessionId), 
																  "");
								log.info("***OBTENCIÓN DE LA SESSION - " + sessionId);
							}
						}
					}
					
					if("".contains(sessionId)) {
						return null;
					}
					
					
				}catch(Exception e) {
					e.printStackTrace();
					log.error("ERROR OBTENIENDO LA SESSION WS-ORACLE", e);
				}
				
				String request = "";
				
				switch(service) {
					case AppConstants.SERVICE_TEST1:
						request = PayloadProducer.getTestAnalytics1(sessionId);
						break;					
					case AppConstants.SERVICE_AR_REPORT_INVOICES:
						request = PayloadProducer.getArReportInvoice(sessionId, dto.getAr_Report_date());
						break;
					case AppConstants.SERVICE_AR_REPORT_PAYMENTS:
						request = PayloadProducer.getArReportPayments(sessionId, dto.getAr_Report_date());
						break;
					case AppConstants.SERVICE_AR_REPORT_TRANSFER:
						request = PayloadProducer.createTransferReport(sessionId, dto.getAr_Report_date());
						break;
					case AppConstants.SERVICE_ASSET_LABEL_REPORT:
						request = PayloadProducer.LabelProccess(sessionId, dto.getAr_Report_date());
						break;
					case AppConstants.SERVICE_RESPONSIVE_LETTER:
						request = PayloadProducer.updateLabel(sessionId, dto.getAr_Report_date());
						break;
					case AppConstants.SERVICE_ITEM_COST_FOR_SO:
						request = PayloadProducer.getItemCostByReportSo(sessionId, dto.getItemId(), dto.getSalesOrder());
						break;
					case AppConstants.SERVICE_AR_REPORT_INITIAL_CHARGE:
						request = PayloadProducer.getArReportInitialCharge(sessionId, dto.getAr_Report_date());
						break;
					case AppConstants.SERVICE_AR_RECEIPTS_REPORTS_HELP:
						request = PayloadProducer.getReportHelpPayments(sessionId, dto.getReceiptNumber());
						break;
					case AppConstants.SERVICE_AR_REPORT_DEBIT_MEMO:
						request = PayloadProducer.debitMemoRequest(sessionId, dto.getAr_Report_date());
						break;
					case AppConstants.SERVICE_AR_REPORT_NUMREGIDTRIB:
						request = PayloadProducer.searchNumRegIdTrib(sessionId, dto.getCustomerName());
						break;
					case AppConstants.SERVICE_AR_REPORT_TAX_REGIME:
						request = PayloadProducer.searchTaxRegime(sessionId, dto.getCustomerName());
						break;
					case AppConstants.SERVICE_AR_REPORT_GET_UUID:
						request = PayloadProducer.searchUUID(sessionId, dto.getTransactionNumber());
						break;//
					case AppConstants.SERVICE_AR_RECEIPTS_REPORTS_VALIDATION:
						request = PayloadProducer.receiptValidation(sessionId, dto.getReceiptId());
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
								
								if(jobject.get("soap:Envelope").getAsJsonObject().get("soap:Body").getAsJsonObject().has("sawsoap:executeSQLQueryResult")) {
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
									log.info("***OBTENCIÓN DE LOS DATOS EXITOSAMENTE****");
									return response;
								}else {
									log.error("ERROR OBTENIENDO LA PETICIÓN WS-ORACLE*****************************");
									log.error("ANALYTICS SERVICE: " + service);
								}
							}
						}	
					}catch(Exception e) {
						e.printStackTrace();
						log.error("ERROR OBTENIENDO LA PETICIÓN WS-ORACLE*****************************");
						log.error("ANALYTICS SERVICE: " + service, e);
						return null;
					}
				}
			}catch(Exception e) {
				log.error("ERROR DURANTE LA PETICIÓN WS-ORACLE", e);
				e.printStackTrace();
			}finally {
				if(sessionId != null && !"".contains(sessionId)) {
					httpRequestService.httpXMLRequest(AppConstants.URL_ANALYTICS_SESSION, PayloadProducer.getSessionLogOffSOAPXmlContent(sessionId), "");
					log.info("***SESSIÓN FINALIZADA EXITOSAMENTE - " + sessionId);	
				}
			}
		}
		return null;
	}

}
