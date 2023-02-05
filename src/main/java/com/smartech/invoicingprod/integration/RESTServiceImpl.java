package com.smartech.invoicingprod.integration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.smartech.invoicingprod.integration.dto.HeadersRestDTO;
import com.smartech.invoicingprod.integration.dto.ParamsRestDTO;
import com.smartech.invoicingprod.integration.json.IncotermByRest.IncotermByRest;
import com.smartech.invoicingprod.integration.json.dailyRates.CurrencyRates;
import com.smartech.invoicingprod.integration.json.inventoryItemSerialNumbers.InventoryItemSerialNumbers;
import com.smartech.invoicingprod.integration.json.invitemlot.InventoryItemLots;
import com.smartech.invoicingprod.integration.json.invorg.InventoryOrganization;
import com.smartech.invoicingprod.integration.json.itemCategories.ItemCategory;
import com.smartech.invoicingprod.integration.json.priceList.PriceLists;
import com.smartech.invoicingprod.integration.json.priceListByItem.PriceListByItem;
import com.smartech.invoicingprod.integration.json.receivablesInvoices.ReceivablesInvoices;
import com.smartech.invoicingprod.integration.json.salesorder.SalesOrder;
import com.smartech.invoicingprod.integration.json.salesorderai.SalesOrderAI;
import com.smartech.invoicingprod.integration.json.unitCost.ItemCosts;
import com.smartech.invoicingprod.integration.service.HTTPRequestService;
import com.smartech.invoicingprod.integration.util.AppConstants;
import com.smartech.invoicingprod.model.Invoice;

@Service("restService")
public class RESTServiceImpl implements RESTService {

	@Autowired
	HTTPRequestService httpRequestService;
	
	static Logger log = Logger.getLogger(RESTServiceImpl.class.getName());
	
	@Override
	public InventoryOrganization getInventoryOrganization() {
		try {
			List<HeadersRestDTO> headers = new ArrayList<HeadersRestDTO>();
			headers.add(new HeadersRestDTO("Content-Type", "application/json"));
			headers.add(new HeadersRestDTO("Accept", "*/*"));
			headers.add(new HeadersRestDTO("User-Agent", "Java Client"));
			List<ParamsRestDTO> params = new ArrayList<ParamsRestDTO>();
			params.add(new ParamsRestDTO("q", "InventoryFlag=true"));
			params.add(new ParamsRestDTO("onlyData", true));
			params.add(new ParamsRestDTO("offset", 0));
			params.add(new ParamsRestDTO("limit", 100));
			
			Map<String, Object> response = httpRequestService.httpRESTRequest(AppConstants.ORACLE_USER, AppConstants.ORACLE_PASS,
					AppConstants.URL_REST_INVORG, HttpMethod.GET, headers, params, null, AppConstants.SERVICE_REST_TEST1);
			
			int statusCode;
			InventoryOrganization responseRest;
			
			if(response != null) {
				statusCode = (int) response.get("code");
				responseRest = (InventoryOrganization) response.get("response");
				if(statusCode >= 200 && statusCode < 300) {
					return responseRest;
				}
			}
			
			return null;
		}catch(Exception e) {
			e.printStackTrace();
			log.error("REST API SERVICE FAIL getInventoryOrganization ****************************", e);
			return null;
		}
	}

