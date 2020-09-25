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
	
	public static String getArReportInvoice(String sessionId, String date) {
		String SOAPRequest = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:v7=\"urn://oracle.bi.webservices/v7\">\r\n" + 
				"   <soapenv:Header/>\r\n" + 
				"   <soapenv:Body>\r\n" + 
				"      <v7:executeSQLQuery>\r\n" + 
				"         <v7:sql>\r\n" + 
				"SELECT\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- Bill-to Customer Details\".\"Bill-to Customer Name\" s_1,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- Bill-to Customer Details\".\"Bill-to Customer Number\" s_2,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- Bill-to Customer Details\".\"Bill-to Customer Taxpayer Identification Number\" s_3,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- Bill-to Customer Identifying Address\".\"Bill-to Customer Country\" s_4,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- Bill-to Customer Identifying Address\".\"Bill-to Customer Postal Code\" s_5,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- Bill-to Customer Identifying Address\".\"Bill-to Customer Street Address 1\" s_6,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- General Information\".\"Transaction Date\" s_7,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- General Information\".\"Transaction Number\" s_8,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- General Information\".\"Transaction Source Name\" s_9,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- General Information\".\"Transaction Type\" s_10,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- Line Information\".\"Sales Order Number\" s_11,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- Line Information\".\"Transaction Line Number\" s_12,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- Line Information\".\"Unit Of Measure Code\" s_13,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- Line Information\".\"Unit Selling Price\" s_14,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- Main\".\"Item Name\" s_15,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- Reference Information\".\"Creation Date\" s_16,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- Reference Information\".\"Previous Transaction Number\" s_17,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- Subledger Accounting Journals Details\".\"Journal Line Description\" s_18,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- Tax Details\".\"Tax Classification Code\" s_19,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"Business Unit\".\"Business Unit Name\" s_20,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"Business Unit\".\"Default Legal Entity Name\" s_21,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"Legal Entity\".\"Legal Entity Address\" s_22,\r\n" + 
				"   DESCRIPTOR_IDOF(\"Receivables - Transactions Real Time\".\"Business Unit\".\"Business Unit Name\") s_23,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- Line Amounts\".\"Quantity Credited\" s_24,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- Line Amounts\".\"Quantity Invoiced\" s_25,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- Tax Amounts\".\"Recoverable Tax Entered Amount\" s_26,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- Transaction Amounts\".\"Transaction Entered Amount\" s_27\r\n" + 
				"FROM \"Receivables - Transactions Real Time\"\r\n" + 
				"WHERE\r\n" + 
				"((\"- Line Information\".\"Sales Order Number\" IS NOT NULL) AND (\"- Reference Information\".\"Creation Date\" > timestamp '2020-09-21 21:00:00'))\r\n" + 
				"ORDER BY 1, 23 ASC NULLS LAST, 22 ASC NULLS LAST, 21 ASC NULLS LAST, 24 ASC NULLS LAST, 16 ASC NULLS LAST, 19 ASC NULLS LAST, 15 ASC NULLS LAST, 14 ASC NULLS LAST, 13 ASC NULLS LAST, 12 ASC NULLS LAST, 10 ASC NULLS LAST, 11 ASC NULLS LAST, 9 ASC NULLS LAST, 8 ASC NULLS LAST, 7 ASC NULLS LAST, 6 ASC NULLS LAST, 5 ASC NULLS LAST, 4 ASC NULLS LAST, 3 ASC NULLS LAST, 2 ASC NULLS LAST, 20 ASC NULLS LAST, 18 ASC NULLS LAST, 17 ASC NULLS LAST" + 
				"         </v7:sql>\r\n" + 
				"         <v7:outputFormat>XML</v7:outputFormat>\r\n" + 
				"         <v7:executionOptions>\r\n" + 
				"            <v7:async></v7:async>\r\n" + 
				"            <v7:maxRowsPerPage></v7:maxRowsPerPage>\r\n" + 
				"            <v7:refresh></v7:refresh>\r\n" + 
				"            <v7:presentationInfo></v7:presentationInfo>\r\n" + 
				"            <v7:type></v7:type>\r\n" + 
				"         </v7:executionOptions>\r\n" + 
				"         <v7:sessionID>" + sessionId + "</v7:sessionID>\r\n" + 
				"      </v7:executeSQLQuery>\r\n" + 
				"   </soapenv:Body>\r\n" + 
				"</soapenv:Envelope>";
		return SOAPRequest;
	}
}
