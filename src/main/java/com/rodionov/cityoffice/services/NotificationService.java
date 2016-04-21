package com.rodionov.cityoffice.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rodionov.cityoffice.model.Document;
import com.rodionov.cityoffice.model.SentNotification;
import com.rodionov.cityoffice.model.User;
import com.rodionov.cityoffice.repository.SentNotificationRepository;

@Service
public class NotificationService {
	
	@Autowired
	private SentNotificationRepository sentNotificationRepository;
	
	@Autowired
	private MailService mailService;
	
	/**
	 * Notifies given user about
	 * @param user
	 * @param doc
	 */
	public void notifyUserAboutDocument(User user, Document doc) {
		List<SentNotification> notifications = sentNotificationRepository.findByDocumentId(doc.getId());
		
		if (notifications.stream().filter(n -> n.getUserId() == user.getId()).count() == 0) {
						
			mailService.send(user, doc);
			
			saveNotification(user, doc);
		}
	}

	private void saveNotification(User user, Document doc) {
		SentNotification notification = new SentNotification();
		notification.setUserId(user.getId());
		notification.setDocumentId(doc.getId());
		notification.setByEmail(true);
		
		sentNotificationRepository.save(notification);
	}
}