	@Override
	public SalesOrder getSalesOrderByOrderNumber(String orderNumber) {
		try {
			if(orderNumber != null && !"".contains(orderNumber)) {
				List<HeadersRestDTO> headers = new ArrayList<HeadersRestDTO>();
				headers.add(new HeadersRestDTO("Content-Type", "application/json"));
				headers.add(new HeadersRestDTO("Accept", "*/*"));
				headers.add(new HeadersRestDTO("User-Agent", "Java Client"));
				List<ParamsRestDTO> params = new ArrayList<ParamsRestDTO>();
				if(StringUtils.isNumeric(orderNumber)) {
					params.add(new ParamsRestDTO("finder", "findByOrderNumber;OrderNumber=" + orderNumber));
				}else {
					params.add(new ParamsRestDTO("finder", "findBySourceOrderNumberAndSystem;SourceTransactionSystem=OPS,SourceTransactionNumber=" + orderNumber));
				}		
				params.add(new ParamsRestDTO("expand", "lines,lines.lotSerials"));
				params.add(new ParamsRestDTO("fields", "HeaderId,OrderNumber,SourceTransactionNumber,SourceTransactionSystem,BusinessUnitId,BusinessUnitName,RequestedFulfillmentOrganizationId,RequestedFulfillmentOrganizationCode,PaymentTermsCode,PaymentTerms,TransactionalCurrencyCode,TransactionalCurrencyName,CurrencyConversionRate,CurrencyConversionType,StatusCode;lines:SourceTransactionLineNumber,AssessableValue,FulfilledQuantity,ProductId,ProductNumber,ProductDescription,InventoryOrganizationCode,OrderedQuantity,OrderedUOMCode,OrderedUOM,StatusCode,TaxClassificationCode,TaxClassification,LineNumber;lines.lotSerials:ItemSerialNumberFrom,ItemSerialNumberTo,LotNumber"));
				params.add(new ParamsRestDTO("orderBy", "LastUpdateDate:desc"));
				params.add(new ParamsRestDTO("onlyData", true));
				params.add(new ParamsRestDTO("offset", 0));
				params.add(new ParamsRestDTO("limit", 1));
				
				Map<String, Object> response = httpRequestService.httpRESTRequest(AppConstants.ORACLE_USER, AppConstants.ORACLE_PASS,
						AppConstants.URL_REST_SALESORDER, HttpMethod.GET, headers, params, null, AppConstants.SERVICE_SALES_ORDER_1);
				
				int statusCode;
				SalesOrder responseRest;
				
				if(response != null) {
					statusCode = (int) response.get("code");
					responseRest = (SalesOrder) response.get("response");
					if(statusCode >= 200 && statusCode < 300) {
						return responseRest;
					}
				}
			}
			
			return null;
		}catch(Exception e) {
			e.printStackTrace();
			log.error("REST API SERVICE FAIL getSalesOrderByOrderNumber ****************************", e);
			return null;
		}
	}

	@Override
	public SalesOrderAI getAddInfoBySalesNumber(SalesOrder salesOrder) {
		try {
			if(salesOrder != null && !salesOrder.getItems().isEmpty()) {
				List<HeadersRestDTO> headers = new ArrayList<HeadersRestDTO>();
				headers.add(new HeadersRestDTO("Content-Type", "application/json"));
				headers.add(new HeadersRestDTO("Accept", "*/*"));
				headers.add(new HeadersRestDTO("User-Agent", "Java Client"));
				List<ParamsRestDTO> params = new ArrayList<ParamsRestDTO>();
				params.add(new ParamsRestDTO("expand", "all"));
				params.add(new ParamsRestDTO("onlyData", true));
				params.add(new ParamsRestDTO("offset", 0));
				params.add(new ParamsRestDTO("limit", 1));
				
				String url = AppConstants.URL_REST_SALESORDER_ADDINF.replaceAll("ORDER_ID", salesOrder.getItems().get(0).getSourceTransactionSystem() + ":" + salesOrder.getItems().get(0).getHeaderId());
				
				Map<String, Object> response = httpRequestService.httpRESTRequest(AppConstants.ORACLE_USER, AppConstants.ORACLE_PASS,
						url, HttpMethod.GET, headers, params, null, AppConstants.SERVICE_SALES_ORDER_AI_1);
				
				int statusCode;
				SalesOrderAI responseRest;
				
				if(response != null) {
					statusCode = (int) response.get("code");
					responseRest = (SalesOrderAI) response.get("response");
					if(statusCode >= 200 && statusCode < 300) {
						return responseRest;
					}
				}
			}
			
			return null;
		}catch(Exception e) {
			e.printStackTrace();
			log.error("REST API SERVICE FAIL getAddInfoBySalesNumber ****************************", e);
			return null;
		}
	}

