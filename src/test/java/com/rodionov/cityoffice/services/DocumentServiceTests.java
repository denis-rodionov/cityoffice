package com.rodionov.cityoffice.services;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.extractProperty;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rodionov.cityoffice.config.RealDatabaseTestConfiguration;
import com.rodionov.cityoffice.model.Document;
import com.rodionov.cityoffice.model.DocumentStatus;
import com.rodionov.cityoffice.model.Notification;
import com.rodionov.cityoffice.model.NotificationSchema;
import com.rodionov.cityoffice.model.Project;
import com.rodionov.cityoffice.model.User;
import com.rodionov.cityoffice.model.helpers.DocumentHelper;
import com.rodionov.cityoffice.model.helpers.NotificationHelper;
import com.rodionov.cityoffice.repository.DocumentRepository;
import com.rodionov.cityoffice.repository.NotificationRepository;
import com.rodionov.cityoffice.repository.NotificationSchemaRepository;
import com.rodionov.cityoffice.repository.ProjectRepository;
import com.rodionov.cityoffice.repository.UserRepository;

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
	
	@Autowired
	private UserRepository userRepository;
	
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
		List<Document> actual = documentService.getUnfinishedDocuments(null);
		
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
		List<Document> actual = documentService.getUnfinishedDocuments(null);
		
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
		Document actual = documentService.getUnfinishedDocuments(null).get(0);
		
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
		List<Document> actual = documentService.getAllDocuments(null);
		
		// assert
		assertThat(actual).hasSize(3);
	}
	
	@Test
	public void getAllDocuments_getByUserTest() {
		// arrange
		Project prj1 = projectRepository.save(new Project("1", true, "primary"));
		Project prj2 = projectRepository.save(new Project("2", true, "primary"));
		User user = userRepository.save(new User("1", "name", "email", Arrays.asList(prj1.getId())));
		
		documentRepository.save(DocumentHelper.CreateDocument(
				"1", prj1.getId(), LocalDate.of(1999, 1, 1), DocumentStatus.NEW));
		documentRepository.save(DocumentHelper.CreateDocument(
				"2",  prj2.getId(),  LocalDate.of(2050, 1, 1), DocumentStatus.NEW));
		documentRepository.save(DocumentHelper.CreateDocument(
				"3",  prj2.getId(),  LocalDate.of(2050, 1, 1), DocumentStatus.NEW));
		
		// act
		List<Document> actual = documentService.getAllDocuments(user);
		
		// assert
		assertThat(actual).hasSize(1);
		assertThat(actual.get(0).getName()).isEqualTo("1");
	}
	
	@Test
	public void getAllDocuments_includeProjectTest() {
		// arrange
		Project prj = projectRepository.save(new Project("ProjectName", true, "primary"));
		documentRepository.save(DocumentHelper.CreateDocument(
				"1", prj.getId(), LocalDate.of(1999, 1, 1), DocumentStatus.NEW));
		
		// act
		Document actual = documentService.getAllDocuments(null).get(0);
		
		// assert
		assertThat(actual.getProject()).isNotNull();
	}
	
	@Test
	public void getAllDocuments_getByResponsibleUser() {
		// arrange
		Project prj1 = projectRepository.save(new Project("1", true, "primary"));
		Project prj2 = projectRepository.save(new Project("2", true, "primary"));
		User user = userRepository.save(new User("1", "name", "email", Arrays.asList()));
		
		documentRepository.save(DocumentHelper.CreateDocument(
				"1", prj1.getId(), LocalDate.of(1999, 1, 1), DocumentStatus.NEW));
		documentRepository.save(DocumentHelper.CreateDocument(
				"2",  prj2.getId(),  LocalDate.of(2050, 1, 1), DocumentStatus.NEW));
		documentRepository.save(DocumentHelper.CreateDocument(
				"3",  prj2.getId(),  LocalDate.of(2050, 1, 1), DocumentStatus.NEW));
		
		documentRepository.save(new Document("4", "Specification", LocalDate.now(), DocumentStatus.NEW, prj1.getId(), null, user.getId()));
		
		// act
		List<Document> actual = documentService.getAllDocuments(user);
		
		// assert
		assertThat(actual).hasSize(1);
		assertThat(actual.get(0).getId()).isEqualTo("4");
	}
	
	// ------------------------- getAllDocuments  ----------------------------------
	
	@Test
	public void getFilteredDocuments_noFilters() {
		// arrange
		LocalDate deadline = LocalDate.now();
		String project1 = "project #1";
		String schema1 = "schema #1";
		String assignee1 = "assignee #1";
		String project2 = "project #2";
		String schema2 = "schema #2";
		String assignee2 = "assignee #2";
		documentRepository.save(new Document("1", "Specification", deadline, DocumentStatus.NEW, project1, schema1, assignee1));
		documentRepository.save(new Document("2", "Technical Specification", deadline, DocumentStatus.FINISHED, project2, schema2, assignee2));
		Pageable pageable = new PageRequest(0, 100);
		
		// act
		Page<Document> actual = documentService.getFilteredDocuments(new ArrayList<String>(), null, null, null, pageable);
		
		// assert
		assertThat(actual.getContent()).hasSize(2);
	}
	
	@Test
	public void getFilteredDocuments_OnlyProjectFilter() {
		// arrange
		LocalDate deadline = LocalDate.now();
		String project1 = "project #1";
		String schema1 = "schema #1";
		String assignee1 = "assignee #1";
		String project2 = "project #2";
		String schema2 = "schema #2";
		String assignee2 = "assignee #2";
		documentRepository.save(new Document("1", "Specification", deadline, DocumentStatus.NEW, project1, schema1, assignee1));
		documentRepository.save(new Document("2", "Technical Specification", deadline, DocumentStatus.FINISHED, project2, schema2, assignee2));
		Pageable pageable = new PageRequest(0, 100);
		
		// act
		Page<Document> actual = documentService.getFilteredDocuments(Arrays.asList(project2), null, null, null, pageable);
		
		// assert
		assertThat(actual.getContent()).hasSize(1);
		assertThat(actual.getContent().get(0).getId()).isEqualTo("2");
	}
	
	@Test
	public void getFilteredDocuments_StatusFilter() {
		// arrange
		LocalDate deadline = LocalDate.now();
		String project1 = "project #1";
		String schema1 = "schema #1";
		String assignee1 = "assignee #1";
		String project2 = "project #2";
		String schema2 = "schema #2";
		String assignee2 = "assignee #2";
		documentRepository.save(new Document("1", "Specification", deadline, DocumentStatus.NEW, project1, schema1, assignee1));
		documentRepository.save(new Document("2", "Technical Specification", deadline, DocumentStatus.FINISHED, project2, schema2, assignee2));
		Pageable pageable = new PageRequest(0, 100);
		
		// act
		Page<Document> actual = documentService.getFilteredDocuments(Arrays.asList(), DocumentStatus.NEW, null, null, pageable);
		
		// assert
		assertThat(actual.getContent()).hasSize(1);
		assertThat(actual.getContent().get(0).getId()).isEqualTo("1");
	}
	
	@Test
	public void getFilteredDocuments_NameFilter1() {
		// arrange
		LocalDate deadline = LocalDate.now();
		String project1 = "project #1";
		String schema1 = "schema #1";
		String assignee1 = "assignee #1";
		String project2 = "project #2";
		String schema2 = "schema #2";
		String assignee2 = "assignee #2";
		documentRepository.save(new Document("1", "Specification", deadline, DocumentStatus.NEW, project1, schema1, assignee1));
		documentRepository.save(new Document("2", "Technical Specification", deadline, DocumentStatus.FINISHED, project2, schema2, assignee2));
		Pageable pageable = new PageRequest(0, 100);
		
		// act
		Page<Document> actual = documentService.getFilteredDocuments(Arrays.asList(), null, "Spec", null, pageable);
		
		// assert
		assertThat(actual.getContent()).hasSize(2);
	}
	
	@Test
	public void getFilteredDocuments_NameFilter2() {
		// arrange
		LocalDate deadline = LocalDate.now();
		String project1 = "project #1";
		String schema1 = "schema #1";
		String assignee1 = "assignee #1";
		String project2 = "project #2";
		String schema2 = "schema #2";
		String assignee2 = "assignee #2";
		documentRepository.save(new Document("1", "Specification", deadline, DocumentStatus.NEW, project1, schema1, assignee1));
		documentRepository.save(new Document("2", "Technical Specification", deadline, DocumentStatus.FINISHED, project2, schema2, assignee2));
		Pageable pageable = new PageRequest(0, 100);
		
		// act
		Page<Document> actual = documentService.getFilteredDocuments(Arrays.asList(), null, "Technical Specification", null, pageable);
		
		// assert
		assertThat(actual.getContent()).hasSize(1);
		assertThat(actual.getContent().get(0).getId()).isEqualTo("2");
	}
	
	@Test
	public void getFilteredDocuments_NameAnyCase() {
		// arrange
		LocalDate deadline = LocalDate.now();
		String project1 = "project #1";
		String schema1 = "schema #1";
		String assignee1 = "assignee #1";
		String project2 = "project #2";
		String schema2 = "schema #2";
		String assignee2 = "assignee #2";
		documentRepository.save(new Document("1", "Specification", deadline, DocumentStatus.NEW, project1, schema1, assignee1));
		documentRepository.save(new Document("2", "Technical Specification", deadline, DocumentStatus.FINISHED, project2, schema2, assignee2));
		Pageable pageable = new PageRequest(0, 100);
		
		// act
		Page<Document> actual = documentService.getFilteredDocuments(Arrays.asList(), null, "technical Specification", null, pageable);
		
		// assert
		assertThat(actual.getContent()).hasSize(1);
		assertThat(actual.getContent().get(0).getId()).isEqualTo("2");
	}
	
	@Test
	public void getFilteredDocuments_NameAnyCase2() {
		// arrange
		LocalDate deadline = LocalDate.now();
		String project1 = "project #1";
		String schema1 = "schema #1";
		String assignee1 = "assignee #1";
		String project2 = "project #2";
		String schema2 = "schema #2";
		String assignee2 = "assignee #2";
		documentRepository.save(new Document("1", "Specification", deadline, DocumentStatus.NEW, project1, schema1, assignee1));
		documentRepository.save(new Document("2", "technical Specification", deadline, DocumentStatus.FINISHED, project2, schema2, assignee2));
		Pageable pageable = new PageRequest(0, 100);
		
		// act
		Page<Document> actual = documentService.getFilteredDocuments(Arrays.asList(), null, "Technical Specification", null, pageable);
		
		// assert
		assertThat(actual.getContent()).hasSize(1);
		assertThat(actual.getContent().get(0).getId()).isEqualTo("2");
	}
	
	@Test
	public void getFilteredDocuments_AssigneeFilter() {
		// arrange
		LocalDate deadline = LocalDate.now();
		String project1 = "project #1";
		String schema1 = "schema #1";
		String assignee1 = "assignee #1";
		String project2 = "project #2";
		String schema2 = "schema #2";
		String assignee2 = "assignee #2";
		documentRepository.save(new Document("1", "Specification", deadline, DocumentStatus.NEW, project1, schema1, assignee1));
		documentRepository.save(new Document("2", "Technical Specification", deadline, DocumentStatus.FINISHED, project2, schema2, assignee2));		
		Pageable pageable = new PageRequest(0, 100);
		
		// act
		Page<Document> actual = documentService.getFilteredDocuments(Arrays.asList(), null, null, assignee1, pageable);
		// assert
		assertThat(actual.getContent()).hasSize(1);
		assertThat(actual.getContent().get(0).getId()).isEqualTo("1");
	}
	
	
}
