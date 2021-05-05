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
//		String SOAPRequest = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:v7=\"urn://oracle.bi.webservices/v7\"> " + 
//				"<soapenv:Header/> " + 
//				"<soapenv:Body> " + 
//					"<v7:logon> " + 
//						"<v7:name>" + user + "</v7:name> " + 
//						"<v7:password>" + password + "</v7:password> " + 
//					"</v7:logon> " + 
//				"</soapenv:Body> " + 
//				"</soapenv:Envelope>";
		
		String SOAPRequest = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:v7=\"urn://oracle.bi.webservices/v7\">\r\n" + 
				"   <soapenv:Header/>\r\n" + 
				"   <soapenv:Body>\r\n" + 
				"      <v7:logon>\r\n" + 
				"         <v7:name>" + user + "</v7:name>\r\n" + 
				"         <v7:password>" + password + "</v7:password>\r\n" + 
				"      </v7:logon>\r\n" + 
				"   </soapenv:Body>\r\n" + 
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
								"<typ1:findAttribute>ItemCategory</typ1:findAttribute>" + 
								"<typ1:childFindCriteria>" + 
									"<typ1:findAttribute>ItemCatalog</typ1:findAttribute>" + 
									"<typ1:findAttribute>CategoryCode</typ1:findAttribute>" + 
									"<typ1:findAttribute>CategoryName</typ1:findAttribute>" + 
									"<typ1:childAttrName>ItemCategory</typ1:childAttrName>" + 
								"</typ1:childFindCriteria>" + 
							"</typ:findCriteria>" + 
						"</typ:findItem>" + 
					"</soapenv:Body>" + 
				"</soapenv:Envelope>";
		
		return SOAPRequest;
	}
	
	public static String getItemDataByItemIdOrgCode(String itemId, String orgCode) {
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
											"<typ1:attribute>ItemId</typ1:attribute>" + 
											"<typ1:operator>=</typ1:operator>" + 
											"<typ1:value>" + itemId + "</typ1:value>" + 
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
								"<typ1:findAttribute>ItemCategory</typ1:findAttribute>" + 
								"<typ1:childFindCriteria>" + 
									"<typ1:findAttribute>ItemCatalog</typ1:findAttribute>" + 
									"<typ1:findAttribute>CategoryCode</typ1:findAttribute>" + 
									"<typ1:findAttribute>CategoryName</typ1:findAttribute>" + 
									"<typ1:childAttrName>ItemCategory</typ1:childAttrName>" + 
								"</typ1:childFindCriteria>" + 
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
				"   \"Receivables - Transactions Real Time\".\"- Additional Header Information\".\"Customer Transaction Reference\" s_35,\r\n" +
				"   \"Receivables - Transactions Real Time\".\"- Freight Details\".\"Ship To Customer Account Name\" s_36,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- Freight Details\".\"Ship To Location Address Line 1\" s_37,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- Freight Details\".\"Ship To Location City\" s_38,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- Freight Details\".\"Ship To Location Country\" s_39,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- Freight Details\".\"Ship To Location Postal Code\" s_40,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- Freight Details\".\"Ship To Location State\" s_41s,\r\n" +
				"   \"Receivables - Transactions Real Time\".\"- Bill-to Customer Details\".\"Bill-to Customer Number\" s_42,\r\n" +
				"	\"Receivables - Transactions Real Time\".\"- General Information\".\"RA_CUSTOMER_TRX_ESTATUS_\" s_43," +
				"   \"Receivables - Transactions Real Time\".\"- Bill-to Customer Descriptive Flexfields\".\"HZ_CUST_ACCOUNTS_CNPLNT_TLST_\" s44,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- Bill-to Customer Descriptive Flexfields\".\"HZ_CUST_ACCOUNTS_CODIGO_DE_INSTRUCCION_\" s_45,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- Bill-to Customer Descriptive Flexfields\".\"HZ_CUST_ACCOUNTS_CONTACTO_DE_COMPRAS_\" s_46,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- Bill-to Customer Descriptive Flexfields\".\"HZ_CUST_ACCOUNTS_GNL_DEL_COMPRADOR_\" s_47,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- Bill-to Customer Descriptive Flexfields\".\"HZ_CUST_ACCOUNTS_IT_RFRNC_TNL_\" s_48,\r\n" +//Tipo de Identificación --TransactionType 
				"   \"Receivables - Transactions Real Time\".\"- Bill-to Customer Descriptive Flexfields\".\"HZ_CUST_ACCOUNTS_NNR_NTRN_TL_PRFTR_\" s_49,\r\n" + //Número de referencia adicional  -- AdicionalInformationNumbe
				"   \"Receivables - Transactions Real Time\".\"- Bill-to Customer Descriptive Flexfields\".\"HZ_CUST_ACCOUNTS_REFERENCIA_ADICIONAL_\" s_50,\n\r" +
				"	\"Receivables - Transactions Real Time\".\"- Additional Header Information\".\"Customer Transaction Reference\" s_51,\n\r" +
				"	\"Receivables - Transactions Real Time\".\"- Bill-to Customer Identifying Address\".\"Bill-to Customer State\" s_52,\n\r" +
				"	\"Receivables - Transactions Real Time\".\"- Bill-to Customer Details\".\"Bill-to Customer Email Address\" s_53\n\r" +
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
	
	public static String setARRegionalFlexfieldByAdvPayments(String transNumber, String val, String transactionSource, String setName) {
		

		String SOAPRequest = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:typ=\"http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/\" xmlns:erp=\"http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/\">\r\n" + 
				"   <soapenv:Header/>\r\n" + 
				"   <soapenv:Body>\r\n" + 
				"      <typ:updateDffEntityDetails>\r\n" + 
				"         <typ:operationMode>SINGLE</typ:operationMode>\r\n" + 
				"         <typ:object>\r\n" + 
				"            <erp:EntityName>Receivables Invoice</erp:EntityName>\r\n" + 
				"            <erp:ContextValue>CFDI Relacionado</erp:ContextValue>\r\n" + 
				"			 <erp:UserKeyA>"+ transNumber +"</erp:UserKeyA>" +
				"			 <erp:UserKeyB>"+ transactionSource +"</erp:UserKeyB>" +
				"			 <erp:UserKeyC>"+ setName +"</erp:UserKeyC>" +
				"            <erp:DFFAttributes>{\"ATTRIBUTE2\":\"" + val + "\"}</erp:DFFAttributes>\r\n" + 
				"         </typ:object>\r\n" + 
				"      </typ:updateDffEntityDetails>\r\n" + 
				"   </soapenv:Body>\r\n" + 
				"</soapenv:Envelope>";
		
		return SOAPRequest;
	}
	
	public static String setARReceiptsRegionalFlexfield(String receiptNumber, String receiptId, String UUID) {
		
		String SOAPRequest = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:typ=\"http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/\" xmlns:erp=\"http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/\">" + 
				"<soapenv:Header/>" + 
					"<soapenv:Body>" + 
						"<typ:updateDffEntityDetails>" + 
							"<typ:operationMode>SINGLE</typ:operationMode>" + 
							"<typ:object>" + 
								"<erp:EntityName>Receivables Invoice Cash Receipt</erp:EntityName>" + 
								"<erp:ContextValue>JLxMXReceipts</erp:ContextValue>" + 
								"<erp:UserKeyA>"+ receiptNumber +"</erp:UserKeyA>" +
								"<erp:UserKeyB>"+ receiptId +"</erp:UserKeyB>" +
								"<erp:DFFAttributes>{\"GLOBAL_ATTRIBUTE1\":\"" + UUID + "\"}</erp:DFFAttributes>" + 
							"</typ:object>" + 
						"</typ:updateDffEntityDetails>" + 
					"</soapenv:Body>" + 
				"</soapenv:Envelope>";
		
		return SOAPRequest;
	}
	
	public static String setARReceiptsSerialFolioFlexfield(String receiptNumber, String receiptId, String serial, String folio) {
		String SOAPRequest = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:typ=\"http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/\" xmlns:erp=\"http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/\">" + 
				"<soapenv:Header/>" + 
					"<soapenv:Body>" + 
						"<typ:updateDffEntityDetails>" + 
							"<typ:operationMode>single</typ:operationMode>" + 
							"<typ:object>" + 
								"<erp:EntityName>Receivables Invoice Cash Receipt</erp:EntityName>" + 
								//"<erp:ContextValue>SerieyFolio</erp:ContextValue>" + 
								"<erp:ContextValue>Serie y Folio</erp:ContextValue>" +
								"<erp:UserKeyA>"+ receiptNumber + "</erp:UserKeyA>" + 
								"<erp:UserKeyB>" + receiptId + "</erp:UserKeyB>" + 
								"<erp:DFFAttributes>{\"ATTRIBUTE1\":\"" + folio + "\",\"ATTRIBUTE2\":\"" + serial + "\"}</erp:DFFAttributes>" + 
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
								"<ord:ExcludeAttachments>N</ord:ExcludeAttachments>" +
							"</typ:Order>" + 
						"</typ:GetOrderDetails>" + 
					"</soapenv:Body>" + 
				"</soapenv:Envelope>";
		
		return SOAPRequest;
	}
	
	public static String getArReportPayments(String sessionId, String date) {
		String request = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:v7=\"urn://oracle.bi.webservices/v7\">\r\n" + 
				"   <soapenv:Header/>\r\n" + 
				"   <soapenv:Body>\r\n" + 
				"      <v7:executeSQLQuery>\r\n" + 
				"         <v7:sql>\r\n" + 
				"        SELECT\r\n" + 
				"   \"Receivables - Standard Receipts Application Details Real Time\".\"- Paying Customer Details\".\"Paying Customer Name\" s_1,\r\n" + 
				"   \"Receivables - Standard Receipts Application Details Real Time\".\"- Paying Customer Details\".\"Paying Customer Taxpayer Identification Number\" s_2,\r\n" + 
				"   \"Receivables - Standard Receipts Application Details Real Time\".\"- Paying Customer Identifying Address\".\"Paying Customer City\" s_3,\r\n" + 
				"   \"Receivables - Standard Receipts Application Details Real Time\".\"- Paying Customer Identifying Address\".\"Paying Customer Country\" s_4,\r\n" + 
				"   \"Receivables - Standard Receipts Application Details Real Time\".\"- Paying Customer Identifying Address\".\"Paying Customer Postal Code\" s_5,\r\n" + 
				"   \"Receivables - Standard Receipts Application Details Real Time\".\"- Paying Customer Identifying Address\".\"Paying Customer State\" s_6,\r\n" + 
				"   \"Receivables - Standard Receipts Application Details Real Time\".\"- Paying Customer Identifying Address\".\"Paying Customer Street Address 1\" s_7,\r\n" + 
				"   \"Receivables - Standard Receipts Application Details Real Time\".\"- Standard Receipt Distribution Details\".\"Conversion Rate\" s_8,\r\n" + 
				"   \"Receivables - Standard Receipts Application Details Real Time\".\"Business Unit\".\"Business Unit Name\" s_9,\r\n" + 
				"   \"Receivables - Standard Receipts Application Details Real Time\".\"Legal Entity\".\"Legal Entity Name\" s_10,\r\n" + 
				"   \"Receivables - Standard Receipts Application Details Real Time\".\"Remittance Bank Account\".\"Bank Account Number\" s_11,\r\n" + 
				"   \"Receivables - Standard Receipts Application Details Real Time\".\"Remittance Bank Account\".\"Bank Name\" s_12,\r\n" + 
				"   \"Receivables - Standard Receipts Application Details Real Time\".\"Standard Receipt Application Details\".\"Applied Transaction Number\" s_13,\r\n" + 
				"   \"Receivables - Standard Receipts Application Details Real Time\".\"Standard Receipt Application Details\".\"JG_AR_RECEIVABLE_APPLICATIONS_DESC_TRANSACTIONSTATUS_\" s_14,\r\n" + 
				"   \"Receivables - Standard Receipts Application Details Real Time\".\"Standard Receipt Application Details\".\"Transaction Class\" s_15,\r\n" + 
				"   \"Receivables - Standard Receipts Application Details Real Time\".\"Standard Receipt Application Details\".\"Transaction to Receipt Conversion Rate\" s_16,\r\n" + 
				"   \"Receivables - Standard Receipts Application Details Real Time\".\"Standard Receipt Application\".\"Document Currency\" s_17,\r\n" + 
				"   \"Receivables - Standard Receipts Application Details Real Time\".\"Standard Receipt Details\".\"AR_CASH_RECEIPTS_ANTICIPO_\" s_18,\r\n" + 
				"   \"Receivables - Standard Receipts Application Details Real Time\".\"Standard Receipt Details\".\"Creation Date\" s_19,\r\n" + 
				"   \"Receivables - Standard Receipts Application Details Real Time\".\"Standard Receipt Details\".\"Last Updated Date\" s_20,\r\n" + 
				"   \"Receivables - Standard Receipts Application Details Real Time\".\"Standard Receipt Details\".\"Receipt Date\" s_21,\r\n" + 
				"   \"Receivables - Standard Receipts Application Details Real Time\".\"Standard Receipt Details\".\"Receipt Exchange Rate Type\" s_22,\r\n" + 
				"   \"Receivables - Standard Receipts Application Details Real Time\".\"Standard Receipt Details\".\"Receipt ID\" s_23,\r\n" + 
				"   \"Receivables - Standard Receipts Application Details Real Time\".\"Standard Receipt Details\".\"Receipt Number\" s_24,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- Payment\".\"Receipt Class\" s_25,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- Payment\".\"Receipt Method\" s_26,\r\n" + 
				"   DESCRIPTOR_IDOF(\"Receivables - Standard Receipts Application Details Real Time\".\"Business Unit\".\"Business Unit Name\") s_27,\r\n" + 
				"   DESCRIPTOR_IDOF(\"Receivables - Standard Receipts Application Details Real Time\".\"Standard Receipt Application Details\".\"Latest Application\") s_28,\r\n" + 
				"   \"Receivables - Standard Receipts Application Details Real Time\".\"Standard Receipt Application\".\"Applied From Entered Amount\" s_29,\r\n" + 
				"   \"Receivables - Standard Receipts Application Details Real Time\".\"Standard Receipt Application\".\"Applied To Entered Amount\" s_30,\r\n" + 
				"   \"Receivables - Standard Receipts Application Details Real Time\".\"Standard Receipt Application\".\"Tax Applied Entered Amount\" s_31,\r\n" + 
				"   \"Receivables - Standard Receipts Application Details Real Time\".\"Standard Receipts\".\"Receipt Entered Amount\" s_32,\r\n" + 
				"   \"Receivables - Standard Receipts Application Details Real Time\".\"Standard Receipts\".\"Total Receipt Amount Pending Application\" s_33,\r\n" + 
				"   \"Receivables - Standard Receipts Application Details Real Time\".\"Standard Receipts\".\"Total Unapplied Amount\" s_34,\r\n" + 
				"   \"Receivables - Transactions Real Time\".\"- Transaction Amounts\".\"Transaction Entered Amount\" s_35,\r\n" + 
				"   \"Receivables - Standard Receipts Application Details Real Time\".\"- Paying Customer Details\".\"Paying Customer Email Address\" s_36,\r\n" + 
				"   \"Receivables - Standard Receipts Application Details Real Time\".\"- Paying Customer Details\".\"Paying Customer Number\" s_37,\r\n" +
				"   \"Receivables - Standard Receipts Application Details Real Time\".\"Standard Receipt Details\".\"AR_CASH_RECEIPTS_FORMA_DE_PAGO_\" s_38,\r\n" + 
				"   \"Receivables - Standard Receipts Application Details Real Time\".\"Standard Receipt Details\".\"AR_CASH_RECEIPTS_SUCURSAL_\" s_39\r\n" +
				"FROM \"Receivables - Standard Receipts Application Details Real Time\"\r\n" + 
				"WHERE\r\n" + 
				"(\"Standard Receipt Details\".\"Last Updated Date\" > timestamp '" + date + "')\r\n" + 
				"FETCH FIRST 75001 ROWS ONLY\r\n" + 
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
		return request;
	}
	
	public static String getRetailerItemCatalogInfo(String categoryCode) {
		String SOAPRequest = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:typ=\"http://xmlns.oracle.com/apps/scm/productModel/catalogs/itemCatalogService/types/\" xmlns:typ1=\"http://xmlns.oracle.com/adf/svc/types/\">" + 
				"<soapenv:Header/>" + 
				"<soapenv:Body>" + 
					"<typ:findCategory>" + 
						"<typ:findCriteria>" + 
							"<typ1:fetchStart>0</typ1:fetchStart>" + 
							"<typ1:fetchSize>1</typ1:fetchSize>" + 
							"<typ1:filter>" + 
								"<typ1:group>" + 
									"<typ1:item>" + 
										"<typ1:conjunction>And</typ1:conjunction>" + 
										"<typ1:attribute>CategoryCode</typ1:attribute>" + 
										"<typ1:operator>=</typ1:operator>" + 
										"<typ1:value>" + categoryCode + "</typ1:value>" + 
									"</typ1:item>" + 
								"</typ1:group>" + 
							"</typ1:filter>" + 
							"<typ1:findAttribute>CategoryCode</typ1:findAttribute>" + 
							"<typ1:findAttribute>CategoryId</typ1:findAttribute>" + 
							"<typ1:findAttribute>CategoryName</typ1:findAttribute>" + 
							"<typ1:findAttribute>CatalogCode</typ1:findAttribute>" + 
							"<typ1:findAttribute>Attachment</typ1:findAttribute>" + 
							"<typ1:childFindCriteria>" + 
								"<typ1:findAttribute>Title</typ1:findAttribute>" + 
								"<typ1:findAttribute>FileName</typ1:findAttribute>" + 
								"<typ1:childAttrName>Attachment</typ1:childAttrName>" + 
							"</typ1:childFindCriteria>" + 
						"</typ:findCriteria>" + 
					"</typ:findCategory>" + 
				"</soapenv:Body>" + 
			"</soapenv:Envelope>";
		
		return SOAPRequest;
	}
	
	public static String getGTINFromItemRelationships(String itemNumber, String organizationCode, String partyNumber) {
		
		String SOAPRequest = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:typ=\"http://xmlns.oracle.com/apps/scm/productModel/items/itemRelationshipService/types/\" xmlns:typ1=\"http://xmlns.oracle.com/adf/svc/types/\">" + 
				"<soapenv:Header/>" + 
				"<soapenv:Body>" + 
					"<typ:findGTINCrossReference>" + 
						"<typ:findCriteria>" + 
							"<typ1:fetchStart>0</typ1:fetchStart>" + 
							"<typ1:fetchSize>1</typ1:fetchSize>" + 
							"<typ1:filter>" + 
								"<typ1:group>" + 
									"<typ1:item>" + 
										"<typ1:conjunction>And</typ1:conjunction>" + 
										"<typ1:attribute>ItemNumber</typ1:attribute>" + 
										"<typ1:operator>=</typ1:operator>" + 
										"<typ1:value>" + itemNumber + "</typ1:value>" + 
									"</typ1:item>" + 
									"<typ1:item>" + 
										"<typ1:conjunction>And</typ1:conjunction>" + 
										"<typ1:attribute>OrganizationCode</typ1:attribute>" + 
										"<typ1:operator>=</typ1:operator>" + 
										"<typ1:value>" + organizationCode + "</typ1:value>" + 
									"</typ1:item>" + 
									"<typ1:item>" + 
										"<typ1:conjunction>And</typ1:conjunction>" + 
										"<typ1:attribute>TradingPartnerNumber</typ1:attribute>" + 
										"<typ1:operator>=</typ1:operator>" + 
										"<typ1:value>" + partyNumber + "</typ1:value>" + 
									"</typ1:item>" + 
								"</typ1:group>" + 
							"</typ1:filter>" + 
							"<typ1:findAttribute>ItemNumber</typ1:findAttribute>" + 
							"<typ1:findAttribute>OrganizationCode</typ1:findAttribute>" + 
							"<typ1:findAttribute>TradingPartnerName</typ1:findAttribute>" + 
							"<typ1:findAttribute>TradingPartnerNumber</typ1:findAttribute>" + 
							"<typ1:findAttribute>UOMCodeValue</typ1:findAttribute>" + 
							"<typ1:findAttribute>GTIN</typ1:findAttribute>" + 
							"<typ1:findAttribute>GTINDescription</typ1:findAttribute>" + 
						"</typ:findCriteria>" + 
					"</typ:findGTINCrossReference>" + 
				"</soapenv:Body>" + 
			"</soapenv:Envelope>";
		
		return SOAPRequest;
	}
	
	public static String createTransferReport(String sessionId, String date) {
		String request = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:v7=\"urn://oracle.bi.webservices/v7\">\r\n" + 
				"   <soapenv:Header/>\r\n" + 
				"   <soapenv:Body>\r\n" + 
				"      <v7:executeSQLQuery>\r\n" + 
				"         <v7:sql>\r\n" + 
				"         SELECT\r\n" + 
				"   \"Costing - Cost Accounting Real Time\".\"-  Main\".\"Item Description\" s_1,\r\n" + 
				"   \"Costing - Cost Accounting Real Time\".\"-  Main\".\"Item Name\" s_2,\r\n" + 
				"   \"Costing - Cost Accounting Real Time\".\"Business Unit\".\"Creation Date\" s_3,\r\n" + 
				"   \"Costing - Cost Accounting Real Time\".\"Cost Accounting Distributions Details\".\"Distribution Transaction Number\" s_4,\r\n" + 
				"   \"Costing - Cost Accounting Real Time\".\"Cost Accounting Distributions Details\".\"Unit of Measure\" s_5,\r\n" + 
				"   \"Costing - Cost Accounting Real Time\".\"Cost Transaction Details\".\"Lot Number\" s_6,\r\n" + 
				"   \"Costing - Cost Accounting Real Time\".\"Cost Transaction Details\".\"Serial Number\" s_7,\r\n" + 
				"   \"Costing - Cost Accounting Real Time\".\"Cost Transaction Details\".\"Transaction Date\" s_8,\r\n" + 
				"   \"Costing - Cost Accounting Real Time\".\"Cost Transaction Details\".\"Transaction Quantity\" s_9,\r\n" + 
				"   \"Costing - Cost Accounting Real Time\".\"Cost Transaction Type\".\"Base Transaction Type Name\" s_10,\r\n" + 
				"   \"Costing - Cost Accounting Real Time\".\"Inventory Organization\".\"Inventory Organization Name\" s_11,\r\n" + 
				"   \"Costing - Cost Accounting Real Time\".\"Locator\".\"Locator Description\" s_12,\r\n" + 
				"   \"Costing - Cost Accounting Real Time\".\"Subinventory\".\"Sub Inventory Name\" s_13,\r\n" + 
				"   \"Costing - Cost Accounting Real Time\".\"Transfer Inventory Organization\".\"Transfer Organization Name\" s_14,\r\n" + 
				"   DESCRIPTOR_IDOF(\"Costing - Cost Accounting Real Time\".\"Cost Accounting Distributions Details\".\"Unit of Measure\") s_15\r\n" + 
				"FROM \"Costing - Cost Accounting Real Time\"\r\n" + 
				"WHERE\r\n" + 
				"((\"Cost Transaction Details\".\"Transaction Date\" > timestamp '" + date + "') AND (\"Cost Transaction Type\".\"Base Transaction Type Name\" IN ('Intransit Shipment', 'Transfer Order Interorganization Shipment')))" + 
				"FETCH FIRST 75001 ROWS ONLY\r\n" + 
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
		
		return request;
	}
	
	public static String LabelProccess(String sessionId, String date) {
		String request = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:v7=\"urn://oracle.bi.webservices/v7\">\r\n" + 
				"   <soapenv:Header/>\r\n" + 
				"   <soapenv:Body>\r\n" + 
				"      <v7:executeSQLQuery>\r\n" + 
				"         <v7:sql>\r\n" + 
				"SELECT\r\n" + 
				"   \"Fixed Assets - Asset Transactions Real Time\".\"- General Information\".\"Book Code\" s_1,\r\n" + 
				"   \"Fixed Assets - Asset Transactions Real Time\".\"Asset Category Book\".\"Asset Cost Account\" s_2,\r\n" + 
				"   \"Fixed Assets - Asset Transactions Real Time\".\"Asset Depreciation Expense Account\".\"Asset Depreciation Expense Account Description\" s_3,\r\n" + 
				"   \"Fixed Assets - Asset Transactions Real Time\".\"Asset Location\".\"Location Concatenated Segment\" s_4,\r\n" + 
				"   \"Fixed Assets - Asset Transactions Real Time\".\"Descriptive Details\".\"Current Units\" s_5,\r\n" + 
				"   \"Fixed Assets - Asset Transactions Real Time\".\"Descriptive Details\".\"Model\" s_6,\r\n" + 
				"   \"Fixed Assets - Asset Transactions Real Time\".\"Descriptive Details\".\"Serial Number\" s_7,\r\n" + 
				"   \"Fixed Assets - Asset Transactions Real Time\".\"Descriptive Details\".\"Tag Number\" s_8,\r\n" + 
				"   \"Fixed Assets - Asset Transactions Real Time\".\"Employee\".\"Employee Name\" s_9,\r\n" + 
				"   \"Fixed Assets - Asset Transactions Real Time\".\"Employee\".\"Employee Number\" s_10,\r\n" + 
				"   \"Fixed Assets - Asset Transactions Real Time\".\"General Information\".\"Asset Number\" s_11,\r\n" + 
				"   \"Fixed Assets - Asset Transactions Real Time\".\"General Information\".\"Creation Date\" s_12,\r\n" + 
				"   \"Fixed Assets - Asset Transactions Real Time\".\"General Information\".\"Description\" s_13,\r\n" + 
				"   \"Fixed Assets - Asset Transactions Real Time\".\"General Information\".\"Last Update Date\" s_14,\r\n" + 
				"   \"Fixed Assets - Asset Transactions Real Time\".\"Transaction Distribution Lines Details\".\"Units Assigned\" s_15\r\n" + 
				"FROM \"Fixed Assets - Asset Transactions Real Time\"\r\n" + 
				"WHERE\r\n" + 
				"(\"General Information\".\"Creation Date\" > timestamp '" + date + "')" +
				"         </v7:sql>\r\n" + 
				"         <v7:outputFormat>XML</v7:outputFormat>\r\n" + 
				"         <v7:executionOptions>\r\n" + 
				"            <v7:async></v7:async>\r\n" + 
				"            <v7:maxRowsPerPage>?</v7:maxRowsPerPage>\r\n" + 
				"            <v7:refresh></v7:refresh>\r\n" + 
				"            <v7:presentationInfo></v7:presentationInfo>\r\n" + 
				"            <v7:type></v7:type>\r\n" + 
				"         </v7:executionOptions>\r\n" + 
				"         <v7:sessionID>" + sessionId + "</v7:sessionID>\r\n" + 
				"      </v7:executeSQLQuery>\r\n" + 
				"   </soapenv:Body>\r\n" + 
				"</soapenv:Envelope>";
		
		return request;
	}
	
	public static String updateLabel(String sessionId, String date) {
		String request = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:v7=\"urn://oracle.bi.webservices/v7\">\r\n" + 
				"   <soapenv:Header/>\r\n" + 
				"   <soapenv:Body>\r\n" + 
				"      <v7:executeSQLQuery>\r\n" + 
				"         <v7:sql>\r\n" + 
				"SELECT\r\n" + 
				"   \"Fixed Assets - Asset Transactions Real Time\".\"- General Information\".\"Book Code\" s_1,\r\n" + 
				"   \"Fixed Assets - Asset Transactions Real Time\".\"Asset Category Book\".\"Asset Cost Account\" s_2,\r\n" + 
				"   \"Fixed Assets - Asset Transactions Real Time\".\"Asset Depreciation Expense Account\".\"Asset Depreciation Expense Account Description\" s_3,\r\n" + 
				"   \"Fixed Assets - Asset Transactions Real Time\".\"Asset Location\".\"Location Concatenated Segment\" s_4,\r\n" + 
				"   \"Fixed Assets - Asset Transactions Real Time\".\"Descriptive Details\".\"Current Units\" s_5,\r\n" + 
				"   \"Fixed Assets - Asset Transactions Real Time\".\"Descriptive Details\".\"Model\" s_6,\r\n" + 
				"   \"Fixed Assets - Asset Transactions Real Time\".\"Descriptive Details\".\"Serial Number\" s_7,\r\n" + 
				"   \"Fixed Assets - Asset Transactions Real Time\".\"Descriptive Details\".\"Tag Number\" s_8,\r\n" + 
				"   \"Fixed Assets - Asset Transactions Real Time\".\"Employee\".\"Employee Name\" s_9,\r\n" + 
				"   \"Fixed Assets - Asset Transactions Real Time\".\"Employee\".\"Employee Number\" s_10,\r\n" + 
				"   \"Fixed Assets - Asset Transactions Real Time\".\"General Information\".\"Asset Number\" s_11,\r\n" + 
				"   \"Fixed Assets - Asset Transactions Real Time\".\"General Information\".\"Creation Date\" s_12,\r\n" + 
				"   \"Fixed Assets - Asset Transactions Real Time\".\"General Information\".\"Description\" s_13,\r\n" + 
				"   \"Fixed Assets - Asset Transactions Real Time\".\"General Information\".\"Last Update Date\" s_14,\r\n" + 
				"   \"Fixed Assets - Asset Transactions Real Time\".\"Transaction Distribution Lines Details\".\"Units Assigned\" s_15\r\n" + 
				"FROM \"Fixed Assets - Asset Transactions Real Time\"\r\n" + 
				"WHERE\r\n" + 
				"((\"General Information\".\"Last Update Date\" > timestamp '" + date + "') AND (\"Employee\".\"Employee Name\" IS NOT NULL))" +
//				"(\"General Information\".\"Last Update Date\" > timestamp '" + date + "')\r\n" + 
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
		
		return request;
	}
}
