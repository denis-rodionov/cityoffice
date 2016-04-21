package com.rodionov.cityoffice.model;

import org.springframework.data.annotation.Id;

public class SentNotification {
	@Id
	private String id;
	private String userId;
	private String documentId;
	private boolean byEmail;
		
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
