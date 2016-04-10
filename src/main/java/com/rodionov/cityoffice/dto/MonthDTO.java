package com.rodionov.cityoffice.dto;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MonthDTO {
	private String monthName;
	private int monthNumber;
	private int year;
	private int documentsCount;
	private List<DocumentDTO> documents;
	
	public MonthDTO() {}
	
	public MonthDTO(Date date, List<DocumentDTO> documents) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		this.monthNumber = cal.get(Calendar.MONTH);
		this.monthName = new DateFormatSymbols().getMonths()[this.monthNumber - 1];
		
		this.year = cal.get(Calendar.YEAR);
		this.documentsCount = documents.size();
		this.documents = documents;
	}

	@Override
	public String toString() {
		return "MonthDTO [monthName=" + monthName + ", monthNumber=" + monthNumber + ", year=" + year
				+ ", documentsCount=" + documentsCount + ", documents=" + documents + "]";
	}

	public String getMonthName() {
		return monthName;
	}

	public void setMonthName(String monthName) {
		this.monthName = monthName;
	}

	public int getMonthNumber() {
		return monthNumber;
	}

	public void setMonthNumber(int monthNumber) {
		this.monthNumber = monthNumber;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getDocumentsCount() {
		return documentsCount;
	}

	public void setDocumentsCount(int documentsCount) {
		this.documentsCount = documentsCount;
	}

	public List<DocumentDTO> getDocuments() {
		return documents;
	}

	public void setDocuments(List<DocumentDTO> documents) {
		this.documents = documents;
	}
	
	
}
