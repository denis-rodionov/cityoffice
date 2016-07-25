package com.rodionov.cityoffice.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;

public class BaseController {

	protected HttpHeaders generatePaginationHeaders(Page<?> page, String baseUrl) {
			 HttpHeaders headers = new HttpHeaders();
			 
			 headers.add("X-Total-Count", "" + page.getTotalElements());
			 
//	        String link = "";
//	        if ((page.getNumber() + 1) < page.getTotalPages()) {
//	            link = "<" + (new URI(baseUrl + "?page=" + (page.getNumber() + 1) + "&size=" + page.getSize())).toString() + ">; rel=\"next\",";
//	        }
//	        // prev link
//	        if ((page.getNumber()) > 0) {
//	            link += "<" + (new URI(baseUrl + "?page=" + (page.getNumber() - 1) + "&size=" + page.getSize())).toString() + ">; rel=\"prev\",";
//	        }
//	        // last and first link
//	        int lastPage = 0;
//	        if (page.getTotalPages() > 0) {
//	            lastPage = page.getTotalPages() - 1;
//	        }
//	        link += "<" + (new URI(baseUrl + "?page=" + lastPage + "&size=" + page.getSize())).toString() + ">; rel=\"last\",";
//	        link += "<" + (new URI(baseUrl + "?page=" + 0 + "&size=" + page.getSize())).toString() + ">; rel=\"first\"";
//	        headers.add(HttpHeaders.LINK, link);
	        return headers;
	    }
	
	protected Pageable getPagiable(int page, int perPage, String sortDir, String sortField) {

		if (sortField != null) {
			sortField = sortField == null ? "DESC" : sortField; 
			return new PageRequest(page-1, perPage, Sort.Direction.fromString(sortDir), sortField);
		}
		else {
			return new PageRequest(page-1, perPage);
		}			 
	}
}
