package com.smartech.invoicingprod.dto;

import java.util.List;


public class CustomerInformationDTO {
	private List<EmailAdressDTO> emailAdress;

	public List<EmailAdressDTO> getEmailAdress() {
		return emailAdress;
	}

	public void setEmailAdress(List<EmailAdressDTO> emailAdress) {
		this.emailAdress = emailAdress;
	}
}
