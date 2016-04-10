package com.rodionov.cityoffice.model.helpers;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import com.rodionov.cityoffice.model.Document;
import com.rodionov.cityoffice.model.DocumentStatus;

public class DocumentHelper {
	public static Document CreateDocument(
			String name, 
			String projectId, 
			LocalDate date,
			DocumentStatus status) {
		
		Instant instant = date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
		Date resDate = Date.from(instant);
		
		return new Document(name, resDate, status, projectId);
	}
}
