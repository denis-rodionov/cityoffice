package com.rodionov.cityoffice.model;

import java.time.LocalDate;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.rodionov.cityoffice.model.serialization.CustomDateDeserializer;
import com.rodionov.cityoffice.model.serialization.CustomDateSerializer;

@org.springframework.data.mongodb.core.mapping.Document
public class Document {
	
	private static final Logger logger = Logger.getLogger(Document.class);
	
	@Id
	private String id;	
	private String name;
	
	@JsonDeserialize(using = CustomDateDeserializer.class)
	@JsonSerialize(using = CustomDateSerializer.class)
	private LocalDate deadline;
	
	private DocumentStatus status;
	private String projectId;
	private String notificationSchemaId;
	private String assigneeId;
	private String description;
		
	@Transient
	private Project project;
	
	@Transient
	private NotificationSchema notificationSchema;
	
	@Transient 
	private User assignee;
	
	public Document() { }
	
	@PersistenceConstructor	
	public Document(
			String id, 
			String name, 
			LocalDate deadline, 
			DocumentStatus status, 
			String projectId, 
			String notificationSchemaId,
			String assigneeId) {
		super();
		this.id = id;
		this.name = name;
		this.deadline = deadline;
		this.status = status;
		this.projectId = projectId;
		this.notificationSchemaId = notificationSchemaId;
		this.assigneeId = assigneeId;
	}	
	
	public Document(
			String id, 
			String name, 
			LocalDate deadline, 
			DocumentStatus status, 
			String projectId, 
			Project project,
			String assigneeId,
			User assignee) {
		super();
		this.id = id;
		this.name = name;
		this.deadline = deadline;
		this.status = status;
		this.projectId = projectId;
		this.project = project;
		this.assigneeId = assigneeId;
		this.assignee = assignee;
	}	

	public Document(String name, LocalDate deadline, DocumentStatus status, String projectId) {
		super();
		this.name = name;
		this.deadline = deadline;
		this.status = status;
		this.projectId = projectId;		
	}
	
	public Document(String name, LocalDate deadline, DocumentStatus status, String projectId, String notificationSchemaId) {
		super();
		this.name = name;
		this.deadline = deadline;
		this.status = status;
		this.projectId = projectId;	
		this.notificationSchemaId = notificationSchemaId;
	}
	
	public Document(Document document) {
		this(document.id, document.name, document.deadline, document.status,
			document.projectId, document.notificationSchemaId, document.assigneeId);
	}
	
	@Override
	public String toString() {
		return "Document [id=" + id + ", name=" + name + ", deadline=" + deadline + ", status=" + status
				+ ", projectId=" + projectId + ", notificationSchemaId=" + notificationSchemaId + ", assigneeId="
				+ assigneeId + ", description=" + description + ", project=" + project + ", notificationSchema="
				+ notificationSchema + "]";
	}

	@JsonIgnore
	public String getSortableMonth() {
		if (deadline == null) {
			logger.error("Deadline is NULL for the document: " + this);
			return "";
		}
		return deadline.getMonthValue() + "/" + deadline.getYear();
	}
	
	public boolean isValid() {
		if (deadline == null)
			return false;
		
		return true;
	}

	public String getAssigneeId() {
		return assigneeId;
	}

	public void setAssigneeId(String assigneeId) {
		this.assigneeId = assigneeId;
	}
	
	public User getAssignee() {
		return assignee;
	}

	public void setAssignee(User assignee) {
		this.assignee = assignee;
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
	public LocalDate getDeadline() {
		return deadline;
	}
	public void setDeadline(LocalDate deadline) {
		this.deadline = deadline;
	}
	
	public String getNotificationSchemaId() {
		return notificationSchemaId;
	}

	public void setNotificationSchemaId(String notificationSchemaId) {
		this.notificationSchemaId = notificationSchemaId;
	}

	public NotificationSchema getNotificationSchema() {
		return notificationSchema;
	}

	public void setNotificationSchema(NotificationSchema notificationSchema) {
		this.notificationSchema = notificationSchema;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
