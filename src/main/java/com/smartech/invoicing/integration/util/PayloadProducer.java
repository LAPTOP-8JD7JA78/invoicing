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
	
	public static String getItemDataByItemNumberOrgCode(String itemNumber, String orgCode) {
		String SOAPRequest = 
				"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:typ=\"http://xmlns.oracle.com/apps/scm/productModel/items/itemServiceV2/types/\" xmlns:typ1=\"http://xmlns.oracle.com/adf/svc/types/\">" + 
				"<soapenv:Header/>" + 
					"<soapenv:Body>" + 
						"<typ:findItem>" + 
							"<typ:findCriteria>" + 
								"<typ1:fetchStart>0</typ1:fetchStart>" + 
								"<typ1:fetchSize>1</typ1:fetchSize>" + 
								"<typ1:filter>" + 
									"<typ1:group>" + 
										"<typ1:item>" + 
											"<typ1:conjunction>And</typ1:conjunction>" + 
											"<typ1:upperCaseCompare>false</typ1:upperCaseCompare>" + 
											"<typ1:attribute>ItemNumber</typ1:attribute>" + 
											"<typ1:operator>=</typ1:operator>" + 
											"<typ1:value>" + itemNumber + "</typ1:value>" + 
										"</typ1:item>" + 
										"<typ1:item>" + 
											"<typ1:conjunction>And</typ1:conjunction>" + 
											"<typ1:upperCaseCompare>false</typ1:upperCaseCompare>" + 
											"<typ1:attribute>OrganizationCode</typ1:attribute>" + 
											"<typ1:operator>=</typ1:operator>" + 
											"<typ1:value>" + orgCode + "</typ1:value>" + 
										"</typ1:item>" + 
									"</typ1:group>" + 
								"</typ1:filter>" + 
								"<typ1:findAttribute>ItemNumber</typ1:findAttribute>" + 
								"<typ1:findAttribute>ItemDescription</typ1:findAttribute>" + 
								"<typ1:findAttribute>ItemDFF</typ1:findAttribute>" +
//								"<typ1:findAttribute>ItemCategory</typ1:findAttribute>" + 
//								"<typ1:childFindCriteria>" + 
//									"<typ1:filter>" + 
//										"<typ1:group>" + 
//											"<typ1:item>" + 
//												"<typ1:upperCaseCompare>false</typ1:upperCaseCompare>" + 
//												"<typ1:attribute>ItemCatalog</typ1:attribute>" + 
//												"<typ1:operator>=</typ1:operator>" + 
//												"<typ1:value>IMEMSA_CATALOGO</typ1:value>" + 
//											"</typ1:item>" + 
//										"</typ1:group>" + 
//									"</typ1:filter>" + 
//									"<typ1:findAttribute>ItemCatalog</typ1:findAttribute>" + 
//									"<typ1:findAttribute>CategoryName</typ1:findAttribute>" + 
//									"<typ1:childAttrName>ItemCategory</typ1:childAttrName>" + 
//								"</typ1:childFindCriteria>" + 
							"</typ:findCriteria>" + 
						"</typ:findItem>" + 
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
				"   \"Receivables - Transactions Real Time\".\"- General Information\".\"Payment Terms Name\" s_7,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- General Information\".\"Transaction Date\" s_8,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- General Information\".\"Transaction Exchange Rate\" s_9,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- General Information\".\"Transaction Number\" s_10,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- General Information\".\"Transaction Source Name\" s_11,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- General Information\".\"Transaction Type\" s_12,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- Line Information\".\"Sales Order Number\" s_13,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- Line Information\".\"Transaction Line Number\" s_14,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- Line Information\".\"Unit Of Measure Code\" s_15,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- Line Information\".\"Unit Selling Price\" s_16,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- Main\".\"Item Description\" s_17,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- Main\".\"Item Name\" s_18,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- Reference Information\".\"Creation Date\" s_19,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- Reference Information\".\"Previous Transaction Number\" s_20,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- Subledger Accounting Journals Details\".\"Journal Line Description\" s_21,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- Tax Details\".\"Tax Classification Code\" s_22,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"Business Unit\".\"Business Unit Name\" s_23,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"Business Unit\".\"Default Currency\" s_24,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"Business Unit\".\"Default Legal Entity Name\" s_25,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"Business Unit\".\"Default Set\" s_26,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"Legal Entity\".\"Legal Entity Address\" s_27,\r\n" + 
				"   DESCRIPTOR_IDOF(\"Receivables - Transactions Real Time\".\"- General Information\".\"Payment Terms Name\") s_28,\r\n" + 
				"   DESCRIPTOR_IDOF(\"Receivables - Transactions Real Time\".\"- Transaction Accounting\".\"Document Currency Name\") s_29,\r\n" + 
				"   DESCRIPTOR_IDOF(\"Receivables - Transactions Real Time\".\"Business Unit\".\"Business Unit Name\") s_30,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- Line Amounts\".\"Quantity Credited\" s_31,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- Line Amounts\".\"Quantity Invoiced\" s_32,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- Tax Amounts\".\"Recoverable Tax Entered Amount\" s_33,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- Transaction Amounts\".\"Transaction Entered Amount\" s_34,\r\n" + 
				"   REPORT_SUM(\"Receivables - Transactions Real Time\".\"- Line Amounts\".\"Quantity Invoiced\" BY \"Receivables - Transactions Real Time\".\"- Main\".\"Item Description\",\"Receivables - Transactions Real Time\".\"- General Information\".\"Transaction Exchange Rate\",\"Receivables - Transactions Real Time\".\"- Reference Information\".\"Previous Transaction Number\",\"Receivables - Transactions Real Time\".\"- Reference Information\".\"Creation Date\",\"Receivables - Transactions Real Time\".\"- Tax Details\".\"Tax Classification Code\",\"Receivables - Transactions Real Time\".\"Business Unit\".\"Default Set\",\"Receivables - Transactions Real Time\".\"Legal Entity\".\"Legal Entity Address\",\"Receivables - Transactions Real Time\".\"Business Unit\".\"Default Legal Entity Name\",DESCRIPTOR_IDOF(\"Receivables - Transactions Real Time\".\"Business Unit\".\"Business Unit Name\"),\"Receivables - Transactions Real Time\".\"- Main\".\"Item Name\",\"Receivables - Transactions Real Time\".\"- Subledger Accounting Journals Details\".\"Journal Line Description\",\"Receivables - Transactions Real Time\".\"- Line Information\".\"Unit Selling Price\",\"Receivables - Transactions Real Time\".\"- Line Information\".\"Unit Of Measure Code\",\"Receivables - Transactions Real Time\".\"- Line Information\".\"Transaction Line Number\",\"Receivables - Transactions Real Time\".\"- Line Information\".\"Sales Order Number\",\"Receivables - Transactions Real Time\".\"- General Information\".\"Transaction Source Name\",\"Receivables - Transactions Real Time\".\"- General Information\".\"Transaction Type\",\"Receivables - Transactions Real Time\".\"- General Information\".\"Transaction Number\",\"Receivables - Transactions Real Time\".\"- General Information\".\"Transaction Date\",\"Receivables - Transactions Real Time\".\"- Bill-to Customer Identifying Address\".\"Bill-to Customer Street Address 1\",\"Receivables - Transactions Real Time\".\"- Bill-to Customer Identifying Address\".\"Bill-to Customer Postal Code\",\"Receivables - Transactions Real Time\".\"- Bill-to Customer Identifying Address\".\"Bill-to Customer Country\",\"Receivables - Transactions Real Time\".\"- Bill-to Customer Details\".\"Bill-to Customer Taxpayer Identification Number\",\"Receivables - Transactions Real Time\".\"- Bill-to Customer Details\".\"Bill-to Customer Number\",\"Receivables - Transactions Real Time\".\"- Bill-to Customer Details\".\"Bill-to Customer Name\",DESCRIPTOR_IDOF(\"Receivables - Transactions Real Time\".\"- General Information\".\"Payment Terms Name\"),\"Receivables - Transactions Real Time\".\"- Transaction Amounts\".\"Document Currency\") s_35,\r\n" + 
				"   REPORT_SUM(\"Receivables - Transactions Real Time\".\"- Tax Amounts\".\"Recoverable Tax Entered Amount\" BY \"Receivables - Transactions Real Time\".\"- Main\".\"Item Description\",\"Receivables - Transactions Real Time\".\"- General Information\".\"Transaction Exchange Rate\",\"Receivables - Transactions Real Time\".\"- Reference Information\".\"Previous Transaction Number\",\"Receivables - Transactions Real Time\".\"- Reference Information\".\"Creation Date\",\"Receivables - Transactions Real Time\".\"- Tax Details\".\"Tax Classification Code\",\"Receivables - Transactions Real Time\".\"Business Unit\".\"Default Set\",\"Receivables - Transactions Real Time\".\"Legal Entity\".\"Legal Entity Address\",\"Receivables - Transactions Real Time\".\"Business Unit\".\"Default Legal Entity Name\",DESCRIPTOR_IDOF(\"Receivables - Transactions Real Time\".\"Business Unit\".\"Business Unit Name\"),\"Receivables - Transactions Real Time\".\"- Main\".\"Item Name\",\"Receivables - Transactions Real Time\".\"- Subledger Accounting Journals Details\".\"Journal Line Description\",\"Receivables - Transactions Real Time\".\"- Line Information\".\"Unit Selling Price\",\"Receivables - Transactions Real Time\".\"- Line Information\".\"Unit Of Measure Code\",\"Receivables - Transactions Real Time\".\"- Line Information\".\"Transaction Line Number\",\"Receivables - Transactions Real Time\".\"- Line Information\".\"Sales Order Number\",\"Receivables - Transactions Real Time\".\"- General Information\".\"Transaction Source Name\",\"Receivables - Transactions Real Time\".\"- General Information\".\"Transaction Type\",\"Receivables - Transactions Real Time\".\"- General Information\".\"Transaction Number\",\"Receivables - Transactions Real Time\".\"- General Information\".\"Transaction Date\",\"Receivables - Transactions Real Time\".\"- Bill-to Customer Identifying Address\".\"Bill-to Customer Street Address 1\",\"Receivables - Transactions Real Time\".\"- Bill-to Customer Identifying Address\".\"Bill-to Customer Postal Code\",\"Receivables - Transactions Real Time\".\"- Bill-to Customer Identifying Address\".\"Bill-to Customer Country\",\"Receivables - Transactions Real Time\".\"- Bill-to Customer Details\".\"Bill-to Customer Taxpayer Identification Number\",\"Receivables - Transactions Real Time\".\"- Bill-to Customer Details\".\"Bill-to Customer Number\",\"Receivables - Transactions Real Time\".\"- Bill-to Customer Details\".\"Bill-to Customer Name\",DESCRIPTOR_IDOF(\"Receivables - Transactions Real Time\".\"- General Information\".\"Payment Terms Name\"),\"Receivables - Transactions Real Time\".\"- Transaction Amounts\".\"Document Currency\") s_36,\r\n" + 
				"   REPORT_SUM(\"Receivables - Transactions Real Time\".\"- Transaction Amounts\".\"Transaction Entered Amount\" BY \"Receivables - Transactions Real Time\".\"- Main\".\"Item Description\",\"Receivables - Transactions Real Time\".\"- General Information\".\"Transaction Exchange Rate\",\"Receivables - Transactions Real Time\".\"- Reference Information\".\"Previous Transaction Number\",\"Receivables - Transactions Real Time\".\"- Reference Information\".\"Creation Date\",\"Receivables - Transactions Real Time\".\"- Tax Details\".\"Tax Classification Code\",\"Receivables - Transactions Real Time\".\"Business Unit\".\"Default Set\",\"Receivables - Transactions Real Time\".\"Legal Entity\".\"Legal Entity Address\",\"Receivables - Transactions Real Time\".\"Business Unit\".\"Default Legal Entity Name\",DESCRIPTOR_IDOF(\"Receivables - Transactions Real Time\".\"Business Unit\".\"Business Unit Name\"),\"Receivables - Transactions Real Time\".\"- Main\".\"Item Name\",\"Receivables - Transactions Real Time\".\"- Subledger Accounting Journals Details\".\"Journal Line Description\",\"Receivables - Transactions Real Time\".\"- Line Information\".\"Unit Selling Price\",\"Receivables - Transactions Real Time\".\"- Line Information\".\"Unit Of Measure Code\",\"Receivables - Transactions Real Time\".\"- Line Information\".\"Transaction Line Number\",\"Receivables - Transactions Real Time\".\"- Line Information\".\"Sales Order Number\",\"Receivables - Transactions Real Time\".\"- General Information\".\"Transaction Source Name\",\"Receivables - Transactions Real Time\".\"- General Information\".\"Transaction Type\",\"Receivables - Transactions Real Time\".\"- General Information\".\"Transaction Number\",\"Receivables - Transactions Real Time\".\"- General Information\".\"Transaction Date\",\"Receivables - Transactions Real Time\".\"- Bill-to Customer Identifying Address\".\"Bill-to Customer Street Address 1\",\"Receivables - Transactions Real Time\".\"- Bill-to Customer Identifying Address\".\"Bill-to Customer Postal Code\",\"Receivables - Transactions Real Time\".\"- Bill-to Customer Identifying Address\".\"Bill-to Customer Country\",\"Receivables - Transactions Real Time\".\"- Bill-to Customer Details\".\"Bill-to Customer Taxpayer Identification Number\",\"Receivables - Transactions Real Time\".\"- Bill-to Customer Details\".\"Bill-to Customer Number\",\"Receivables - Transactions Real Time\".\"- Bill-to Customer Details\".\"Bill-to Customer Name\",DESCRIPTOR_IDOF(\"Receivables - Transactions Real Time\".\"- General Information\".\"Payment Terms Name\"),\"Receivables - Transactions Real Time\".\"- Transaction Amounts\".\"Document Currency\") s_37\r\n" + 
				"FROM \"Receivables - Transactions Real Time\"\r\n" + 
				"WHERE\r\n" + 
				"((\"- Line Information\".\"Sales Order Number\" IS NOT NULL) AND (\"- Reference Information\".\"Creation Date\" > timestamp '" + date + "'))\r\n" + 
				"ORDER BY 1, 14 ASC NULLS LAST, 11 ASC NULLS LAST, 26 ASC NULLS LAST, 28 ASC NULLS LAST, 24 ASC NULLS LAST, 12 ASC NULLS LAST, 13 ASC NULLS LAST, 15 ASC NULLS LAST, 19 ASC NULLS LAST, 18 ASC NULLS LAST, 22 ASC NULLS LAST, 17 ASC NULLS LAST, 30 ASC NULLS LAST, 16 ASC NULLS LAST, 9 ASC NULLS LAST, 7 ASC NULLS LAST, 6 ASC NULLS LAST, 5 ASC NULLS LAST, 4 ASC NULLS LAST, 3 ASC NULLS LAST, 2 ASC NULLS LAST, 31 ASC NULLS LAST, 23 ASC NULLS LAST, 21 ASC NULLS LAST, 20 ASC NULLS LAST, 8 ASC NULLS LAST, 27 ASC NULLS LAST, 10 ASC NULLS LAST" +
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
	
	public static String setARRegionalFlexfield(String transNumber, String UUID, String serie, String folio, String transactionSource, String setName) {
		
		String SOAPRequest = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:typ=\"http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/\" xmlns:erp=\"http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/\">" + 
				"<soapenv:Header/>" + 
					"<soapenv:Body>" + 
						"<typ:updateDffEntityDetails>" + 
							"<typ:operationMode>SINGLE</typ:operationMode>" + 
							"<typ:object>" + 
								"<erp:EntityName>Receivables Invoice</erp:EntityName>" + 
								"<erp:ContextValue>JLxMXReceivablesInformation</erp:ContextValue>" + 
								"<erp:UserKeyA>"+ transNumber +"</erp:UserKeyA>" +
								"<erp:UserKeyB>"+ transactionSource +"</erp:UserKeyB>" +
								"<erp:UserKeyC>"+ setName +"</erp:UserKeyC>" +
								"<erp:DFFAttributes>{\"GLOBAL_ATTRIBUTE1\":\"" + UUID + "\",\"GLOBAL_ATTRIBUTE2\":\"" + serie +"\",\"GLOBAL_ATTRIBUTE3\":\"" + folio +"\"}</erp:DFFAttributes>" + 
							"</typ:object>" + 
						"</typ:updateDffEntityDetails>" + 
					"</soapenv:Body>" + 
				"</soapenv:Envelope>";
		
		return SOAPRequest;
	}
	
	public static String getSalesOrderInfoBySalesNumber(String so) {
		String SOAPRequest = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:typ=\"http://xmlns.oracle.com/apps/scm/doo/decomposition/orderDetailServices/orderInformationService/types/\" xmlns:ord=\"http://xmlns.oracle.com/apps/scm/doo/decomposition/orderDetailServices/orderInformationService/\">" + 
				"<soapenv:Header/>" + 
					"<soapenv:Body>" + 
						"<typ:GetOrderDetails>" + 
							"<typ:Order>" + 
								"<ord:SourceTransactionNumber>" + so + "</ord:SourceTransactionNumber>" + 
							"</typ:Order>" + 
						"</typ:GetOrderDetails>" + 
					"</soapenv:Body>" + 
				"</soapenv:Envelope>";
		
		return SOAPRequest;
	}
}
