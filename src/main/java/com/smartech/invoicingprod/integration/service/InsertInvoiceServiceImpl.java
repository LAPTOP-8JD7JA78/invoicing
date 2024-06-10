package com.smartech.invoicingprod.integration.service;

import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.smartech.invoicingprod.dto.CustomerAccountDTO;
import com.smartech.invoicingprod.dto.CustomerInformation2DTO;
import com.smartech.invoicingprod.dto.InsertInvoiceCloudBodyDTO;
import com.smartech.invoicingprod.dto.InvoiceInsertHeaderDTO;
import com.smartech.invoicingprod.dto.ReceivablesInvoiceDistributionDTO;
import com.smartech.invoicingprod.dto.ReceivablesInvoiceGdfDTO;
import com.smartech.invoicingprod.dto.ReceivablesInvoiceLineDTO;
import com.smartech.invoicingprod.dto.ReceivablesInvoiceLineTaxLineDTO;
import com.smartech.invoicingprod.dto.ResponseInvoiceCloudBodyDTO;
import com.smartech.invoicingprod.dto.responseInsertInvoiceDTO;
import com.smartech.invoicingprod.integration.RESTService;
import com.smartech.invoicingprod.integration.SOAPService;
import com.smartech.invoicingprod.integration.json.receivablesInvoices.ReceivablesInvoices;
import com.smartech.invoicingprod.integration.util.AppConstants;
import com.smartech.invoicingprod.model.Company;
import com.smartech.invoicingprod.model.Payments;
import com.smartech.invoicingprod.service.CompanyService;
import com.smartech.invoicingprod.service.UdcService;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.zip.GZIPInputStream;

@Service("insertInvoiceService")
public class InsertInvoiceServiceImpl implements InsertInvoiceService {

	@Autowired
	UdcService udcService;

	@Autowired
	CompanyService companyService;
	
	@Autowired
	SOAPService soapService;
	
	@Autowired
	RESTService restService;

