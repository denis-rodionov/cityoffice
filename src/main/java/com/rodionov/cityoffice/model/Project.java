package com.rodionov.cityoffice.model;

import org.springframework.data.annotation.Id;

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
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}
	
	
}
