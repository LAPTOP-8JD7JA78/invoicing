package com.smartech.invoicing.integration.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartech.invoicing.dao.InvoiceDao;
import com.smartech.invoicing.dto.InvoicesByReportsDTO;
import com.smartech.invoicing.integration.util.AppConstants;
import com.smartech.invoicing.integration.xml.rowset.Row;
import com.smartech.invoicing.model.Invoice;
import com.smartech.invoicing.model.InvoiceDetails;
import com.smartech.invoicing.model.TaxCodes;
import com.smartech.invoicing.service.CompanyService;
import com.smartech.invoicing.service.InvoiceService;
import com.smartech.invoicing.service.TaxCodesService;
import com.smartech.invoicing.util.NullValidator;

@Service("invoicingService")
public class InvoicingServiceImpl implements InvoicingService{
	
	@Autowired
	InvoiceService invoiceService;
	
	@Autowired
	CompanyService companyService;
	
	@Autowired
	TaxCodesService taxCodesService;
	
	@Autowired
	InvoiceDao invoiceDao;
	
	static Logger log = Logger.getLogger(InvoicingServiceImpl.class.getName());
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat sdfNoTime = new SimpleDateFormat("yyyy-MM-dd");
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean createStampInvoice(List<Row> r) {
		try {
			//Llenado de objeto DTO de la respuesta del reporte
			List<String> arr = new ArrayList<String>();
			List<InvoicesByReportsDTO> invlist = new ArrayList<InvoicesByReportsDTO>();	
			List<Invoice> invList = new ArrayList<Invoice>();
			for(Row ro: r) {
				InvoicesByReportsDTO invReports = new InvoicesByReportsDTO();
				invReports = fullDTO(ro);
				if(invReports != null) {
					System.out.println(invReports.getTransactionNumber());				
					invlist.add(invReports);
					
				}			
			}
			
			//llenar header---------------------------------------------------------------------------------------------------
			for(InvoicesByReportsDTO inv: invlist) {				
				if(!arr.contains(inv.getTransactionNumber())) {
					Invoice invoice = new Invoice();
					//Datos del cliente---------------------------------------------------------------------------------------
					invoice.setCustomerName(inv.getCustomerName());
					invoice.setCustomerZip(inv.getCustomerPostalCode());
					invoice.setAddress1(inv.getCustomerAddress1());
					invoice.setCountry(inv.getCustomerCountry());
					invoice.setCustomerTaxIdentifier(inv.getCustomerTaxIdentifier());
					invoice.setCustomerEmail("llopez@smartech.com.mx");
					
					//Datos de la unidad de negocio---------------------------------------------------------------------------
					invoice.setCompany(companyService.getCompanyByName(inv.getBusisinesUnitName()));
					invoice.setBranch(null);
					invoice.setSerial(null);
//					invoice.setSerial(NullValidator.isNull(invoice.getBranch().getInvOrganizationCode()));
					
					//Datos generales---------------------------------------------------------------------------------------
					invoice.setSetName(inv.getSetName());
					invoice.setFromSalesOrder(inv.getSalesOrderNumber());
					invoice.setPaymentTerms(inv.getPaymentTerms());
					invoice.setFolio(inv.getTransactionNumber());
					invoice.setInvoiceDetails(null);
					invoice.setStatus(AppConstants.STATUS_START);
					invoice.setInvoice(true);
					if(!inv.getTransactionTypeName().contains(AppConstants.STATUS_REPORTS_ESP)//Es nota de credito
						&& !inv.getTransactionTypeName().contains(AppConstants.STATUS_REPORTS_ING)) {
						invoice.setInvoice(false);
						invoice.setInvoiceReferenceTransactionNumber(inv.getPreviousTransactionNumber());
						
					}
					invoice.setOrderType(inv.getTransactionTypeName());
					
					//Datos extras-------------------------------------------------------------------------------------------------
					invoice.setCreatedBy("llopez");
					invoice.setCreationDate(sdfNoTime.parse(inv.getTransactionDate()));
					invoice.setUpdatedBy("llopez");
					invoice.setUpdatedDate(new Date());
					
					//Añadir registro a la lista facturas
					invList.add(invoice);
					arr.add(inv.getTransactionNumber());
				}
			}
			
			//Llenado de líneas---------------------------------------------------------------------------------------
			for(Invoice i: invList) {
				Set<InvoiceDetails> invDListNormal = new HashSet<InvoiceDetails>();
				Set<InvoiceDetails> invDListDiscount = new HashSet<InvoiceDetails>();
				int disc = 0;
				
				for(InvoicesByReportsDTO in : invlist) {
					if(i.getFolio().equals(in.getTransactionNumber())) {
						InvoiceDetails invDetails = new InvoiceDetails();
						Set<TaxCodes> tcList = new HashSet<TaxCodes>();
						
						invDetails.setItemNumber(in.getItemName());
						invDetails.setItemDescription(in.getItemDescription());
						if(NullValidator.isNull(Double.parseDouble(in.getTransactionLineUnitSellingPrice())) > 0) {
							invDetails.setUnitPrice(NullValidator.isNull(Double.parseDouble(in.getTransactionLineUnitSellingPrice())));
							invDetails.setLineType(AppConstants.REPORT_LINE_TYPE_NOR);
						}else {
							invDetails.setUnitPrice(Math.abs(Double.parseDouble(in.getTransactionLineUnitSellingPrice())));
							invDetails.setLineType(AppConstants.REPORT_LINE_TYPE_DIS);
						}
						
						invDetails.setTransactionLineNumber(in.getTransactionLineNumber());
						if(i.isInvoice()) {
							invDetails.setQuantity(NullValidator.isNull(Double.parseDouble(in.getQuantityInvoiced())));
						}else {
							invDetails.setQuantity(NullValidator.isNull(Double.parseDouble(in.getQuantityCredited())));
						}
						invDetails.setUomName(in.getUomCode());
						if(in.getTaxRecoverableAmount().isEmpty()) {
							invDetails.setTotalTaxAmount(0.00);
						}else {
							invDetails.setTotalTaxAmount(NullValidator.isNull(Double.parseDouble(in.getTaxRecoverableAmount())));
						}						
						invDetails.setTotalAmount(invDetails.getQuantity()*invDetails.getUnitPrice());
						
						//List<TaxCodes> tcL = taxCodesService.getTCList(0, 100);
						List<TaxCodes> tclConsult = taxCodesService.getTCList(0, 10);
						Set<TaxCodes> tcl = new HashSet<TaxCodes>(tclConsult);
						for(TaxCodes tc: tcl) {
							if(tc.getTaxName().equals(in.getTaxClassificationCode())) {
								tcList.add(tc);
							}
						}
						if(tcList.size() == 0) {
							tcList.add(taxCodesService.getTCById(2));
						}
						invDetails.setTaxCodes(tcList);
						//invDetails.setTaxCodes(tcList);
						if(invDetails.getLineType().equals(AppConstants.REPORT_LINE_TYPE_NOR)) {							
							invDListNormal.add(invDetails);
						}else {	
							invDListDiscount.add(invDetails);
							disc+=1;
						}
					}					
				}
				//Valida si hay descuentos
				if(disc > 0 && i.isInvoice()) {
					i.setInvoiceDetails(this.getDiscount(invDListNormal, invDListDiscount));
				}else {
					i.setInvoiceDetails(invDListNormal);
				}				
				//Obtiene y setea los valores de total, descuento y total de impuestos
				double taxAmount = 0.00;
				double subtotal = 0.00;
				double discount = 0.00;
				for(InvoiceDetails id: i.getInvoiceDetails()) {
					taxAmount = taxAmount + id.getTotalTaxAmount();
					subtotal = subtotal + id.getTotalAmount();
					discount = discount + id.getTotalDiscount();
				}
				i.setInvoiceTaxAmount(taxAmount); 
				i.setInvoiceTotal(subtotal + taxAmount);
				i.setInvoiceSubTotal(subtotal);
				i.setInvoiceDiscount(discount);
				//Guarda los datos en la base de datos pero antes valida si ya existe esa factura
				/*Invoice invoicetest = invoiceDao.getSingleInvoiceByFolio(i.getFolio());
				if(invoicetest == null) {
					System.out.println(true);
				}*/
				if(invoiceDao.getSingleInvoiceByFolio(i.getFolio()) == null) {
					if(!invoiceService.createInvoice(i)) {
						System.out.println(false);
					}
				}
			}
			return true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public InvoicesByReportsDTO fullDTO (Row r) {
		InvoicesByReportsDTO invoice = new InvoicesByReportsDTO();
		try {
			invoice.setCustomerName(NullValidator.isNull(r.getColumn0()));
			invoice.setCustomerNumber(NullValidator.isNull(r.getColumn1()));
			invoice.setCustomerTaxIdentifier(NullValidator.isNull(r.getColumn2()));
			invoice.setCustomerCountry(NullValidator.isNull(r.getColumn3()));
			invoice.setCustomerPostalCode(NullValidator.isNull(r.getColumn4()));
			invoice.setCustomerAddress1(NullValidator.isNull(r.getColumn5()));
			invoice.setPaymentTerms(NullValidator.isNull(r.getColumn6()));			
			invoice.setTransactionDate(NullValidator.isNull(r.getColumn7()));
			invoice.setTransactionNumber(NullValidator.isNull(r.getColumn8()));
			invoice.setTransactionSource(NullValidator.isNull(r.getColumn9()));
			invoice.setTransactionTypeName(NullValidator.isNull(r.getColumn10()));
			invoice.setSalesOrderNumber(NullValidator.isNull(r.getColumn11()));
			invoice.setTransactionLineNumber(NullValidator.isNull(r.getColumn12()));
			invoice.setUomCode(NullValidator.isNull(r.getColumn13()));
			invoice.setTransactionLineUnitSellingPrice(NullValidator.isNull(r.getColumn14()));
			invoice.setItemDescription(NullValidator.isNull(r.getColumn15()));
			invoice.setItemName(NullValidator.isNull(r.getColumn16()));
			invoice.setCreationDate(NullValidator.isNull(r.getColumn17()));
			invoice.setPreviousTransactionNumber(NullValidator.isNull(r.getColumn18()));
			invoice.setJournalLineDescriptor(NullValidator.isNull(r.getColumn19()));			
			invoice.setTaxClassificationCode(NullValidator.isNull(r.getColumn20()));
			invoice.setBusisinesUnitName(NullValidator.isNull(r.getColumn21()));
			invoice.setLegalEntityName(NullValidator.isNull(r.getColumn22()));
			invoice.setSetName(NullValidator.isNull(r.getColumn23()));			
			invoice.setLegalEntityAddress(NullValidator.isNull(r.getColumn24()));			
			invoice.setLegalEntityId(NullValidator.isNull(r.getColumn26()));			
			invoice.setQuantityCredited(NullValidator.isNull(r.getColumn27()));
			invoice.setQuantityInvoiced(NullValidator.isNull(r.getColumn28()));
			invoice.setTaxRecoverableAmount(NullValidator.isNull(r.getColumn29()));
			invoice.setTransactionEnteredAmouny(NullValidator.isNull(r.getColumn30()));
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		return invoice;
	}
	
	public Set<InvoiceDetails> getDiscount(Set<InvoiceDetails> Normal, Set<InvoiceDetails> discount){
		Set<InvoiceDetails> detailList = new HashSet<InvoiceDetails>();
		try {
			for(InvoiceDetails iN: Normal) {
				double total = 0.00;
				double tax = 0.00;
				double unitPrice = 0.00;
				double disc = 0.00;
				for(InvoiceDetails iD: discount) {
					if(iD.getItemNumber().equals(iN.getItemNumber()) &&
							iD.getUomName().equals(iN.getUomName()) &&
							iD.getQuantity() == iN.getQuantity()) {
						total = Math.abs(iN.getTotalAmount()) - Math.abs(iD.getTotalAmount());
						tax = Math.abs(iN.getTotalTaxAmount()) - Math.abs(iD.getTotalTaxAmount());
						unitPrice = iN.getUnitPrice() - iD.getUnitPrice();
						disc = iD.getTotalAmount();
						
						iN.setTotalAmount(total);
						iN.setTotalTaxAmount(tax);
						iN.setTotalDiscount(disc);
						iN.setUnitPrice(unitPrice);
					}
				}
				detailList.add(iN);
			}		
			
			return detailList;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
