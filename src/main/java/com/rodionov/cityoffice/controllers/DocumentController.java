package com.rodionov.cityoffice.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rodionov.cityoffice.CityofficeApplication;
import com.rodionov.cityoffice.dto.DocumentDTO;
import com.rodionov.cityoffice.repository.DocumentRepository;

@RestController

public class DocumentController {
	
	private static final Logger logger = Logger.getLogger(DocumentController.class);
	
	@Autowired
	private DocumentRepository documentRepository;
	
	//@RequestMapping("/home")
	public Map<String, Object> home() {
		
		Map<String,Object> model = new HashMap<String,Object>();
	    model.put("id", UUID.randomUUID().toString());
	    model.put("content", "Hello World");
	    return model;
	}
	
	@RequestMapping("/document")
	public List<DocumentDTO> documentList() {
		logger.debug("/document");
		
		return documentRepository.findAll().stream()
				.map(d -> new DocumentDTO(d.getId(), d.getName(), d.getDeadline()))
				.collect(Collectors.toList());
	}
}
