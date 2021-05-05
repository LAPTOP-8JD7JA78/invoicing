package com.smartech.invoicing.integration.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartech.invoicing.dto.AssetLabel;
import com.smartech.invoicing.integration.util.AppConstants;
import com.smartech.invoicing.integration.util.ExcelFiles;
import com.smartech.invoicing.integration.xml.rowset.Row;
import com.smartech.invoicing.model.Company;
import com.smartech.invoicing.model.FixedAsset;
import com.smartech.invoicing.model.NextNumber;
import com.smartech.invoicing.model.Udc;
import com.smartech.invoicing.service.BranchService;
import com.smartech.invoicing.service.CompanyService;
import com.smartech.invoicing.service.FixedAssetService;
import com.smartech.invoicing.service.NextNumberService;
import com.smartech.invoicing.service.UdcService;
import com.smartech.invoicing.util.NullValidator;

@Service("labelService")
public class LabelServiceImpl implements LabelService{
	
	@Autowired
	MailService mailService;
	
	@Autowired
	UdcService udcService;
	
	@Autowired
	NextNumberService nextNumberService;
	
	@Autowired
	CompanyService companyService;
	
	@Autowired
	BranchService branchService;
	
	@Autowired
	FixedAssetService fixedAssetService;
	
	SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	public boolean createLabel(List<Row> r) {
		try {
			List<AssetLabel> alList = new ArrayList<AssetLabel>();
			for(Row row: r) {
				FixedAsset fa = new FixedAsset();
				
				AssetLabel al = new AssetLabel();
				String locationSegment = NullValidator.isNull(row.getColumn3().replaceAll("\"", ""));
				Udc searchSegment = udcService.searchBySystemAndKey(AppConstants.UDC_SYSTEM_CENTERCOST, locationSegment);
				if(searchSegment != null) {
					String locationSegmentCode = searchSegment.getStrValue1();
					Company com = companyService.getCompanyByBook(NullValidator.isNull(row.getColumn0()));
					NextNumber nnumber = nextNumberService.getNextNumberByItem(AppConstants.UDC_KEY_LABEL, com);
//					int nnumber = nextNumberService.getNextNumber("LABEL", bran);
					if(nnumber != null) {
						String labelCostCenter = NullValidator.isNull(row.getColumn1());
						String [] var = labelCostCenter.split("-");
						String code = var[0] + var[3] + locationSegmentCode + nnumber.getFolio();
						
						al.setAssetNumber(NullValidator.isNull(row.getColumn10()));
						al.setAssetDescription(NullValidator.isNull(row.getColumn12()));
						al.setSerialNumber(NullValidator.isNull(row.getColumn6()));
						al.setBrand(NullValidator.isNull(row.getColumn5()));
						al.setLabelCode(NullValidator.isNull(code));
						
						FixedAsset searchPreviuos = fixedAssetService.searchByAssetNumber(NullValidator.isNull(al.getAssetNumber()));
						if(searchPreviuos == null) {
							fa.setAssetNumber(al.getAssetNumber());
							fa.setAssetDescription(al.getAssetDescription());
							fa.setSerialNumber(al.getSerialNumber());
							fa.setAssetModel(al.getBrand());
							fa.setAssetCode(al.getLabelCode());
							fa.setAssetQuantity(null);
							fa.setPersonAssigned(null);
							fa.setCreationDate(sdfTime.format(new Date()));
							fa.setUpdateDate(sdfTime.format(new Date()));
							alList.add(al);
							fixedAssetService.saveFA(fa);
						}
					}else {
						return false;
					}
				}else {
					continue;
				}
			}
			
			if(!alList.isEmpty() && alList != null) {
				Map<String, byte[]> attachedExcel = ExcelFiles.Main(alList);
				if(attachedExcel != null && !attachedExcel.isEmpty()) {
					List<Udc> emails = udcService.searchBySystem("EMAILS");
					List<String> email = new ArrayList<String>();
					for(Udc u: emails) {
						email.add(u.getUdcKey());
					}
					mailService.sendMail(email,
							"EXCEL PARA ETIQUETAS",
							"EL SIGUIENTE EXCEL TIENE LOS SIGUIENTES ACTIVOS CON SU CÓDIGO DE ETIQUETA EN LA FECHA: " + new Date(),
							attachedExcel);
					return true;
				}else {
					Log.error("ERROR AL GENERAR EL EXCEL PARA LAS ETIQUETAS" + new Date());
				}
			}
			return true; 
		}catch(Exception e) {
			Log.error("ERROR /LABELSERVICEIMPL " + e);
			return false;
		}
	}
	
	
	public boolean sendFiles() {
		List<Udc> emails = udcService.searchBySystem("EMAILS");
		String e = "lopluis98@gmail.com";
		List<String> email = new ArrayList<String>();
		email.add(e);
		for(Udc u: emails) {
			email.add(u.getUdcKey());
		}
		ExcelFiles excelFiles = new ExcelFiles();
		mailService.sendMail(email,
				"TEST EXCEL",
				"TEST EXCEL",
				excelFiles.Main(null));
		return true;
	}

}
