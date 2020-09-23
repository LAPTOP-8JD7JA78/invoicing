package com.smartech.invoicing.integration.util;

public class PayloadProducer {

	public static String getTestAnalytics1(String sessionId) {	
		String request = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:v7=\"urn://oracle.bi.webservices/v7\">\r\n" + 
				"   <soapenv:Header/>\r\n" + 
				"   <soapenv:Body>\r\n" + 
				"      <v7:executeSQLQuery>\r\n" + 
				"         <v7:sql>\r\n" + 
				"         	SELECT\r\n" + 
				"		      \"Receivables - Standard Receipts Application Details Real Time\".\"Standard Receipt Details\".\"Receipt Number\" s_1\r\n" + 
				"		    FROM \"Receivables - Standard Receipts Application Details Real Time\"\r\n" + 
				"		    ORDER BY 1\r\n" + 
				"		    FETCH FIRST 75001 ROWS ONLY\r\n" + 
				"         </v7:sql>\r\n" + 
				"         <v7:outputFormat>XML</v7:outputFormat>\r\n" + 
				"         <v7:executionOptions>\r\n" + 
				"            <v7:async></v7:async>\r\n" + 
				"            <v7:maxRowsPerPage></v7:maxRowsPerPage>\r\n" + 
				"            <v7:refresh></v7:refresh>\r\n" + 
				"            <v7:presentationInfo></v7:presentationInfo>\r\n" + 
				"            <v7:type></v7:type>\r\n" + 
				"         </v7:executionOptions>\r\n" + 
				"         <v7:sessionID>"+ sessionId + "</v7:sessionID>\r\n" + 
				"      </v7:executeSQLQuery>\r\n" + 
				"   </soapenv:Body>\r\n" + 
				"</soapenv:Envelope>";
		
		return request;
	}
	
	public static String getSessionIdSOAPXmlContent(String user, String password) {
		String SOAPRequest = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:v7=\"urn://oracle.bi.webservices/v7\"> " + 
				"   <soapenv:Header/> " + 
				"   <soapenv:Body> " + 
				"      <v7:logon> " + 
				"         <v7:name>" + user + "</v7:name> " + 
				"         <v7:password>" + password + "</v7:password> " + 
				"      </v7:logon> " + 
				"   </soapenv:Body> " + 
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
				"   <soapenv:Header/> " + 
				"   <soapenv:Body> " + 
				"      <v7:logoff> " + 
				"         <v7:sessionID>" + sessionId + "</v7:sessionID> " + 
				"      </v7:logoff> " + 
				"   </soapenv:Body> " + 
				"</soapenv:Envelope>";
		return SOAPRequest;
	}
}
