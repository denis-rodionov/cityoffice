package com.rodionov.cityoffice.services;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.rodionov.cityoffice.model.Document;
import com.rodionov.cityoffice.model.Project;
import com.rodionov.cityoffice.model.User;

@Service
public class MailService {
	
	Logger logger = Logger.getLogger(MailService.class);
	
	@Autowired
	MailSender mailSender;

	public void send(User user, Document doc, Project project) {
		
		logger.info("Sending email to " + user.getEmail());
		
		SimpleMailMessage message = new SimpleMailMessage();
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		long daysToDeadline = Duration.between(doc.getDeadline(), LocalDate.now()).toDays();		
		
		message.setTo(user.getEmail());
		
		message.setSubject("Notification for the project " + project.getName());
		
		message.setText("Dear " + user.getUsername() + ",\n" + 
						"You have notification about '" + doc.getName() + "'\n which has deadline at " +
						doc.getDeadline().format(formatter) + " (in " + daysToDeadline + " days)!");
		
		try {
			mailSender.send(message);
		}
		catch (MailException ex) {
			logger.error(ex);
		}
		
	}

}
