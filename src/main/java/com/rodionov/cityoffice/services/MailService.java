package com.rodionov.cityoffice.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
	
	@Autowired
	SimpleMailMessage messageTemplate;

	public void send(User user, Document doc, Project project) {
		
		logger.info("Sending email to " + user.getEmail());
		
		SimpleMailMessage message = new SimpleMailMessage(messageTemplate);
				
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		
		long daysToDeadline = ChronoUnit.DAYS.between(LocalDate.now(), doc.getDeadline());
				
		message.setTo(user.getEmail());
		
		message.setSubject("Deadline in " + project.getName());
		
		String assignee = doc.getAssignee() != null ? "\nResponsible: " + doc.getAssignee().getUsername() : "";
		message.setText("Dear " + user.getUsername() + ",\n" + 
						"You have notification about '" + doc.getName() + "' which has deadline " + formatedDays(daysToDeadline) + " (" +
						doc.getDeadline().format(formatter) + ")\nProject: " + project.getName() + 
						assignee +
						"\n\nMore details on http://cityoffice.loc");
		
		logger.info(message);
		mailSender.send(message);		
	}
	
	public String formatedDays(long daysTo) {
		if (daysTo == 0)
			return "today";
		else if (daysTo == 1)
			return "tomorrow";
		else if (daysTo == 2)
			return "in the day after tomorrow";
		else
			return "in " + daysTo + " days";
	}

}
