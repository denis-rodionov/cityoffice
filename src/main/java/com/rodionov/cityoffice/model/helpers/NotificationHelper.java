package com.rodionov.cityoffice.model.helpers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.rodionov.cityoffice.model.Document;
import com.rodionov.cityoffice.model.NotificationSchema;

public class NotificationHelper {
	
	private static final Logger logger = Logger.getLogger(NotificationHelper.class);

	public static final String ONLY_DEADLINE_NOTIFY = "Only deadline";
	public static final String DEADLINE_AND_WEEK_BEFORE = "Week";
	public static final String DEADLINE_MONTH_BEFORE = "Month";
	
	public static List<NotificationSchema> createNotificationSchemes() {
		return Arrays.asList(
				new NotificationSchema(ONLY_DEADLINE_NOTIFY),
				new NotificationSchema(DEADLINE_AND_WEEK_BEFORE),
				new NotificationSchema(DEADLINE_MONTH_BEFORE));
	}
	
	public static NotificationSchema create(String type) {
		return new NotificationSchema(type);
	}
	
	public static List<LocalDate> getNotificationDates(NotificationSchema schema, Document doc)  {
		LocalDate deadline = doc.getDeadline();
		
		try {
			return getDaysBeforeNotifications(schema)
						.stream()
						.map(d -> deadline.minusDays(d))
						.collect(Collectors.toList());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Cannot find notification schema", e);
			return null;
		}
	}
	
	private static List<Integer> getDaysBeforeNotifications(NotificationSchema schema)
			throws Exception {
		
		if (schema == null)
			return new ArrayList<Integer>();
		
		switch (schema.getName()) {
			case ONLY_DEADLINE_NOTIFY:
				return Arrays.asList(0);
			case DEADLINE_AND_WEEK_BEFORE:
				return Arrays.asList(0, 7);
			case DEADLINE_MONTH_BEFORE:
				return Arrays.asList(0, 7, 30);
			default:
				throw new Exception("Unknown notification schema");				
		}
	}
}
