package com.rodionov.cityoffice.dto;

import java.time.LocalDate;

public class EmployeeProjectDTO {

	private String id;
	private String name;
	private LocalDate startDate;
	private LocalDate finishDate;
	private double workload;
	
	
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
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalDate getFinishDate() {
		return finishDate;
	}
	public void setFinishDate(LocalDate finishDate) {
		this.finishDate = finishDate;
	}
	public double getWorkload() {
		return workload;
	}
	public void setWorkload(double workload) {
		this.workload = workload;
	}
	
	
}
