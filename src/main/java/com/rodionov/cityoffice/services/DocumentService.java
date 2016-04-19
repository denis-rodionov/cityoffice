package com.rodionov.cityoffice.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rodionov.cityoffice.model.Document;
import com.rodionov.cityoffice.model.DocumentStatus;
import com.rodionov.cityoffice.model.Project;
import com.rodionov.cityoffice.model.User;
import com.rodionov.cityoffice.repository.DocumentRepository;
import com.rodionov.cityoffice.repository.ProjectRepository;

@Service
public class DocumentService {
	
	private static final Logger logger = Logger.getLogger(DocumentService.class);
	
	@Autowired
	private DocumentRepository documentRepository;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private DateService dateService;
	
	/**
	 * @return All not done documents
	 */
	public List<Document> getUnfinishedDocuments() {
		return getDocuments(Arrays.asList( DocumentStatus.NEW)); 
	}
	
	/**
	 * @return All documents in database
	 */
	public List<Document> getAllDocuments() {
		return getDocuments(Arrays.asList(DocumentStatus.values()));
	}
		
	/**
	 * @return All documents which match the criterias
	 */
	public List<Document> getDocuments(List<DocumentStatus> statuses) {
		
		List<Document> res = new ArrayList<>();
		for (DocumentStatus s : statuses)
			res.addAll(0, documentRepository.findByStatus(s));		
		
		res = res.stream()
			.map((Document d) -> {
				Project proj = projectRepository.findOne(d.getProjectId());				
				d.setProject(proj);
				return d;
			})
			.collect(Collectors.toList());
		
		StringBuilder b = new StringBuilder();
		res.forEach(b::append);
		
		logger.debug(b);
		
		return res;
	}
	
	/**
	 * @return documents which need to be nofified about
	 */
	public List<Document> getDocumentsToNotify() {
		List<Document> res = new ArrayList<Document>();
		
		return res;
	}
	
	/**
	 * Finds out which users must be notifyed about the document
	 * @param doc Document to notify about
	 * @return List of users
	 */
	public List<User> getUsersToNotify(Document doc) {
		List<User> res = new ArrayList<User>();
		
		return res;
	}
}
