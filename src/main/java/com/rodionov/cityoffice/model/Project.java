package com.rodionov.cityoffice.model;

import org.springframework.data.annotation.Id;

@org.springframework.data.mongodb.core.mapping.Document
public class Project {
	@Id
	private String id;
	private String name;
	private boolean isActive;
	private String colorName;
	
	public Project(String name, boolean isActive, String colorName) {
		super();
		this.name = name;
		this.isActive = isActive;
		this.colorName = colorName;
	}
	
	public Project() {
		
	}
	
	@Override
	public String toString() {
		return "Project [id=" + id + ", name=" + name + ", isActive=" + isActive + ", colorName=" + colorName + "]";
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
	public boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}
	
	
}