	@Override
	public InventoryItemLots getInventoryLot(String invOrgCode, String itemNumber, String itemLot) {
		try {
			List<HeadersRestDTO> headers = new ArrayList<HeadersRestDTO>();
			headers.add(new HeadersRestDTO("Content-Type", "application/json"));
			headers.add(new HeadersRestDTO("Accept", "*/*"));
			headers.add(new HeadersRestDTO("User-Agent", "Java Client"));
			List<ParamsRestDTO> params = new ArrayList<ParamsRestDTO>();
			params.add(new ParamsRestDTO("finder", "findByOrgItemAndLot;bindOrganizationCode=" + invOrgCode +",bindItemNumber=" + itemNumber +",bindLotNumber=" + itemLot));
			params.add(new ParamsRestDTO("fields", "OrganizationId,OrganizationCode,InventoryItemId,ItemNumber,LotNumber,OriginationDate"));
			params.add(new ParamsRestDTO("onlyData", true));
			
			Map<String, Object> response = httpRequestService.httpRESTRequest(AppConstants.ORACLE_USER, AppConstants.ORACLE_PASS,
					AppConstants.URL_REST_ITEMLOT, HttpMethod.GET, headers, params, null, AppConstants.SERVICE_REST_ITEMLOT);
			
			int statusCode;
			InventoryItemLots responseRest;
			
			if(response != null) {
				statusCode = (int) response.get("code");
				responseRest = (InventoryItemLots) response.get("response");
				if(statusCode >= 200 && statusCode < 300) {
					return responseRest;
				}
			}
			
			return null;
		}catch(Exception e) {
			e.printStackTrace();
			log.error("REST API SERVICE FAIL getInventoryLot ****************************", e);
			return null;
		}
	}

	@Override
	public CurrencyRates getDailyCurrency(String date, String fromCurrency, String toCurrency) {
		try {
			List<HeadersRestDTO> headers = new ArrayList<HeadersRestDTO>();
			headers.add(new HeadersRestDTO("Content-Type", "application/json"));
			headers.add(new HeadersRestDTO("Accept", "*/*"));
			headers.add(new HeadersRestDTO("User-Agent", "Java Client"));
			List<ParamsRestDTO> params = new ArrayList<ParamsRestDTO>();
			params.add(new ParamsRestDTO("finder", "CurrencyRatesFinder;fromCurrency=" + fromCurrency + ",toCurrency=" + toCurrency + ",startDate=" + date + ",endDate=" + date + ",userConversionType=Diario"));
			params.add(new ParamsRestDTO("onlyData", true));
			
			Map<String, Object> response = httpRequestService.httpRESTRequest(AppConstants.ORACLE_USER, AppConstants.ORACLE_PASS,
					AppConstants.URL_REST_CURRENCYRATES, HttpMethod.GET, headers, params, null, AppConstants.SERVICE_REST_CURRENCY_RATES);
			
			int statusCode;
			CurrencyRates responseRest;
			
			if(response != null) {
				statusCode = (int) response.get("code");
				responseRest = (CurrencyRates) response.get("response");
				if(statusCode >= 200 && statusCode < 300) {
					return responseRest;
				}
			}
			
			return null;
		}catch(Exception e) {
			e.printStackTrace();
			log.error("REST API SERVICE FAIL getInventoryLot ****************************", e);
			return null;
		}
	}

	@Override
	public PriceLists getPriceList(Invoice i) {
		try {
			List<HeadersRestDTO> headers = new ArrayList<HeadersRestDTO>();
			headers.add(new HeadersRestDTO("Content-Type", "application/json"));
			headers.add(new HeadersRestDTO("Accept", "*/*"));
			headers.add(new HeadersRestDTO("User-Agent", "Java Client"));
			List<ParamsRestDTO> params = new ArrayList<ParamsRestDTO>();
			params.add(new ParamsRestDTO("q", "BusinessUnit=" + i.getCompany().getName() + "; Status=Approved"));
			params.add(new ParamsRestDTO("fields", "PriceListName,PriceListId,CurrencyCode"));
			params.add(new ParamsRestDTO("onlyData", true));
			
			Map<String, Object> response = httpRequestService.httpRESTRequest(AppConstants.ORACLE_USER, AppConstants.ORACLE_PASS,
					AppConstants.URL_REST_PRICELIST, HttpMethod.GET, headers, params, null, AppConstants.SERVICE_REST_PRICE_LIST);
			
			int statusCode;
			PriceLists responseRest;
			
			if(response != null) {
				statusCode = (int) response.get("code");
				responseRest = (PriceLists) response.get("response");
				if(statusCode >= 200 && statusCode < 300) {
					return responseRest;
				}
			}
			
			return null;
		}catch(Exception e) {
			e.printStackTrace();
			log.error("REST API SERVICE FAIL getInventoryLot ****************************", e);
			return null;
		}
	}

