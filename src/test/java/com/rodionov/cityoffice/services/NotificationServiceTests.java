package com.rodionov.cityoffice.services;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rodionov.cityoffice.config.RealDatabaseTestConfiguration;
import com.rodionov.cityoffice.model.Document;
import com.rodionov.cityoffice.model.DocumentStatus;
import com.rodionov.cityoffice.model.SentNotification;
import com.rodionov.cityoffice.model.User;
import com.rodionov.cityoffice.repository.SentNotificationRepository;

import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RealDatabaseTestConfiguration.class })
public class NotificationServiceTests {

	@Autowired
	@InjectMocks
	private NotificationService notificationSevice;
	
	@Autowired
	private SentNotificationRepository sentNotificationRepository;
	
	@Mock
	private MailService mailService;
	
	@Mock
	private DateService dateService;
	
	@Before
	public void cleanUp() {
		sentNotificationRepository.deleteAll();
		
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void notifyUserAboutDocumentTest_firstTime() {
		// arrange
		User user = new User("username", "email");
		Document doc = new Document("name", LocalDate.of(2001, 1, 1), DocumentStatus.NEW, "1");
		doc.setId("123");
		
		// act
		notificationSevice.notifyUserAboutDocument(user, doc);
		
		// assert
		verify(mailService, times(1)).send(Mockito.any(), Mockito.any());
	}
	
	@Test
	public void notifyUserAboutDocumentTest_secondTime() {
		// arrange		
		User user = new User("username", "email");
		user.setId("1222");
		Document doc = new Document("name", LocalDate.of(2050, 12, 12), DocumentStatus.NEW, "1");
		doc.setId("123");
		sentNotificationRepository.save( new SentNotification(LocalDate.of(2001, 1, 1),user.getId(), doc.getId(), true));
		when(dateService.getCurrentDate()).thenReturn(LocalDate.of(2001, 1, 1));
		
		// act
		notificationSevice.notifyUserAboutDocument(user, doc);
		
		// assert
		verify(mailService, times(0)).send(Mockito.any(), Mockito.any());
	}
}
