package com.rodionov.cityoffice.services;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.extractProperty;

import java.time.LocalDate;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rodionov.cityoffice.config.RealDatabaseTestConfiguration;
import com.rodionov.cityoffice.model.Document;
import com.rodionov.cityoffice.model.DocumentStatus;
import com.rodionov.cityoffice.model.Project;
import com.rodionov.cityoffice.model.helpers.DocumentHelper;
import com.rodionov.cityoffice.repository.DocumentRepository;
import com.rodionov.cityoffice.repository.ProjectRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RealDatabaseTestConfiguration.class })
public class DocumentServiceTests {
	
/*	@Rule
    public MongoDbRule mongoDbRule = 
    	newMongoDbRule().defaultSpringMongoDb(RealDatabaseTestConfiguration.TEST_DATABASE_NAME);*/

	@Autowired
	private DocumentService documentService;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private DocumentRepository documentRepository;
	
	@Before
	public void cleanup() {
		projectRepository.deleteAll();
		documentRepository.deleteAll();
	}
	
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
