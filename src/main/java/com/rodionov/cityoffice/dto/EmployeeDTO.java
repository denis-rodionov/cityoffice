package com.rodionov.cityoffice.dto;

import java.util.List;

public class EmployeeDTO {

	private String id;
	private String name;
	private List<EmployeeProjectDTO> projects;
	private List<EmployeeVacationDTO> vacations;
	
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
	public List<EmployeeProjectDTO> getProjects() {
		return projects;
	}
	public void setProjects(List<EmployeeProjectDTO> projects) {
		this.projects = projects;
	}
	public List<EmployeeVacationDTO> getVacations() {
		return vacations;
	}
	public void setVacations(List<EmployeeVacationDTO> vacations) {
		this.vacations = vacations;
	}
	
	
}