	@Override
	public PriceListByItem getPrice(String itemNumber, String priceListId) {
		try {
			String url = AppConstants.URL_REST_PRICELIST + "/" + priceListId + "/child/items";
			List<HeadersRestDTO> headers = new ArrayList<HeadersRestDTO>();
			headers.add(new HeadersRestDTO("Content-Type", "application/json"));
			headers.add(new HeadersRestDTO("Accept", "*/*"));
			headers.add(new HeadersRestDTO("User-Agent", "Java Client"));
			List<ParamsRestDTO> params = new ArrayList<ParamsRestDTO>();
			params.add(new ParamsRestDTO("q", "Item=" + itemNumber));
			params.add(new ParamsRestDTO("fields", "Item;charges:BasePrice"));
			params.add(new ParamsRestDTO("expand", "charges"));
			params.add(new ParamsRestDTO("onlyData", true));
			
			Map<String, Object> response = httpRequestService.httpRESTRequest(AppConstants.ORACLE_USER, AppConstants.ORACLE_PASS,
					url , HttpMethod.GET, headers, params, null, AppConstants.SERVICE_REST_PRICE_LIST_BY_ITEM);
			
			int statusCode;
			PriceListByItem responseRest;
			
			if(response != null) {
				statusCode = (int) response.get("code");
				responseRest = (PriceListByItem) response.get("response");
				if(statusCode >= 200 && statusCode < 300) {
					return responseRest;
				}
			}
			
			return null;
		}catch(Exception e) {
			e.printStackTrace();
			log.error("REST API SERVICE FAIL getInventoryLot ****************************", e);
			return null;
		}
	}

	@Override
	public IncotermByRest getIncoterm(String orderNumber) {
		try {
			List<HeadersRestDTO> headers = new ArrayList<HeadersRestDTO>();
			headers.add(new HeadersRestDTO("Content-Type", "application/json"));
			headers.add(new HeadersRestDTO("Accept", "*/*"));
			headers.add(new HeadersRestDTO("User-Agent", "Java Client"));
			List<ParamsRestDTO> params = new ArrayList<ParamsRestDTO>();
			params.add(new ParamsRestDTO("q", "OrderNumber=" + orderNumber + ";SubmittedFlag=true"));
			params.add(new ParamsRestDTO("expand", "totals"));
			params.add(new ParamsRestDTO("fields", "FreightTermsCode,ShippingCarrier,CustomerPONumber;totals:TotalCode,TotalAmount"));
			params.add(new ParamsRestDTO("onlyData", true));
			
			Map<String, Object> response = httpRequestService.httpRESTRequest(AppConstants.ORACLE_USER, AppConstants.ORACLE_PASS,
					AppConstants.URL_REST_SALESORDER , HttpMethod.GET, headers, params, null, AppConstants.SERVICE_REST_SALES_ORDER_INCOTERM);
			
			int statusCode;
			IncotermByRest responseRest;
			
			if(response != null) {
				statusCode = (int) response.get("code");
				responseRest = (IncotermByRest) response.get("response");
				if(statusCode >= 200 && statusCode < 300) {
					return responseRest;
				}
			}
			
			return null;
		}catch(Exception e) {
			e.printStackTrace();
			log.error("REST API SERVICE FAIL getInventoryLot ****************************", e);
			return null;
		}
	}

