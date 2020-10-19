package com.smartech.invoicing.integration.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.stereotype.Service;

@Service("mailService")
public class MailServiceImpl implements MailService{

	@Override
	public void sendMail(List<String> targetEmailAddresses, String messageSubject, String messageText) {
		 try {        	
	        	if(targetEmailAddresses != null && !targetEmailAddresses.isEmpty()) {
	        		InputStream input = MailServiceImpl.class.getClassLoader().getResourceAsStream("app.properties");
	        		Properties prop = new Properties();
	        		prop.load(input);

	                Properties props = System.getProperties();
	                props.put("mail.smtp.host", prop.getProperty("mail.smtp.host"));	//Servidor SMTP
	                props.put("mail.smtp.user", prop.getProperty("mail.smtp.user"));	//Usuario
	                props.put("mail.smtp.clave", prop.getProperty("mail.smtp.clave"));	//Clave de la cuenta
	                props.put("mail.smtp.auth", prop.getProperty("mail.smtp.auth"));	//Usar autenticaci�n mediante usuario y clave
	                props.put("mail.smtp.starttls.enable", prop.getProperty("mail.smtp.starttls.enable"));	//Para conectar de manera segura al servidor SMTP
	                props.put("mail.smtp.port", prop.getProperty("mail.smtp.port"));	//Puerto SMTP seguro

	                Session session = Session.getDefaultInstance(props);
	                MimeMessage message = new MimeMessage(session);
	                Multipart multipart = new MimeMultipart();

		        	MimeBodyPart messageBodyPart = new MimeBodyPart();				        	
		        	messageBodyPart.setContent(messageText, "text/html");
		        	multipart.addBodyPart(messageBodyPart);

	            	for(String targetAddress : targetEmailAddresses) {
	            		message.addRecipient(Message.RecipientType.TO, new InternetAddress(targetAddress));
	            	}
	            	
	                message.setFrom(new InternetAddress(prop.getProperty("mail.smtp.user")));
	                message.setSubject(messageSubject);
	                message.setContent(multipart);
	                
	                Transport transport = session.getTransport("smtp");
	                transport.connect(prop.getProperty("mail.smtp.host"), prop.getProperty("mail.smtp.user"), prop.getProperty("mail.smtp.clave"));
	                transport.sendMessage(message, message.getAllRecipients());
	                transport.close();
	        	}
	        }
	        catch (MessagingException me) {
	            me.printStackTrace();
	        }
	        catch (IOException ioe){
	        	ioe.printStackTrace();
	        }
	        catch (Exception e){
	        	e.printStackTrace();
	        }
	}

}
