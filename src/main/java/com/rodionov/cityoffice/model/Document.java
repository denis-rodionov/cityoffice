package com.rodionov.cityoffice.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;

public class Document {
	@Id
	private String Id;
	
	private String name;
	private LocalDate deadline;
	
	public Document() { }
	
	public Document(String name, LocalDate deadline) {
		super();
		this.name = name;
		this.deadline = deadline;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LocalDate getDeadline() {
		return deadline;
	}
	public void setDeadline(LocalDate deadline) {
		this.deadline = deadline;
	}
}
