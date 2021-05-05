package com.smartech.invoicing.integration.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartech.invoicing.dto.ResponsiveLetterHeader;
import com.smartech.invoicing.dto.ResponsiveLetterLines;
import com.smartech.invoicing.integration.service.MailService;
import com.smartech.invoicing.integration.xml.rowset.Row;
import com.smartech.invoicing.model.Company;
import com.smartech.invoicing.model.FixedAsset;
import com.smartech.invoicing.service.CompanyService;
import com.smartech.invoicing.service.FixedAssetService;
import com.smartech.invoicing.util.NullValidator;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
@Service("responsiveLetter")
public class ResponsiveLetterImpl implements ResponsiveLetter{
	
	@Autowired
	MailService mailService;
	
	@Autowired
	FixedAssetService fixedAssetService;
	
	@Autowired
	CompanyService companyService;
	
	SimpleDateFormat sdfNoTime = new SimpleDateFormat("yyyy-MM-dd");
	
	@SuppressWarnings("static-access")
	@Override
	public boolean createFile(List<Row> r) {
		try {	
			List<FixedAsset> listFa = new ArrayList<FixedAsset>();
			for(Row ro: r) {
				String bussinesUnit = null;
				String assetNumber = null;				
				FixedAsset fa = new FixedAsset();
				assetNumber = NullValidator.isNull(ro.getColumn10());
				fa = fixedAssetService.searchByAssetNumber(assetNumber);
				if(fa != null) {
					bussinesUnit = NullValidator.isNull(ro.getColumn0());
					Company com= companyService.getCompanyByBook(bussinesUnit);
					if(com != null) {
						fa.setBussinesUnit(com.getBusinessUnitName());
						fa.setUpdateDate(sdfNoTime.format(new Date()));
						fa.setPersonAssigned(NullValidator.isNull(ro.getColumn8()));
						fa.setPersonNumber(NullValidator.isNull(ro.getColumn9()));
						fa.setAssetQuantity(NullValidator.isNull(ro.getColumn14()));
						listFa.add(fa);
						fixedAssetService.updateFA(fa);
					}
				}			
			}
			
			List<String> names = new ArrayList<String>();
			List<ResponsiveLetterHeader> headerList = new ArrayList<ResponsiveLetterHeader>();
			for(FixedAsset f: listFa) {	
				ResponsiveLetterHeader header = new ResponsiveLetterHeader();
				if(!names.contains(f.getPersonAssigned())) {
					header.setBusinessUnit(f.getBussinesUnit());
					header.setEmployeeName(f.getPersonAssigned());
					header.setEmployeeNumber(f.getPersonNumber());					
					header.setDate(NullValidator.isNull(this.getDate(f.getUpdateDate())));
					header.setResponsiveLetterLines(null);
					names.add(f.getPersonAssigned());
					headerList.add(header);
				}
			}
			
			for(ResponsiveLetterHeader head: headerList) {
				List<ResponsiveLetterLines> lines = new ArrayList<ResponsiveLetterLines>();
				for(FixedAsset f: listFa) {
					ResponsiveLetterLines line = new ResponsiveLetterLines();
					if(head.getEmployeeName().equals(f.getPersonAssigned())
							&& head.getBusinessUnit().equals(f.getBussinesUnit())) {
						line.setAssetName(f.getAssetNumber());
						line.setAssetDescription(f.getAssetDescription());
						line.setAssetBrand(f.getAssetModel());
						line.setAssetSerial(f.getSerialNumber());
						line.setAssetQuantity(f.getAssetQuantity());
						lines.add(line);	
					}
				}	
				head.setResponsiveLetterLines(lines);
				sendFile(head);
			}
			
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@SuppressWarnings("static-access")
	public void sendFile(ResponsiveLetterHeader header) {
		try {
			String sourceFileName = "C:\\Users\\llope\\JaspersoftWorkspace\\MyReports\\Responsive_Letter.jrxml";            
			File theFile = new File(sourceFileName);
			JasperDesign jasperDesign = JRXmlLoader.load(theFile);
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);			
			
			byte[] pdfBytes = this.getFilePDFInvoice(header, jasperReport);
			List<String> email = new ArrayList<String>();
			
			String emails = "llopez@smartech.com.mx";
			email.add(emails);
			Map<String, byte[]> responsive = new HashMap<String, byte[]>();
			responsive.put("CARTA RESPOSNIVA.PDF", pdfBytes);
			mailService.sendMail(email,
					"Prueba",
					"Prueba", responsive);
//			return true;
		}catch(Exception e) {
			
		}
	}
	
	@SuppressWarnings("deprecation")
	public static byte[] getFilePDFInvoice(ResponsiveLetterHeader header, JasperReport jasperReport) {
		byte[] fileByteArray = null;
		HashMap<String,Object> hmParams = new HashMap<String,Object>();
		try {      
			File logo = new File("C:\\Users\\llope\\Desktop\\LIBRO_IME.PNG");
			
			hmParams.put("invoiceDate", NullValidator.isNull(header.getDate()));
			hmParams.put("employeeName", NullValidator.isNull(header.getEmployeeName()));
			hmParams.put("employeeNumber", NullValidator.isNull(header.getEmployeeNumber()));
			hmParams.put("businessUnit", NullValidator.isNull(header.getBusinessUnit()));
			hmParams.put("logo", logo.toURL());
			
			JRBeanCollectionDataSource dtlJRBean = new JRBeanCollectionDataSource(header.getResponsiveLetterLines()); 
			hmParams.put("dataSource", dtlJRBean);
			fileByteArray = generateReportPDF(hmParams, jasperReport, new JREmptyDataSource());
		
		} catch (Exception e) {
			e.printStackTrace();
		}
     
		return fileByteArray;	
	}
	
	@SuppressWarnings("unused")
	private static JasperReport getCompiledFile(String filePathJasper, String filePathJrxml) throws JRException {
		File reportFile = new File(filePathJasper);
		JasperCompileManager.compileReportToFile(filePathJrxml, filePathJasper);
		JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportFile.getPath());
		return jasperReport;
	} 
	
	private static byte[] generateReportPDF (Map<String, Object> parameters, JasperReport jasperReport, JREmptyDataSource conn) throws Exception{		
		byte[] bytes = null;
		bytes = JasperRunManager.runReportToPdf(jasperReport, parameters, conn);
		return bytes;
	}
	private static String getDate(String completeDate) {
		String fecha = completeDate;
		String complete = null;
		try {
			String alo = fecha.substring(0, 4);
			String mes = fecha.substring(5, 7);
			String dia = fecha.substring(8);
			switch(mes) {
			case "01":
				complete = dia + " de enero del " + alo;
				break;					
			case "02":
				complete = dia + " de febrero del " + alo;
				break;
			case "03":
				complete = dia + " de marzo del " + alo;
				break;
			case "04":
				complete = dia + " de abril del " + alo;
				break;
			case "05":
				complete = dia + " de mayo del " + alo;
				break;
			case "06":
				complete = dia + " de junio del " + alo;
				break;
			case "07":
				complete = dia + " de julio del " + alo;
				break;
			case "08":
				complete = dia + " de agosto del " + alo;
				break;
			case "09":
				complete = dia + " de septiembre del " + alo;
				break;
			case "10":
				complete = dia + " de octubre del " + alo;
				break;
			case "11":
				complete = dia + " de noviembre del " + alo;
				break;
			case "12":
				complete = dia + " de diciembre del " + alo;
				break;
		}
			return complete;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
