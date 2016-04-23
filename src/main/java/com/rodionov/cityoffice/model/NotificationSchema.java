package com.rodionov.cityoffice.model;

import java.util.List;

import org.springframework.data.annotation.Id;

public class NotificationSchema {
	
	@Id
	private String id;
	private String name;	
	private String description;
	private List<String> notifications;
	
	public NotificationSchema() { }
	
	public NotificationSchema(String name) {
		this.name = name;
	}
	

	public List<String> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<String> notifications) {
		this.notifications = notifications;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
