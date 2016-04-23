package com.rodionov.cityoffice.services;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.extractProperty;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rodionov.cityoffice.config.RealDatabaseTestConfiguration;
import com.rodionov.cityoffice.model.Document;
import com.rodionov.cityoffice.model.DocumentStatus;
import com.rodionov.cityoffice.model.Notification;
import com.rodionov.cityoffice.model.NotificationSchema;
import com.rodionov.cityoffice.model.Project;
import com.rodionov.cityoffice.model.helpers.DocumentHelper;
import com.rodionov.cityoffice.model.helpers.NotificationHelper;
import com.rodionov.cityoffice.repository.DocumentRepository;
import com.rodionov.cityoffice.repository.NotificationRepository;
import com.rodionov.cityoffice.repository.NotificationSchemaRepository;
import com.rodionov.cityoffice.repository.ProjectRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RealDatabaseTestConfiguration.class })
public class DocumentServiceTests {
	
/*	@Rule
    public MongoDbRule mongoDbRule = 
    	newMongoDbRule().defaultSpringMongoDb(RealDatabaseTestConfiguration.TEST_DATABASE_NAME);*/

	@Autowired
	@InjectMocks
	private DocumentService documentService;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private DocumentRepository documentRepository;
	
	@Autowired
	private NotificationSchemaRepository notificationSchemaRepository;
	
	@Autowired
	private NotificationRepository notificationRepository;
	
	//@Autowired
	//private UserRepository userRepository;
	
	@Mock
	private DateService dateService;
	
	@Before
	public void cleanup() {
		projectRepository.deleteAll();
		documentRepository.deleteAll();
		notificationSchemaRepository.deleteAll();
		notificationRepository.deleteAll();
		
		MockitoAnnotations.initMocks(this);
	}
	
	// ------------------------- getDocumentsToNotifyTest ----------------------------------
	
	@Test
	public void getDocumentsToNotifyTest_DeadlineNotify() {
		// arrange
		Notification notification = notificationRepository.save(new Notification(0, "0"));		
		NotificationSchema schema = NotificationHelper.create("0", notification);
		notificationSchemaRepository.save(schema);
		documentRepository.save(DocumentHelper.CreateDocument("1", "projectId", LocalDate.of(2000, 1, 1), DocumentStatus.NEW, schema.getId()));
		when(dateService.getCurrentDate()).thenReturn(LocalDate.of(2000, 1, 1));
		
		// act
		List<Document> actual = documentService.getDocumentsToNotify();
		
		// assert
		assertThat(actual).hasSize(1);
	}
	
	@Test
	public void getDocumentsToNotifyTest_AdvanceNotify() {
		// arrange
		Notification notification = notificationRepository.save(new Notification(7, "7"));		
		NotificationSchema schema = NotificationHelper.create("week", notification);		
		notificationSchemaRepository.save(schema);
		documentRepository.save(DocumentHelper.CreateDocument("1", "projectId", LocalDate.of(2000, 1, 8), DocumentStatus.NEW, schema.getId()));
		when(dateService.getCurrentDate()).thenReturn(LocalDate.of(2000, 1, 1));
		
		// act
		List<Document> actual = documentService.getDocumentsToNotify();
		
		// assert
		assertThat(actual).hasSize(1);
	}
	
	@Test
	public void getDocumentsToNotifyTest_NotNotifyFinished() {
		// arrange
		Notification notification = notificationRepository.save(new Notification(0, "0"));		
		NotificationSchema schema = NotificationHelper.create("0", notification);
		notificationSchemaRepository.save(schema);
		documentRepository.save(DocumentHelper.CreateDocument("1", "projectId", LocalDate.of(2000, 1, 1), DocumentStatus.FINISHED, schema.getId()));
		when(dateService.getCurrentDate()).thenReturn(LocalDate.of(2000, 1, 1));
		
		// act
		List<Document> actual = documentService.getDocumentsToNotify();
		
		// assert
		assertThat(actual).hasSize(0);
	}	
	
