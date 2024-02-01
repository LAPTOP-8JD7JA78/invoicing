package com.smartech.invoicingprod.distribuitorportal.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartech.invoicingprod.dao.InvoiceDao;
import com.smartech.invoicingprod.dao.InvoiceDetailsDao;
import com.smartech.invoicingprod.dao.PaymentsDao;
import com.smartech.invoicingprod.dao.UdcDao;
import com.smartech.invoicingprod.distribuitorportal.dto.FileInfoDTO;
import com.smartech.invoicingprod.integration.dto.WarrantyDataDTO;
import com.smartech.invoicingprod.integration.dto.WarrantyDataLinesDTO;
import com.smartech.invoicingprod.integration.dto.WarrantyDataSerialLinesDTO;
import com.smartech.invoicingprod.integration.service.InvoicingServiceImpl;
import com.smartech.invoicingprod.integration.util.AppConstants;
import com.smartech.invoicingprod.model.Invoice;
import com.smartech.invoicingprod.model.InvoiceDetails;
import com.smartech.invoicingprod.model.Payments;
import com.smartech.invoicingprod.model.Udc;
import com.smartech.invoicingprod.service.PaymentsService;
import com.smartech.invoicingprod.service.UdcService;
import com.smartech.invoicingprod.util.AppConstantsUtil;
import com.smartech.invoicingprod.util.NullValidator;

@Service("distribuitorServices")
public class DistribuitorServicesImpl implements DistribuitorServices{

	@Autowired
	InvoiceDao invoiceDao;
	@Autowired
	InvoiceDetailsDao invoiceDetailsDao;
	@Autowired
	UdcDao udcDao;	
	@Autowired
	UdcService udcService;
	@Autowired
	PaymentsService paymentsService;

