package com.rodionov.cityoffice.controllers;

import java.security.Principal;
import java.time.LocalDate;
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
import com.rodionov.cityoffice.services.DocumentService;

@RestController
public class MonthController {
	
	private static final Logger logger = Logger.getLogger(MonthController.class);
	
	@Autowired
	private DocumentService documentService;
	
	@RequestMapping("/month")
	public List<MonthDTO> getMonthList(
			Principal principal,
			@RequestParam(value = "includePast", required = false) Boolean includePast) {
		
		logger.info("REST: '/month' with includePast=" + includePast);
		
		if (includePast == null)
			includePast = false;
		
		Map<String, List<Document>> groupedDocs = documentService.getUnfinishedDocuments()
				.stream()
				.collect(Collectors.groupingBy(Document::getSortableMonth));
				
		List<MonthDTO> res = new ArrayList<MonthDTO>();
		for (Map.Entry<String, List<Document>> entry : groupedDocs.entrySet()) {
			
			LocalDate monthDate = entry.getValue().get(0).getDeadline();
			
			List<DocumentDTO> docs = entry.getValue().stream()
					.map(d ->  {
						return new DocumentDTO(
								d.getId(), 
								d.getName(), 
								d.getDeadline(),
								d.getProject().getName(),
								false,
								d.getProject().getColorName());
					})
					.collect(Collectors.toList());
			
			MonthDTO newMonth = new MonthDTO(monthDate, docs);
			res.add(newMonth);
		}
		
		logger.debug(res);
		
		Collections.sort(res, (a, b) -> Integer.compare(a.getMonthNumber(), b.getMonthNumber()));
		
		return res;
	}
}
