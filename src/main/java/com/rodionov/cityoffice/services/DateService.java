package com.rodionov.cityoffice.services;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

@Service
public class DateService {
	
	public LocalDate getCurrentDate() {
		return LocalDate.now();
	}
	
	public boolean isPastMonth(LocalDate date) {
		LocalDate current = getCurrentDate().withDayOfMonth(1);
		
		int res = date.compareTo(current);
		
		return res < 0;
	}
	
	public boolean isPresentMonth(LocalDate date) {
		LocalDate current = getCurrentDate();
		
		return current.getMonth() == date.getMonth() && current.getYear() == date.getYear();
	}
	
}
