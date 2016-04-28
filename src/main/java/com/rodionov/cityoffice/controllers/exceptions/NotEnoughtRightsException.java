package com.rodionov.cityoffice.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.FORBIDDEN, reason="Need admin rights to access")
public class NotEnoughtRightsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1199961967247054031L;

}
