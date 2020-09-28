package com.smartech.invoicing.integration;

import com.smartech.invoicing.dto.ItemsDTO;

public interface SOAPService {

	public ItemsDTO getItemDataByItemNumberOrgCode(String ItemNumber, String orgCode);
}
