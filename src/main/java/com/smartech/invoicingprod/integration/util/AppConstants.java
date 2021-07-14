package com.smartech.invoicingprod.integration.util;

public class AppConstants {
	
	public static final String ORACLE_USER = "INTEGUSER";
	public static final String ORACLE_PASS = "Mexico$2020";

	public static final String PORTAL_DIST_USER = "JSaavedra";
	public static final String PORTAL_DIST_PASS = "Pa$$word12345";
	
	public static final String ORACLE_URL = "https://fa-epog-test-saasfaprod1.fa.ocs.oraclecloud.com";
//	public static final String ORACLE_URL = "https://fa-epog-saasfaprod1.fa.ocs.oraclecloud.com";
	
	public static final String DIST_PORTAL_URL = "https://integraciontest-axvdt1gojzpc-px.integration.ocp.oraclecloud.com/ic/builder/design/Portal_test_Security/1.0.10/resources/data";
//	public static final String DIST_PORTAL_URL = "https://integraciontest-axvdt1gojzpc-px.integration.ocp.oraclecloud.com/ic/builder/rt/Portal_test_Security/live/resources/data";
	
	public static final String ORACLE_ITEMMASTER = "IMA";
	
	//Analytics
	public static final String URL_ANALYTICS_SESSION = ORACLE_URL + "/analytics-ws/saw.dll?SoapImpl=nQSessionService";
	public static final String URL_ANALYTICS = ORACLE_URL + "/analytics-ws/saw.dll?SoapImpl=xmlViewService";
	
	//Endpoints Portal de Distribudores
	public static final String URL_ENDPOINT_INVOICE = DIST_PORTAL_URL + "/invoice"; 

	//Servicios
	public static final String SERVICE_TEST1 = "TEST1";
	public static final String SERVICE_AR_REPORT_INVOICES = "AR_REPORT_INVOICES";
	public static final String SERVICE_AR_REPORT_PAYMENTS = "AR_REPORT_PAYMENTS";
	public static final String SERVICE_AR_REPORT_TRANSFER = "AR_REPORT_TRANSFER";
	public static final String SERVICE_AR_REPORT_INITIAL_CHARGE = "AR_REPORT_INITIAL_CHARGE";
	public static final String SERVICE_ITEM_COST_FOR_SO = "ITEM_COST_FOR_SO";
	public static final String SERVICE_ITEM_COST_FOR_TRANSFER = "ITEM_COST_FOR_TRANSFER";
	public static final String SERVICE_ASSET_LABEL_REPORT = "ASSET_LABEL";
	public static final String SERVICE_RESPONSIVE_LETTER = "RESPONSIVE_LETTER";
	public static final String SERVICE_REST_TEST1 = "REST_TEST1";
	public static final String SERVICE_REST_ITEMLOT = "REST_ITEMLOT";
	public static final String SERVICE_SALES_ORDER_1 = "SALES_ORDER_1";
	public static final String SERVICE_SALES_ORDER_AI_1 = "SALES_ORDER_AI_1";
	public static final String SERVICE_REST_CURRENCY_RATES = "REST_CURRENCY_RATES";
	public static final String SERVICE_REST_PRICE_LIST = "REST_PRICE_LIST";
	public static final String SERVICE_REST_PRICE_LIST_BY_ITEM = "REST_PRICE_LIST_BY_ITEM";
	public static final String SERVICE_REST_SALES_ORDER_INCOTERM = "SALES_ORDER_INCOTERM";
	public static final String SERVICE_REST_ITEM_CATEGORY = "SERVICE_ITEM_CATEGORY";
	public static final String SERVICE_REST_ITEM_SERIAL_NUMBER = "SERVICE_ITEM_SERIAL_NUMBER";
	public static final String SERVICE_REST_ITEM_COSTS = "ITEM_COSTS";
	//REST API
	public static final String URL_REST_INVORG = ORACLE_URL + "/fscmRestApi/resources/11.13.18.05/inventoryOrganizations";
	public static final String URL_REST_SALESORDER = ORACLE_URL + "/fscmRestApi/resources/11.13.18.05/salesOrdersForOrderHub";
	public static final String URL_REST_SALESORDER_ADDINF = ORACLE_URL + "/fscmRestApi/resources/11.13.18.05/salesOrdersForOrderHub/ORDER_ID/child/additionalInformation";
	public static final String URL_REST_ITEMLOT = ORACLE_URL + "/fscmRestApi/resources/11.13.18.05/inventoryItemLots";
	public static final String URL_REST_CURRENCYRATES = ORACLE_URL + "/fscmRestApi/resources/11.13.18.05/currencyRates";
	public static final String URL_REST_PRICELIST = ORACLE_URL + "/fscmRestApi/resources/11.13.18.05/priceLists";
	public static final String URL_REST_ITEM_CATEGORY = ORACLE_URL + "/fscmRestApi/resources/11.13.18.05/itemCategories";
	public static final String URL_REST_ITEM_SERIAL_NUMBER = ORACLE_URL + "/fscmRestApi/resources/11.13.18.05/inventoryItemSerialNumbers";
	public static final String URL_REST_ITEM_COSTS = ORACLE_URL + "/fscmRestApi/resources/11.13.18.05/itemCosts";
	//SOAP SERVICE
	public static final String URL_SOAP_ITEMSV2 = ORACLE_URL + ":443/fscmService/ItemServiceV2?invoke=";
	public static final String URL_SOAP_DFFFIN =  ORACLE_URL + "/fscmService/ErpObjectDFFUpdateService?invoke=";
	public static final String URL_SOAP_SALESORDERINFO =  ORACLE_URL + ":443/fscmService/OrderInformationService?invoke=";
	public static final String URL_SOAP_ITEMRELATIONSHIP = ORACLE_URL + "/fscmService/ItemRelationshipService?invoke=";
	public static final String URL_SOAP_ITEMCATALOG = ORACLE_URL + "/fscmService/ItemCatalogService?invoke=";
	public static final String URL_SOAP_FOUNDATION_PARTIES = ORACLE_URL + "/crmService/FoundationPartiesOrganizationService?invoke=";
	
