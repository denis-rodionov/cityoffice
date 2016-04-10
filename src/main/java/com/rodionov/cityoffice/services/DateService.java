package com.rodionov.cityoffice.services;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

@Service
public class DateService {
	
	public LocalDate getCurentDate() {
		return LocalDate.now();
	}
}
