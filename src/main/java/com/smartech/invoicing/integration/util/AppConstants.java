package com.smartech.invoicing.integration.util;

public class AppConstants {

	public static final String ORACLE_USER = "INTEGUSER";
	public static final String ORACLE_PASS = "Mexico$2020";
	
//	public static final String ORACLE_USER = "INTEGUSER";
//	public static final String ORACLE_PASS = "Mexico$2020";
	
	public static final String ORACLE_URL = "https://fa-epog-test-saasfaprod1.fa.ocs.oraclecloud.com";
//	public static final String ORACLE_URL = "https://fa-epog-saasfaprod1.fa.ocs.oraclecloud.com";
	
	//Analytics
	public static final String URL_ANALYTICS_SESSION = ORACLE_URL + "/analytics-ws/saw.dll?SoapImpl=nQSessionService";
	public static final String URL_ANALYTICS = ORACLE_URL + "/analytics-ws/saw.dll?SoapImpl=xmlViewService";
	
	//Servicios
	public static final String SERVICE_TEST1 = "TEST1";
	public static final String SERVICE_AR_REPORT_INVOICES = "AR_REPORT_INVOICES";
	public static final String SERVICE_REST_TEST1 = "REST_TEST1";
	
	//REST API
	public static final String URL_REST_INVORG = ORACLE_URL + "/fscmRestApi/resources/11.13.18.05/inventoryOrganizations";
	
}
