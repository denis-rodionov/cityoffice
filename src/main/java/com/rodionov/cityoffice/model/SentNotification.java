package com.rodionov.cityoffice.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;

public class SentNotification {
	@Id
	private String id;
	private String userId;
	private String documentId;
	private boolean byEmail;
	private LocalDate date;
	
	public SentNotification() { }	
			
	public SentNotification(LocalDate date, String userId, String documentId, boolean byEmail) {
		super();
		this.date = date;
		this.userId = userId;
		this.documentId = documentId;
		this.byEmail = byEmail;
	}
	
	
	
	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getId() {
		return id;
	}
	public boolean isByEmail() {
		return byEmail;
	}
	public void setByEmail(boolean byEmail) {
		this.byEmail = byEmail;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getDocumentId() {
		return documentId;
	}
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
}
