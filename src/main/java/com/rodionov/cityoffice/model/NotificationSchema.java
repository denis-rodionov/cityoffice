package com.rodionov.cityoffice.model;

import org.springframework.data.annotation.Id;

public class NotificationSchema {
	
	@Id
	private String id;
	private String name;	
	
	public NotificationSchema() { }
	
	public NotificationSchema(String name) {
		this.name = name;
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
