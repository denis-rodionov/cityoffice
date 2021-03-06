package com.rodionov.cityoffice.controllers;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.rodionov.cityoffice.controllers.exceptions.NotFoundException;
import com.rodionov.cityoffice.model.Document;
import com.rodionov.cityoffice.model.DocumentStatus;
import com.rodionov.cityoffice.repository.DocumentRepository;
import com.rodionov.cityoffice.services.DocumentService;

@RestController
public class DocumentController extends BaseController<Document> {

	private static final Logger logger = Logger.getLogger(DocumentController.class);
		
	@Autowired
	private DocumentService documentService;
	
	@Autowired
	public DocumentController(DocumentRepository documentRepository) {
		super(documentRepository);
	}
	
	//-------------------Finish the document --------------------------------------------------------
	
	@RequestMapping(value = "/finish/{id}", method = RequestMethod.POST)
	public Document doneDocument(Principal principal, @PathVariable("id") String id) {
		logger.info("Make document with id=" + id + " done");
		
		Document document = repository.findOne(id);
		if (document == null) 
			throw new NotFoundException();
		
		document.setStatus(DocumentStatus.FINISHED);
		document.setAssigneeId(getCurrentUser(principal).getId());
		repository.save(document);
		
		return document;
	}
	
	//-------------------Retrieve All Documents --------------------------------------------------------
    
    @RequestMapping(value = "/document", method = RequestMethod.GET)    
    public ResponseEntity<List<Document>> listAllDocuments(Principal principal,
    		@RequestParam(value="_page", required=false) Integer page, 
    		@RequestParam(value="_perPage", required=false) Integer perPage,
    		@RequestParam(value="_sortField", required=false) String sortField,
    		@RequestParam(value="_sortDir", required=false) String sortDir,
    		@RequestParam(value="status", required=false) String status,
    		@RequestParam(value="name", required=false) String name,
    		@RequestParam(value="project", required=false) String projectId,
    		@RequestParam(value="assignee", required=false) String assigneeId) {
    	
    	
    	Pageable pageable = getPagiable(page, perPage, sortDir, sortField);
    	
    	DocumentStatus searchStatus = status != null ? DocumentStatus.valueOf(status) : null;    	
    	List<String> projects = getAvailableProjects(principal);
    	projects = projectId == null ? projects : Arrays.asList(projectId);
    	
    	Page<Document> docs = documentService.getFilteredDocuments(projects, searchStatus, name, assigneeId, pageable);
    	
        return new ResponseEntity<>(docs.getContent(), generatePaginationHeaders(docs, ""), HttpStatus.OK);
    }
    
  //-------------------Retrieve Single Document--------------------------------------------------------
    
    @RequestMapping(value = "/document/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Document> getDocument(@PathVariable("id") String id) {
    	System.out.println("Fetching Document with id " + id);        
        return getEntity(id);
    }
     
     
    //-------------------Create a Document--------------------------------------------------------
     
    @RequestMapping(value = "/document", method = RequestMethod.POST)
    public ResponseEntity<Document> createDocument(@RequestBody Document document, UriComponentsBuilder ucBuilder) {
        System.out.println("Creating Document " + document.getName());
         
        if (!document.isValid()) {
        	System.out.println("Document is not valid!");
            return new ResponseEntity<Document>(HttpStatus.NOT_ACCEPTABLE);
        }
        
        return createEntity(document);
    }
 
     
    //------------------- Update a Document --------------------------------------------------------     
    @RequestMapping(value = "/document/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Document> updateDocument(@PathVariable("id") String id, @RequestBody Document document) {
        logger.info("Updating Document " + document);
         
        Document currentDocument = repository.findOne(document.getId());
         
        if (currentDocument == null) {
            System.out.println("Document with id " + id + " not found");
            return new ResponseEntity<Document>(HttpStatus.NOT_FOUND);
        }
 
        currentDocument.setName(document.getName());        
        currentDocument.setProjectId(document.getProjectId());
        currentDocument.setProject(document.getProject());
        currentDocument.setStatus(document.getStatus());
        currentDocument.setNotificationSchemaId(document.getNotificationSchemaId());
        currentDocument.setAssigneeId(document.getAssigneeId());
        currentDocument.setDescription(document.getDescription());
        
        if (document.getDeadline() != null)
        	currentDocument.setDeadline(document.getDeadline());
        
        if (!currentDocument.isValid()) {
        	System.out.println("Document is not valid!");
            return new ResponseEntity<Document>(HttpStatus.NOT_ACCEPTABLE);
        }
        
        repository.save(currentDocument);
        return new ResponseEntity<Document>(currentDocument, HttpStatus.OK);
    }
 
    //------------------- Delete a Document --------------------------------------------------------     
    @RequestMapping(value = "/document/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Document> deleteDocument(@PathVariable("id") String id) {
 
    	ResponseEntity<Document> res =  deleteEntity(id);    	
    	logger.info("Document " + res + " DELETED");    	
    	return res;
    }

}
