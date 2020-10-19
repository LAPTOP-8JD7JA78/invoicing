package com.smartech.invoicing.integration.util;

public class AppConstants {

	public static final String ORACLE_USER = "INTEGUSER";
	public static final String ORACLE_PASS = "Mexico$2020";
	
//	public static final String ORACLE_USER = "INTEGUSER";
//	public static final String ORACLE_PASS = "Mexico$2020";
	
	public static final String ORACLE_URL = "https://fa-epog-test-saasfaprod1.fa.ocs.oraclecloud.com";
//	public static final String ORACLE_URL = "https://fa-epog-saasfaprod1.fa.ocs.oraclecloud.com";
	
	public static final String ORACLE_ITEMMASTER = "IMA";
	
	//Analytics
	public static final String URL_ANALYTICS_SESSION = ORACLE_URL + "/analytics-ws/saw.dll?SoapImpl=nQSessionService";
	public static final String URL_ANALYTICS = ORACLE_URL + "/analytics-ws/saw.dll?SoapImpl=xmlViewService";
	
	//Servicios
	public static final String SERVICE_TEST1 = "TEST1";
	public static final String SERVICE_AR_REPORT_INVOICES = "AR_REPORT_INVOICES";
	public static final String SERVICE_AR_REPORT_PAYMENTS = "AR_REPORT_PAYMENTS";
	public static final String SERVICE_REST_TEST1 = "REST_TEST1";
	public static final String SERVICE_REST_ITEMLOT = "REST_ITEMLOT";
	public static final String SERVICE_SALES_ORDER_1 = "SALES_ORDER_1";
	public static final String SERVICE_SALES_ORDER_AI_1 = "SALES_ORDER_AI_1";
	
	//REST API
	public static final String URL_REST_INVORG = ORACLE_URL + "/fscmRestApi/resources/11.13.18.05/inventoryOrganizations";
	public static final String URL_REST_SALESORDER = ORACLE_URL + "/fscmRestApi/resources/11.13.18.05/salesOrdersForOrderHub";
	public static final String URL_REST_SALESORDER_ADDINF = ORACLE_URL + "/fscmRestApi/resources/11.13.18.05/salesOrdersForOrderHub/ORDER_ID/child/additionalInformation";
	public static final String URL_REST_ITEMLOT = ORACLE_URL + "/fscmRestApi/resources/11.13.18.05/inventoryItemLots";
	
	//SOAP SERVICE
	public static final String URL_SOAP_ITEMSV2 = ORACLE_URL + "/fscmService/ItemServiceV2?invoke=";
	public static final String URL_SOAP_DFFFIN =  ORACLE_URL + "/fscmService/ErpObjectDFFUpdateService?invoke=";
	public static final String URL_SOAP_SALESORDERINFO =  ORACLE_URL + "/fscmService/OrderInformationService?invoke=";
	public static final String URL_SOAP_ITEMRELATIONSHIP = ORACLE_URL + "/fscmService/ItemRelationshipService?invoke=";
	public static final String URL_SOAP_ITEMCATALOG = ORACLE_URL + "/fscmService/ItemCatalogService?invoke=";
	
	//Estado de facturacion
	public static final String STATUS_START = "START";
	public static final String STATUS_GETRESTDATA = "DATA";
	public static final String STATUS_PETITIONDATA = "PETITION";
	public static final String STATUS_PENDING = "PENDING";
	public static final String STATUS_UPDUUID = "UPDUUID";
	public static final String STATUS_INVOICED = "INVOICED";
	public static final String STATUS_ERROR_DATA = "ERROR DATA";
	public static final String STATUS_ERROR_PAC = "ERROR PAC";
	public static final String STATUS_ERROR_PETITION = "ERROR PETITION";
	public static final String STATUS_FINISHED = "FINISHED";

	public static final String PAY_METHOD= "PUE";
	
	//Estados de reportes
	public static final String STATUS_REPORTS_ING = "Invoice";
	public static final String STATUS_REPORTS_ESP = "Factura";
	
	//Lineas del reporte
	public static final String REPORT_LINE_TYPE_NOR = "NORMAL";
	public static final String REPORT_LINE_TYPE_DIS = "DISCOUNT";
	
	//TIPO DE ORDEN
	public static final String ORDER_TYPE_FACTURA = "FACTURA";
	public static final String ORDER_TYPE_NC = "NOTA CREDITO";
	public static final String ORDER_TYPE_CPAGO = "COMPLEMENTO PAGO";
	public static final String ORDER_TYPE_EXP = "EXPORTACION";
	public static final String ORDER_TYPE_LIV = "LIVERPOOL";
	public static final String ORDER_TYPE_MAR = "MARINA";
	
	//TIPO DE CAMBIO ESTANDAR
	public static final double INVOICE_EXCHANGE_RATE= 1.00;
	
	//UDC
	public static final String UDC_SYSTEM_UOMSAT = "UOMSAT";
	public static final String UDC_SYSTEM_OPERATIONTYPE = "OPERATIONTYPE";
	public static final String UDC_SYSTEM_PETITIONKEY = "PETITIONKEY";
	public static final String UDC_STRVALUE1_DEFAULT = "DEFAULT";
	public static final String UDC_SYSTEM_COUNTRY = "COUNTRY";
	public static final String UDC_SYSTEM_RTYPE = "SATRTYPE";
	public static final String UDC_STRVALUE1_CPAGOS = "CPAGOS";
	public static final String UDC_SYSTEM_ACCBANK = "ACCBANK";
	public static final String UDC_SYSTEM_TIMEZONE = "TIMEZONE";
	public static final String UDC_STRVALUE1_TIMEZONE = "USO HORARIO";
	public static final String UDC_SYSTEM_SCHEDULER = "SCHEDULER";
	public static final String UDC_STRVALUE1_INVOICES = "INVOICES";
	public static final String UDC_SYSTEM_EMAILS = "EMAILS";
	public static final String UDC_STRVALUE1_PAYMENTS = "PAYMENTS";
	
	//Complemento detallista
	public static final String LIVERPOOL_INVOICE = "INVOICE";
	public static final String LIVERPOOL_CREDIT_NOTE = "CREDIT_NOTE";

	//User defaul
	public static final String USER_DEFAULT = "SYSTEM";
	
	//Enviar errores por correo para aviso
	public static final String EMAIL_INVOICE_SUBJECT = "ERROR EN PROCESO DE REPORTE (INVOICE-InvoicesSchedule)";
	public static final String EMAIL_INVOICE_CONTENT = "SE HAN HECHO 5 INTENTOS DE PROCESAR LA INFORMACIóN PERO SE HAN OBTENIDO ERRORES DURANTE EL PROCESO DE FACTURAS";
	public static final String EMAIL_PAYMENTS_SUBJECT = "ERROR EN PROCESO DE REPORTE (PAYMENTS-createPayments)";
	public static final String EMAIL_PAYMENTS_CONTENT = "SE HAN HECHO 5 INTENTOS DE PROCESAR LA INFORMACIóN PERO SE HAN OBTENIDO ERRORES DURANTE EL PROCESO DE PAGOS";
}
