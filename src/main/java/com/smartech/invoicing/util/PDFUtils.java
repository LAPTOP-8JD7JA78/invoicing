package com.smartech.invoicing.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.smartech.invoicing.dto.ResponsiveLetterHeader;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

public class PDFUtils {
	
	public static byte[] getFilePDFInvoice(ResponsiveLetterHeader header, String filePathJasper, String filePathJrxml) {
		byte[] fileByteArray = null;
		HashMap<String,Object> hmParams = new HashMap<String,Object>();
		
		try {            
			JasperReport jasperReport = null;

			hmParams.put("invoiceDate", NullValidator.isNull(header.getDate()));
			hmParams.put("employeeName", NullValidator.isNull(header.getEmployeeName()));
			hmParams.put("employeeNumber", NullValidator.isNull(header.getEmployeeNumber()));
			hmParams.put("businessUnit", NullValidator.isNull(header.getBusinessUnit()));
			
			JRBeanCollectionDataSource dtlJRBean = new JRBeanCollectionDataSource(header.getResponsiveLetterLines()); 
			hmParams.put("dataSource", dtlJRBean);
			
			jasperReport = getCompiledFile(filePathJasper, filePathJrxml);
			fileByteArray = generateReportPDF(hmParams, jasperReport, new JREmptyDataSource());
		
		} catch (Exception e) {
			e.printStackTrace();
		}
     
		return fileByteArray;	
	}
	
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
}
