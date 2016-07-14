package com.rodionov.cityoffice.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.rodionov.cityoffice.model.Document;
import com.rodionov.cityoffice.model.Project;
import com.rodionov.cityoffice.model.User;
import com.rodionov.cityoffice.services.mail.DeadlineMailTemplate;

@Service
public class MailService {
	
	Logger logger = Logger.getLogger(MailService.class);
	
	@Autowired
	MailSender mailSender;
	
	@Autowired
	SimpleMailMessage messageTemplate;
	
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
		
		SimpleMailMessage message = new SimpleMailMessage(messageTemplate);
		message.setTo(user.getEmail());		
		message.setSubject(mailTemplate.getMailTheme());
		message.setText(mailTemplate.getMailBody());
		
		logger.info(message);
		mailSender.send(message);		
	}
	
	

}
