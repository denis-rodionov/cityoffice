package com.rodionov.cityoffice.dto;

import java.util.List;

public class EmployeeDTO {

	private String id;
	private String name;
	private List<EmployeeProjectDTO> projects;
	private List<EmployeeVacationDTO> vacations;
	private String managerUsername;
	private int hours;
	private String email;
	private String phone;
	
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
	public String getManagerUsername() {
		return managerUsername;
	}
	public void setManagerUsername(String managerUsername) {
		this.managerUsername = managerUsername;
	}
	public int getHours() {
		return hours;
	}
	public void setHours(int hours) {
		this.hours = hours;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
}
