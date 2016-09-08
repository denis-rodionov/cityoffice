package com.rodionov.cityoffice.controllers;

import java.security.Principal;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.rodionov.cityoffice.model.User;
import com.rodionov.cityoffice.services.MongoUserDetailsService;

/**
 * Base controller for accessing entities from mongo reposiotries
 * @author Denis
 *
 * @param <T>
 */
public class BaseController<T> {
	
	private static final Logger logger = Logger.getLogger(BaseController.class);
	
	@Autowired
	protected MongoUserDetailsService userDetailsService;
	
	protected MongoRepository<T, String> repository;
	
	/**
	 * Constructor for base controller
	 * @param repository
	 */
	public BaseController(MongoRepository<T, String> repository) {
		this.repository = repository;
	}
	
	/**
	 * Checking if the user has admin rights
	 * @param principal
	 * @return
	 */
	protected boolean isAdmin(Principal principal) {
		return userDetailsService.isAdmin(principal);
	}

	protected HttpHeaders generatePaginationHeaders(Page<?> page, String baseUrl) {
			 HttpHeaders headers = new HttpHeaders();
			 
			 headers.add("X-Total-Count", "" + page.getTotalElements());
			 
	        return headers;
	    }
	
	protected Pageable getPagiable(Integer page, Integer perPage, String sortDir, String sortField) {

		if (page == null) {
			page = 1;
			perPage = Integer.MAX_VALUE;
		}
		
		if (sortField != null) {
			sortField = sortField == null ? "DESC" : sortField; 
			return new PageRequest(page-1, perPage, Sort.Direction.fromString(sortDir), sortField);
		}
		else {
			return new PageRequest(page-1, perPage);
		}			 
	}
	
	/**
	 * Gets ids of projects which available for the current user
	 * @param principal User which requests data
	 * @return null if all projects are available, or ids of available projects
	 */
	protected List<String> getAvailableProjects(Principal principal) {
		
		User currentUser = getCurrentUser(principal);
		
		if (userDetailsService.isAdmin(principal))
			return null;
		else
			return currentUser.getProjectIds();
	}
	
	/**
	 * Gets user model by principal
	 * @param principal User which requests data
	 * @return User model
	 */
	protected User getCurrentUser(Principal principal) {
		return userDetailsService.getUserByPrincipal(principal);
	}
	
	/**
	 * Get requested entity from mongo repository
	 * @param id	ID of requested entity
	 * @return
	 */
	protected ResponseEntity<T> getEntity(String id) {
		
        T entity = repository.findOne(id);
        
        if (entity == null) {
        	logger.warn("Document with id " + id + " not found");
            return new ResponseEntity<T>(HttpStatus.NOT_FOUND);
        }
        
        return new ResponseEntity<T>(entity, HttpStatus.OK);
	}
	
	/**
	 * Creates entity by mongo repository
	 * @param entity
	 * @return
	 */
	protected ResponseEntity<T> createEntity(T entity) {		 
        T createdEntity = repository.save(entity);        
        return new ResponseEntity<T>(createdEntity, HttpStatus.CREATED);
	}
	
	/**
	 * Delete entity by given id from mongo repository
	 * @param id
	 * @return
	 */
	protected ResponseEntity<T> deleteEntity(String id) {
		
		T entity = repository.findOne(id);
        if (entity == null) {
            System.out.println("Unable to delete. Entity with id " + id + " not found");
            return new ResponseEntity<T>(HttpStatus.NOT_FOUND);
        }

        logger.info("Deleting entity " + entity);
        repository.delete(id);
        return new ResponseEntity<T>(HttpStatus.NO_CONTENT);
	}
}
