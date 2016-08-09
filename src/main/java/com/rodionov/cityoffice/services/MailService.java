package com.rodionov.cityoffice.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rodionov.cityoffice.model.Document;
import com.rodionov.cityoffice.model.Project;
import com.rodionov.cityoffice.model.User;
import com.rodionov.cityoffice.services.mail.DeadlineMailTemplate;

@Service
public class MailService {
	
	Logger logger = Logger.getLogger(MailService.class);
	
//	@Autowired
//	Transport transport;
	
	@Autowired
	MimeMessage mimeMessage;
	
	@Autowired
	DeadlineMailTemplate mailTemplate;

	public void send(User user, Document doc, Project project) {
		
		logger.info("Sending email to " + user.getEmail());

		mailTemplate.setAddressee(user.getUsername());
		mailTemplate.setDocumentName(doc.getName());
		mailTemplate.setDaysToDeadline(ChronoUnit.DAYS.between(LocalDate.now(), doc.getDeadline()));
		mailTemplate.setDeadline(doc.getDeadline());
		mailTemplate.setProjectName(project.getName());		
		if (doc.getAssignee() != null)
			mailTemplate.setAssignee(doc.getAssignee().getUsername());					
		
		sendMessage(mailTemplate, user.getEmail());		
	}
	
	private void sendMessage(DeadlineMailTemplate template, String email) {
		try {		
			mimeMessage.setRecipient(RecipientType.TO, new InternetAddress(email));		
			mimeMessage.setSubject(template.getMailTheme());
			
			Multipart multipart = new MimeMultipart( "alternative" );

		    //MimeBodyPart textPart = new MimeBodyPart();
		    //textPart.setText( template.getMailBody(), "utf-8" );

		    MimeBodyPart htmlPart = new MimeBodyPart();
		    htmlPart.setContent( template.getMailBody(), template.getContentType() );

		    //multipart.addBodyPart( textPart );
		    multipart.addBodyPart( htmlPart );

		    mimeMessage.setContent(multipart);
			
		    //mimeMessage.saveChanges();
			
			Transport.send(mimeMessage);
			logger.info(template.getMailBody());
		} catch (MessagingException e) {
			logger.error("Fail sending email", e);
			throw new RuntimeException(e);
		}
	}
	
	

}
