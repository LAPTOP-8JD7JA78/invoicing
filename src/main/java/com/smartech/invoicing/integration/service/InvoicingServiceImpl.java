package com.smartech.invoicing.integration.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartech.invoicing.dto.InvoicesByReportsDTO;
import com.smartech.invoicing.integration.util.AppConstants;
import com.smartech.invoicing.integration.xml.rowset.Row;
import com.smartech.invoicing.model.Company;
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
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat sdfNoTime = new SimpleDateFormat("yyyy-MM-dd");
	
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
					Company com = new Company();
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
				List<InvoiceDetails> invDList = new ArrayList<InvoiceDetails>();
				
				for(InvoicesByReportsDTO in : invlist) {
					if(i.getFolio().equals(in.getTransactionNumber())) {
						InvoiceDetails invDetails = new InvoiceDetails();
						List<TaxCodes> tcList = new ArrayList<TaxCodes>();
						
						invDetails.setItemNumber(in.getItemName());
						invDetails.setItemDescription(in.getItemDescription());
						if(NullValidator.isNull(Double.parseDouble(in.getTransactionLineUnitSellingPrice())) > 0) {
							invDetails.setUnitPrice(NullValidator.isNull(Double.parseDouble(in.getTransactionLineUnitSellingPrice())));
							invDetails.setLineType(AppConstants.REPORT_LINE_TYPE_NOR);
						}else {
							invDetails.setUnitPrice(Math.abs(Double.parseDouble(in.getTransactionLineUnitSellingPrice())));
							invDetails.setLineType("");
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
						
						List<TaxCodes> tcL = taxCodesService.getTCList(0, 100);
						for(TaxCodes tc: tcL) {
							if(tc.getTaxName().equals(in.getTaxClassificationCode())) {
								tcList.add(tc);
							}
						}
						if(tcList.size() == 0) {
							invDetails.setTaxCodes(null);
						}else {
							invDetails.setTaxCodes(tcList);
						}
						
						invDList.add(invDetails);
					}					
				}
				
				i.setInvoiceDetails(invDList);
				if(!invoiceService.createInvoice(i)) {
					System.out.println(false);
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
			invoice.setTransactionDate(NullValidator.isNull(r.getColumn6()));
			invoice.setTransactionNumber(NullValidator.isNull(r.getColumn7()));
			invoice.setTransactionSource(NullValidator.isNull(r.getColumn8()));
			invoice.setTransactionTypeName(NullValidator.isNull(r.getColumn9()));
			invoice.setSalesOrderNumber(NullValidator.isNull(r.getColumn10()));
			invoice.setTransactionLineNumber(NullValidator.isNull(r.getColumn11()));
			invoice.setUomCode(NullValidator.isNull(r.getColumn12()));
			invoice.setTransactionLineUnitSellingPrice(NullValidator.isNull(r.getColumn13()));
			invoice.setItemName(NullValidator.isNull(r.getColumn14()));
			invoice.setCreationDate(NullValidator.isNull(r.getColumn15()));
			invoice.setPreviousTransactionNumber(NullValidator.isNull(r.getColumn16()));
			invoice.setJournalLineDescriptor(NullValidator.isNull(r.getColumn17()));
			invoice.setTaxClassificationCode(NullValidator.isNull(r.getColumn18()));
			invoice.setBusisinesUnitName(NullValidator.isNull(r.getColumn19()));
			invoice.setLegalEntityName(NullValidator.isNull(r.getColumn20()));
			invoice.setLegalEntityAddress(NullValidator.isNull(r.getColumn21()));
			invoice.setLegalEntityId(NullValidator.isNull(r.getColumn22()));
			invoice.setQuantityCredited(NullValidator.isNull(r.getColumn23()));
			invoice.setQuantityInvoiced(NullValidator.isNull(r.getColumn24()));
			invoice.setTaxRecoverableAmount(NullValidator.isNull(r.getColumn25()));
			invoice.setTransactionEnteredAmouny(NullValidator.isNull(r.getColumn26()));
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		return invoice;
	}

}
