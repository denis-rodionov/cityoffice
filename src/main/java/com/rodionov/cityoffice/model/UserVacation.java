package com.rodionov.cityoffice.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.rodionov.cityoffice.model.serialization.CustomDateDeserializer;
import com.rodionov.cityoffice.model.serialization.CustomDateSerializer;

@org.springframework.data.mongodb.core.mapping.Document
public class UserVacation {

	@Id
	private String Id;
	
	private String userId;
	
	@JsonDeserialize(using = CustomDateDeserializer.class)
	@JsonSerialize(using = CustomDateSerializer.class)
	private LocalDate startDate;	
	
	@JsonDeserialize(using = CustomDateDeserializer.class)
	@JsonSerialize(using = CustomDateSerializer.class)
	private LocalDate finishDate;
	
	public UserVacation() {		
	}

	public UserVacation(String userId, LocalDate startDate, LocalDate finishDate) {
		super();
		this.userId = userId;
		this.startDate = startDate;
		this.finishDate = finishDate;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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
	
	
}
