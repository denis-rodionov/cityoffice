package com.rodionov.cityoffice.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class MonthDTO implements Comparable<MonthDTO> {	
	final static Locale LOCALE = new Locale("ru", "RU");
	
	private String monthName;
	private int monthNumber;
	private int year;
	private int documentsCount;
	private List<DocumentDTO> documents;
	
	public MonthDTO() {}
	
	public MonthDTO(LocalDate date, List<DocumentDTO> documents) {
		
		this.monthNumber = date.getMonthValue();
		this.monthName = getMonth(date);
		
		this.year = date.getYear();
		this.documentsCount = documents.size();
		this.documents = documents;
	}

	@Override
	public String toString() {
		return "MonthDTO [monthName=" + monthName + ", monthNumber=" + monthNumber + ", year=" + year
				+ ", documentsCount=" + documentsCount + ", documents=" + documents + "]";
	}
	
	public static String getMonth(LocalDate date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM", LOCALE);
		String month = date.format(formatter);
		return month;
	}
	
	public LocalDate getMonthStartDate() {
		return LocalDate.of(year, monthNumber, 1);
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

	@Override
	public int compareTo(MonthDTO other) {
		
		return (year * 10000 + monthNumber) - (other.year * 10000 + other.monthNumber);
	}
	
	
}
