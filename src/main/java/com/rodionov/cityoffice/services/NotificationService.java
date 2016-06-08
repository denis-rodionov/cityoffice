package com.rodionov.cityoffice.services;

import java.time.LocalDate;
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
	
	@Autowired
	private DateService dateService;
	
	/**
	 * Notifies given user about
	 * @param user
	 * @param doc
	 */
	public void notifyUserAboutDocument(User user, Document doc) {
				
		if (!isNotificationHappened(user, doc)) {
						
			mailService.send(user, doc, doc.getProject());
			
			saveNotification(user, doc);
		}
	}
	
	private boolean isNotificationHappened(User user, Document doc) {
		List<SentNotification> notifications = sentNotificationRepository.findByDocumentId(doc.getId());
		
		LocalDate today = dateService.getCurrentDate();
		String userId = user.getId();
		boolean res = notifications
				.stream()
				.anyMatch(n -> n.getUserId().compareTo(userId) == 0 && n.getDate().compareTo(today) == 0);
		
		return res;
	}

	private void saveNotification(User user, Document doc) {
		SentNotification notification = new SentNotification();
		notification.setUserId(user.getId());
		notification.setDocumentId(doc.getId());
		notification.setByEmail(true);
		notification.setDate(dateService.getCurrentDate());
		
		sentNotificationRepository.save(notification);
	}
}
