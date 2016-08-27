package com.rodionov.cityoffice.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public class EmployeeProjectDTO {

	private String id;
	private String name;
	private double workload;
	
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate startDate;
	
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate finishDate;
	
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
