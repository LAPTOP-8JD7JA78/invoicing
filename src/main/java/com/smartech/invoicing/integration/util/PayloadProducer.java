package com.smartech.invoicing.integration.util;

public class PayloadProducer {

	public static String getTestAnalytics1(String sessionId) {	
		String request = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:v7=\"urn://oracle.bi.webservices/v7\">" + 
				"<soapenv:Header/>" + 
				"<soapenv:Body>" + 
					"<v7:executeSQLQuery>" + 
						"<v7:sql>" + 
							"SELECT" + 
							" \"Receivables - Standard Receipts Application Details Real Time\".\"Standard Receipt Details\".\"Receipt Number\" s_1" + 
							" FROM \"Receivables - Standard Receipts Application Details Real Time\"" + 
							" ORDER BY 1" + 
							" FETCH FIRST 75001 ROWS ONLY" + 
						"</v7:sql>" + 
						"<v7:outputFormat>XML</v7:outputFormat>" + 
						"<v7:executionOptions>" + 
						"<v7:async></v7:async>" + 
						"<v7:maxRowsPerPage></v7:maxRowsPerPage>" + 
						"<v7:refresh></v7:refresh>" + 
						"<v7:presentationInfo></v7:presentationInfo>" + 
						"<v7:type></v7:type>" + 
						"</v7:executionOptions>" + 
						"<v7:sessionID>"+ sessionId + "</v7:sessionID>" + 
					"</v7:executeSQLQuery>" + 
				"</soapenv:Body>" + 
				"</soapenv:Envelope>";
		
		return request;
	}
	
	public static String getSessionIdSOAPXmlContent(String user, String password) {
		String SOAPRequest = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:v7=\"urn://oracle.bi.webservices/v7\"> " + 
				"<soapenv:Header/> " + 
				"<soapenv:Body> " + 
					"<v7:logon> " + 
						"<v7:name>" + user + "</v7:name> " + 
						"<v7:password>" + password + "</v7:password> " + 
					"</v7:logon> " + 
				"</soapenv:Body> " + 
				"</soapenv:Envelope>";
		return SOAPRequest;
	}
	
	public static String keepSessionIdSOAPXmlContent(String sessionId) {
		String SOAPRequest = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:v7=\"urn://oracle.bi.webservices/v7\">" + 
				"<soapenv:Header/>" + 
					"<soapenv:Body>" + 
						"<v7:keepAlive>" + 
							"<v7:sessionID>" + sessionId + "</v7:sessionID>" + 
						"</v7:keepAlive>" + 
						"</soapenv:Body>" + 
				"</soapenv:Envelope>";
		return SOAPRequest;
	}
	
	public static String getSessionLogOffSOAPXmlContent(String sessionId) {
		String SOAPRequest = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:v7=\"urn://oracle.bi.webservices/v7\"> " + 
				"<soapenv:Header/>" + 
					"<soapenv:Body>" + 
						"<v7:logoff>" + 
							"<v7:sessionID>" + sessionId + "</v7:sessionID>" + 
						"</v7:logoff>" + 
					"</soapenv:Body>" + 
				"</soapenv:Envelope>";
		return SOAPRequest;
	}
}
