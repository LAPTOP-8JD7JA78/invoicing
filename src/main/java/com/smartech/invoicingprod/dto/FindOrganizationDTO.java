package com.smartech.invoicingprod.dto;

import java.util.List;

public class FindOrganizationDTO {

	private String PartyId;
	private String PartyNumber;
	private String JgzzFiscalCode;
	private List<PartySiteDTO> partySite;
	public String getPartyId() {
		return PartyId;
	}
	public void setPartyId(String partyId) {
		PartyId = partyId;
	}
	public String getPartyNumber() {
		return PartyNumber;
	}
	public void setPartyNumber(String partyNumber) {
		PartyNumber = partyNumber;
	}
	public String getJgzzFiscalCode() {
		return JgzzFiscalCode;
	}
	public void setJgzzFiscalCode(String jgzzFiscalCode) {
		JgzzFiscalCode = jgzzFiscalCode;
	}
	public List<PartySiteDTO> getPartySite() {
		return partySite;
	}
	public void setPartySite(List<PartySiteDTO> partySite) {
		this.partySite = partySite;
	}
	
	
}
