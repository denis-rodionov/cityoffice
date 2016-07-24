package com.rodionov.cityoffice.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mysema.query.BooleanBuilder;
import com.rodionov.cityoffice.model.Document;
import com.rodionov.cityoffice.model.DocumentStatus;
import com.rodionov.cityoffice.model.NotificationSchema;
import com.rodionov.cityoffice.model.Project;
import com.rodionov.cityoffice.model.QDocument;
import com.rodionov.cityoffice.model.User;
import com.rodionov.cityoffice.model.helpers.NotificationHelper;
import com.rodionov.cityoffice.repository.DocumentRepository;
import com.rodionov.cityoffice.repository.NotificationSchemaRepository;
import com.rodionov.cityoffice.repository.ProjectRepository;
import com.rodionov.cityoffice.repository.UserRepository;

@Service
public class DocumentService {
	
	//private static final Logger logger = Logger.getLogger(DocumentService.class);
	
	@Autowired
	private DocumentRepository documentRepository;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private DateService dateService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private NotificationSchemaRepository notificationSchemaRepository;
	
	/**
	 * @param user For document filtering
	 * @return All not done documents
	 */
	public List<Document> getUnfinishedDocuments(User user) {
		return getDocuments(user, Arrays.asList(DocumentStatus.NEW)); 
	}
	
	/**
	 * @return All documents in database
	 */
	public List<Document> getAllDocuments(User user) {
		return getDocuments(user, Arrays.asList(DocumentStatus.values()));
	}
		
	/**
	 * @param user For document filtering by project where user is taking part
	 * @return All documents which match the criterias
	 */
	public List<Document> getDocuments(User user, List<DocumentStatus> statuses) {
		
		List<Document> res = new ArrayList<>();
		for (DocumentStatus s : statuses)
			res.addAll(0, documentRepository.findByStatus(s));		
		
		res = res.stream()
			.filter(d -> isAccessible(d, user))
			.map((Document d) -> {				
				eagerLoadDocument(d);
				return d;
			})
			.collect(Collectors.toList());
		
		return res;
	}
	
	/**
	 * Getting document list filtered by given params
	 * @param projectsIds If the list is empty - documents are not filtered by projects
	 * @param status Is status is null, documents are not filtered by status
	 * @param nameFilter Filtering by substring inclusion. No filter if parameter is null
	 * @return
	 */
	public Page<Document> getFilteredDocuments(
			List<String> projectsIds, 
			DocumentStatus status, 
			String nameFilter,
			String assigneeIdFilter,
			Pageable pageable) {
		
		QDocument docQuery = new QDocument("document"); 
		
		BooleanBuilder where = new BooleanBuilder();
	    if (status != null) {
	        where.and(docQuery.status.eq(status));
	    }
	    
	    if (nameFilter != null) {
	    	where.and(docQuery.name.containsIgnoreCase(nameFilter));
	    }
	    
	    if (projectsIds != null && !projectsIds.isEmpty()) {
	    	where.and(docQuery.projectId.in(projectsIds));
	    }
	    
	    if (assigneeIdFilter != null) {
	    	where.and(docQuery.assigneeId.eq(assigneeIdFilter));
	    }
		
		return documentRepository.findAll(where, pageable);
	}
	

	/**
	 * @return documents which need to be notified about
	 */
	public List<Document> getDocumentsToNotify() {
		
		List<Document> res = new ArrayList<Document>();
		
		List<Document> unfinished = getDocuments(null, Arrays.asList(DocumentStatus.NEW));
		LocalDate today = dateService.getCurrentDate();
		
		unfinished.forEach(d -> {
			List<LocalDate> dates = NotificationHelper.getNotificationDates(d.getNotificationSchema(), d);
			if (dates.stream().anyMatch(date -> date.compareTo(today) == 0))
				res.add(d);					
		});
		
		return res;
	}
	
	private boolean isAccessible(Document document, User user) {
		if (user == null)
			return true;
		
		return user.getProjectIds().contains(document.getProjectId());
	}
	
	/**
	 * Load document and include relation entities
	 * @param d
	 */
	private void eagerLoadDocument(Document d) {
		
		Project proj = projectRepository.findOne(d.getProjectId());
		d.setProject(proj);
		
		if (d.getNotificationSchemaId() != null) {
			NotificationSchema n = notificationSchemaRepository.findOne(d.getNotificationSchemaId());
			d.setNotificationSchema(n);
		}
		
		if (d.getAssigneeId() != null) {
			d.setAssignee(userRepository.findOne(d.getAssigneeId()));
		}
	}
	
	
}
