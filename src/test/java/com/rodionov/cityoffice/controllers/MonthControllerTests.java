package com.rodionov.cityoffice.controllers;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rodionov.cityoffice.config.RealDatabaseTestConfiguration;
import com.rodionov.cityoffice.dto.MonthDTO;
import com.rodionov.cityoffice.model.Document;
import com.rodionov.cityoffice.model.DocumentStatus;
import com.rodionov.cityoffice.model.Project;
import com.rodionov.cityoffice.model.helpers.DocumentHelper;
import com.rodionov.cityoffice.services.DateService;
import com.rodionov.cityoffice.services.DocumentService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RealDatabaseTestConfiguration.class })
public class MonthControllerTests {
	@InjectMocks
	private MonthController monthController;
	
	@Mock
	private DocumentService documentService;
	
	@Mock
	private DateService dateService;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		when(dateService.isPastMonth(Mockito.any())).thenCallRealMethod();
		when(dateService.isPresentMonth(Mockito.any())).thenCallRealMethod();
	}
	
	@Test
	public void getMonthList_monthGroupingTest() {
		// arrange
		Project prj = new Project("Project1", true, "123");
		List<Document> docs = Arrays.asList(
				new Document("1", "1", LocalDate.of(2012, 3, 24), DocumentStatus.NEW, "1", prj),
				new Document("2", "2", LocalDate.of(2012, 3, 12), DocumentStatus.NEW, "1", prj),
				new Document("3", "3", LocalDate.of(2012, 2, 12), DocumentStatus.NEW, "1", prj)
				);
		when(documentService.getUnfinishedDocuments()).thenReturn(docs);
		when(dateService.getCurrentDate()).thenReturn(LocalDate.of(2012, 2,  13));
		
		// act
		List<MonthDTO> actual = monthController.getMonthList(null, false);
		
		// assert
		MonthDTO march = actual.get(1);
		assertThat(actual).hasSize(2);
		assertThat(march.getMonthNumber()).isEqualTo(3);
		assertThat(march.getDocuments()).hasSize(2);
	}
	
	@Test
	public void getMonthList_uninishedDocumentsTest() {
		// arrange
		Project prj = new Project("Project1", true, "123");
		List<Document> docs = Arrays.asList(
				new Document("1", "1", LocalDate.of(2012, 3, 24), DocumentStatus.NEW, "1", prj),
				new Document("2", "2", LocalDate.of(2012, 3, 12), DocumentStatus.NEW, "1", prj),
				new Document("3", "3", LocalDate.of(2012, 2, 12), DocumentStatus.NEW, "1", prj)
				);
		when(documentService.getUnfinishedDocuments()).thenReturn(docs);
		when(dateService.getCurrentDate()).thenReturn(LocalDate.of(2012, 3,  1));
		
		// act
		List<MonthDTO> actual = monthController.getMonthList(null, false);
		
		// assert
		assertThat(actual).hasSize(1).describedAs("Only ongoing months must be shown");
		assertThat(actual.get(0).getDocuments()).hasSize(3)
			.describedAs("Because February's documen is unfinished");
	}
	
	@Test
	public void getMonthList_uninishedDocuments2Test() {
		// arrange
		Project prj = new Project("Project1", true, "123");
		List<Document> docs = Arrays.asList(
				new Document("1", "1", LocalDate.of(2012, 4, 24), DocumentStatus.NEW, "1", prj),
				new Document("2", "2", LocalDate.of(2012, 4, 12), DocumentStatus.NEW, "1", prj),
				new Document("3", "3", LocalDate.of(2012, 2, 12), DocumentStatus.NEW, "1", prj)
				);
		when(documentService.getUnfinishedDocuments()).thenReturn(docs);
		when(dateService.getCurrentDate()).thenReturn(LocalDate.of(2012, 3,  1));		
		
		// act
		List<MonthDTO> actual = monthController.getMonthList(null, false);		
		
		// assert
		assertThat(actual).hasSize(2).describedAs("Only ongoing months must be shown: March (doc from the February) and April");
		assertThat(actual.get(0).getMonthNumber()).isEqualTo(3);
		assertThat(actual.get(0).getDocuments()).hasSize(1)
			.describedAs("Because February's document is unfinished and it is moved to the current month");
	}
	
	@Test
	public void getMonthList_only12MonthsTest() {
		// arrange
		Project prj = new Project("Project1", true, "123");
		List<Document> docs = Arrays.asList(
				new Document("1", "1", LocalDate.of(2012, 3, 24), DocumentStatus.NEW, "1", prj),
				new Document("2", "2", LocalDate.of(2013, 3, 12), DocumentStatus.NEW, "1", prj),
				new Document("3", "3", LocalDate.of(2013, 2, 28), DocumentStatus.NEW, "1", prj)
				);
		when(documentService.getUnfinishedDocuments()).thenReturn(docs);
		when(dateService.getCurrentDate()).thenReturn(LocalDate.of(2012, 3,  30));
		
		// act
		List<MonthDTO> actual = monthController.getMonthList(null, false);
		
		// assert
		assertThat(actual).hasSize(2).describedAs("March of 2013 must be out of scope");
	}
	
	@Test
	public void getMonthList_showCurrentFinishedDocuments() {
		// arrange
				Project prj = new Project("Project1", true, "123");
				List<Document> docs = Arrays.asList(
						new Document("1", "1", LocalDate.of(2012, 3, 24), DocumentStatus.FINISHED, "1", prj),
						new Document("2", "2", LocalDate.of(2012, 3, 12), DocumentStatus.FINISHED, "1", prj),
						new Document("3", "3", LocalDate.of(2012, 4, 12), DocumentStatus.FINISHED, "1", prj)
						);
				when(documentService.getUnfinishedDocuments()).thenReturn(docs);
				when(dateService.getCurrentDate()).thenReturn(LocalDate.of(2012, 3,  20));
				
				// act
				List<MonthDTO> actual = monthController.getMonthList(null, false);
				
				// assert
				assertThat(actual).hasSize(2);
				assertThat(actual.get(0).getDocuments()).hasSize(2)
					.describedAs("All current and future finished documents must show");
				assertThat(actual.get(1).getDocuments()).hasSize(1)
					.describedAs("All current and future finished documents must show");
	}
	
	@Test
	public void getMonthList_doNotShowPastFinished() {
		// arrange
				Project prj = new Project("Project1", true, "123");
				List<Document> docs = Arrays.asList(
						new Document("1", "1", LocalDate.of(2012, 2, 24), DocumentStatus.FINISHED, "1", prj),
						new Document("2", "2", LocalDate.of(2012, 2, 12), DocumentStatus.FINISHED, "1", prj),
						new Document("3", "3", LocalDate.of(2012, 3, 12), DocumentStatus.FINISHED, "1", prj)
						);
				when(documentService.getUnfinishedDocuments()).thenReturn(docs);
				when(dateService.getCurrentDate()).thenReturn(LocalDate.of(2012, 3,  1));
				
				// act
				List<MonthDTO> actual = monthController.getMonthList(null, false);
				
				// assert
				assertThat(actual).hasSize(1).describedAs("Only March must be shown");
				assertThat(actual.get(0).getDocuments().get(0).getId()).isEqualTo("3");
	}
}