	@Test
	public void getDocumentsToNotifyTest_IncludeProject() {
		// arrange
		Notification notification = notificationRepository.save(new Notification(0, "0"));		
		NotificationSchema schema = NotificationHelper.create("0", notification);
		Project prj = projectRepository.save(new Project("A Project", true, "default"));
		notificationSchemaRepository.save(schema);
		documentRepository.save(DocumentHelper.CreateDocument("1", prj.getId(), LocalDate.of(2000, 1, 1), DocumentStatus.NEW, schema.getId()));
		when(dateService.getCurrentDate()).thenReturn(LocalDate.of(2000, 1, 1));
		
		// act
		List<Document> actual = documentService.getDocumentsToNotify();
		
		// assert
		assertThat(actual).hasSize(1);
		assertThat(actual.get(0).getProject()).isNotNull();
	}	
	
	// ------------------------- getUnfinishedDocumentsTest  ----------------------------------
	
	@Test
	public void getUnfinishedDocumentsTest() {
		// arrange
		Project prj = projectRepository.save(new Project("ProjectName", true, "primary"));
		documentRepository.save(DocumentHelper.CreateDocument(
				"1", prj.getId(), LocalDate.of(1999, 1, 1), DocumentStatus.FINISHED));
		documentRepository.save(DocumentHelper.CreateDocument(
				"2",  prj.getId(),  LocalDate.of(2050, 1, 1), DocumentStatus.FINISHED));
		Document unfinished = documentRepository.save(DocumentHelper.CreateDocument(
				"3",  prj.getId(),  LocalDate.of(2050, 1, 1), DocumentStatus.NEW));
		
		// act
		List<Document> actual = documentService.getUnfinishedDocuments();
		
		// assert
		assertThat(actual).hasSize(1);
		assertThat(extractProperty("id", String.class).from(actual).contains(unfinished.getId()));						  
	}
	
	@Test
	public void getUnfinishedDocuments_getListTest() {
		// arrange
		Project prj = projectRepository.save(new Project("ProjectName", true, "primary"));
		documentRepository.save(DocumentHelper.CreateDocument(
				"1", prj.getId(), LocalDate.of(1999, 1, 1), DocumentStatus.NEW));
		documentRepository.save(DocumentHelper.CreateDocument(
				"2",  prj.getId(),  LocalDate.of(2050, 1, 1), DocumentStatus.NEW));
		documentRepository.save(DocumentHelper.CreateDocument(
				"3",  prj.getId(),  LocalDate.of(2050, 1, 1), DocumentStatus.NEW));
		
		// act
		List<Document> actual = documentService.getUnfinishedDocuments();
		
		// assert
		assertThat(actual).hasSize(3);
	}
	
	@Test
	public void getUnfinishedDocuments_includeProjectTest() {
		// arrange
		Project prj = projectRepository.save(new Project("ProjectName", true, "primary"));
		documentRepository.save(DocumentHelper.CreateDocument(
				"1", prj.getId(), LocalDate.of(1999, 1, 1), DocumentStatus.NEW));
		
		// act
		Document actual = documentService.getUnfinishedDocuments().get(0);
		
		// assert
		assertThat(actual.getProject()).isNotNull();
	}
	
	// ------------------------- getAllDocuments  ----------------------------------
	
	@Test
	public void getAllDocuments_getListTest() {
		// arrange
		Project prj = projectRepository.save(new Project("ProjectName", true, "primary"));
		documentRepository.save(DocumentHelper.CreateDocument(
				"1", prj.getId(), LocalDate.of(1999, 1, 1), DocumentStatus.NEW));
		documentRepository.save(DocumentHelper.CreateDocument(
				"2",  prj.getId(),  LocalDate.of(2050, 1, 1), DocumentStatus.FINISHED));
		documentRepository.save(DocumentHelper.CreateDocument(
				"3",  prj.getId(),  LocalDate.of(2050, 1, 1), DocumentStatus.NEW));
		
		// act
		List<Document> actual = documentService.getAllDocuments();
		
		// assert
		assertThat(actual).hasSize(3);
	}
	
	@Test
	public void getAllDocuments_includeProjectTest() {
		// arrange
		Project prj = projectRepository.save(new Project("ProjectName", true, "primary"));
		documentRepository.save(DocumentHelper.CreateDocument(
				"1", prj.getId(), LocalDate.of(1999, 1, 1), DocumentStatus.NEW));
		
		// act
		Document actual = documentService.getAllDocuments().get(0);
		
		// assert
		assertThat(actual.getProject()).isNotNull();
	}
}
