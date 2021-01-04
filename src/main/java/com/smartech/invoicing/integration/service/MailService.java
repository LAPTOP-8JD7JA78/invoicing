package com.smartech.invoicing.integration.service;

import java.util.List;
import java.util.Map;

public interface MailService {
//	public void sendMail(List<String> targetEmailAddresses, String messageSubject, String messageText);
	public void sendMail(List<String> targetEmailAddresses, String messageSubject, String messageText, Map<String, byte[]> map);
}