	static Logger log = Logger.getLogger(InvoicingServiceImpl.class.getName());	
	SimpleDateFormat sdfNoTime = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	public WarrantyDataDTO getDataInvoice(String invoiceNumber, String itemNumber, String itemSerial, String customerName) {
		// TODO Auto-generated method stub
		WarrantyDataDTO data = new WarrantyDataDTO();
		try {
			//Jalar datos desde el número de serie
			if(itemSerial != null && !itemSerial.isEmpty()) {
				List<InvoiceDetails> invDetails2 = invoiceDetailsDao.searchBySerialNumber(itemSerial);
				if(invDetails2 != null && invDetails2.size() > 0) {
					log.warn("Proceso de obtención de datos para garantías: " + invDetails2.size());
					List<InvoiceDetails> invDetails = new ArrayList<InvoiceDetails>();
					for(InvoiceDetails invDetal: invDetails2) {
						Invoice invDetalle = invoiceDao.getInvoiceIdFromInvoiceDetails(invDetal.getId());
						String[] serialsNumberInvoice1 = invDetal.getItemSerial().split(",");
						log.warn("Proceso de obtención de datos para garantías: " + serialsNumberInvoice1);
						for(String s: serialsNumberInvoice1) {
							if((customerName.equals(invDetalle.getCustomerName()) && s.equals(itemSerial)) || (customerName.equals(invDetalle.getBranch().getName()) && s.equals(itemSerial))) {
								invDetails.add(invDetal);
								break;
							}
						}
					}
//					invDetails.add(invDetails2.get(0));
					List<WarrantyDataLinesDTO> arrayLines = new ArrayList<WarrantyDataLinesDTO>();
					for(InvoiceDetails iD: invDetails) {
						if(!iD.isWarrantyFull()) {
							if(iD.getProductTypeCode().equals("36")) {
								continue;
							}
							Invoice inv = invoiceDao.getInvoiceIdFromInvoiceDetails(iD.getId());
							if(inv != null) {
								if(inv.getInvoiceType().equals(AppConstants.ORDER_TYPE_FACTURA)) {
									if(inv.getFolio().contains("00000")) {
										continue;
									}
									if(inv.getCustomerName().equals(customerName)) {
										data.setDistribuitor(true);
										data.setName1(inv.getCustomerName());
										data.setName2("");
										data.setName3("");
										data.setAddress(inv.getShipToaddress());
										data.setInternalNumber("");
										data.setOutdoorNumber("");
										data.setReferenceAddress("");
										data.setColony("");//colonia
										data.setLocation("");//segunda colonua
										data.setPopulation(inv.getShipToCity());//ciudad
										data.setState(inv.getCustomerState());
										data.setCountry(inv.getCustomerCountry());
										data.setZip(inv.getShipToZip());
										data.setTelephoneNumber("");
										data.setEmail(inv.getCustomerEmail());
										
									}else {
										if(!inv.getBranch().getName().equals(customerName.toUpperCase())) {
											continue;
										}
										data.setDistribuitor(false);
										data.setName1(inv.getBranch().getName());
										data.setName2("");
										data.setName3("");
										data.setAddress(inv.getBranch().getAddress());
										data.setInternalNumber("");
										data.setOutdoorNumber("");
										data.setReferenceAddress("");
										data.setColony(inv.getBranch().getColony());
										data.setLocation("");
										data.setPopulation(inv.getBranch().getCity());
										data.setState(inv.getBranch().getState());
										data.setCountry(inv.getBranch().getCountry());
										data.setZip(inv.getBranch().getZip());
										data.setTelephoneNumber(inv.getBranch().getCellPhoneNumber());
										data.setEmail("");
									}
									//Mas datos
									if(inv.getFolio().contains("-")) {
										data.setInvoiceNumber(inv.getFolio().substring(0, inv.getFolio().indexOf("-")));//
									}else {
										data.setInvoiceNumber(inv.getFolio());
									}								
									data.setCustomerClass(inv.getCustomerClass());
									data.setSalesOrder(inv.getFromSalesOrder());
									data.setSalesOrderType(inv.getSalesOrderType());
									data.setInvoiceSerial(inv.getSerial());
									data.setInvoiceDate(sdfNoTime.format(inv.getUpdatedDate()));
									data.setBranchNumber(inv.getBranch().getInvOrganizationId());
									
									WarrantyDataLinesDTO lines = new WarrantyDataLinesDTO();
									List<WarrantyDataSerialLinesDTO> arraySerialItem = new ArrayList<WarrantyDataSerialLinesDTO>();
									
									if(iD.getItemSerial() != null) {
										List<String> serialsNumberInvoice = new ArrayList<String>();
										List<String> serialsNumberWarranty = new ArrayList<String>();
										if(iD.getItemSerial().contains(",")) {
											String[] serialsNumberInvoice1 = iD.getItemSerial().split(",");
											for(String s: serialsNumberInvoice1) {
												serialsNumberInvoice.add(s);
											}
										}else {
											serialsNumberInvoice.add(iD.getItemSerial());
										}
										if(NullValidator.isNull(iD.getWarrantyUsed()).contains(",")) {
											String[] serialsNumberWarranty1 = iD.getWarrantyUsed().split(",");
											for(String s: serialsNumberWarranty1) {
												serialsNumberWarranty.add(s);
											}
										}else {
											serialsNumberWarranty.add(iD.getWarrantyUsed());
										}
										List<String> serial = new ArrayList<String>();
										for(String str: serialsNumberInvoice) {
											if(str != null) {
												if(!serialsNumberWarranty.toString().contains(str)) {
													serial.add(str);
												}
											}
											
										}
										for(String s: serial) {
											if(s.equals(itemSerial)) {
												WarrantyDataSerialLinesDTO moreData = new WarrantyDataSerialLinesDTO();
												moreData.setItemNumber(iD.getItemNumber());
												moreData.setItemSerial(s);
												arraySerialItem.add(moreData);											
											}
										}
									}
									
									//Demas datos de las líneas
									lines.setItemNumber(iD.getItemNumber());
									lines.setEquipment(true);
									lines.setItemBrand(iD.getItemBrand());
									lines.setItemDescription(iD.getItemDescription());
									lines.setItemModel(iD.getItemModel());
									lines.setProductType(iD.getProductTypeCode());
									lines.setItemSerial(arraySerialItem);
									lines.setInvoiceLineType(iD.getIsInvoiceLine());
									
									arrayLines.add(lines);
								}
							}
						}
					}
					data.setLinesWarranty(arrayLines);
					return data;
				}else {
					return null;
				}
			}/*else {
				//Consultar el número de factura
				Invoice inv = invoiceDao.getSingleInvoiceByFolio(invoiceNumber);
				if(inv != null) {
					WarrantyDataDTO dataToAdd = new WarrantyDataDTO();
					if(inv.getCustomerName().equals(customerName)) {
						dataToAdd.setDistribuitor(true);
						dataToAdd.setName1(inv.getCustomerName());
						dataToAdd.setName2("");
						dataToAdd.setName3("");
						dataToAdd.setAddress(inv.getShipToaddress());
						dataToAdd.setInternalNumber("");
						dataToAdd.setOutdoorNumber("");
						dataToAdd.setReferenceAddress("");
						dataToAdd.setColony("");//colonia
						dataToAdd.setLocation("");//segunda colonua
						dataToAdd.setPopulation(inv.getShipToCity());//ciudad
						dataToAdd.setState(inv.getCustomerState());
						dataToAdd.setCountry(inv.getCustomerCountry());
						dataToAdd.setZip(inv.getShipToZip());
						dataToAdd.setTelephoneNumber("");
						dataToAdd.setEmail(inv.getCustomerEmail());
						
					}else {
						if(!inv.getBranch().getName().equals(customerName.toUpperCase())) {
							return data;
						}
						dataToAdd.setDistribuitor(false);
						dataToAdd.setName1(inv.getBranch().getName());
						dataToAdd.setName2("");
						dataToAdd.setName3("");
						dataToAdd.setAddress(inv.getBranch().getAddress());
						dataToAdd.setInternalNumber("");
						dataToAdd.setOutdoorNumber("");
						dataToAdd.setReferenceAddress("");
						dataToAdd.setColony(inv.getBranch().getColony());
						dataToAdd.setLocation("");
						dataToAdd.setPopulation(inv.getBranch().getCity());
						dataToAdd.setState(inv.getBranch().getState());
						dataToAdd.setCountry(inv.getBranch().getCountry());
						dataToAdd.setZip(inv.getBranch().getZip());
						dataToAdd.setTelephoneNumber(inv.getBranch().getCellPhoneNumber());
						dataToAdd.setEmail("");
					}
					dataToAdd.setInvoiceNumber(invoiceNumber);
					dataToAdd.setCustomerClass(inv.getCustomerClass());
					dataToAdd.setSalesOrder(inv.getFromSalesOrder());
					dataToAdd.setSalesOrderType(inv.getSalesOrderType());
					dataToAdd.setInvoiceSerial(inv.getSerial());
					dataToAdd.setInvoiceDate(sdfNoTime.format(inv.getUpdatedDate()));
					dataToAdd.setBranchNumber(inv.getBranch().getInvOrganizationId());
					
					if(inv.getInvoiceDetails().size() > 0) {
						List<WarrantyDataLinesDTO> arrayLines = new ArrayList<WarrantyDataLinesDTO>();
						if(itemNumber != null && !itemNumber.isEmpty()) {
							for(InvoiceDetails iDet: inv.getInvoiceDetails()) {
								if(iDet.getEquipmentReference().equals("E")) {
									if(!iDet.getProductTypeCode().equals("36")) {
										if(iDet.getItemNumber().equals(itemNumber)) {
											if(!iDet.isWarrantyFull()) {
												List<String> serialsNumberInvoice = new ArrayList<String>();
												List<String> serialsNumberWarranty = new ArrayList<String>();
												if(iDet.getItemSerial().contains(",")) {
													String[] serialsNumberInvoice1 = iDet.getItemSerial().split(",");
													for(String s: serialsNumberInvoice1) {
														serialsNumberInvoice.add(s);
													}
												}else {
													serialsNumberInvoice.add(iDet.getItemSerial());
												}
												if(NullValidator.isNull(iDet.getWarrantyUsed()).contains(",")) {
													String[] serialsNumberWarranty1 = iDet.getWarrantyUsed().split(",");
													for(String s: serialsNumberWarranty1) {
														serialsNumberWarranty.add(s);
													}
												}else {
													serialsNumberWarranty.add(iDet.getWarrantyUsed());
												}
												
												List<String> serial = new ArrayList<String>();
												for(String str: serialsNumberInvoice) {
													if(str != null) {
														if(!serialsNumberWarranty.toString().contains(str)) {
															serial.add(str);
														}
													}
													
												}
												
												//Datos de los números de serie
												WarrantyDataLinesDTO lines = new WarrantyDataLinesDTO();
												List<WarrantyDataSerialLinesDTO> arraySerialItem = new ArrayList<WarrantyDataSerialLinesDTO>();
												if(itemSerial != null && !itemSerial.isEmpty()) {
//													if(iDet.getProductTypeCode().equals("36")) {
//														WarrantyDataSerialLinesDTO byProductType = new WarrantyDataSerialLinesDTO();
//														byProductType.setItemNumber(iDet.getItemNumber());
//														byProductType.setItemSerial(iDet.getItemSerial());
//														arraySerialItem.add(byProductType);							
//													}else {
													if(iDet.getItemSerial().contains(",")) {
														//String[ ] serial = iDet.getItemSerial().split(",");
														for(String s: serial) {
															if(s.equals(itemSerial)) {
																WarrantyDataSerialLinesDTO moreData = new WarrantyDataSerialLinesDTO();
																moreData.setItemNumber(iDet.getItemNumber());
																moreData.setItemSerial(s);
																arraySerialItem.add(moreData);
																
															}
														}
													}else {
														WarrantyDataSerialLinesDTO oneData = new WarrantyDataSerialLinesDTO();
														oneData.setItemNumber(iDet.getItemNumber());
														oneData.setItemSerial(iDet.getItemSerial());
														arraySerialItem.add(oneData);
													}
//													}
												}else {
//													if(iDet.getProductTypeCode().equals("36")) {
//														WarrantyDataSerialLinesDTO byProductType = new WarrantyDataSerialLinesDTO();
//														byProductType.setItemNumber(iDet.getItemNumber());
//														byProductType.setItemSerial(iDet.getItemSerial());
//														arraySerialItem.add(byProductType);							
//													}else {
													if(iDet.getItemSerial().contains(",")) {
														//String[ ] serial = iDet.getItemSerial().split(",");
														for(String s: serial) {
															WarrantyDataSerialLinesDTO moreData = new WarrantyDataSerialLinesDTO();
															moreData.setItemNumber(iDet.getItemNumber());
															moreData.setItemSerial(s);
															arraySerialItem.add(moreData);
														}
													}else {
														WarrantyDataSerialLinesDTO oneData = new WarrantyDataSerialLinesDTO();
														oneData.setItemNumber(iDet.getItemNumber());
														oneData.setItemSerial(iDet.getItemSerial());
														arraySerialItem.add(oneData);
													}
//													}
												}
												//Demas datos de las líneas
												lines.setItemNumber(iDet.getItemNumber());
												lines.setEquipment(true);
												lines.setItemBrand(iDet.getItemBrand());
												lines.setItemDescription(iDet.getItemDescription());
												lines.setItemModel(iDet.getItemModel());
												lines.setProductType(iDet.getProductTypeCode());
												lines.setItemSerial(arraySerialItem);
												lines.setInvoiceLineType(iDet.getIsInvoiceLine());
												arrayLines.add(lines);
											}	
										}
									}
								}
							}
						}else {
							for(InvoiceDetails iDet: inv.getInvoiceDetails()) {
								if(iDet.getEquipmentReference().equals("E")) {
									if(!iDet.getProductTypeCode().equals("36")) {
										if(!iDet.isWarrantyFull()) {	
											List<String> serialsNumberInvoice = new ArrayList<String>();
											List<String> serialsNumberWarranty = new ArrayList<String>();
											if(iDet.getItemSerial().contains(",")) {
												String[] serialsNumberInvoice1 = iDet.getItemSerial().split(",");
												for(String s: serialsNumberInvoice1) {
													serialsNumberInvoice.add(s);
												}
											}else {
												serialsNumberInvoice.add(iDet.getItemSerial());
											}
											if(NullValidator.isNull(iDet.getWarrantyUsed()).contains(",")) {
												String[] serialsNumberWarranty1 = iDet.getWarrantyUsed().split(",");
												for(String s: serialsNumberWarranty1) {
													serialsNumberWarranty.add(s);
												}
											}else {
												serialsNumberWarranty.add(iDet.getWarrantyUsed());
											}
											
											List<String> serial = new ArrayList<String>();
											for(String str: serialsNumberInvoice) {
												if(str != null) {
													if(!serialsNumberWarranty.toString().contains(str)) {
														serial.add(str);
													}
												}
												
											}
											//Datos de los números de serie
											WarrantyDataLinesDTO lines = new WarrantyDataLinesDTO();
											List<WarrantyDataSerialLinesDTO> arraySerialItem = new ArrayList<WarrantyDataSerialLinesDTO>();
											
											if(iDet.getItemSerial().contains(",")) {
												for(String s: serial) {
													WarrantyDataSerialLinesDTO moreData = new WarrantyDataSerialLinesDTO();
													moreData.setItemNumber(iDet.getItemNumber());
													moreData.setItemSerial(s);
													arraySerialItem.add(moreData);
												}
											}else {
												WarrantyDataSerialLinesDTO oneData = new WarrantyDataSerialLinesDTO();
												oneData.setItemNumber(iDet.getItemNumber());
												oneData.setItemSerial(iDet.getItemSerial());
												arraySerialItem.add(oneData);
											}
											
											//Demas datos de las líneas
											lines.setItemNumber(iDet.getItemNumber());
											lines.setEquipment(true);
											lines.setItemBrand(iDet.getItemBrand());
											lines.setItemDescription(iDet.getItemDescription());
											lines.setItemModel(iDet.getItemModel());
											lines.setProductType(iDet.getProductTypeCode());
											lines.setItemSerial(arraySerialItem);
											lines.setInvoiceLineType(iDet.getIsInvoiceLine());
											arrayLines.add(lines);
										}	
									}
								}
							}
						}					
						dataToAdd.setLinesWarranty(arrayLines);
					}else {
						return data;
					}
					
					data = dataToAdd;				
				}else {
					return data;
				}
			}*/

		}catch(Exception e) {
			e.printStackTrace();
			return data;
		}
		return data;
	}

