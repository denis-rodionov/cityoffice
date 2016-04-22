package com.rodionov.cityoffice.services;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rodionov.cityoffice.model.Document;
import com.rodionov.cityoffice.model.User;
import com.rodionov.cityoffice.repository.DocumentRepository;

@Service
public class MailingJob implements Runnable {

	Logger logger = Logger.getLogger(MailingJob.class);
	
	@Autowired
	private DocumentService documentService;
	
	@Autowired
	private DocumentRepository documentRepository;
	
	@Autowired
	private NotificationService notificationService;
	
	@Override
	public void run() {
		try 
		{
			logger.info("Checking documents for notifiction...");
		
			List<Document> docsToNotifyAbout = documentService.getDocumentsToNotify();
			
			for (Document doc : docsToNotifyAbout) {
				
				//logger.info("Notifying about the document " + doc.getName());
				
				List<User> usersToNotify = documentService.getUsersToNotify(doc);
							
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
