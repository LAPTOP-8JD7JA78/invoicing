package com.smartech.invoicingprod.integration.dto;

public class AnalyticsDTO {
	private String Ar_Report_date;
	private String itemId;
	private String orgCode;
	private String salesOrder;

	public String getAr_Report_date() {
		return Ar_Report_date;
	}

	public void setAr_Report_date(String ar_Report_date) {
		Ar_Report_date = ar_Report_date;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getSalesOrder() {
		return salesOrder;
	}

	public void setSalesOrder(String salesOrder) {
		this.salesOrder = salesOrder;
	}
	
}
