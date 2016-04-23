package com.rodionov.cityoffice.model.helpers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.rodionov.cityoffice.model.Document;
import com.rodionov.cityoffice.model.Notification;
import com.rodionov.cityoffice.model.NotificationSchema;

public class NotificationHelper {
	
	private static final Logger logger = Logger.getLogger(NotificationHelper.class);

	public static NotificationSchema create(String type) {
		return new NotificationSchema(type);
	}
	
	public static NotificationSchema create(String name, int... daysBefore) {
		NotificationSchema schema = new NotificationSchema(name);
		
		List<Notification> notifications = new ArrayList<Notification>();
		
		for (int i : daysBefore) {
			Notification notification = new Notification(i, new Integer(i).toString());
			notification.setId(new Integer(i).toString());
			notifications.add(notification);
		}
		
		schema.setNotifications(notifications);
		
		return schema;
	}
	
	public static NotificationSchema create(String name, Notification... notifications) {
		NotificationSchema schema = new NotificationSchema(name);
		
		schema.setNotifications(new ArrayList<Notification>(Arrays.asList(notifications)));
		
		return schema;
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
		
		List<Integer> days = schema.getNotifications().stream()
			.map(n -> n.getDaysBefore())
			.collect(Collectors.toList());
		
		//logger.info(days);
		
		return days;
	}
}
