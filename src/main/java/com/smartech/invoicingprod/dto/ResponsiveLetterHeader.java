package com.smartech.invoicing.dto;

import java.util.List;

public class ResponsiveLetterHeader {
	private String employeeName;
	private String employeeNumber;
	private String businessUnit;
	private String date;	
	private List<ResponsiveLetterLines> responsiveLetterLines;
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getEmployeeNumber() {
		return employeeNumber;
	}
	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}
	public String getBusinessUnit() {
		return businessUnit;
	}
	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public List<ResponsiveLetterLines> getResponsiveLetterLines() {
		return responsiveLetterLines;
	}
	public void setResponsiveLetterLines(List<ResponsiveLetterLines> responsiveLetterLines) {
		this.responsiveLetterLines = responsiveLetterLines;
	}
}