	//Estado de facturacion
	public static final String STATUS_START = "START";
	public static final String STATUS_GETRESTDATA = "DATA";
	public static final String STATUS_PETITIONDATA = "PETITION";
	public static final String STATUS_PENDING = "PENDING";
	public static final String STATUS_PENDING_UUID_NC = "PENDINGUUIDNC";	
	public static final String STATUS_UPDUUID = "UPDUUID";
	public static final String STATUS_INVOICED = "INVOICED";
	public static final String STATUS_ERROR_CREATE_FILE = "ERROR CREATE FILE";
	public static final String STATUS_ERROR_DATA = "ERROR DATA";
	public static final String STATUS_ERROR_PAC = "ERROR PAC";
	public static final String STATUS_ERROR_PETITION = "ERROR PETITION";
	public static final String STATUS_FINISHED = "FINISHED";
	public static final String STATUS_ERROR_DATA_PAY = "ERROR_PAY";
	public static final String STATUS_ERROR_DATA_TRANSFER = "ERROR_TRANSFER";
	public static final String STATUS_ERROR_DATA_PAY_LIST = "ERROR PAYMENTS LIST";
	public static final String STATUS_CANCEL_PENDING = "CANCEL PENDING";
	public static final String STATUS_CANCEL_ERROR = "CANCEL ERROR";
	
	
	public static final String STATUS_PAYMENT_LIST_START = "PAY_START";

	public static final String PAY_METHOD= "PUE";
	
	//Estados de reportes
	public static final String STATUS_REPORTS_ING = "Invoice";
	public static final String STATUS_REPORTS_ESP = "Factura";
	public static final String STATUS_REPORTS_LIV_CON_16 = "Fac contado 16";
	
	public static final String STATUS_REPORTS_FCON16 = "Factura contado 16";
	public static final String STATUS_REPORTS_FCRE16 = "Factura credito 16";
	public static final String STATUS_REPORTS_FCON0 = "Factura contado 0";
	public static final String STATUS_REPORTS_FCRE0 = "Factura credito 0";
	public static final String STATUS_REPORTS_FECON = "Fac exp contado";
	public static final String STATUS_REPORTS_FECRE = "Fac exp credito";
	public static final String STATUS_REPORTS_INVOICE = "Invoice";
	public static final String STATUS_REPORTS_FACTURA = "Factura";
	public static final String STATUS_REPORTS_CREDIT_NOTE = "Credit Memo";
	public static final String STATUS_REPORTS_FACCON16 = "Fac contado 16";
	public static final String STATUS_REPORTS_FACCRE16 = "Fac credito 16";
	public static final String STATUS_REPORTS_FACCRE0 = "Fac credito 0";
	public static final String STATUS_REPORTS_FACCON0 = "Fac contado 0";
	