	@Override
	public boolean insertData(String invoiceNumber, String itemNumber, String itemSerial, String productTypeCode) {		
		try{
			Invoice inv = invoiceDao.getSingleInvoiceByFolio(invoiceNumber, AppConstants.ORDER_TYPE_FACTURA);
			if(inv != null) {
				if(inv.getInvoiceDetails().size() > 0) {
					for(InvoiceDetails iDet: inv.getInvoiceDetails()) {
						boolean setTrue = false;
						if(iDet.getItemNumber().equals(itemNumber)) {
							if(NullValidator.isNull(iDet.getWarrantyUsed()).contains(itemSerial)) {
								return false;
							}
							if(productTypeCode.equals("36") || iDet.getProductTypeCode().equals("36")) {
								/*if(iDet.getItemSerial().equals(itemSerial)) {
									iDet.setWarrantyUsed(itemSerial);
									iDet.setWarrantyFull(true);
									setTrue = true;
								}else {
									return false;
								}
								if(setTrue) {
									invoiceDetailsDao.updateInvoiceDetails(iDet);
									return true;
								}else {
									invoiceDetailsDao.updateInvoiceDetails(iDet);
									return false;
								}*/
								return false;
							}else if(iDet.getItemSerial().contains(",")) {
								String[] arraySerial = iDet.getItemSerial().split(",");								
								for(String s: arraySerial) {
									if(s.equals(itemSerial)) {
										if(iDet.getWarrantyUsed() == null) {
											iDet.setWarrantyUsed(itemSerial);
											setTrue = true;
											break;
										}else {
											iDet.setWarrantyUsed(iDet.getWarrantyUsed() + "," + itemSerial);
											setTrue = true;
											break;
										}
									}
								}
								if(iDet.getWarrantyUsed().contains(",")) {
									String[] fully = iDet.getWarrantyUsed().split(",");
									if(fully.length == arraySerial.length) {
										iDet.setWarrantyFull(true);
									}else {
										iDet.setWarrantyFull(false);
									}
								}else {
									iDet.setWarrantyFull(false);
								}
								if(setTrue) {
									invoiceDetailsDao.updateInvoiceDetails(iDet);
									return true;
								}else {
									invoiceDetailsDao.updateInvoiceDetails(iDet);
									return false;
								}
									
							}else {
								if(iDet.getItemSerial().equals(itemSerial)) {
									iDet.setWarrantyFull(true);
									iDet.setWarrantyUsed(itemSerial);
									invoiceDetailsDao.updateInvoiceDetails(iDet);//Actualizar registro
									return true;
								}else {
									return false;
								}
							}
						}
					}
				}else {
					return false;
				}
			}else {
				return false;
			}
			return false;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}		
	}