	@Override
	public ItemCategory getCategoryCode(String categoryName) {
		try {
			List<HeadersRestDTO> headers = new ArrayList<HeadersRestDTO>();
			headers.add(new HeadersRestDTO("Content-Type", "application/json"));
			headers.add(new HeadersRestDTO("Accept", "*/*"));
			headers.add(new HeadersRestDTO("User-Agent", "Java Client"));
			List<ParamsRestDTO> params = new ArrayList<ParamsRestDTO>();
			params.add(new ParamsRestDTO("q", "CategoryName = \"" + categoryName +"\""));
			params.add(new ParamsRestDTO("expand", "DFF"));
			params.add(new ParamsRestDTO("fields", "CategoryName;DFF:tipoProducto,yearModel"));
			params.add(new ParamsRestDTO("onlyData", true));
			
			Map<String, Object> response = httpRequestService.httpRESTRequest(AppConstants.ORACLE_USER, AppConstants.ORACLE_PASS,
					AppConstants.URL_REST_ITEM_CATEGORY , HttpMethod.GET, headers, params, null, AppConstants.SERVICE_REST_ITEM_CATEGORY);
			
			int statusCode;
			ItemCategory responseRest;
			
			if(response != null) {
				statusCode = (int) response.get("code");
				responseRest = (ItemCategory) response.get("response");
				if(statusCode >= 200 && statusCode < 300) {
					return responseRest;
				}
			}
			
			return null;
		}catch(Exception e) {
			e.printStackTrace();
			log.error("REST API SERVICE FAIL getInventoryLot ****************************", e);
			return null;
		}
	}

	@Override
	public InventoryItemSerialNumbers getInventoryItemSerialModel(String orgCode, String itemNumber,
			String SerialNumber) {
		try {
			List<HeadersRestDTO> headers = new ArrayList<HeadersRestDTO>();
			headers.add(new HeadersRestDTO("Content-Type", "application/json"));
			headers.add(new HeadersRestDTO("Accept", "*/*"));
			headers.add(new HeadersRestDTO("User-Agent", "Java Client"));
			List<ParamsRestDTO> params = new ArrayList<ParamsRestDTO>();
			params.add(new ParamsRestDTO("q", "OrganizationCode=" + orgCode + ";ItemNumber="+ itemNumber +";SerialNumber="  + SerialNumber));
			params.add(new ParamsRestDTO("fields", "SupplierSerialNumber"));
			params.add(new ParamsRestDTO("onlyData", true));
			
			Map<String, Object> response = httpRequestService.httpRESTRequest(AppConstants.ORACLE_USER, AppConstants.ORACLE_PASS,
					AppConstants.URL_REST_ITEM_SERIAL_NUMBER , HttpMethod.GET, headers, params, null, AppConstants.SERVICE_REST_ITEM_SERIAL_NUMBER);
			
			int statusCode;
			InventoryItemSerialNumbers responseRest;
			
			if(response != null) {
				statusCode = (int) response.get("code");
				responseRest = (InventoryItemSerialNumbers) response.get("response");
				if(statusCode >= 200 && statusCode < 300) {
					return responseRest;
				}
			}
			
			return null;
		}catch(Exception e) {
			e.printStackTrace();
			log.error("REST API SERVICE FAIL getInventoryLot ****************************", e);
			return null;
		}
	}

	@Override
	public ItemCosts getItemCostWitoutSerialNumber(String itemNumber) {
		try {
			List<HeadersRestDTO> headers = new ArrayList<HeadersRestDTO>();
			headers.add(new HeadersRestDTO("Content-Type", "application/json"));
			headers.add(new HeadersRestDTO("Accept", "*/*"));
			headers.add(new HeadersRestDTO("User-Agent", "Java Client"));
			List<ParamsRestDTO> params = new ArrayList<ParamsRestDTO>();
			params.add(new ParamsRestDTO("q", "CostBook=SECUNDARIO;ItemNumber=" + itemNumber));
			params.add(new ParamsRestDTO("fields", "ItemNumber;costDetails:CostElement,UnitCostAverage"));
			params.add(new ParamsRestDTO("onlyData", true));
			
			Map<String, Object> response = httpRequestService.httpRESTRequest(AppConstants.ORACLE_USER, AppConstants.ORACLE_PASS,
					AppConstants.URL_REST_ITEM_COSTS , HttpMethod.GET, headers, params, null, AppConstants.SERVICE_REST_ITEM_COSTS);
			
			int statusCode;
			ItemCosts responseRest;
			
			if(response != null) {
				statusCode = (int) response.get("code");
				responseRest = (ItemCosts) response.get("response");
				if(statusCode >= 200 && statusCode < 300) {
					return responseRest;
				}
			}
			
			return null;
		}catch(Exception e) {
			e.printStackTrace();
			log.error("REST API SERVICE FAIL getInventoryLot ****************************", e);
			return null;
		}
	}

