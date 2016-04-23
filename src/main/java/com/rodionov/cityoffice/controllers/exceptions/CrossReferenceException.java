package com.rodionov.cityoffice.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.CONFLICT, reason="Database contains element which linked with this entity")
public class CrossReferenceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6346828297411059880L;
	

}
