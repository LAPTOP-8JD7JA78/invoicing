package com.smartech.invoicing.integration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Base64;
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
import com.smartech.invoicing.dto.CatAttachmentDTO;
import com.smartech.invoicing.dto.CategoryDTO;
import com.smartech.invoicing.dto.ItemGtinDTO;
import com.smartech.invoicing.dto.ItemsDTO;
import com.smartech.invoicing.dto.SalesLineLotSerDTO;
import com.smartech.invoicing.dto.SalesOrderDTO;
import com.smartech.invoicing.dto.SalesOrderLinesDTO;
import com.smartech.invoicing.integration.service.HTTPRequestService;
import com.smartech.invoicing.integration.util.AppConstants;
import com.smartech.invoicing.integration.util.PayloadProducer;
import com.smartech.invoicing.model.Invoice;
import com.smartech.invoicing.model.Payments;
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
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
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
												c.setCategoryCode(NullValidator.isNull(op.getAsJsonObject().get("ns1:CategoryCode").toString()));
												if(!c.getCatalogCode().isEmpty()) {
													catList.add(c);
												}
											}
										}else {
											CategoryDTO c = new CategoryDTO();
											c.setCatalogCode(NullValidator.isNull(result.get("ns1:ItemCategory").getAsJsonObject().get("ns1:ItemCatalog").toString()));
											c.setCategoryName(NullValidator.isNull(result.get("ns1:ItemCategory").getAsJsonObject().get("ns1:CategoryName").toString()));
											c.setCategoryCode(NullValidator.isNull(result.get("ns1:ItemCategory").getAsJsonObject().get("ns1:CategoryCode").toString()));
											if(!c.getCatalogCode().isEmpty()) {
												catList.add(c);
											}
										}
										item.setItemCategory(catList);
									}
									Udc udcFlex = udcService.searchBySystemAndKey("XMLFLEX", "PRODFLEX");
									if(result.has("ns1:ItemDFF") && udcFlex != null){
										item.setItemDFFClavProdServ(NullValidator.isNull(result.get("ns1:ItemDFF").getAsJsonObject().get(udcFlex.getStrValue1() + "codigoSat").toString()));
										//Comercio Exterior
										item.setItemDFFFraccionArancelaria(NullValidator.isNull(result.get("ns1:ItemDFF").getAsJsonObject().get(udcFlex.getStrValue1() + "fraccionArancelaria").toString()));
										item.setItemDFFMarca(NullValidator.isNull(result.get("ns1:ItemDFF").getAsJsonObject().get(udcFlex.getStrValue1() + "marca").toString()));
										item.setItemDFFModelo(NullValidator.isNull(result.get("ns1:ItemDFF").getAsJsonObject().get(udcFlex.getStrValue1() + "modelo").toString()));
										//Importación
										String isImported = NullValidator.isNull(result.get("ns1:ItemDFF").getAsJsonObject().get(udcFlex.getStrValue1() + "importacion").toString());
										if(isImported != null && !"".contains(isImported) && "Si".contains(isImported)) {
											item.setItemDFFIsImported(true);
										}
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
	public Invoice updateUUIDToOracleERPInvoice(Invoice inv) {
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
											soLine.setFreightTermsCode(NullValidator.isNull(oeLine.getAsJsonObject().get("ns0:FreightTermsCode").toString()));
											
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
													JsonElement jaSerLots = oeLine.getAsJsonObject().get("ns0:LineLotSerial").getAsJsonObject();
													SalesLineLotSerDTO soSerLot = new SalesLineLotSerDTO();
													soSerLot.setLotNumber(NullValidator.isNull(jaSerLots.getAsJsonObject().get("ns0:LotNumber").toString()));
													soSerLot.setSerialNumberFrom(NullValidator.isNull(jaSerLots.getAsJsonObject().get("ns0:SerialNumberFrom").toString()));
													soSerLot.setSerialNumberTo(NullValidator.isNull(jaSerLots.getAsJsonObject().get("ns0:SerialNumberTo").toString()));
													
													lotSerialsList.add(soSerLot);
												}
												soLine.setLotSerials(lotSerialsList);
											}
											
											if(oeLine.getAsJsonObject().has("ns0:LineAttachment")) {
												JsonObject jaLineAtt = oeLine.getAsJsonObject().get("ns0:LineAttachment").getAsJsonObject();
												if(jaLineAtt.isJsonArray()) {
													JsonArray attList = jaLineAtt.getAsJsonArray();
													for(int a = 0; a < attList.size(); a++) {
														String att = attList.get(a).getAsJsonObject().get("ns0:Data").getAsString();
														if(att != null && !"".contains(att)) {
															try {
																String attDecode = new String(Base64.getMimeDecoder().decode(att), "UTF-8");
																soLine.setAdditionalInformation(attDecode);
															}catch(Exception e) {
																e.printStackTrace();
																log.warn("ERROR AL OBTENER EL ATTACHMENT DE LA LINEA " + soLine.getSourceTransactionLineNumber() + " DE LA ORDEN " + so.getOrderNumber(),e);
															}
														}else {
															log.warn("ERROR AL OBTENER EL ATTACHMENT DE LA LINEA " + soLine.getSourceTransactionLineNumber() + " DE LA ORDEN " + so.getOrderNumber());
														}
													}
												}else {
													String att = jaLineAtt.getAsJsonObject().get("ns0:Data").getAsString();
													if(att != null && !"".contains(att)) {
														try {
															String attDecode = new String(Base64.getMimeDecoder().decode(att), "UTF-8");
															soLine.setAdditionalInformation(attDecode);
														}catch(Exception e) {
															e.printStackTrace();
															log.warn("ERROR AL OBTENER EL ATTACHMENT DE LA LINEA " + soLine.getSourceTransactionLineNumber() + " DE LA ORDEN " + so.getOrderNumber(),e);
														}
													}else {
														log.warn("ERROR AL OBTENER EL ATTACHMENT DE LA LINEA " + soLine.getSourceTransactionLineNumber() + " DE LA ORDEN " + so.getOrderNumber());
													}
												}
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
										soLine.setFreightTermsCode(NullValidator.isNull(oeLine.getAsJsonObject().get("ns0:FreightTermsCode").toString()));
										
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
												JsonElement jaSerLots = oeLine.getAsJsonObject().get("ns0:LineLotSerial").getAsJsonObject();
												SalesLineLotSerDTO soSerLot = new SalesLineLotSerDTO();
												soSerLot.setLotNumber(NullValidator.isNull(jaSerLots.getAsJsonObject().get("ns0:LotNumber").toString()));
												soSerLot.setSerialNumberFrom(NullValidator.isNull(jaSerLots.getAsJsonObject().get("ns0:SerialNumberFrom").toString()));
												soSerLot.setSerialNumberTo(NullValidator.isNull(jaSerLots.getAsJsonObject().get("ns0:SerialNumberTo").toString()));
												
												lotSerialsList.add(soSerLot);
											}
											soLine.setLotSerials(lotSerialsList);
										}
										
										if(oeLine.getAsJsonObject().has("ns0:LineAttachment")) {
											JsonObject jaLineAtt = oeLine.getAsJsonObject().get("ns0:LineAttachment").getAsJsonObject();
											if(jaLineAtt.isJsonArray()) {
												JsonArray attList = jaLineAtt.getAsJsonArray();
												for(int i = 0; i < attList.size(); i++) {
													String att = attList.get(i).getAsJsonObject().get("ns0:Data").getAsString();
													if(att != null && !"".contains(att)) {
														try {
															String attDecode = new String(Base64.getMimeDecoder().decode(att), "UTF-8");
															soLine.setAdditionalInformation(attDecode);
														}catch(Exception e) {
															e.printStackTrace();
															log.warn("ERROR AL OBTENER EL ATTACHMENT DE LA LINEA " + soLine.getSourceTransactionLineNumber() + " DE LA ORDEN " + so.getOrderNumber(),e);
														}
													}else {
														log.warn("ERROR AL OBTENER EL ATTACHMENT DE LA LINEA " + soLine.getSourceTransactionLineNumber() + " DE LA ORDEN " + so.getOrderNumber());
													}
												}
											}else {
												String att = jaLineAtt.getAsJsonObject().get("ns0:Data").getAsString();
												if(att != null && !"".contains(att)) {
													try {
														String attDecode = new String(Base64.getMimeDecoder().decode(att), "UTF-8");
														soLine.setAdditionalInformation(attDecode);
													}catch(Exception e) {
														e.printStackTrace();
														log.warn("ERROR AL OBTENER EL ATTACHMENT DE LA LINEA " + soLine.getSourceTransactionLineNumber() + " DE LA ORDEN " + so.getOrderNumber(),e);
													}
												}else {
													log.warn("ERROR AL OBTENER EL ATTACHMENT DE LA LINEA " + soLine.getSourceTransactionLineNumber() + " DE LA ORDEN " + so.getOrderNumber());
												}
											}
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
											soLine.setFreightTermsCode(NullValidator.isNull(oeLine.getAsJsonObject().get("ns0:FreightTermsCode").toString()));
											
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
													JsonElement jaSerLots = oeLine.getAsJsonObject().get("ns0:LineLotSerial").getAsJsonObject();
													SalesLineLotSerDTO soSerLot = new SalesLineLotSerDTO();
													soSerLot.setLotNumber(NullValidator.isNull(jaSerLots.getAsJsonObject().get("ns0:LotNumber").toString()));
													soSerLot.setSerialNumberFrom(NullValidator.isNull(jaSerLots.getAsJsonObject().get("ns0:SerialNumberFrom").toString()));
													soSerLot.setSerialNumberTo(NullValidator.isNull(jaSerLots.getAsJsonObject().get("ns0:SerialNumberTo").toString()));
													
													lotSerialsList.add(soSerLot);
												}
												soLine.setLotSerials(lotSerialsList);
											}
											
											if(oeLine.getAsJsonObject().has("ns0:LineAttachment")) {
												JsonObject jaLineAtt = oeLine.getAsJsonObject().get("ns0:LineAttachment").getAsJsonObject();
												if(jaLineAtt.isJsonArray()) {
													JsonArray attList = jaLineAtt.getAsJsonArray();
													for(int a = 0; a < attList.size(); a++) {
														String att = attList.get(a).getAsJsonObject().get("ns0:Data").getAsString();
														if(att != null && !"".contains(att)) {
															try {
																String attDecode = new String(Base64.getMimeDecoder().decode(att), "UTF-8");
																soLine.setAdditionalInformation(attDecode);
															}catch(Exception e) {
																e.printStackTrace();
																log.warn("ERROR AL OBTENER EL ATTACHMENT DE LA LINEA " + soLine.getSourceTransactionLineNumber() + " DE LA ORDEN " + so.getOrderNumber(),e);
															}
														}else {
															log.warn("ERROR AL OBTENER EL ATTACHMENT DE LA LINEA " + soLine.getSourceTransactionLineNumber() + " DE LA ORDEN " + so.getOrderNumber());
														}
													}
												}else {
													String att = jaLineAtt.getAsJsonObject().get("ns0:Data").getAsString();
													if(att != null && !"".contains(att)) {
														try {
															String attDecode = new String(Base64.getMimeDecoder().decode(att), "UTF-8");
															soLine.setAdditionalInformation(attDecode);
														}catch(Exception e) {
															e.printStackTrace();
															log.warn("ERROR AL OBTENER EL ATTACHMENT DE LA LINEA " + soLine.getSourceTransactionLineNumber() + " DE LA ORDEN " + so.getOrderNumber(),e);
														}
													}else {
														log.warn("ERROR AL OBTENER EL ATTACHMENT DE LA LINEA " + soLine.getSourceTransactionLineNumber() + " DE LA ORDEN " + so.getOrderNumber());
													}
												}
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
										soLine.setFreightTermsCode(NullValidator.isNull(oeLine.getAsJsonObject().get("ns0:FreightTermsCode").toString()));
										
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
												JsonElement jaSerLots = oeLine.getAsJsonObject().get("ns0:LineLotSerial").getAsJsonObject();
												SalesLineLotSerDTO soSerLot = new SalesLineLotSerDTO();
												soSerLot.setLotNumber(NullValidator.isNull(jaSerLots.getAsJsonObject().get("ns0:LotNumber").toString()));
												soSerLot.setSerialNumberFrom(NullValidator.isNull(jaSerLots.getAsJsonObject().get("ns0:SerialNumberFrom").toString()));
												soSerLot.setSerialNumberTo(NullValidator.isNull(jaSerLots.getAsJsonObject().get("ns0:SerialNumberTo").toString()));
												
												lotSerialsList.add(soSerLot);
											}
											soLine.setLotSerials(lotSerialsList);
										}
										
										if(oeLine.getAsJsonObject().has("ns0:LineAttachment")) {
											JsonObject jaLineAtt = oeLine.getAsJsonObject().get("ns0:LineAttachment").getAsJsonObject();
											if(jaLineAtt.isJsonArray()) {
												JsonArray attList = jaLineAtt.getAsJsonArray();
												for(int i = 0; i < attList.size(); i++) {
													String att = attList.get(i).getAsJsonObject().get("ns0:Data").getAsString();
													if(att != null && !"".contains(att)) {
														try {
															String attDecode = new String(Base64.getMimeDecoder().decode(att), "UTF-8");
															soLine.setAdditionalInformation(attDecode);
														}catch(Exception e) {
															e.printStackTrace();
															log.warn("ERROR AL OBTENER EL ATTACHMENT DE LA LINEA " + soLine.getSourceTransactionLineNumber() + " DE LA ORDEN " + so.getOrderNumber(),e);
														}
													}else {
														log.warn("ERROR AL OBTENER EL ATTACHMENT DE LA LINEA " + soLine.getSourceTransactionLineNumber() + " DE LA ORDEN " + so.getOrderNumber());
													}
												}
											}else {
												String att = jaLineAtt.getAsJsonObject().get("ns0:Data").getAsString();
												if(att != null && !"".contains(att)) {
													try {
														String attDecode = new String(Base64.getMimeDecoder().decode(att), "UTF-8");
														soLine.setAdditionalInformation(attDecode);
													}catch(Exception e) {
														e.printStackTrace();
														log.warn("ERROR AL OBTENER EL ATTACHMENT DE LA LINEA " + soLine.getSourceTransactionLineNumber() + " DE LA ORDEN " + so.getOrderNumber(),e);
													}
												}else {
													log.warn("ERROR AL OBTENER EL ATTACHMENT DE LA LINEA " + soLine.getSourceTransactionLineNumber() + " DE LA ORDEN " + so.getOrderNumber());
												}
											}
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

	@Override
	public Payments updateUUIDToOracleERPPayments(Payments pay) {
		JSONObject xmlJSONObj;
		JSONObject json;
		JsonElement jelement;
		JsonObject jobject;
		boolean status1 = false;
		boolean status2 = false;
		if(pay != null) {
			if((pay.getUUID() != null && !"".contains(pay.getUUID())) && (pay.getSerial() != null && !"".contains(pay.getSerial())) 
					&& (pay.getFolio() != null && !"".contains(pay.getFolio()))) {
				try {
					Map<String, Object> request1 = httpRequestService.httpXMLRequest(AppConstants.URL_SOAP_DFFFIN, 
																		PayloadProducer.setARReceiptsRegionalFlexfield(pay.getReceiptNumber(), pay.getReceiptId(), pay.getUUID()), AppConstants.ORACLE_USER + ":" + AppConstants.ORACLE_PASS);
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
											status1 = true;
										}else {
											log.warn("ERROR AL ACTUALIZAR UUID RECEIPT - " + pay.getFolio() + " - " + pay.getReceiptNumber() + "***************************");
										}
									}
								}
																 
							}else {
								log.warn("ERROR AL ACTUALIZAR UUID RECEIPT - " + pay.getFolio() + " - " + pay.getReceiptNumber() + "***************************");
							}
						}
					}
					
					Map<String, Object> request2 = httpRequestService.httpXMLRequest(AppConstants.URL_SOAP_DFFFIN, 
							PayloadProducer.setARReceiptsSerialFolioFlexfield(pay.getReceiptNumber(), pay.getReceiptId(), pay.getSerial(), pay.getFolio()), AppConstants.ORACLE_USER + ":" + AppConstants.ORACLE_PASS);
					
					String strResponse2 = (String) request2.get("response");
					int codeResponse2 = (int) request2.get("code");
					String strHttpResponse2 = (String) request2.get("httpResponse");
					
					if(codeResponse2 >= 200 && codeResponse2< 300) {
						if(strResponse2 != null && !"".contains(strResponse2)) {
							xmlJSONObj = XML.toJSONObject(strResponse2, true);
							jelement = new JsonParser().parse(xmlJSONObj.toString());
							jobject = jelement.getAsJsonObject();
							if(jobject.get("env:Envelope").getAsJsonObject().get("env:Body").getAsJsonObject().has("ns0:updateDffEntityDetailsResponse")) {
								if(jobject.get("env:Envelope").getAsJsonObject().get("env:Body").getAsJsonObject().get("ns0:updateDffEntityDetailsResponse").getAsJsonObject().has("result")) {
									JsonObject result = jobject.get("env:Envelope").getAsJsonObject().get("env:Body").getAsJsonObject()
											.get("ns0:updateDffEntityDetailsResponse").getAsJsonObject().get("result").getAsJsonObject();
								
									if(!result.isJsonNull()) {
										if(result.get("content").getAsInt() == 1) {
											status2 = true;
										}else {
											log.warn("ERROR AL ACTUALIZAR SERIAL - FOLIO RECEIPT - " + pay.getFolio() + " - " + pay.getReceiptNumber() + "***************************");
										}
									}
								}			 
							}else {
								log.warn("ERROR AL ACTUALIZAR SERIAL - FOLIO RECEIPT RECEIPT - " + pay.getFolio() + " - " + pay.getReceiptNumber() + "***************************");
							}
						}
					}
					
				}catch(Exception e) {
					e.printStackTrace();
					log.error("ERROR AL EJECUTAR WS DE ERPDDF - updateUUIDToOracleERP***************************", e);
				}
			}else {
				log.warn("EL RECIBO" + pay.getFolio() + " - " + pay.getReceiptNumber() + " NO CUENTA CON UUID, SERIE O FOLI0 - updateUUIDToOracleERPPayments***************************");
			}
		}
		
		if(status1 && status2) {
			pay.setPaymentStatus(AppConstants.STATUS_FINISHED);
		}
		pay.setUpdateDate(sdf.format(new Date()));
		
		return pay;
	}

	@Override
	public CategoryDTO getCategoryDataFrom(String categoryCode) {
		CategoryDTO cat = null;
		JSONObject xmlJSONObj;
		JSONObject json;
		JsonElement jelement;
		JsonObject jobject;
		if(categoryCode != null && !"".contains(categoryCode)) {
			try {
				Map<String, Object> request1 = httpRequestService.httpXMLRequest(AppConstants.URL_SOAP_ITEMCATALOG, 
																	PayloadProducer.getRetailerItemCatalogInfo(categoryCode), AppConstants.ORACLE_USER + ":" + AppConstants.ORACLE_PASS);
				
				String strResponse1 = (String) request1.get("response");
				int codeResponse1 = (int) request1.get("code");
				String strHttpResponse1 = (String) request1.get("httpResponse");
				
				if(codeResponse1 >= 200 && codeResponse1 < 300) {
					if(strResponse1 != null && !"".contains(strResponse1)) {
						xmlJSONObj = XML.toJSONObject(strResponse1, true);
						jelement = new JsonParser().parse(xmlJSONObj.toString());
						jobject = jelement.getAsJsonObject();
						if(jobject.get("env:Envelope").getAsJsonObject().get("env:Body").getAsJsonObject().has("ns0:findCategoryResponse")) {
							if(jobject.get("env:Envelope").getAsJsonObject().get("env:Body").getAsJsonObject()
									.get("ns0:findCategoryResponse").getAsJsonObject().has("ns2:result")) {
								JsonObject result = jobject.get("env:Envelope").getAsJsonObject().get("env:Body").getAsJsonObject()
										.get("ns0:findCategoryResponse").getAsJsonObject().get("ns2:result").getAsJsonObject();
								
								if(!result.isJsonNull()) {
									cat = new CategoryDTO();
									cat.setCatalogCode(NullValidator.isNull(result.getAsJsonObject().get("ns1:CatalogCode").getAsString()));
									cat.setCategoryName(NullValidator.isNull(result.getAsJsonObject().get("ns1:CategoryName").getAsString()));
									cat.setCategoryCode(NullValidator.isNull(result.getAsJsonObject().get("ns1:CategoryCode").getAsString()));
									
									if(result.has("ns1:Attachment")) {
										List<CatAttachmentDTO> catAttList = new ArrayList<CatAttachmentDTO>();
										
										if(result.getAsJsonObject().get("ns1:Attachment").isJsonArray()) {
											JsonArray jattlist = result.getAsJsonObject().get("ns1:Attachment").getAsJsonArray();
											for(int i = 0; i < jattlist.size(); i++) {
												CatAttachmentDTO catAtt = new CatAttachmentDTO();
												catAtt.setFileName(NullValidator.isNull(jattlist.get(i).getAsJsonObject().get("ns1:FileName").getAsString()));
												catAtt.setTitle(NullValidator.isNull(jattlist.get(i).getAsJsonObject().get("ns1:Title").getAsString()));
												catAttList.add(catAtt);
											}
										}else {
											JsonObject jattach = result.getAsJsonObject().get("ns1:Attachment").getAsJsonObject();
											CatAttachmentDTO catAtt = new CatAttachmentDTO();
											catAtt.setFileName(NullValidator.isNull(jattach.getAsJsonObject().get("ns1:FileName").getAsString()));
											catAtt.setTitle(NullValidator.isNull(jattach.getAsJsonObject().get("ns1:Title").getAsString()));
											catAttList.add(catAtt);
										}
										cat.setAttachments(catAttList);
									}else {
										log.warn("ERROR AL OBTENER LOS ADJUNTOS DE LA CATEGORIA - " + categoryCode + "***************************");
									}
									
								}
							}
															 
						}else {
							log.warn("ERROR AL OBTENER DATOS DE LA CATEGORIA - " + categoryCode + "***************************");
						}
					}
				}
			}catch(Exception e) {
				e.printStackTrace();
				log.error("ERROR AL OBTENER DATOS DE LA CATEGORIA - " + categoryCode + "***************************", e);
			}
		}
		return cat;
	}

	@Override
	public ItemGtinDTO getItemGTINData(String itemNumber, String orgCode, String partyNumber) {
		ItemGtinDTO gtin = null;
		JSONObject xmlJSONObj;
		JSONObject json;
		JsonElement jelement;
		JsonObject jobject;
		if((itemNumber != null && !"".contains(itemNumber)) && (orgCode != null && !"".contains(orgCode)) && (partyNumber != null && !"".contains(partyNumber))) {
			try {
				Map<String, Object> request1 = httpRequestService.httpXMLRequest(AppConstants.URL_SOAP_ITEMRELATIONSHIP, 
						PayloadProducer.getGTINFromItemRelationships(itemNumber, orgCode, partyNumber), AppConstants.ORACLE_USER + ":" + AppConstants.ORACLE_PASS);
	
				String strResponse1 = (String) request1.get("response");
				int codeResponse1 = (int) request1.get("code");
				String strHttpResponse1 = (String) request1.get("httpResponse");
				
				if(codeResponse1 >= 200 && codeResponse1 < 300) {
					if(strResponse1 != null && !"".contains(strResponse1)) {
						xmlJSONObj = XML.toJSONObject(strResponse1, true);
						jelement = new JsonParser().parse(xmlJSONObj.toString());
						jobject = jelement.getAsJsonObject();
						if(jobject.get("env:Envelope").getAsJsonObject().get("env:Body").getAsJsonObject().has("ns0:findGTINCrossReferenceResponse")) {
							if(jobject.get("env:Envelope").getAsJsonObject().get("env:Body").getAsJsonObject()
									.get("ns0:findGTINCrossReferenceResponse").getAsJsonObject().get("ns2:result").getAsJsonObject().has("ns0:Value")) {
								JsonObject result = jobject.get("env:Envelope").getAsJsonObject().get("env:Body").getAsJsonObject()
										.get("ns0:findGTINCrossReferenceResponse").getAsJsonObject().get("ns2:result").getAsJsonObject().get("ns0:Value").getAsJsonObject();
								
								if(!result.isJsonNull()) {
									gtin = new ItemGtinDTO();
									gtin.setGtin(NullValidator.isNull(result.get("ns1:GTIN").getAsString()));
									gtin.setItemNumber(NullValidator.isNull(result.get("ns1:ItemNumber").getAsString()));
									gtin.setOrganizationCode(NullValidator.isNull(result.get("ns1:OrganizationCode").getAsString()));
									gtin.setTradingPartnerName(NullValidator.isNull(result.get("ns1:TradingPartnerName").getAsString()));
									gtin.setTradingPartnerNumber(NullValidator.isNull(result.get("ns1:TradingPartnerNumber").getAsString()));
									gtin.setUomCodeValue(NullValidator.isNull(result.get("ns1:UOMCodeValue").getAsString()));
								}
							}
						}else {
							log.warn("ERROR AL OBTENER DATOS DEL GTIN (RESPONSE ERROR) - " + itemNumber + " - " + partyNumber + "***************************");
						}
					}
				}else {
					log.warn("ERROR AL OBTENER DATOS DEL GTIN - " + itemNumber + " - " + partyNumber + "***************************");
				}
			}catch(Exception e) {
				e.printStackTrace();
				log.error("ERROR AL OBTENER DATOS DEL GTIN - " + itemNumber + " - " + partyNumber + "***************************", e);
			}
		}
		return gtin;
	}

	
	
}