	@Override
	public ReceivablesInvoices getInvoiceData(String transactionNumber) {
		try {
			List<HeadersRestDTO> headers = new ArrayList<HeadersRestDTO>();
			headers.add(new HeadersRestDTO("Content-Type", "application/json"));
			headers.add(new HeadersRestDTO("Accept", "*/*"));
			headers.add(new HeadersRestDTO("User-Agent", "Java Client"));
			List<ParamsRestDTO> params = new ArrayList<ParamsRestDTO>();
			params.add(new ParamsRestDTO("q", "TransactionNumber=" + transactionNumber));
			params.add(new ParamsRestDTO("fields", "InvoiceCurrencyCode,TransactionNumber,TransactionType,ConversionRateType,EnteredAmount,InvoiceBalanceAmount,BillToCustomerName,BillToCustomerNumber,BillToContact"));
			params.add(new ParamsRestDTO("onlyData", true));
			
			Map<String, Object> response = httpRequestService.httpRESTRequest(AppConstants.ORACLE_USER, AppConstants.ORACLE_PASS,
					AppConstants.URL_REST_RECEIVABLES_INVOICES , HttpMethod.GET, headers, params, null, AppConstants.SERVICE_REST_RECEIVABLES_INVOICES);
			
			int statusCode;
			ReceivablesInvoices responseRest;
			
			if(response != null) {
				statusCode = (int) response.get("code");
				responseRest = (ReceivablesInvoices) response.get("response");
				if(statusCode >= 200 && statusCode < 300) {
					return responseRest;
				}
			}
			
			return null;
		}catch(Exception e) {
			e.printStackTrace();
			log.error("REST API SERVICE FAIL getInventoryLot ****************************", e);
			return null;
		}
	}

	@Override
	public CurrencyRates getDailyCurrencyExportacion(String date, String fromCurrency, String toCurrency) {
		try {
			List<HeadersRestDTO> headers = new ArrayList<HeadersRestDTO>();
			headers.add(new HeadersRestDTO("Content-Type", "application/json"));
			headers.add(new HeadersRestDTO("Accept", "*/*"));
			headers.add(new HeadersRestDTO("User-Agent", "Java Client"));
			List<ParamsRestDTO> params = new ArrayList<ParamsRestDTO>();
			params.add(new ParamsRestDTO("finder", "CurrencyRatesFinder;fromCurrency=" + fromCurrency + ",toCurrency=" + toCurrency + ",startDate=" + date + ",endDate=" + date + ",userConversionType=Fix _Exportacion"));
			params.add(new ParamsRestDTO("onlyData", true));
			
			Map<String, Object> response = httpRequestService.httpRESTRequest(AppConstants.ORACLE_USER, AppConstants.ORACLE_PASS,
					AppConstants.URL_REST_CURRENCYRATES, HttpMethod.GET, headers, params, null, AppConstants.SERVICE_REST_CURRENCY_RATES);
			
			int statusCode;
			CurrencyRates responseRest;
			
			if(response != null) {
				statusCode = (int) response.get("code");
				responseRest = (CurrencyRates) response.get("response");
				if(statusCode >= 200 && statusCode < 300) {
					return responseRest;
				}
			}
			
			return null;
		}catch(Exception e) {
			e.printStackTrace();
			log.error("REST API SERVICE FAIL getInventoryLot ****************************", e);
			return null;
		}
	}
}
