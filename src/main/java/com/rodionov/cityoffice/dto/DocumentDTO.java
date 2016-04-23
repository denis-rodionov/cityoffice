package com.rodionov.cityoffice.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.rodionov.cityoffice.model.Document;

public class DocumentDTO {
	private String id;
	private String name;
	private int dateOfMonth;
	private int year;
	private int monthNumber;
	private String monthName;
	private String projectName;
	private boolean isUrgent;
	private String formatedDeadline;
	private String projectColor;
	private String visibility;
	
	public DocumentDTO(String id, String name, LocalDate date, String projectName, boolean isUrgent, String projectColor) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM");	
		
		this.id = id;
		this.name = name;
		this.dateOfMonth = date.getDayOfMonth();
		this.year = date.getYear();
		this.monthNumber = date.getMonthValue();
		this.monthName = date.getMonth().toString();
		this.projectName = projectName;
		this.isUrgent = isUrgent;
		this.formatedDeadline = date.format(formatter);
		this.projectColor = projectColor;
	}

	@Override
	public String toString() {
		return "DocumentDTO [id=" + id + ", name=" + name + ", dateOfMonth=" + dateOfMonth + ", year=" + year
				+ ", monthNumber=" + monthNumber + ", monthName=" + monthName + ", projectName=" + projectName
				+ ", isUrgent=" + isUrgent + ", formatedDeadline=" + formatedDeadline + ", projectColor=" + projectColor
				+ "]";
	}

	public static DocumentDTO of(Document d) {
		return new DocumentDTO(
				d.getId(), 
				d.getName(), 
				d.getDeadline(),
				d.getProject().getName(),
				false,
				d.getProject().getColorName());
	}	

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public int getDateOfMonth() {
		return dateOfMonth;
	}

	public void setDateOfMonth(int dateOfMonth) {
		this.dateOfMonth = dateOfMonth;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonthNumber() {
		return monthNumber;
	}

	public void setMonthNumber(int monthNumber) {
		this.monthNumber = monthNumber;
	}

	public String getMonthName() {
		return monthName;
	}

	public void setMonthName(String monthName) {
		this.monthName = monthName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public boolean isUrgent() {
		return isUrgent;
	}

	public void setUrgent(boolean isUrgent) {
		this.isUrgent = isUrgent;
	}

	public String getFormatedDeadline() {
		return formatedDeadline;
	}

	public void setFormatedDeadline(String formatedDate) {
		this.formatedDeadline = formatedDate;
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

	public String getProjectColor() {
		return projectColor;
	}

	public void setProjectColor(String projectColor) {
		this.projectColor = projectColor;
	}

	
}
