package com.smartech.invoicing.integration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.smartech.invoicing.dto.CategoryDTO;
import com.smartech.invoicing.dto.ItemsDTO;
import com.smartech.invoicing.dto.SalesLineLotSerDTO;
import com.smartech.invoicing.dto.SalesOrderDTO;
import com.smartech.invoicing.dto.SalesOrderLinesDTO;
import com.smartech.invoicing.integration.service.HTTPRequestService;
import com.smartech.invoicing.integration.util.AppConstants;
import com.smartech.invoicing.integration.util.PayloadProducer;
import com.smartech.invoicing.model.Invoice;
import com.smartech.invoicing.model.Udc;
import com.smartech.invoicing.service.UdcService;
import com.smartech.invoicing.util.NullValidator;

@Service("soapService")
public class SOAPServiceImpl implements SOAPService {

	@Autowired
	HTTPRequestService httpRequestService;
	@Autowired
	UdcService udcService;
	
	static Logger log = Logger.getLogger(SOAPService.class.getName());
	
	@Override
	public ItemsDTO getItemDataByItemNumberOrgCode(String itemNumber, String orgCode) {
		ItemsDTO item = null;
		JSONObject xmlJSONObj;
		JSONObject json;
		JsonElement jelement;
		JsonObject jobject;
		if((itemNumber != null && !"".contains(itemNumber)) && (itemNumber != null && !"".contains(itemNumber))) {
			try {
				Map<String, Object> request1 = httpRequestService.httpXMLRequest(AppConstants.URL_SOAP_ITEMSV2, 
																	PayloadProducer.getItemDataByItemNumberOrgCode(itemNumber, orgCode),AppConstants.ORACLE_USER + ":" + AppConstants.ORACLE_PASS);;
				String strResponse1 = (String) request1.get("response");
				int codeResponse1 = (int) request1.get("code");
				String strHttpResponse1 = (String) request1.get("httpResponse");
				
				if(codeResponse1 >= 200 && codeResponse1 < 300) {
					if(strResponse1 != null && !"".contains(strResponse1)) {
						xmlJSONObj = XML.toJSONObject(strResponse1, true);
						jelement = new JsonParser().parse(xmlJSONObj.toString());
						jobject = jelement.getAsJsonObject();
						if(jobject.get("env:Envelope").getAsJsonObject().get("env:Body").getAsJsonObject().has("ns0:findItemResponse")) {
							if(jobject.get("env:Envelope").getAsJsonObject().get("env:Body").getAsJsonObject()
									.get("ns0:findItemResponse").getAsJsonObject().get("ns2:result").getAsJsonObject().has("ns0:Value")) {
								JsonObject result = jobject.get("env:Envelope").getAsJsonObject().get("env:Body").getAsJsonObject()
										.get("ns0:findItemResponse").getAsJsonObject().get("ns2:result").getAsJsonObject().get("ns0:Value").getAsJsonObject();
								
								if(!result.isJsonNull()) {
									item = new ItemsDTO();
									item.setItemNumber(result.get("ns1:ItemNumber").getAsString());
									item.setItemDescription(result.get("ns1:ItemDescription").getAsString());
									if(result.has("ns1:ItemCategory")) {
										List<CategoryDTO> catList = new ArrayList<CategoryDTO>();
										if(result.get("ns1:ItemCategory").isJsonArray()) {
											JsonArray jsonarray = result.get("ns1:ItemCategory").getAsJsonArray();
											for (int i = 0; i < jsonarray.size(); i++) {
												CategoryDTO c = new CategoryDTO();
												JsonElement op = jsonarray.get(i).getAsJsonObject();
												c.setCatalogCode(NullValidator.isNull(op.getAsJsonObject().get("ns1:ItemCatalog").toString()));
												c.setCategoryName(NullValidator.isNull(op.getAsJsonObject().get("ns1:CategoryName").toString()));
												if(!c.getCatalogCode().isEmpty()) {
													catList.add(c);
												}
											}
										}else {
											CategoryDTO c = new CategoryDTO();
											c.setCatalogCode(NullValidator.isNull(result.get("ns1:ItemCategory").getAsJsonObject().get("ns1:ItemCatalog").toString()));
											c.setCategoryName(NullValidator.isNull(result.get("ns1:ItemCategory").getAsJsonObject().get("ns1:CategoryName").toString()));
											if(!c.getCatalogCode().isEmpty()) {
												catList.add(c);
											}
										}
										item.setItemCategory(catList);
									}
									Udc udcFlex = udcService.searchBySystemAndKey("XMLFLEX", "PRODFLEX");
									if(result.has("ns1:ItemDFF") && udcFlex != null){
										item.setItemDFFClavProdServ(NullValidator.isNull(result.get("ns1:ItemDFF").getAsJsonObject().get(udcFlex.getStrValue1() + "codigoSat").toString()));
									}
								}
							}
															 
						}
					}
				}
			}catch(Exception e) {
				e.printStackTrace();
				log.error("ERROR AL OBTENER WS DE ITEMS_V2 - getItemDataByItemNumberOrgCode***************************", e);
			}
		}
		return item;
	}

