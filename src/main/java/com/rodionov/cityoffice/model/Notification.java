package com.rodionov.cityoffice.model;

import org.springframework.data.annotation.Id;

public class Notification {
	@Id
	private String id;
	private int daysBefore;
	private String name;
	
	public Notification() { }
	
	public Notification(int daysBefore, String name) {
		super();
		this.daysBefore = daysBefore;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getDaysBefore() {
		return daysBefore;
	}

	public void setDaysBefore(int daysBefore) {
		this.daysBefore = daysBefore;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