	public static final String STATUS_REPORTS_NC_CON_16 = "Nc contado 16";
	public static final String STATUS_REPORTS_NC_CON_0 = "Nc contado 0";
	public static final String STATUS_REPORTS_NC_CRE_16 = "Nc credito 16";
	public static final String STATUS_REPORTS_NC_CRE_0 = "Nc contado 0";
		
	public static final String[] STATUS_ALL_INVOICES = {
														"Factura contado 16", 
														"Factura credito 16", 
														"Factura contado 0", 
														"Factura credito 0",
														"Fac exp contado",
														"Fac exp credito",
														"Invoice",
														"Factura"};
	public static final String[] STATUS_ALL_NC = {
													"Nc contado 16",
													"Nc credito 16",
													"Nc contado 0",
													"Nc credito 0",
													"Nc exp contado",
													"Nc exp credito",
													"Credit Memo"};
	
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
	public static final String ORDER_TYPE_ADV = "ADVPAYMENT";
	public static final String ORDER_TYPE_TRANS = "TRANSFER";
	public static final String ORDER_TYPE_CANCEL = "CANCEL";
	
	public static final String PAYMENTS_CPAGO = "CPAGO";
	public static final String PAYMENTS_ADVPAY = "ADVPAY";
	
	public static final String INVOICE_SAT_TYPE_I = "I"; 
	public static final String INVOICE_SAT_TYPE_E = "E";
	public static final String IS_ADVANCE_PAYMENT = "Y";
	public static final String CHOICE_TAX = "Y";
	
	//TIPO DE CAMBIO ESTANDAR
	public static final double INVOICE_EXCHANGE_RATE= 1.00;

	//Datos para anticipos
	public static final double INVOICE_TAX_CODE_000 = 0;
	public static final double INVOICE_TAX_CODE_016 = 0.16;
	public static final double INVOICE_TAX_CODE_116 = 1.16;
	public static final String INVOICE_ADVPAY_DEFAULT_UOM = "PZ";
	public static final double INVOICE_ADVPAY_DEFAULT_QUANTITY = 1.00;
	public static final String INVOICE_ADVPAY_DEFAULT_TRANSLINNUMBER = "1";
	
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
	public static final String UDC_SYSTEM_EMAILSERRORS = "EMAILSERRORS";
	public static final String UDC_STRVALUE1_PAYMENTS = "PAYMENTS";
	public static final String UDC_STRVALUE1_ADVANCE_PAYMENTS = "ANTICIPOS";
	public static final String UDC_SYSTEM_PAYTERMS = "PAYMENTTERMS";
	public static final String PTERMS_CONTADO = "CONTADO";
	public static final String UDC_SYSTEM_MARINA = "MARINA";
	public static final String UDC_KEY_MARINA = "SNA850102TQ3";
	public static final String UDC_KEY_TRANSFER = "TRANSFER";
	public static final String UDC_SYSTEM_CENTERCOST = "CENTERCOST";
	public static final String UDC_KEY_LABEL = "LABEL";
	public static final String UDC_SYSTEM_IMEMSAPRDTYPE = "IMEMSAPRDTYPE";
	public static final String UDC_SYSTEM_REPINVOICE = "REPINVOICE";
	public static final String UDC_KEY_NOIVA = "NO IVA";
	public static final String UDC_KEY_ANTICIPOS = "ANTICIPOS";
	public static final String UDC_KEY_UPDATE_LABEL = "UPDATELABEL";
	public static final String UDC_KEY_FIXED_ASSET = "ACTIVO FIJO";	
	public static final String UDC_KEY_SERVICE1 = "SERVICIOS1";	
	public static final String UDC_KEY_SERVICE_SEAMEX = "SERVICIOSSEAMEX";
	public static final String UDC_KEY_OTHERS_PRODUCTS = "OTROS PRODUCTOS";
	public static final String UDC_KEY_INITIAL_CHARGE = "INITIAL_CHARGE";
	public static final String UDC_SYSTEM_INVOICE_RELTYPE = "INVOICERELTYPE";
	public static final String UDC_KEY_ADVPAYMENT = "ADVPAYMENT";
	public static final String UDC_SYSTEM_RFC_EXT = "RFCGEN";
	public static final String UDC_KEY_CANCEL_TRANSACTION_TYPE = "CANCELACIONES";
	
	//Complemento detallista
	public static final String LIVERPOOL_INVOICE = "INVOICE";
	public static final String LIVERPOOL_CREDIT_NOTE = "CREDIT_NOTE";

