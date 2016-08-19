package com.rodionov.cityoffice.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;

import com.rodionov.cityoffice.model.User;
import com.rodionov.cityoffice.services.MongoUserDetailsService;

public class BaseController {
	
	@Autowired
	private MongoUserDetailsService userDetailsService;

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
}
