package com.rodionov.cityoffice.controllers;

import java.security.Principal;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import com.rodionov.cityoffice.model.User;
import com.rodionov.cityoffice.repository.DocumentRepository;
import com.rodionov.cityoffice.services.MongoUserDetailsService;

@RestController
public class DocumentController extends BaseController {

	private static final Logger logger = Logger.getLogger(DocumentController.class);
	
	@Autowired
	private DocumentRepository documentRepository;
	
	@Autowired
	private MongoUserDetailsService userDetailsService;
	
	@RequestMapping(value = "/finish/{id}", method = RequestMethod.POST)
	public Document doneDocument(Principal principal, @PathVariable("id") String id) {
		logger.info("Make document with id=" + id + " done");
		
		Document document = documentRepository.findOne(id);
		if (document == null) 
			throw new NotFoundException();
		
		document.setStatus(DocumentStatus.FINISHED);
		document.setAssigneeId(userDetailsService.getUserByPrincipal(principal).getId());
		documentRepository.save(document);
		
		return document;
	}
	
	//-------------------Retrieve All Documents --------------------------------------------------------
    
    @RequestMapping(value = "/document", method = RequestMethod.GET)    
    public ResponseEntity<List<Document>> listAllDocuments(Principal principal,
    		@RequestParam("_page") int page, 
    		@RequestParam("_perPage") int per_page
    		) { //@RequestParam(value = "sort", defaultValue = "username", required = false) String sortField) {
        
    	Page<Document> docs;
    	
    	User currentUser = userDetailsService.getUserByPrincipal(principal);
    	Pageable pageable = new PageRequest(page-1, per_page, Sort.Direction.ASC, "123");
    	
    	if (userDetailsService.isAdmin(principal))
    		docs = documentRepository.findAll(pageable);
    	else
    		docs = documentRepository.findByProjectIdIn(currentUser.getProjectIds(), pageable);     	
        
        // docs.sort((d1, d2) -> d1.getDeadline().compareTo(d2.getDeadline()));       
    	
        return new ResponseEntity<>(docs.getContent(), generatePaginationHeaders(docs, ""), HttpStatus.OK);
    }
    
  //-------------------Retrieve Single Document--------------------------------------------------------
    
    @RequestMapping(value = "/document/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Document> getDocument(@PathVariable("id") String id) {
        System.out.println("Fetching Document with id " + id);
        Document document = documentRepository.findOne(id);
        if (document == null) {
            System.out.println("Document with id " + id + " not found");
            return new ResponseEntity<Document>(HttpStatus.NOT_FOUND);
        }
        
        return new ResponseEntity<Document>(document, HttpStatus.OK);
    }
     
     
    //-------------------Create a Document--------------------------------------------------------
     
    @RequestMapping(value = "/document", method = RequestMethod.POST)
    public ResponseEntity<Document> createDocument(@RequestBody Document document, UriComponentsBuilder ucBuilder) {
        System.out.println("Creating Document " + document.getName());
         
        if (!document.isValid()) {
        	System.out.println("Document is not valid!");
            return new ResponseEntity<Document>(HttpStatus.NOT_ACCEPTABLE);
        }
        
        documentRepository.save(document);
 
        //HttpHeaders headers = new HttpHeaders();
        //headers.setLocation(ucBuilder.path("/document/{id}").buildAndExpand(document.getId()).toUri());
        //return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
        return new ResponseEntity<Document>(document, HttpStatus.CREATED);
    }
 
     
    //------------------- Update a Document --------------------------------------------------------
     
    @RequestMapping(value = "/document/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Document> updateDocument(@PathVariable("id") String id, @RequestBody Document document) {
        logger.info("Updating Document " + document);
         
        Document currentDocument = documentRepository.findOne(document.getId());
         
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
        
        documentRepository.save(currentDocument);
        return new ResponseEntity<Document>(currentDocument, HttpStatus.OK);
    }
 
    //------------------- Delete a Document --------------------------------------------------------
     
    @RequestMapping(value = "/document/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Document> deleteDocument(@PathVariable("id") String id) {
 
        Document document = documentRepository.findOne(id);
        if (document == null) {
            System.out.println("Unable to delete. Document with id " + id + " not found");
            return new ResponseEntity<Document>(HttpStatus.NOT_FOUND);
        }

        logger.info("Deleting Document with name " + document.getName());
        documentRepository.delete(id);
        return new ResponseEntity<Document>(HttpStatus.NO_CONTENT);
    }
 
     
    //------------------- Delete All Documents --------------------------------------------------------
     
/*    @RequestMapping(value = "/document/", method = RequestMethod.DELETE)
    public ResponseEntity<Document> deleteAllDocuments() {
        System.out.println("Deleting All Documents");
 
        documentRepository.deleteAll();
        return new ResponseEntity<Document>(HttpStatus.NO_CONTENT);
    }*/

}
