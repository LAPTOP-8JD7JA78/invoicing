package com.smartech.invoicingprod.distribuitorportal.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartech.invoicingprod.dao.InvoiceDao;
import com.smartech.invoicingprod.dao.InvoiceDetailsDao;
import com.smartech.invoicingprod.dao.UdcDao;
import com.smartech.invoicingprod.integration.dto.WarrantyDataDTO;
import com.smartech.invoicingprod.integration.dto.WarrantyDataLinesDTO;
import com.smartech.invoicingprod.integration.dto.WarrantyDataSerialLinesDTO;
import com.smartech.invoicingprod.integration.util.AppConstants;
import com.smartech.invoicingprod.model.Invoice;
import com.smartech.invoicingprod.model.InvoiceDetails;
import com.smartech.invoicingprod.model.Udc;
import com.smartech.invoicingprod.util.NullValidator;

@Service("distribuitorServices")
public class DistribuitorServicesImpl implements DistribuitorServices{

	@Autowired
	InvoiceDao invoiceDao;
	@Autowired
	InvoiceDetailsDao invoiceDetailsDao;
	@Autowired
	UdcDao udcDao;
	
	SimpleDateFormat sdfNoTime = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	public WarrantyDataDTO getDataInvoice(String invoiceNumber, String itemNumber, String itemSerial, String customerName) {
		// TODO Auto-generated method stub
		WarrantyDataDTO data = new WarrantyDataDTO();
		try {
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
//												if(iDet.getProductTypeCode().equals("36")) {
//													WarrantyDataSerialLinesDTO byProductType = new WarrantyDataSerialLinesDTO();
//													byProductType.setItemNumber(iDet.getItemNumber());
//													byProductType.setItemSerial(iDet.getItemSerial());
//													arraySerialItem.add(byProductType);							
//												}else {
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
//												}
											}else {
//												if(iDet.getProductTypeCode().equals("36")) {
//													WarrantyDataSerialLinesDTO byProductType = new WarrantyDataSerialLinesDTO();
//													byProductType.setItemNumber(iDet.getItemNumber());
//													byProductType.setItemSerial(iDet.getItemSerial());
//													arraySerialItem.add(byProductType);							
//												}else {
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
//												}
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
		}catch(Exception e) {
			e.printStackTrace();
			return data;
		}
		return data;
	}

	@Override
	public boolean insertData(String invoiceNumber, String itemNumber, String itemSerial, String productTypeCode) {		
		try{
			Invoice inv = invoiceDao.getSingleInvoiceByFolio(invoiceNumber);
			if(inv != null) {
				if(inv.getInvoiceDetails().size() > 0) {
					for(InvoiceDetails iDet: inv.getInvoiceDetails()) {
						boolean setTrue = false;
						if(iDet.getItemNumber().equals(itemNumber)) {
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

}
