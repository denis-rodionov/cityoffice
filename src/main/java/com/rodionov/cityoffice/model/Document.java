package com.rodionov.cityoffice.model;

import java.util.Calendar;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Transient;

public class Document {
	@Id
	private String id;	
	private String name;
	private Date deadline;
	private DocumentStatus status;
	private String projectId;
	
	@Transient
	private Project project;
	
	public Document() { }
	
	@PersistenceConstructor	
	public Document(String id, String name, Date deadline, DocumentStatus status, String projectId) {
		super();
		this.id = id;
		this.name = name;
		this.deadline = deadline;
		this.status = status;
		this.projectId = projectId;
	}	

	public Document(String name, Date deadline, DocumentStatus status, String projectId) {
		super();
		this.name = name;
		this.deadline = deadline;
		this.status = status;
		this.projectId = projectId;
	}
	
	public Document(Document document) {
		this(document.id, document.name, document.deadline, document.status,
			document.projectId);
	}

	@Override
	public String toString() {
		return "Document [id=" + id + ", name=" + name + ", deadline=" + deadline + ", status=" + status
				+ ", projectId=" + projectId + ", project=" + project + "]";
	}
	
	public String getMonth() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(deadline);
		
		return cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR);
	}

	public DocumentStatus getStatus() {
		return status;
	}

	public void setStatus(DocumentStatus status) {
		this.status = status;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
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
	public Date getDeadline() {
		return deadline;
	}
	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}
}
