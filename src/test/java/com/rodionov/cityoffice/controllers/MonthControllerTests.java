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
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rodionov.cityoffice.config.RealDatabaseTestConfiguration;
import com.rodionov.cityoffice.dto.MonthDTO;
import com.rodionov.cityoffice.model.Document;
import com.rodionov.cityoffice.model.DocumentStatus;
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
	}
	
	@Test
	public void getMonthList_monthGroupingTest() {
		// arrange
		List<Document> docs = Arrays.asList(
				DocumentHelper.CreateDocument("1", "1", LocalDate.of(2012, 3, 24), DocumentStatus.NEW),
				DocumentHelper.CreateDocument("2", "1", LocalDate.of(2012, 3, 12), DocumentStatus.NEW),
				DocumentHelper.CreateDocument("3", "1", LocalDate.of(2012, 2, 12), DocumentStatus.NEW)
				);
		when(documentService.getUnfinishedDocuments()).thenReturn(docs);
		when(dateService.getCurentDate()).thenReturn(LocalDate.of(2012, 2,  13));
		
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
		List<Document> docs = Arrays.asList(
				DocumentHelper.CreateDocument("1", "1", LocalDate.of(2012, 3, 24), DocumentStatus.NEW),
				DocumentHelper.CreateDocument("2", "1", LocalDate.of(2012, 3, 12), DocumentStatus.NEW),
				DocumentHelper.CreateDocument("3", "1", LocalDate.of(2012, 2, 12), DocumentStatus.NEW)
				);
		when(documentService.getUnfinishedDocuments()).thenReturn(docs);
		when(dateService.getCurentDate()).thenReturn(LocalDate.of(2012, 3,  1));
		
		// act
		List<MonthDTO> actual = monthController.getMonthList(null, false);
		
		// assert
		assertThat(actual).hasSize(1).describedAs("Only ongoing month must be shown");
		assertThat(actual.get(0).getDocuments()).hasSize(3)
			.describedAs("Because February's documen is unfinished");
	}
	
	@Test
	public void getMonthList_only12MonthsTest() {
		// arrange
		List<Document> docs = Arrays.asList(
				DocumentHelper.CreateDocument("1", "1", LocalDate.of(2012, 3, 24), DocumentStatus.NEW),
				DocumentHelper.CreateDocument("2", "1", LocalDate.of(2013, 3, 12), DocumentStatus.NEW),
				DocumentHelper.CreateDocument("3", "1", LocalDate.of(2013, 2, 12), DocumentStatus.NEW)
				);
		when(documentService.getUnfinishedDocuments()).thenReturn(docs);
		when(dateService.getCurentDate()).thenReturn(LocalDate.of(2012, 3,  1));
		
		// act
		List<MonthDTO> actual = monthController.getMonthList(null, false);
		
		// assert
		assertThat(actual).hasSize(2).describedAs("March of 2013 must be out of scope");
	}
}