	//User defaul
	public static final String USER_DEFAULT = "SYSTEM";
	public static final String CROSS_REFERENCE_TYPE_LIV = "SKU CLIENTE";
	public static final String BRANCH_DEFUALT = "CEDIS";
	
	//Enviar errores por correo para aviso
	public static final String EMAIL_INVOICE_SUBJECT = "ERROR EN PROCESO DE REPORTE (INVOICE-InvoicesSchedule)";
	public static final String EMAIL_INVOICE_CONTENT = "SE HAN HECHO 5 INTENTOS DE PROCESAR LA INFORMACIóN PERO SE HAN OBTENIDO ERRORES DURANTE EL PROCESO DE FACTURAS";
	public static final String EMAIL_PAYMENTS_SUBJECT = "ERROR EN PROCESO DE REPORTE (PAYMENTS-createPayments)";
	public static final String EMAIL_PAYMENTS_CONTENT = "SE HAN HECHO 5 INTENTOS DE PROCESAR LA INFORMACIóN PERO SE HAN OBTENIDO ERRORES DURANTE EL PROCESO DE PAGOS";
	public static final String EMAIL_TRANSFER_SUBJECT = "ERROR EN PROCESO DE REPORTE (TRANSFER-createTransferInvoice)";
	public static final String EMAIL_TRANSFER_CONTENT = "SE HAN HECHO 5 INTENTOS DE PROCESAR LA INFORMACIóN PERO SE HAN OBTENIDO ERRORES DURANTE EL PROCESO DEL CFDI DE TRASLADOS";
	public static final String EMAIL_LABEL_SUBJECT = "ERROR EN PROCESO DE REPORTE DE ETIQUETAS (LABEL-labelProccessScheduler)";
	public static final String EMAIL_LABEL_CONTENT = "SE HAN HECHO 5 INTENTOS DE PROCESAR LA INFORMACIóN PERO SE HAN OBTENIDO ERRORES DURANTE EL PROCESO DEL LA OBTENCIÓN DE LOS ACTIVOS MEDIANTE EL REPORTE";
	
	//Notificación de alertas por correo
	public static final String EMAIL_ADV_PAYMENTS_SUBJECT = "NOTIFICACIÓN DEL PROCESO DE ANTICIPOS";
	public static final String EMAIL_ADV_PAYMENTS_CONTENT_PENDING_PAY = "LA FACTURA CON FOLIO: _FOLIO_ TIENE SALDO PENDIENTE DE PAGO, SE PODRÁ CUBRIR EN PARCIALIDADES O EN UNA SOLA EXHIBICIÓN.";
	
	//Comercio exterior
	public static final String INVOICE_EXTERIOR_COMPLEMENT = "IME-VTAEXPORTACION";
	public static final String COUNTRY_DEFAULT = "MEX";
	
	//TRANSFERENCIA
	public static final String DEFAUL_CURRENCY = "MXN";
	
	//LEYENDAS
	public static final String LEY_EMB_COM = "EMBARCACIÓN COMPLETA PARA USO COMERCIAL \"DESTINADA PARA PESCA\", \r\n" + 
			"SE GRABA A TASA 0% DE IVA CON FUNDAMENTO\r\n " + 
			"LEGAL EN EL ART. 2-A, FRACC. I, INCISO E, DE LA LEY DEL IMPUESTO\r\n " + 
			"AL VALOR AGREGADO Y SU ART. 8 DEL REGLAMENTO DE LA MISMA LEY.\r\n " + 
			"EL EQUIPO FACTURADO ES EL SIGUIENTE:\r\n ";
	public static final String LEY_INV_CAT_LAN = "LANCHA";
	public static final String LEY_INV_CAT_EMB = "MOTOR";
	public static final String LEY_LANCHAS = "EMBARCACIÓN PARA PESCA COMERCIAL, SE GRAVA A TASA 0% DE IVA\r\n " + 
			"CON FUNDAMENTO LEGAL EN EL ART. 2-A, FRACC. I. INCISO E, DE LA LEY DEL IMPUESTO AL VALOR AGREGADO Y SU\r\n " + 
			"ART. 8 DEL REGLAMENTO DE LA MISMA LEY.";
	
	
	//PORTAL DE DISTRIBUIDORES
	public static final String UDC_SYSTEM_GET_ALL_DISTRIBUITORS = "DISTRIBUITORSNAMES";
}
