package com.rodionov.cityoffice.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.rodionov.cityoffice.model.serialization.CustomDateDeserializer;
import com.rodionov.cityoffice.model.serialization.CustomDateSerializer;

/**
 * Assignment user for a project for 
 * the specified period 
 * 
 * @author Denis
 *
 */
@org.springframework.data.mongodb.core.mapping.Document
public class UserProject {
	
	@Id
	private String id;
	
	private String projectId;	
	
	private String userId;
	
	@JsonDeserialize(using = CustomDateDeserializer.class)
	@JsonSerialize(using = CustomDateSerializer.class)
	private LocalDate startDate;	
	
	@JsonDeserialize(using = CustomDateDeserializer.class)
	@JsonSerialize(using = CustomDateSerializer.class)
	private LocalDate finishDate;
	
	private double load;	// percentage of user loading on the project [0..1]
	
	public UserProject(String projectId, String userId, LocalDate startDate, LocalDate finishDate, double load) {
		super();
		this.userId = userId;
		this.projectId = projectId;
		this.startDate = startDate;
		this.finishDate = finishDate;
		this.load = load;
	}
	
	public UserProject() {
		super();
	}
	
	@Override
	public String toString() {
		return "UserProject [id=" + id + ", projectId=" + projectId + ", userId=" + userId + ", startDate=" + startDate
				+ ", finishDate=" + finishDate + ", load=" + load + "]";
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
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

	public double getLoad() {
		return load;
	}

	public void setLoad(double load) {
		this.load = load;
	}
	
	
	
}
