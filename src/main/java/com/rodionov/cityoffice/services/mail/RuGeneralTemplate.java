package com.rodionov.cityoffice.services.mail;

public class RuGeneralTemplate extends DeadlineMailTemplate {
	
	protected final String THEME_GOOD = "Подходит срок по '%1$s' (проект: %2$s)";
	protected final String THEME_EXPIRED = "Просрочено по '%1$s' (проект: %2$s)";
	protected final String THEME_TODAY = "Сегодня срок по '%1$s' (проект: %2$s)";
	
	protected final String NOTIFICATION_GOOD = "Напоминаем Вам, что %1$s подходит срок по '%2$s'";
	protected final String NOTIFICATION_EXPIRED = "Напоминаем Вам, что срок по '%2$s' истёк %1$s назад";

	@Override
	public String getMailTheme() {
		return String.format(getThemeTemplate(daysToDeadline), documentName, projectName);
	}

	@Override
	public String getMailBody() {
		String assigneeText = assignee != null ? 
				assignee : "<не назначен>";
		
		return addressee + ",\n" + 
		getNotificationText() +
		
		"\n\nСрок: " + deadline.format(formatter) +
		"\nПроект: " + projectName + 
		"\nОтветственный: " + assigneeText +
		"\n\nПодробности на http://cityoffice.loc/";
	}

	private String getNotificationText() {
		return String.format(getNotificationTemplate(), formatedDays(daysToDeadline), documentName);
	}
	
	private String getNotificationTemplate() {
		if (daysToDeadline >= 0)
			return NOTIFICATION_GOOD;
		else
			return NOTIFICATION_EXPIRED;
	}

	@Override
	public String getContentType() {
		return "text/plain; charset=utf-8";
	}
	
	public String formatedDays(long daysTo) {
		if (daysTo == 0)
			return "сегодня";
		else if (daysTo == 1)
			return "завтра";
		else if (daysTo == 2)
			return "послезавтра";
		else if (daysTo < 0)
			return Math.abs(daysTo) + " " + getDays(daysTo);
		else
			return "через " + daysTo + " " + getDays(daysTo);
	}

	private String getDays(long daysTo) {
		
		String res = "";
		long last = Math.abs(daysTo) % 10;
		long last2 = Math.abs(daysTo) % 100;
		if (last2 >= 11 && last2 <= 20)
			res += "дней";
		else if (last == 1)
			res += "день";
		else if (last == 0 || last >= 5)
			res += "дней";
		else
			res += "дня";
		
		return res;
	}

	protected String getThemeTemplate(long daysToDeadline) {

		if (daysToDeadline > 0)
			return THEME_GOOD;
		else if (daysToDeadline == 0)
			return THEME_TODAY;
		else
			return THEME_EXPIRED;
	}
}
