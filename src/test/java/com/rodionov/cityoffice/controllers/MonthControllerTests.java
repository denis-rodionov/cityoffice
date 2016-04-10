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
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.rodionov.cityoffice.CityofficeApplication;
import com.rodionov.cityoffice.config.RealDatabaseTestConfiguration;
import com.rodionov.cityoffice.dto.MonthDTO;
import com.rodionov.cityoffice.model.Document;
import com.rodionov.cityoffice.model.DocumentStatus;
import com.rodionov.cityoffice.model.helpers.DocumentHelper;
import com.rodionov.cityoffice.services.DocumentService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RealDatabaseTestConfiguration.class })
public class MonthControllerTests {
	@InjectMocks
	private MonthController monthController;
	
	@Mock
	private DocumentService documentService;
	
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
		
		// act
		List<MonthDTO> actual = monthController.getMonthList(null, false);
		
		// assert
		assertThat(actual).hasSize(2);
	}
}
