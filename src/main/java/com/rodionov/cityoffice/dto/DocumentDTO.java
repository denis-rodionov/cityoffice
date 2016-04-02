package com.rodionov.cityoffice.dto;

import java.time.LocalDate;

public class DocumentDTO {
	private String id;
	private String name;
	private String deadline;

	public DocumentDTO(String id, String name, LocalDate localDate) {
		super();
		this.id = id;
		this.name = name;
		this.deadline = localDate.toString();
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

	public String getDeadline() {
		return deadline;
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}

	
}
