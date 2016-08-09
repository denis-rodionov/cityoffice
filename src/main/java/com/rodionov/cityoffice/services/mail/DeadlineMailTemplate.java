package com.rodionov.cityoffice.services.mail;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Mail template interface
 * 
 * @author Denis
 *
 */
public abstract class DeadlineMailTemplate {
	
	final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	
	protected String addressee;
	protected String projectName;
	protected long daysToDeadline;	
	protected String assignee;
	protected String documentName;
	protected LocalDate deadline;
	
	/**
	 * Set deadline of the document
	 * @param deadline
	 */
	public void setDeadline(LocalDate deadline) {
		this.deadline = deadline;
	}
	
	/**
	 * Set document name which has deadline
	 * @param documentName
	 */
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	
	/**
	 * Set username of the mail addressee
	 * @param username Addressee username
	 */
	public void setAddressee(String username) {
		this.addressee = username;
	}
	
	/**
	 * Set projectname which notification is related to
	 * @param projectName Deadline's projectname
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	/**
	 * Set count of days to the deadline
	 * @param daysToDeadline Days to deadline
	 */
	public void setDaysToDeadline(long daysToDeadline) {
		this.daysToDeadline = daysToDeadline;
	}
	
	/**
	 * Set assignee of the deadline. Can differs from addressee
	 * @param username Username of assignee
	 */
	public void setAssignee(String username) {
		this.assignee = username;
	}
	
	/**
	 * Get theme of the notification email
	 * @return The email text
	 */
	public abstract String getMailTheme();
	
	/**
	 * Get body of the notification email
	 * @return The email test
	 */
	public abstract String getMailBody();

	/**
	 * Content type of mime email
	 * @return
	 */
	public abstract String getContentType();
}
