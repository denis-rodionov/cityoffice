package com.rodionov.cityoffice.services.mail;

public class EnGeneralTemplate extends DeadlineMailTemplate {

	@Override
	public String getMailTheme() {
		return "Deadline in " + projectName;
	}

	@Override
	public String getMailBody() {
		
		String assigneeText = assignee != null ? 
				"\nResponsible: " + assignee : "";
		
		return "Dear " + addressee + ",\n" + 
		"You have notification about '" + documentName + "' which has deadline " + 
		formatedDays(daysToDeadline) + " (" +
		deadline.format(formatter) + ")\nProject: " + projectName + 
		assigneeText +
		"\n\nMore details on http://cityoffice.loc";
	}
	
	public String formatedDays(long daysTo) {
		if (daysTo == 0)
			return "today";
		else if (daysTo == 1)
			return "tomorrow";
		else if (daysTo == 2)
			return "in the day after tomorrow";
		else
			return "in " + daysTo + " days";
	}

	@Override
	public String getContentType() {
		return "text/plain; charset=utf-8";
		//return "text/html; charset=utf-8";
	}
}