	@SuppressWarnings("unused")
	@Override
	public ResponseInvoiceCloudBodyDTO createInvoice(InsertInvoiceCloudBodyDTO data) {
		ResponseInvoiceCloudBodyDTO response = new ResponseInvoiceCloudBodyDTO();
		try {
			//Conversión del archivo base 64 a XML
			String valorArchivo = descomprimirArchivo(data.getFileIn64Base()); 
			//Convertir el xml a un formato legible
			JsonElement conversionStringJson = convertionStringToJson(valorArchivo);
			//Generación de cuerpo a insertar
			InvoiceInsertHeaderDTO invoiceAR = setInvoiceAr(conversionStringJson);
			//Insertar en Cloud
			Gson gson = new Gson();
	        String json = gson.toJson(invoiceAR);
			System.out.print(json);
			String respuestaInsercion = restService.insertInvoiceToCloud(invoiceAR);
//			String respuestaInsercion= "<201 Created,{Content-Type=[application/vnd.oracle.adf.resourceitem+json], Referrer-Policy=[origin], X-Content-Type-Options=[nosniff], Cache-Control=[no-cache, no-store, must-revalidate], Location=[https://fa-epog-test-saasfaprod1.fa.ocs.oraclecloud.com:443/fscmRestApi/resources/11.13.18.05/receivablesInvoices/300000985821166], X-ORACLE-DMS-ECID=[0065CuDkZoE23VKqySMaMG00021v00002F], Link=[<https://fa-epog-test-saasfaprod1.fa.ocs.oraclecloud.com:443/fscmRestApi/resources/11.13.18.05/receivablesInvoices/300000985821166>;rel=\"self\";kind=\"item\";name=\"receivablesInvoices\"], X-ORACLE-DMS-RID=[0:5], ETag=[\"ACED0005737200136A6176612E7574696C2E41727261794C6973747881D21D99C7619D03000149000473697A65787000000001770400000001737200116A6176612E6C616E672E496E746567657212E2A0A4F781873802000149000576616C7565787200106A6176612E6C616E672E4E756D62657286AC951D0B94E08B02000078700000000278\"], REST-Framework-Version=[1], X-XSS-Protection=[1; mode=block], Pragma=[no-cache], Content-Language=[en], X-Request-ID=[7a27df60da013c23a29ff3655cb3e1dc], Strict-Transport-Security=[max-age=31536000; includeSubDomains], Date=[Fri, 29 Mar 2024 15:47:15 GMT], Connection=[close], AKGRN=[0.bb2d2d17.1711727234.8c3cdb6]}>";
			if(respuestaInsercion != null) {
				//Buscar datos de transacción
				String[] iterar = respuestaInsercion.split(",");
				String valor = "";
				for(String s: iterar) {
					if(s.contains(("Location"))) {
						String[] iterar1 = s.split("/");
						int numTotal = iterar1.length;
						valor = iterar1[numTotal-1].substring(0,15);
						break;
					}
				}
				ReceivablesInvoices resp = restService.getInvoiceData2(valor);
				
				response.setComments("Complete");
				response.setInvoiceNumber(resp.getItems().get(0).getTransactionNumber());
				response.setStatus(true);
			}else {
				response.setComments("Error al insertar la Factura");
				response.setInvoiceNumber(null);
				response.setStatus(false);
			}
			System.out.print("Validacion");
			//Generación de respuesta
			return response;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String descomprimirArchivo(String codificado) {
		String valor = null; 
		try {
			// Decodificar el archivo Base64
            byte[] archivoDecodificado = Base64.getDecoder().decode(codificado);

            // Crear un stream de entrada para leer los bytes decodificados
            ByteArrayInputStream inputStream = new ByteArrayInputStream(archivoDecodificado);

            // Crear un lector de entrada para leer el contenido
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            // Leer el contenido del archivo línea por línea
            String linea;
            while ((linea = reader.readLine()) != null) {
                System.out.println(linea);
                valor = linea;
            }

            // Cerrar el lector y el stream de entrada
            reader.close();
            inputStream.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return valor;
	}
	
	public JsonElement convertionStringToJson(String valor) {
		
		try {
			JSONObject xmlJSONObj = new JSONObject();
			xmlJSONObj = XML.toJSONObject(valor, true);
			JsonElement jelement = new JsonParser().parse(xmlJSONObj.toString());
			return jelement;
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@SuppressWarnings("unused")
	public InvoiceInsertHeaderDTO setInvoiceAr(JsonElement cuerpo) {
		InvoiceInsertHeaderDTO invAr = new InvoiceInsertHeaderDTO();
		
		List<ReceivablesInvoiceLineDTO> lineas = new ArrayList<ReceivablesInvoiceLineDTO>();
		List<ReceivablesInvoiceGdfDTO> flexHeader = new ArrayList<ReceivablesInvoiceGdfDTO>();
		List<ReceivablesInvoiceDistributionDTO> distribuitions = new ArrayList<ReceivablesInvoiceDistributionDTO>();
		try {
			//Descomposicion en elementos
			JsonObject jobjectComplete = cuerpo.getAsJsonObject();
			JsonElement soapEnvelopeComplete = jobjectComplete.get("cfdi:Comprobante");
			
			JsonElement soapEnvelopeEmis = soapEnvelopeComplete.getAsJsonObject().get("cfdi:Emisor");
			JsonElement soapEnvelopeComplemento = soapEnvelopeComplete.getAsJsonObject().get("cfdi:Complemento").getAsJsonObject().get("tfd:TimbreFiscalDigital");
			JsonElement soapEnvelopeReceptor = soapEnvelopeComplete.getAsJsonObject().get("cfdi:Receptor");
			
			//Validacion de datos
			String insertDate =  String.valueOf(soapEnvelopeComplete.getAsJsonObject().get("Fecha"));
			insertDate = insertDate.replaceAll("\"", "");
			insertDate = insertDate.substring(0,10);
			
			String customerName = String.valueOf(soapEnvelopeReceptor.getAsJsonObject().get("Nombre")).replaceAll("\"", "");
			String customerTaxId = String.valueOf(soapEnvelopeReceptor.getAsJsonObject().get("Rfc")).replaceAll("\"", "");
			String companyTaxId = String.valueOf(soapEnvelopeEmis.getAsJsonObject().get("Rfc")).replaceAll("\"", "");
			Company com = companyService.getCompanyByTaxId(companyTaxId);
			CustomerInformation2DTO infoCust = soapService.getDataCustomer(customerName);		
			CustomerAccountDTO custAccount = soapService.getDataCustomerAccount(infoCust.getCustInformation().getPartyId());
			String invoiceTotal = String.valueOf(soapEnvelopeComplete.getAsJsonObject().get("Total")).replaceAll("\"", "");
			
			String conversionDate = null;
			String monedaInvoice = String.valueOf(soapEnvelopeComplete.getAsJsonObject().get("Moneda")).replaceAll("\"", "");
			double eRate = 0.0;
			boolean currencyMXN = true;
			if(!monedaInvoice.equals(AppConstants.DEFAUL_CURRENCY)) {
				conversionDate = insertDate;
				currencyMXN = false;
				eRate = Double.parseDouble(String.valueOf(soapEnvelopeComplete.getAsJsonObject().get("TipoCambio")).replaceAll("\"", ""));
			}
			
			//Llenado del cabecero
			invAr.setDueDate(insertDate);
			invAr.setInvoiceCurrencyCode(monedaInvoice);
			invAr.setConversionDate(conversionDate);
			invAr.setTransactionDate(insertDate);
			invAr.setTransactionType("Arrendamiento y serv");
			invAr.setTransactionSource("Manual IMEMSA");
			invAr.setBillToCustomerNumber(custAccount.getAccountNumber());//----------------------------------------------------------------------------------------------
			invAr.setPaymentTerms("CONTADO");
			invAr.setLegalEntityIdentifier(companyTaxId);
			invAr.setShipToCustomerName(infoCust.getCustInformation().getPartyName());//----------------------------------------------------------------------------------------------
			invAr.setShipToCustomerNumber(infoCust.getCustInformation().getPartyNumber());//----------------------------------------------------------------------------------------------
			invAr.setBusinessUnit(com.getName());//----------------------------------------------------------------------------------------------
			invAr.setAccountingDate(insertDate);
			//invAr.setShipToSite(infoCust.getCustInformation().getPartyNumber());//----------------------------------------------------------------------------------------------
			invAr.setPayingCustomerName(infoCust.getCustInformation().getPartyName());//----------------------------------------------------------------------------------------------
			invAr.setBillToCustomerName(infoCust.getCustInformation().getPartyName());//----------------------------------------------------------------------------------------------
			invAr.setPayingCustomerAccount(custAccount.getAccountNumber());//----------------------------------------------------------------------------------------------
			invAr.setSoldToPartyNumber(infoCust.getCustInformation().getPartyNumber());//----------------------------------------------------------------------------------------------
			//Llenado de flexfields cabeceros
			ReceivablesInvoiceGdfDTO flex1 = new ReceivablesInvoiceGdfDTO();
			flex1.set__FLEX_Context("JLxMXReceivablesInformation");
			flex1.set__FLEX_Context_DisplayValue("Transactions for Mexico");
			flex1.setCFDIUniqueIdentifier(String.valueOf(soapEnvelopeComplemento.getAsJsonObject().get("UUID")).replaceAll("\"", ""));
			flex1.setCFDCBBSerialNumber(String.valueOf(soapEnvelopeComplete.getAsJsonObject().get("Serie")).replaceAll("\"", ""));
			flex1.setCFDCBBInvoiceNumber(String.valueOf(soapEnvelopeComplete.getAsJsonObject().get("Folio")).replaceAll("\"", ""));
			//Llenado de flex al listado 
			flexHeader.add(flex1);
			//Llenado de líneas
			JsonObject jobject = cuerpo.getAsJsonObject();
			JsonElement soapEnvelope = jobject.get("cfdi:Comprobante").getAsJsonObject().get("cfdi:Conceptos").getAsJsonObject().get("cfdi:Concepto");
			if(soapEnvelope instanceof JsonArray) {
				JsonArray jsonarray = soapEnvelope.getAsJsonArray();
				for (int i = 0; i < jsonarray.size(); i++) {
					JsonElement op = jsonarray.get(i).getAsJsonObject();
					
					JsonElement bdy = op;
					JsonElement bdyTaxes = bdy.getAsJsonObject().get("cfdi:Impuestos").getAsJsonObject().get(("cfdi:Traslados")).getAsJsonObject().get("cfdi:Traslado");
					
					String canLine = String.valueOf(bdy.getAsJsonObject().get("Cantidad")).replaceAll("\"", "");
					String unitPrice = String.valueOf(bdy.getAsJsonObject().get("ValorUnitario")).replaceAll("\"", "");
					String lineTotal = String.valueOf(bdy.getAsJsonObject().get("Importe")).replaceAll("\"", "");
					String uom = String.valueOf(bdy.getAsJsonObject().get("ClaveUnidad")).replaceAll("\"", "");
					String uomCloud = "";
					String taxRate = String.valueOf(bdyTaxes.getAsJsonObject().get("TasaOCuota")).replaceAll("\"", "");
					String taxRateCloud = "";
					String taxAmount = String.valueOf(bdyTaxes.getAsJsonObject().get("Importe")).replaceAll("\"", "");	
					
//					String totalInvoice = String.valueOf(soapEnvelopeComplete.getAsJsonObject().get("Total")).replaceAll("\"", "");
					
					switch(uom) {
						case "H87":
							uomCloud = "Pieza";
							break;
						case "E48":
							uomCloud = "Servicio";
							break;
						case "ACT":
							uomCloud = "Actividad";
							break;
						case "XBJ":
							uomCloud = "Cubeta";
							break;
						case "X1A":
							uomCloud = "Tambo Litros";
							break;
						case "KT":
							uomCloud = "Juego";
							break;
						case "KGM":
							uomCloud = "Kilogramo";
							break;
						case "MTR":
							uomCloud = "Metro";
							break;
						case "MTK":
							uomCloud = "Metro Cuadrado";
							break;
						case "BB":
							uomCloud = "Caja";
							break;
						case "GRM":
							uomCloud = "Gramo";
							break;
						case "LTR":
							uomCloud = "Litro";
							break;
						default:
							uomCloud = "Servicio";
							break;
					}
					switch(taxRate) {
						case "0.000000":
							taxRateCloud = "IVA 0 AR";
							break;
						case "0.080000":
							taxRateCloud = "IVA 8 AR";
							break;
						case "0.160000":
							taxRateCloud = "IVA 16 AR";
							break;
						default :
							taxRateCloud = "IVA 16 AR";
							break;
					}
					
					ReceivablesInvoiceLineDTO line = new ReceivablesInvoiceLineDTO();
					
					line.setDescription("Arrendamiento y servicios");
					line.setLineNumber(i+1);
					line.setQuantity(Double.parseDouble(canLine));
					line.setUnitSellingPrice(Double.parseDouble(unitPrice));
					line.setTaxClassificationCode(taxRateCloud);
					line.setUnitOfMeasure(uomCloud);
					line.setLineAmount(Double.parseDouble(lineTotal));
					line.setMemoLine("Arrendamiento y servicios");
					line.setLineAmountIncludesTax("Use Tax Rate Code");
					line.setTaxExemptionHandling("Standard");
					
					List<ReceivablesInvoiceLineTaxLineDTO> taxes =  new ArrayList<ReceivablesInvoiceLineTaxLineDTO>();
					ReceivablesInvoiceLineTaxLineDTO tax = new ReceivablesInvoiceLineTaxLineDTO();
					tax.setTaxAmount(Double.parseDouble(taxAmount));
					tax.setTaxRegimeCode("SAT");
					tax.setTaxRateCode(taxRateCloud);
					taxes.add(tax);
					
					line.setReceivablesInvoiceLineTaxLines(taxes);
					lineas.add(line);
					
					//Llenado de líneas de distribuicion
					for(int j = 1; j<4; j++) {
						ReceivablesInvoiceDistributionDTO distri = new ReceivablesInvoiceDistributionDTO();
						if(j == 1) {
							distri.setInvoiceLineNumber(String.valueOf(i+1));
							distri.setAccountClass("Revenue");
							distri.setAccountCombination("001-015-1020-72000203-0000");
							distri.setAccountedAmount(Double.parseDouble(lineTotal));
							distri.setPercent(100);
//							if(!currencyMXN) {
//								distri.setAccountedAmount(distri.getAccountedAmount() * eRate);
//							}
							distribuitions.add(distri);
						}/*else if(i == 2) {
							distri.setDetailedTaxLineNumber(i+1);
							distri.setInvoiceLineNumber(i+2);
							distri.setAccountClass("Tax");
							distri.setAccountCombination("001-000-0000-20070201-0000");
							distri.setAccountedAmount(16);
							distri.setPercent(100);
							distribuitions.add(distri);
						}*/else if(j == 3) {
//							distri.setInvoiceLineNumber(i+2);
//							distri.setAccountClass("Receivable");
//							distri.setAccountCombination("001-000-0000-10040101-0000");
//							distri.setAccountedAmount(Double.parseDouble(lineTotal) + Double.parseDouble(taxAmount));
//							distri.setPercent(100);
//							distribuitions.add(distri);
						}
					}
				}
			}else {
				JsonElement bdy = soapEnvelope;
				JsonElement bdyTaxes = bdy.getAsJsonObject().get("cfdi:Impuestos").getAsJsonObject().get(("cfdi:Traslados")).getAsJsonObject().get("cfdi:Traslado");
				
				String canLine = String.valueOf(bdy.getAsJsonObject().get("Cantidad")).replaceAll("\"", "");
				String unitPrice = String.valueOf(bdy.getAsJsonObject().get("ValorUnitario")).replaceAll("\"", "");
				String lineTotal = String.valueOf(bdy.getAsJsonObject().get("Importe")).replaceAll("\"", "");
				String uom = String.valueOf(bdy.getAsJsonObject().get("ClaveUnidad")).replaceAll("\"", "");
				String uomCloud = "";
				String taxRate = String.valueOf(bdyTaxes.getAsJsonObject().get("TasaOCuota")).replaceAll("\"", "");
				String taxRateCloud = "";
				String taxAmount = String.valueOf(bdyTaxes.getAsJsonObject().get("Importe")).replaceAll("\"", "");
				
//				String totalInvoice = String.valueOf(soapEnvelopeComplete.getAsJsonObject().get("Total")).replaceAll("\"", "");
				switch(uom) {
					case "H87":
						uomCloud = "Pieza";
						break;
					case "E48":
						uomCloud = "Servicio";
						break;
					case "ACT":
						uomCloud = "Actividad";
						break;
					case "XBJ":
						uomCloud = "Cubeta";
						break;
					case "X1A":
						uomCloud = "Tambo Litros";
						break;
					case "KT":
						uomCloud = "Juego";
						break;
					case "KGM":
						uomCloud = "Kilogramo";
						break;
					case "MTR":
						uomCloud = "Metro";
						break;
					case "MTK":
						uomCloud = "Metro Cuadrado";
						break;
					case "BB":
						uomCloud = "Caja";
						break;
					case "GRM":
						uomCloud = "Gramo";
						break;
					case "LTR":
						uomCloud = "Litro";
						break;
					default:
						uomCloud = "Servicio";
						break;
				}
				switch(taxRate) {
					case "0.000000":
						taxRateCloud = "IVA 0 AR";
						break;
					case "0.080000":
						taxRateCloud = "IVA 8 AR";
						break;
					case "0.160000":
						taxRateCloud = "IVA 16 AR";
						break;
					default :
						taxRateCloud = "IVA 16 AR";
						break;
				}
				
				ReceivablesInvoiceLineDTO line = new ReceivablesInvoiceLineDTO();
				
				line.setDescription("Arrendamiento y servicios");
				line.setLineNumber(1);
				line.setQuantity(Double.parseDouble(canLine));
				line.setUnitSellingPrice(Double.parseDouble(unitPrice));
				line.setTaxClassificationCode(taxRateCloud);
				line.setUnitOfMeasure(uomCloud);
				line.setLineAmount(Double.parseDouble(lineTotal));
				line.setMemoLine("Arrendamiento y servicios");
				line.setLineAmountIncludesTax("Use Tax Rate Code");
				line.setTaxExemptionHandling("Standard");
				
				List<ReceivablesInvoiceLineTaxLineDTO> taxes =  new ArrayList<ReceivablesInvoiceLineTaxLineDTO>();
				ReceivablesInvoiceLineTaxLineDTO tax = new ReceivablesInvoiceLineTaxLineDTO();
				tax.setTaxAmount(Double.parseDouble(taxAmount));
				tax.setTaxRegimeCode("SAT");
				tax.setTaxRateCode(taxRateCloud);
				taxes.add(tax);
				
				line.setReceivablesInvoiceLineTaxLines(taxes);
			
				//Llenado de líneas de distribuicion
				for(int i = 1; i<4; i++) {
					ReceivablesInvoiceDistributionDTO distri = new ReceivablesInvoiceDistributionDTO();
					if(i == 1) {
						distri.setInvoiceLineNumber(String.valueOf(1));
						distri.setAccountClass("Revenue");
						distri.setAccountCombination("001-015-1020-72000203-0000");
						distri.setAccountedAmount(Double.parseDouble(lineTotal));
						distri.setPercent(100);
//						if(!currencyMXN) {
//							distri.setAccountedAmount(distri.getAccountedAmount() * eRate);
//						}
						distribuitions.add(distri);
					}/*else if(i == 2) {
						distri.setDetailedTaxLineNumber(1);
						distri.setInvoiceLineNumber(1);
						distri.setAccountClass("Tax");
						distri.setAccountCombination("001-000-0000-20070201-0000");
						distri.setAccountedAmount(Double.parseDouble(taxAmount));
						distri.setPercent(100);
						distribuitions.add(distri);
					}*/else if(i == 3) {
//						distri.setInvoiceLineNumber(1);
//						distri.setAccountClass("Receivable");
//						distri.setAccountCombination("001-000-0000-10040101-0000");
//						distri.setAccountedAmount(Double.parseDouble(totalInvoice));
//						distri.setPercent(100);
//						distribuitions.add(distri);
					}
				}
				
				lineas.add(line);
			}
			
			ReceivablesInvoiceDistributionDTO distri = new ReceivablesInvoiceDistributionDTO();
			distri.setInvoiceLineNumber(null);
			distri.setAccountClass("Receivable");
			distri.setAccountCombination("001-000-0000-10040101-0000");
			distri.setAccountedAmount(Double.parseDouble(invoiceTotal));
			distri.setPercent(100);
			distribuitions.add(distri);
			
			//Llenado de lineas al cabecero
			invAr.setReceivablesInvoiceLines(lineas);
			//Llenado de flex al cabecero
			invAr.setReceivablesInvoiceGdf(flexHeader);
			//Llenado de lineas de distribucion
			invAr.setReceivablesInvoiceDistributions(distribuitions);
			
			return invAr;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	

}