	@Override
	public Invoice updateUUIDToOracleERP(Invoice inv) {
		JSONObject xmlJSONObj;
		JSONObject json;
		JsonElement jelement;
		JsonObject jobject;
		if(inv != null) {
			if((inv.getUUID() != null && !"".contains(inv.getUUID())) && (inv.getSerial() != null && !"".contains(inv.getSerial())) 
					&& (inv.getFolio() != null && !"".contains(inv.getFolio()))) {
				try {
					Map<String, Object> request1 = httpRequestService.httpXMLRequest(AppConstants.URL_SOAP_DFFFIN, 
																		PayloadProducer.setARRegionalFlexfield(inv.getFolio(), inv.getUUID(), inv.getSerial(), inv.getFolio(), "", ""), AppConstants.ORACLE_USER + ":" + AppConstants.ORACLE_PASS);
					String strResponse1 = (String) request1.get("response");
					int codeResponse1 = (int) request1.get("code");
					String strHttpResponse1 = (String) request1.get("httpResponse");
					
					if(codeResponse1 >= 200 && codeResponse1 < 300) {
						if(strResponse1 != null && !"".contains(strResponse1)) {
							xmlJSONObj = XML.toJSONObject(strResponse1, true);
							jelement = new JsonParser().parse(xmlJSONObj.toString());
							jobject = jelement.getAsJsonObject();
							if(jobject.get("env:Envelope").getAsJsonObject().get("env:Body").getAsJsonObject().has("ns0:updateDffEntityDetailsResponse")) {
								if(jobject.get("env:Envelope").getAsJsonObject().get("env:Body").getAsJsonObject()
										.get("ns0:updateDffEntityDetailsResponse").getAsJsonObject().has("result")) {
									JsonObject result = jobject.get("env:Envelope").getAsJsonObject().get("env:Body").getAsJsonObject()
											.get("ns0:updateDffEntityDetailsResponse").getAsJsonObject().get("result").getAsJsonObject();
									
									if(!result.isJsonNull()) {
										if(result.get("content").getAsInt() == 1) {
											inv.setStatus(AppConstants.STATUS_FINISHED);
											inv.setUpdatedDate(new Date());
											inv.setUpdatedBy("SYSTEM");
										}else {
											log.warn("ERROR AL ACTUALIZAR UUID TRANSNUM - " + inv.getFolio() + " - " + inv.getOrderType() + "***************************");
										}
									}
								}
																 
							}else {
								log.warn("ERROR AL ACTUALIZAR UUID TRANSNUM - " + inv.getFolio() + " - " + inv.getOrderType() + "***************************");
							}
						}
					}
				}catch(Exception e) {
					e.printStackTrace();
					log.error("ERROR AL EJECUTAR WS DE ERPDDF - updateUUIDToOracleERP***************************", e);
				}
			}else {
				log.warn("LA FACTURA" + inv.getFolio() + " - " + inv.getOrderType() + " NO CUENTA CON UUID, SERIE O FOLI0 - updateUUIDToOracleERP***************************");
			}
		}
		return inv;
	}

