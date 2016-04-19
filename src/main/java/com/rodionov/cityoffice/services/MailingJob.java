package com.rodionov.cityoffice.services;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.rodionov.cityoffice.model.Document;
import com.rodionov.cityoffice.model.User;

public class MailingJob implements Runnable {

	Logger logger = Logger.getLogger(MailingJob.class);
	
	@Autowired
	private DocumentService documentService;
	
	@Autowired
	private NotificationService notificationService;
	
	@Override
	public void run() {
		logger.info("MAILING");
		
		List<Document> docsToNotifyAbout = documentService.getDocumentsToNotify();
		
		for (Document doc : docsToNotifyAbout) {
			List<User> usersToNotify = documentService.getUsersToNotify(doc);
			
			for (User user : usersToNotify) {
				notificationService.notifyUserAboutDocument(user, doc);
			}
		}
	}

}
