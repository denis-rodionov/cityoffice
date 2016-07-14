package com.rodionov.cityoffice.controllers;

import java.security.Principal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rodionov.cityoffice.dto.DocumentDTO;
import com.rodionov.cityoffice.dto.MonthDTO;
import com.rodionov.cityoffice.model.Document;
import com.rodionov.cityoffice.model.DocumentStatus;
import com.rodionov.cityoffice.model.User;
import com.rodionov.cityoffice.services.DateService;
import com.rodionov.cityoffice.services.DocumentService;
import com.rodionov.cityoffice.services.MongoUserDetailsService;

import java.time.temporal.ChronoUnit;

@RestController
public class MonthController {
	
	private static final Logger logger = Logger.getLogger(MonthController.class);
	
	@Autowired
	private DocumentService documentService;
	
	@Autowired
	private DateService dateService;
	
	@Autowired
	private MongoUserDetailsService mongoUserDetailsService;
	
	/**
	 * Getting documents grouped by months
	 * @param principal
	 * @param includePast
	 * @return
	 */
	@RequestMapping("/month")
	public List<MonthDTO> getMonthList(
			Principal principal,
			@RequestParam(value = "includePast", required = false) Boolean includePast) {
		
		logger.info("REST: '/month' with includePast=" + includePast);
		
		if (includePast == null)
			includePast = false;
		
		User user = principal != null ? mongoUserDetailsService.getUserByPrincipal(principal) : null;
		
		Map<String, List<Document>> groupedDocs = documentService.getUnfinishedDocuments(user)
				.stream()
				.filter(this::isYearPerspective)
				.filter(this::isNotPastFinished)
				.collect(Collectors.groupingBy(Document::getSortableMonth));
				
		ArrayList<MonthDTO> res = new ArrayList<MonthDTO>();
		for (Map.Entry<String, List<Document>> entry : groupedDocs.entrySet()) {
			
			LocalDate monthDate = entry.getValue().get(0).getDeadline();
			
			List<DocumentDTO> docs = entry.getValue().stream()
					.sorted((f1, f2) -> (int)ChronoUnit.DAYS.between(f2.getDeadline(), f1.getDeadline()))
					.map(DocumentDTO::of)					
					.collect(Collectors.toList());
							
			MonthDTO newMonth = new MonthDTO(monthDate, docs);
			res.add(newMonth);
		}
		
		removeLastMonths(res);
		
		logger.debug(res);
		
		Collections.sort(res, (a, b) -> Integer.compare(a.getMonthNumber(), b.getMonthNumber()));
		
		return res;
	}
	
	private void removeLastMonths(ArrayList<MonthDTO> months) {
		List<MonthDTO> temp = months.stream()
				.filter(m -> dateService.isPastMonth(m.getMonthStartDate()))
				.collect(Collectors.toList());
		
		System.out.println("LastMonths count: " + temp.size());
		
		List<DocumentDTO> lastMonthsDocuments = months.stream()
				.filter(m -> dateService.isPastMonth(m.getMonthStartDate()))
				.map(m -> m.getDocuments())
				.reduce(new ArrayList<DocumentDTO>(), (a, b) -> {
					a.addAll(b);
					return a; 
				});
		
		if (!lastMonthsDocuments.isEmpty()) {
			
			System.out.println("Docs to move: " + lastMonthsDocuments);
		
			List<MonthDTO> lastMonths = months.stream()
					.filter(m -> dateService.isPastMonth(m.getMonthStartDate()))
					.collect(Collectors.toList());
			
			months.removeAll(lastMonths);			
			
			MonthDTO currentMonth = months.stream().filter(m -> dateService.isPresentMonth(m.getMonthStartDate())).findAny().orElse(null);
						
			if (currentMonth == null) {
				currentMonth = new MonthDTO(dateService.getCurrentDate(), lastMonthsDocuments);
				months.add(currentMonth);
			}
			else {
				currentMonth.getDocuments().addAll(0, lastMonthsDocuments);
			}
		}
	}
	
	private boolean isNotPastFinished(Document document) {
		return !(document.getStatus() == DocumentStatus.FINISHED && isPast(document));
	}
	
	private boolean isPast(Document document) {
		LocalDate date = dateService.getCurrentDate().withDayOfMonth(1);
		return document.getDeadline().compareTo(date) < 0;
	}
	
	private boolean isYearPerspective(Document document) {
		LocalDate limit = dateService.getCurrentDate().withDayOfMonth(1).plusMonths(12);
		
		return document.getDeadline().compareTo(limit) < 0;
	}
}
