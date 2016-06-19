package com.rodionov.cityoffice.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;

/**
 * Assignment user for a project for 
 * the specified period 
 * 
 * @author Denis
 *
 */
public class UserProject {
	
	@Id
	private String id;
	
	private String projectId;	
	private LocalDate startDate;	
	private LocalDate finishDate;
	private int load;	// percentage of loadinguser on the project
	
	public UserProject(String projectId, LocalDate startDate, LocalDate finishDate, int load) {
		super();
		this.projectId = projectId;
		this.startDate = startDate;
		this.finishDate = finishDate;
		this.load = load;
	}

	@Override
	public String toString() {
		return "UserProject [id=" + id + ", projectId=" + projectId + ", startDate=" + startDate + ", finishDate="
				+ finishDate + ", load=" + load + "]";
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

	public int getLoad() {
		return load;
	}

	public void setLoad(int load) {
		this.load = load;
	}
	
	
	
}
