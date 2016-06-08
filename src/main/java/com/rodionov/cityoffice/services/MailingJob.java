package com.rodionov.cityoffice.services;

import java.time.LocalTime;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rodionov.cityoffice.model.Document;
import com.rodionov.cityoffice.model.User;

@Service
public class MailingJob implements Runnable {

	private final int START_HOUR = 10;
	
	Logger logger = Logger.getLogger(MailingJob.class);
	
	@Autowired
	private DocumentService documentService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private NotificationService notificationService;
	
	@Override
	public void run() {
		try 
		{
			if (LocalTime.now().getHour() < START_HOUR)
				return;
			
			logger.info("Checking documents for notifiction...");
		
			List<Document> docsToNotifyAbout = documentService.getDocumentsToNotify();
			
			for (Document doc : docsToNotifyAbout) {
				
				//logger.info("Notifying about the document " + doc.getName());
				
				List<User> usersToNotify = projectService.getUsersToNotify(doc.getProjectId());
							
				for (User user : usersToNotify) {
					//logger.info("Notifying user " + usersToNotify);
					notificationService.notifyUserAboutDocument(user, doc);
				}
			}			
		}
		catch (Throwable t)
		{
			logger.error("User notification Exception", t);
		}		
	}

}