	@Override
	public SalesOrderDTO getSalesOrderInformation(String orderNumber) {
		SalesOrderDTO so = null;
		JSONObject xmlJSONObj;
		JsonObject json;
		JsonElement jelement;
		JsonObject jobject;
		
		if(orderNumber != null && !"".contains(orderNumber)) {
			try {
				Udc udc = udcService.searchBySystemAndKey("XMLFLEX", "OMFLEX");
				Map<String, Object> request1 = httpRequestService.httpXMLRequest(AppConstants.URL_SOAP_SALESORDERINFO, 
						PayloadProducer.getSalesOrderInfoBySalesNumber(orderNumber), AppConstants.ORACLE_USER + ":" + AppConstants.ORACLE_PASS);
				String strResponse1 = (String) request1.get("response");
				int codeResponse1 = (int) request1.get("code");
				String strHttpResponse1 = (String) request1.get("httpResponse");
				
				if(codeResponse1 >= 200 && codeResponse1 < 300) {
					xmlJSONObj = XML.toJSONObject(strResponse1, true);
					jelement = new JsonParser().parse(xmlJSONObj.toString());
					jobject = jelement.getAsJsonObject();
					if(jobject.get("env:Envelope").getAsJsonObject().get("env:Body").getAsJsonObject().has("ns0:GetOrderDetailsResponse")) {
						if(jobject.get("env:Envelope").getAsJsonObject().get("env:Body").getAsJsonObject()
								.get("ns0:GetOrderDetailsResponse").getAsJsonObject().has("ns1:Order")) {
							json = jobject.get("env:Envelope").getAsJsonObject().get("env:Body").getAsJsonObject().get("ns0:GetOrderDetailsResponse").getAsJsonObject();		
							so = new SalesOrderDTO();
							if(json.get("ns1:Order").isJsonArray()) {
								JsonArray jsonarray = json.get("ns1:Order").getAsJsonArray();
								JsonElement op = jsonarray.get(0).getAsJsonObject();
								so.setBusinessUnitId(NullValidator.isNull(op.getAsJsonObject().get("ns0:RequestingBusinessUnitIdentifier").toString()));
								so.setBusinessUnitName(NullValidator.isNull(op.getAsJsonObject().get("ns0:RequestingBusinessUnitName").toString()));
								so.setOrderNumber(NullValidator.isNull(op.getAsJsonObject().get("ns0:SourceTransactionNumber").toString()));
								so.setSourceTransactionNumber(NullValidator.isNull(op.getAsJsonObject().get("ns0:SourceTransactionNumber").toString()));
								so.setSourceTransactionSystem(NullValidator.isNull(op.getAsJsonObject().get("ns0:SourceTransactionSystem").toString()));
								so.setHeaderId(NullValidator.isNull(op.getAsJsonObject().get("ns0:SourceTransactionIdentifier").toString()));
								so.setTransactionalCurrencyCode(NullValidator.isNull(op.getAsJsonObject().get("ns0:TransactionalCurrencyCode").toString()));
								so.setCurrencyConversionRate(NullValidator.isNull(op.getAsJsonObject().get("ns0:CurrencyConversionRate").toString()));
								so.setRequestedFulfillmentOrganizationCode("");
								
								if(op.getAsJsonObject().has("ns0:AdditionalOrderInformationCategories")) {
									JsonElement opFlex = op.getAsJsonObject().get("ns0:AdditionalOrderInformationCategories");
									if(opFlex.getAsJsonObject().has(udc.getStrValue1() + "HeaderEffBMETODOPAGOprivateVO")) {
										so.setMetodoPago(NullValidator.isNull(opFlex.getAsJsonObject().get(udc.getStrValue1() + "HeaderEffBMETODOPAGOprivateVO").getAsJsonObject().get(udc.getStrValue2() + "metodopago").toString()));
									}
									if(opFlex.getAsJsonObject().has(udc.getStrValue1() + "HeaderEffBUSOCFDIprivateVO")) {
										so.setUsoCFDI(NullValidator.isNull(opFlex.getAsJsonObject().get(udc.getStrValue1() + "HeaderEffBUSOCFDIprivateVO").getAsJsonObject().get(udc.getStrValue2() + "usocfdi").toString()));									
									}
									if(opFlex.getAsJsonObject().has(udc.getStrValue1() + "HeaderEffBFORMAPAGOprivateVO")) {
										so.setFormaPago(NullValidator.isNull(opFlex.getAsJsonObject().get(udc.getStrValue1() + "HeaderEffBFORMAPAGOprivateVO").getAsJsonObject().get(udc.getStrValue2() + "formapago").toString()));
									}
								}
								
								if(op.getAsJsonObject().has("ns0:OrderLine")) {
									List<SalesOrderLinesDTO> soLines = new ArrayList<SalesOrderLinesDTO>();
									if(op.getAsJsonObject().get("ns0:OrderLine").isJsonArray()) {
										JsonArray jaLines = op.getAsJsonObject().get("ns0:OrderLine").getAsJsonArray();
										for(int i = 0; i < jaLines.size(); i++) {
											JsonElement oeLine = jaLines.get(i).getAsJsonObject();
											SalesOrderLinesDTO soLine = new SalesOrderLinesDTO();
											
											if(so.getRequestedFulfillmentOrganizationCode() == null && "".contains(so.getRequestedFulfillmentOrganizationCode())) {
												so.setRequestedFulfillmentOrganizationCode(NullValidator.isNull(oeLine.getAsJsonObject().get("ns0:RequestedFulfillmentOrganizationCode").toString()));
												so.setRequestedFulfillmentOrganizationId(NullValidator.isNull(oeLine.getAsJsonObject().get("ns0:RequestedFulfillmentOrganizationIdentifier").toString()));
											}
											
											soLine.setSourceTransactionLineNumber(NullValidator.isNull(oeLine.getAsJsonObject().get("ns0:SourceTransactionLineNumber").toString()));
											soLine.setOrderedQuantity(NullValidator.isNull(oeLine.getAsJsonObject().get("ns0:OrderedQuantity").getAsJsonObject().get("content").toString()));
											soLine.setOrderedUOMCode(NullValidator.isNull(oeLine.getAsJsonObject().get("ns0:OrderedUOMCode").toString()));
											soLine.setOrderedUOM(NullValidator.isNull(oeLine.getAsJsonObject().get("ns0:OrderedUOM").toString()));
											soLine.setProductNumber(NullValidator.isNull(oeLine.getAsJsonObject().get("ns0:ProductNumber").toString()));
											soLine.setProductDescription(NullValidator.isNull(oeLine.getAsJsonObject().get("ns0:ProductDescription").toString()));
											soLine.setTaxClassificationCode(NullValidator.isNull(oeLine.getAsJsonObject().get("ns0:ProductDescription").toString()));
											soLine.setStatusCode(NullValidator.isNull(oeLine.getAsJsonObject().get("ns0:StatusCode").toString()));
											
											if(oeLine.getAsJsonObject().has("ns0:LineLotSerial")) {
												List<SalesLineLotSerDTO> lotSerialsList = new ArrayList<SalesLineLotSerDTO>();
												if(oeLine.getAsJsonObject().get("ns0:LineLotSerial").isJsonArray()) {
													JsonArray jaSerLots = json.get("ns0:LineLotSerial").getAsJsonArray();
													for(int j = 0; j < jaSerLots.size(); j++) {
														JsonElement oeSerLot = jaSerLots.get(j).getAsJsonObject();
														SalesLineLotSerDTO soSerLot = new SalesLineLotSerDTO();
														soSerLot.setLotNumber(NullValidator.isNull(oeSerLot.getAsJsonObject().get("ns0:LotNumber").toString()));
														soSerLot.setSerialNumberFrom(NullValidator.isNull(oeSerLot.getAsJsonObject().get("ns0:SerialNumberFrom").toString()));
														soSerLot.setSerialNumberTo(NullValidator.isNull(oeSerLot.getAsJsonObject().get("ns0:SerialNumberTo").toString()));
														
														lotSerialsList.add(soSerLot);
													}
												}else {
													JsonElement jaSerLots = json.get("ns0:LineLotSerial").getAsJsonObject();
													SalesLineLotSerDTO soSerLot = new SalesLineLotSerDTO();
													soSerLot.setLotNumber(NullValidator.isNull(jaSerLots.getAsJsonObject().get("ns0:LotNumber").toString()));
													soSerLot.setSerialNumberFrom(NullValidator.isNull(jaSerLots.getAsJsonObject().get("ns0:SerialNumberFrom").toString()));
													soSerLot.setSerialNumberTo(NullValidator.isNull(jaSerLots.getAsJsonObject().get("ns0:SerialNumberTo").toString()));
													
													lotSerialsList.add(soSerLot);
												}
												soLine.setLotSerials(lotSerialsList);
											}
											
											soLines.add(soLine);
										}
									}else {
										JsonElement oeLine = op.getAsJsonObject().get("ns0:OrderLine").getAsJsonObject();
										SalesOrderLinesDTO soLine = new SalesOrderLinesDTO();
										
										if(so.getRequestedFulfillmentOrganizationCode() == null && "".contains(so.getRequestedFulfillmentOrganizationCode())) {
											so.setRequestedFulfillmentOrganizationCode(NullValidator.isNull(oeLine.getAsJsonObject().get("ns0:RequestedFulfillmentOrganizationCode").toString()));
											so.setRequestedFulfillmentOrganizationId(NullValidator.isNull(oeLine.getAsJsonObject().get("ns0:RequestedFulfillmentOrganizationIdentifier").toString()));
										}
										
										soLine.setSourceTransactionLineNumber(NullValidator.isNull(oeLine.getAsJsonObject().get("ns0:SourceTransactionLineNumber").toString()));
										soLine.setOrderedQuantity(NullValidator.isNull(oeLine.getAsJsonObject().get("ns0:OrderedQuantity").getAsJsonObject().get("content").toString()));
										soLine.setOrderedUOMCode(NullValidator.isNull(oeLine.getAsJsonObject().get("ns0:OrderedUOMCode").toString()));
										soLine.setOrderedUOM(NullValidator.isNull(oeLine.getAsJsonObject().get("ns0:OrderedUOM").toString()));
										soLine.setProductNumber(NullValidator.isNull(oeLine.getAsJsonObject().get("ns0:ProductNumber").toString()));
										soLine.setProductDescription(NullValidator.isNull(oeLine.getAsJsonObject().get("ns0:ProductDescription").toString()));
										soLine.setTaxClassificationCode(NullValidator.isNull(oeLine.getAsJsonObject().get("ns0:ProductDescription").toString()));
										soLine.setStatusCode(NullValidator.isNull(oeLine.getAsJsonObject().get("ns0:StatusCode").toString()));
										
										if(oeLine.getAsJsonObject().has("ns0:LineLotSerial")) {
											List<SalesLineLotSerDTO> lotSerialsList = new ArrayList<SalesLineLotSerDTO>();
											if(oeLine.getAsJsonObject().get("ns0:LineLotSerial").isJsonArray()) {
												JsonArray jaSerLots = json.get("ns0:LineLotSerial").getAsJsonArray();
												for(int j = 0; j < jaSerLots.size(); j++) {
													JsonElement oeSerLot = jaSerLots.get(j).getAsJsonObject();
													SalesLineLotSerDTO soSerLot = new SalesLineLotSerDTO();
													soSerLot.setLotNumber(NullValidator.isNull(oeSerLot.getAsJsonObject().get("ns0:LotNumber").toString()));
													soSerLot.setSerialNumberFrom(NullValidator.isNull(oeSerLot.getAsJsonObject().get("ns0:SerialNumberFrom").toString()));
													soSerLot.setSerialNumberTo(NullValidator.isNull(oeSerLot.getAsJsonObject().get("ns0:SerialNumberTo").toString()));
													
													lotSerialsList.add(soSerLot);
												}
											}else {
												JsonElement jaSerLots = json.get("ns0:LineLotSerial").getAsJsonObject();
												SalesLineLotSerDTO soSerLot = new SalesLineLotSerDTO();
												soSerLot.setLotNumber(NullValidator.isNull(jaSerLots.getAsJsonObject().get("ns0:LotNumber").toString()));
												soSerLot.setSerialNumberFrom(NullValidator.isNull(jaSerLots.getAsJsonObject().get("ns0:SerialNumberFrom").toString()));
												soSerLot.setSerialNumberTo(NullValidator.isNull(jaSerLots.getAsJsonObject().get("ns0:SerialNumberTo").toString()));
												
												lotSerialsList.add(soSerLot);
											}
											soLine.setLotSerials(lotSerialsList);
										}
										
										soLines.add(soLine);
									}
									so.setLines(soLines);
								}
							}else {
								JsonElement op = json.get("ns1:Order").getAsJsonObject();
								so.setBusinessUnitId(NullValidator.isNull(op.getAsJsonObject().get("ns0:RequestingBusinessUnitIdentifier").toString()));
								so.setBusinessUnitName(NullValidator.isNull(op.getAsJsonObject().get("ns0:RequestingBusinessUnitName").toString()));
								so.setOrderNumber(NullValidator.isNull(op.getAsJsonObject().get("ns0:SourceTransactionNumber").toString()));
								so.setSourceTransactionNumber(NullValidator.isNull(op.getAsJsonObject().get("ns0:SourceTransactionNumber").toString()));
								so.setSourceTransactionSystem(NullValidator.isNull(op.getAsJsonObject().get("ns0:SourceTransactionSystem").toString()));
								so.setHeaderId(NullValidator.isNull(op.getAsJsonObject().get("ns0:SourceTransactionIdentifier").toString()));
								so.setTransactionalCurrencyCode(NullValidator.isNull(op.getAsJsonObject().get("ns0:TransactionalCurrencyCode").toString()));
								so.setCurrencyConversionRate(NullValidator.isNull(op.getAsJsonObject().get("ns0:CurrencyConversionRate").toString()));
								so.setRequestedFulfillmentOrganizationCode("");
								
								if(op.getAsJsonObject().has("ns0:AdditionalOrderInformationCategories")) {
									JsonElement opFlex = op.getAsJsonObject().get("ns0:AdditionalOrderInformationCategories");
									if(opFlex.getAsJsonObject().has(udc.getStrValue1() + "HeaderEffBMETODOPAGOprivateVO")) {
										so.setMetodoPago(NullValidator.isNull(opFlex.getAsJsonObject().get(udc.getStrValue1() + "HeaderEffBMETODOPAGOprivateVO").getAsJsonObject().get(udc.getStrValue2() + "metodopago").toString()));
									}
									if(opFlex.getAsJsonObject().has(udc.getStrValue1() + "HeaderEffBUSOCFDIprivateVO")) {
										so.setUsoCFDI(NullValidator.isNull(opFlex.getAsJsonObject().get(udc.getStrValue1() + "HeaderEffBUSOCFDIprivateVO").getAsJsonObject().get(udc.getStrValue2() + "usocfdi").toString()));									
									}
									if(opFlex.getAsJsonObject().has(udc.getStrValue1() + "HeaderEffBFORMAPAGOprivateVO")) {
										so.setFormaPago(NullValidator.isNull(opFlex.getAsJsonObject().get(udc.getStrValue1() + "HeaderEffBFORMAPAGOprivateVO").getAsJsonObject().get(udc.getStrValue2() + "formapago").toString()));
									}
								}
								
								if(op.getAsJsonObject().has("ns0:OrderLine")) {
									List<SalesOrderLinesDTO> soLines = new ArrayList<SalesOrderLinesDTO>();
									if(op.getAsJsonObject().get("ns0:OrderLine").isJsonArray()) {
										JsonArray jaLines = op.getAsJsonObject().get("ns0:OrderLine").getAsJsonArray();
										for(int i = 0; i < jaLines.size(); i++) {
											JsonElement oeLine = jaLines.get(i).getAsJsonObject();
											SalesOrderLinesDTO soLine = new SalesOrderLinesDTO();
											
											so.setRequestedFulfillmentOrganizationCode(NullValidator.isNull(oeLine.getAsJsonObject().get("ns0:RequestedFulfillmentOrganizationCode").toString()));
											so.setRequestedFulfillmentOrganizationId(NullValidator.isNull(oeLine.getAsJsonObject().get("ns0:RequestedFulfillmentOrganizationIdentifier").toString()));	
											
											soLine.setSourceTransactionLineNumber(NullValidator.isNull(oeLine.getAsJsonObject().get("ns0:SourceTransactionLineNumber").toString()));
											soLine.setOrderedQuantity(NullValidator.isNull(oeLine.getAsJsonObject().get("ns0:OrderedQuantity").getAsJsonObject().get("content").toString()));
											soLine.setOrderedUOMCode(NullValidator.isNull(oeLine.getAsJsonObject().get("ns0:OrderedUOMCode").toString()));
											soLine.setOrderedUOM(NullValidator.isNull(oeLine.getAsJsonObject().get("ns0:OrderedUOM").toString()));
											soLine.setProductNumber(NullValidator.isNull(oeLine.getAsJsonObject().get("ns0:ProductNumber").toString()));
											soLine.setProductDescription(NullValidator.isNull(oeLine.getAsJsonObject().get("ns0:ProductDescription").toString()));
											soLine.setTaxClassificationCode(NullValidator.isNull(oeLine.getAsJsonObject().get("ns0:ProductDescription").toString()));
											soLine.setStatusCode(NullValidator.isNull(oeLine.getAsJsonObject().get("ns0:StatusCode").toString()));
											
											if(oeLine.getAsJsonObject().has("ns0:LineLotSerial")) {
												List<SalesLineLotSerDTO> lotSerialsList = new ArrayList<SalesLineLotSerDTO>();
												if(oeLine.getAsJsonObject().get("ns0:LineLotSerial").isJsonArray()) {
													JsonArray jaSerLots = oeLine.getAsJsonObject().get("ns0:LineLotSerial").getAsJsonArray();
													for(int j = 0; j < jaSerLots.size(); j++) {
														JsonElement oeSerLot = jaSerLots.get(j).getAsJsonObject();
														SalesLineLotSerDTO soSerLot = new SalesLineLotSerDTO();
														soSerLot.setLotNumber(NullValidator.isNull(oeSerLot.getAsJsonObject().get("ns0:LotNumber").toString()));
														soSerLot.setSerialNumberFrom(NullValidator.isNull(oeSerLot.getAsJsonObject().get("ns0:SerialNumberFrom").toString()));
														soSerLot.setSerialNumberTo(NullValidator.isNull(oeSerLot.getAsJsonObject().get("ns0:SerialNumberTo").toString()));
														
														lotSerialsList.add(soSerLot);
													}
												}else {
													JsonElement jaSerLots = json.get("ns0:LineLotSerial").getAsJsonObject();
													SalesLineLotSerDTO soSerLot = new SalesLineLotSerDTO();
													soSerLot.setLotNumber(NullValidator.isNull(jaSerLots.getAsJsonObject().get("ns0:LotNumber").toString()));
													soSerLot.setSerialNumberFrom(NullValidator.isNull(jaSerLots.getAsJsonObject().get("ns0:SerialNumberFrom").toString()));
													soSerLot.setSerialNumberTo(NullValidator.isNull(jaSerLots.getAsJsonObject().get("ns0:SerialNumberTo").toString()));
													
													lotSerialsList.add(soSerLot);
												}
												soLine.setLotSerials(lotSerialsList);
											}
											
											soLines.add(soLine);
										}
									}else {
										JsonElement oeLine = op.getAsJsonObject().get("ns0:OrderLine").getAsJsonObject();
										SalesOrderLinesDTO soLine = new SalesOrderLinesDTO();
										
										so.setRequestedFulfillmentOrganizationCode(NullValidator.isNull(oeLine.getAsJsonObject().get("ns0:RequestedFulfillmentOrganizationCode").toString()));
										so.setRequestedFulfillmentOrganizationId(NullValidator.isNull(oeLine.getAsJsonObject().get("ns0:RequestedFulfillmentOrganizationIdentifier").toString()));	
										
										soLine.setSourceTransactionLineNumber(NullValidator.isNull(oeLine.getAsJsonObject().get("ns0:SourceTransactionLineNumber").toString()));
										soLine.setOrderedQuantity(NullValidator.isNull(oeLine.getAsJsonObject().get("ns0:OrderedQuantity").getAsJsonObject().get("content").toString()));
										soLine.setOrderedUOMCode(NullValidator.isNull(oeLine.getAsJsonObject().get("ns0:OrderedUOMCode").toString()));
										soLine.setOrderedUOM(NullValidator.isNull(oeLine.getAsJsonObject().get("ns0:OrderedUOM").toString()));
										soLine.setProductNumber(NullValidator.isNull(oeLine.getAsJsonObject().get("ns0:ProductNumber").toString()));
										soLine.setProductDescription(NullValidator.isNull(oeLine.getAsJsonObject().get("ns0:ProductDescription").toString()));
										soLine.setTaxClassificationCode(NullValidator.isNull(oeLine.getAsJsonObject().get("ns0:ProductDescription").toString()));
										soLine.setStatusCode(NullValidator.isNull(oeLine.getAsJsonObject().get("ns0:StatusCode").toString()));
										
										if(oeLine.getAsJsonObject().has("ns0:LineLotSerial")) {
											List<SalesLineLotSerDTO> lotSerialsList = new ArrayList<SalesLineLotSerDTO>();
											if(oeLine.getAsJsonObject().get("ns0:LineLotSerial").isJsonArray()) {
												JsonArray jaSerLots = oeLine.getAsJsonObject().get("ns0:LineLotSerial").getAsJsonArray();
												for(int j = 0; j < jaSerLots.size(); j++) {
													JsonElement oeSerLot = jaSerLots.get(j).getAsJsonObject();
													SalesLineLotSerDTO soSerLot = new SalesLineLotSerDTO();
													soSerLot.setLotNumber(NullValidator.isNull(oeSerLot.getAsJsonObject().get("ns0:LotNumber").toString()));
													soSerLot.setSerialNumberFrom(NullValidator.isNull(oeSerLot.getAsJsonObject().get("ns0:SerialNumberFrom").toString()));
													soSerLot.setSerialNumberTo(NullValidator.isNull(oeSerLot.getAsJsonObject().get("ns0:SerialNumberTo").toString()));
													
													lotSerialsList.add(soSerLot);
												}
											}else {
												JsonElement jaSerLots = json.get("ns0:LineLotSerial").getAsJsonObject();
												SalesLineLotSerDTO soSerLot = new SalesLineLotSerDTO();
												soSerLot.setLotNumber(NullValidator.isNull(jaSerLots.getAsJsonObject().get("ns0:LotNumber").toString()));
												soSerLot.setSerialNumberFrom(NullValidator.isNull(jaSerLots.getAsJsonObject().get("ns0:SerialNumberFrom").toString()));
												soSerLot.setSerialNumberTo(NullValidator.isNull(jaSerLots.getAsJsonObject().get("ns0:SerialNumberTo").toString()));
												
												lotSerialsList.add(soSerLot);
											}
											soLine.setLotSerials(lotSerialsList);
										}
										
										soLines.add(soLine);
									}
									so.setLines(soLines);
								}
							}
						}
					}else {
						String msgError = jobject.get("env:Envelope").getAsJsonObject().get("env:Body").getAsJsonObject().get("env:Fault").getAsJsonObject().get("faultstring").getAsString();
						log.warn("ERROR AL EJECUTAR WS DE ERPDDF - getSalesOrderInformation - " + orderNumber + "msg: " + msgError);
					}
				}
			}catch(Exception e){
				e.printStackTrace();
				log.warn("ERROR AL EJECUTAR WS DE ERPDDF - getSalesOrderInformation - " + orderNumber, e);
			}
		}
		
		return so;
	}

	
	
}