	@SuppressWarnings("unused")
	@Override
	public List<WarrantyDataDTO> retrieveAllData(String dataSearch) {
		// TODO Auto-generated method stub
		List<WarrantyDataDTO> allData = new ArrayList<WarrantyDataDTO>();
		try {
			List<String> arr = new ArrayList<String>();
			int count = 0;
			List<Invoice> getAllInvoice = new ArrayList<Invoice>();
			getAllInvoice = invoiceDao.getAllInvoiceToWarranty(dataSearch);
			if(getAllInvoice != null && getAllInvoice.size() > 0) {
				for(Invoice i: getAllInvoice) {
					if(!arr.contains(i.getFolio())) {
						WarrantyDataDTO warrantyNoEmite = new WarrantyDataDTO();
						List<WarrantyDataLinesDTO> linesWarranty = new ArrayList<WarrantyDataLinesDTO>();
						int count2 = 0;
						for(InvoiceDetails iDet: i.getInvoiceDetails()) {//Llenado de las líneas del DTO de garantías
							if(NullValidator.isNull(iDet.getEquipmentReference()).equals("E")) {
								if(!iDet.isWarrantyFull()) {
									if(!iDet.getProductTypeCode().equals("36")) {
										WarrantyDataLinesDTO arrayLines = new WarrantyDataLinesDTO();
										List<WarrantyDataSerialLinesDTO> arrayLinesSerial = new ArrayList<WarrantyDataSerialLinesDTO>();
										//Saber los # de series
										/*if(iDet.getProductTypeCode().equals("36")) {
											WarrantyDataSerialLinesDTO arraySerials = new WarrantyDataSerialLinesDTO();
											arraySerials.setItemNumber(iDet.getItemNumber());
											arraySerials.setItemSerial(iDet.getItemSerial());
											arrayLinesSerial.add(arraySerials);
										}else */
										if(iDet.getItemSerial().contains(",")) {
											String[ ] itemSerialOriginal = iDet.getItemSerial().split(",");
											if(NullValidator.isNull(iDet.getWarrantyUsed()).contains(",")) {
												//String[ ] itemSerialWarranty = iDet.getWarrantyUsed().split(",");
												//List<String> repValue = new ArrayList<String>();
												for(String s: itemSerialOriginal) {
													if(!iDet.getWarrantyUsed().contains(s)) {
														WarrantyDataSerialLinesDTO arraySerials = new WarrantyDataSerialLinesDTO();
														arraySerials.setItemNumber(iDet.getItemNumber());
														arraySerials.setItemSerial(s);
														arrayLinesSerial.add(arraySerials);
													}
												}
											}else {
												String itemSerialWarranty = iDet.getWarrantyUsed();
												if(itemSerialWarranty != null && !itemSerialWarranty.isEmpty()) {//un valor de serie utilizado
													for(String s: itemSerialOriginal) {
														if(!s.equals(itemSerialWarranty)) {
															WarrantyDataSerialLinesDTO arraySerials = new WarrantyDataSerialLinesDTO();
															arraySerials.setItemNumber(iDet.getItemNumber());
															arraySerials.setItemSerial(s);
															arrayLinesSerial.add(arraySerials);
														}
													}
												}else {//Ningun valor de serie utilizado
													for(String s: itemSerialOriginal) {
														WarrantyDataSerialLinesDTO arraySerials = new WarrantyDataSerialLinesDTO();
														arraySerials.setItemNumber(iDet.getItemNumber());
														arraySerials.setItemSerial(s);
														arrayLinesSerial.add(arraySerials);
													}
												}
											}
											
										}else {
											WarrantyDataSerialLinesDTO arraySerials = new WarrantyDataSerialLinesDTO();
											arraySerials.setItemNumber(iDet.getItemNumber());
											arraySerials.setItemSerial(iDet.getItemSerial());
											arrayLinesSerial.add(arraySerials);
										}
										//Seteo de los números de series de los productos
										arrayLines.setItemSerial(arrayLinesSerial);
										//Llenado de las lineas
										arrayLines.setItemNumber(iDet.getItemNumber());
										arrayLines.setItemDescription(iDet.getItemDescription());
										arrayLines.setInvoiceLineType(iDet.getIsInvoiceLine());
										arrayLines.setEquipment(true);
										arrayLines.setItemBrand(iDet.getItemBrand());
										arrayLines.setItemModel(iDet.getItemModel());
										arrayLines.setProductType(iDet.getProductTypeCode());
										linesWarranty.add(arrayLines);									
										count2++;
									}
								}
							}
						}		
						if(count2 > 0) {//Llenado del cabecero del DTO de garantías
							//Llenado principal
							warrantyNoEmite.setInvoiceNumber(i.getFolio());
							warrantyNoEmite.setCustomerClass(i.getCustomerClass());
							warrantyNoEmite.setSalesOrder(i.getFromSalesOrder());
							warrantyNoEmite.setSalesOrderType(i.getSalesOrderType());
							warrantyNoEmite.setInvoiceSerial(i.getSerial());
							warrantyNoEmite.setInvoiceDate(sdfNoTime.format(i.getUpdatedDate()));
							warrantyNoEmite.setBranchNumber(i.getBranch().getInvOrganizationId());
							//Llenado de direcciones de acuerdo a sucursal o a distribuidor
							//Seteo de las lineas en el cabecero del DTO -------------DISTRIBUITORSNAMES
							List<Udc> allDistribuitors = udcDao.searchBySystem(AppConstants.UDC_SYSTEM_GET_ALL_DISTRIBUITORS);
							List<String> nameDistribuitorsList = new ArrayList<String>();
							if(allDistribuitors != null && allDistribuitors.size() > 0) {
								for(Udc u: allDistribuitors) {
									nameDistribuitorsList.add(u.getUdcKey());
								}
							}
							if(nameDistribuitorsList.toString().contains(i.getCustomerName())) {//Datos del distribuidor
								warrantyNoEmite.setDistribuitor(true);
								warrantyNoEmite.setName1(i.getCustomerName());
								warrantyNoEmite.setName2("");
								warrantyNoEmite.setName3("");
								warrantyNoEmite.setAddress(i.getShipToaddress());
								warrantyNoEmite.setInternalNumber("");
								warrantyNoEmite.setOutdoorNumber("");
								warrantyNoEmite.setReferenceAddress("");
								warrantyNoEmite.setColony("");
								warrantyNoEmite.setLocation("");
								warrantyNoEmite.setPopulation(i.getShipToCity());
								warrantyNoEmite.setState(i.getCustomerState());
								warrantyNoEmite.setCountry(i.getShipToCountry());
								warrantyNoEmite.setZip(i.getShipToZip());
								warrantyNoEmite.setTelephoneNumber("");
								warrantyNoEmite.setEmail(i.getCustomerEmail());
							}else {//Datos de la sucursal
								warrantyNoEmite.setDistribuitor(false);
								warrantyNoEmite.setName1(i.getBranch().getName());
								warrantyNoEmite.setName2("");
								warrantyNoEmite.setName3("");
								warrantyNoEmite.setAddress(i.getBranch().getAddress());
								warrantyNoEmite.setInternalNumber("");
								warrantyNoEmite.setOutdoorNumber("");
								warrantyNoEmite.setReferenceAddress("");
								warrantyNoEmite.setColony(i.getBranch().getColony());
								warrantyNoEmite.setLocation("");
								warrantyNoEmite.setPopulation(i.getBranch().getCity());
								warrantyNoEmite.setState(i.getBranch().getState());
								warrantyNoEmite.setCountry(i.getBranch().getCountry());
								warrantyNoEmite.setZip(i.getBranch().getZip());
								warrantyNoEmite.setTelephoneNumber(i.getBranch().getCellPhoneNumber());
								warrantyNoEmite.setEmail("");
							}
							warrantyNoEmite.setLinesWarranty(linesWarranty);
							//Llenado de los datos del distribuidor y/o sucursal
							allData.add(warrantyNoEmite);
						}				
						count++;
						arr.add(i.getFolio());
					}
				}
			}
			return allData;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public FileInfoDTO getFileInfo(String invoiceNumber, String invoiceType) {
		log.info("PORTAL DIST: NUMERO FACTURA " + invoiceNumber + " TIPO: " + invoiceType);
		FileInfoDTO fileInfo = new FileInfoDTO();
		String filePathResponse = "";
		String filePathPay = "";
		String filePathResponsePdf = "";
		String filePathPayPdf = "";
		String fileName1 = "";
		String fileName2 = "";
		File file1;
		File file2;
		File fileZip;
		byte[] fileContent1;
		byte[] fileContent2;
		byte[] fileContentZip;
		String encodedFile;
		try {	
			//Rutas donde estan guardados los archivos timbrados Proceso Alejandro
			String data = this.folioByData(invoiceNumber);
			/*if(invoiceType.equals(AppConstants.ORDER_TYPE_FACTURA) || invoiceType.equals(AppConstants.ORDER_TYPE_NC)) {
//				Invoice inv = invoiceDao.getSingleInvoiceByFolio(invoiceNumber, invoiceType);
				data = this.folioByData(invoiceNumber);
			}else if(invoiceType.equals(AppConstants.ORDER_TYPE_CPAGO)) {
//				Payments paym = paymentsService.getPaymentByFolio(invoiceNumber);
				data = this.folioByData(invoiceNumber);
			}*/
//			String data = "C:\\Produccion\\SCATemp\\2009\\ComprobantesPDF\\2023\\8\\MEFAC2368123.pdf,C:\\Produccion\\SCATemp\\2009\\ComprobantesXML\\2023\\8\\MEFAC2368123.xml";
			if(data != null && !data.isEmpty()) {
				String[] splitData = data.split(",");
				file1 = new File(splitData[0]);
				file2 = new File(splitData[1]);
				if(file1.exists() || file2.exists()) {
					fileName1 = invoiceNumber + ".pdf";
					fileName2 = invoiceNumber + ".xml";
					fileZip = new File(invoiceNumber + ".zip");
					FileOutputStream out = new FileOutputStream(fileZip);
					ZipOutputStream zipOut = new ZipOutputStream(out);
					
					if(file1.exists()) {
						fileContent1 = FileUtils.readFileToByteArray(file1);
						zipOut.putNextEntry(new ZipEntry(fileName1));
						zipOut.write(fileContent1, 0, fileContent1.length);
						zipOut.closeEntry();
					}
					
					if(file2.exists()) {
						fileContent2 = FileUtils.readFileToByteArray(file2);
						zipOut.putNextEntry(new ZipEntry(fileName2));
						zipOut.write(fileContent2, 0, fileContent2.length);
						zipOut.closeEntry();
					}

					zipOut.close();
					fileContentZip = FileUtils.readFileToByteArray(fileZip);
					encodedFile = Base64.getEncoder().encodeToString(fileContentZip);
					
					fileInfo.setInvoiceNumber(invoiceNumber);
					fileInfo.setInvoiceType(invoiceType);
					fileInfo.setFileExt("zip");
					fileInfo.setFileContent(encodedFile);
					log.info("SE CREA ZIP CORRECTAMENTE.");
					return fileInfo;
				}
			}
			//Rutas donde estan guardados los archivos timbrados Proceso Fernando Tellez
			List<Udc> udcPaths = udcService.searchBySystem(AppConstantsUtil.RUTA_FILES);
			for(Udc u: udcPaths) {
				if(u.getStrValue1().equals(AppConstantsUtil.FILE_RESPONSE)) {
					filePathResponse = u.getUdcKey();
					filePathPay = u.getStrValue2();
				}
				
				if(u.getStrValue1().equals(AppConstantsUtil.FILE_RESPONSE_PDF)) {
					filePathResponsePdf = u.getUdcKey();
					filePathPayPdf = u.getStrValue2();
				}
			}			

			if(AppConstants.PAYMENTS_CPAGO.equals(invoiceType)) {
				fileName1 = "P_" + invoiceNumber + ".xml";
				fileName2 = "P_" + invoiceNumber + ".pdf";				
				file1 = new File(filePathPay + fileName1);
				file2 = new File(filePathPayPdf + fileName2);
			} else {
				fileName1 = invoiceNumber + ".xml";
				fileName2 = invoiceNumber + ".pdf";
				file1 = new File(filePathResponse + fileName1);
				file2 = new File(filePathResponsePdf + fileName2);
			}

			if(file1.exists() || file2.exists()) {
				fileZip = new File(invoiceNumber + ".zip");
				FileOutputStream out = new FileOutputStream(fileZip);
				ZipOutputStream zipOut = new ZipOutputStream(out);
				
				if(file1.exists()) {
					fileContent1 = FileUtils.readFileToByteArray(file1);
					zipOut.putNextEntry(new ZipEntry(fileName1));
					zipOut.write(fileContent1, 0, fileContent1.length);
					zipOut.closeEntry();					
				}
				
				if(file2.exists()) {
					fileContent2 = FileUtils.readFileToByteArray(file2);
					zipOut.putNextEntry(new ZipEntry(fileName2));
					zipOut.write(fileContent2, 0, fileContent2.length);
					zipOut.closeEntry();
				}

				zipOut.close();
				fileContentZip = FileUtils.readFileToByteArray(fileZip);
				encodedFile = Base64.getEncoder().encodeToString(fileContentZip);
				
				fileInfo.setInvoiceNumber(invoiceNumber);
				fileInfo.setInvoiceType(invoiceType);
				fileInfo.setFileExt("zip");
				fileInfo.setFileContent(encodedFile);
				log.info("SE CREA ZIP CORRECTAMENTE.");
			}
		} catch (Exception e) {
			log.error(e);
			log.info("PORTAL DIST: ", e);
			return null;
		}
		log.info("TERMINA PROCESO PORTAL DIST: NUMERO FACTURA " + invoiceNumber + " TIPO: " + invoiceType);
		return fileInfo;
	}
	
	public String folioByData (String folio) {
		Connection cn = null;
		try {
//			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//			String url = "jdbc:sqlserver://localhost:1433;databaseName=SCADB-P-IMEMSA;integratedSecurity=false;encrypt=true;trustServerCertificate=true";
//			cn = DriverManager.getConnection(url, "sa", "1234");
//			String url = "jdbc:sqlserver://base-de-datos:1433;databaseName=SCADB;integratedSecurity=false;encrypt=true;trustServerCertificate=true";//Google
//			cn = DriverManager.getConnection(url, "sa", "ScG990720Rf1.$");//Google
//			String url = "jdbc:sqlserver://EC2AMAZ-MHT40UR:1433;databaseName=SCADB-D-IMEMSA;integratedSecurity=false;encrypt=true;trustServerCertificate=true";//AWS TEST
//			cn = DriverManager.getConnection(url, "sa", "ScG990720Rf1.$");//AWS TEST
			String url = "jdbc:sqlserver://EC2AMAZ-MHT40UR:1433;databaseName=SCADB-P-IMEMSA;integratedSecurity=false;encrypt=true;trustServerCertificate=true";//AWS PROD
			cn = DriverManager.getConnection(url, "sa", "ScG990720Rf1.$");//AWS PROD
			if(cn != null) {
				String query = "Declare @Archivo varchar(100) = '" + folio + ".txt'\n" + 
						"						select ISNULL(Serie,'') + ISNULL(Folio,'') + '.txt', RutaPDF , RutaXML, UUID, emisor.Rfc RFCE, emisor.Nombre NombreE, receptor.Rfc RFCR, receptor.Nombre NombreR\n" + 
						"						from Comprobante Txt\n" + 
						"						inner join FACT_Factura Archivos \n" + 
						"						on\n" + 
						"						txt.ClaveComprobante = Archivos.ClaveComprobante \n" + 
						"						inner join TimbreFiscalDigital timbre\n" + 
						"						on\n" + 
						"						txt.ClaveComprobante = timbre.ClaveComprobante\n" + 
						"						inner join ComprobanteEmisor emisor\n" + 
						"						on\n" + 
						"						txt.ClaveComprobante = emisor.ClaveComprobante\n" + 
						"						inner join ComprobanteReceptor receptor\n" + 
						"						on \n" + 
						"						txt.ClaveComprobante = receptor.ClaveComprobante\n" + 
						"						where (ISNULL(Serie,'') + ISNULL(Folio,'') + '.txt') =  @Archivo";
				PreparedStatement pstmt = cn.prepareStatement(query);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					String rutaArchivo = rs.getString(1);
					String rutaPdf = rs.getString(2);
					String rutaXml = rs.getString(3);
					String uuidRs = rs.getString(4);
					String rfcEmisor = rs.getString(5);
					String nombreEmisor = rs.getString(6);
					String rfcReceptor = rs.getString(7);
					String nombreReceptor = rs.getString(8);
					System.out.println("Revisar valor");
					if(rutaPdf != null && rutaXml != null) {
						rs.close();
						return rutaPdf + "," + rutaXml;
					}
				}
				rs.close();
			}
		}catch(Exception e) {
			e.printStackTrace();
			log.error("Error en la obtención del registro: " + e);
		}finally {
            try {
                if (cn != null && !cn.isClosed()) {
                    cn.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
		return null;
	}
	
}
