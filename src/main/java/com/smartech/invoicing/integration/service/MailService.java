package com.smartech.invoicing.integration.service;

import java.util.List;

public interface MailService {
	public void sendMail(List<String> targetEmailAddresses, String messageSubject, String messageText);
}
