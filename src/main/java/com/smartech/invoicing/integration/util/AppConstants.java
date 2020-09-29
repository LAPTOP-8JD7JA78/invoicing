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
	public static final String SERVICE_SALES_ORDER_1 = "SALES_ORDER_1";
	
	//REST API
	public static final String URL_REST_INVORG = ORACLE_URL + "/fscmRestApi/resources/11.13.18.05/inventoryOrganizations";
	public static final String URL_REST_SALESORDER = ORACLE_URL + "/fscmRestApi/resources/11.13.18.05/salesOrdersForOrderHub";
	
	//SOAP SERVICE
	public static final String URL_SOAP_ITEMSV2 = ORACLE_URL + "/fscmService/ItemServiceV2?invoke=";
	public static final String URL_SOAP_DFFFIN =  ORACLE_URL + "/fscmService/ErpObjectDFFUpdateService?invoke=";
	
	//Estado de facturacion
	public static final String STATUS_START = "START";
	public static final String STATUS_GETRESTDATA = "DATA";
	public static final String STATUS_PENDING = "PENDING";
	public static final String STATUS_INVOICED = "INVOICED";
	public static final String STATUS_ERROR = "ERROR";
	public static final String STATUS_FINISHED = "FINISHED";

	//Estados de reportes
	public static final String STATUS_REPORTS_ING = "Invoice";
	public static final String STATUS_REPORTS_ESP = "Factura";
	//Lineas del reporte
	public static final String REPORT_LINE_TYPE_NOR = "NORMAL";
	public static final String REPORT_LINE_TYPE_DIS = "DISCOUNT";
	//TIPO DE ORDEN
	public static final String ORDER_TYPE_FACTURA = "FACTURA";
}
