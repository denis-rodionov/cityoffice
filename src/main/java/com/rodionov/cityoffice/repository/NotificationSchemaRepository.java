package com.rodionov.cityoffice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.rodionov.cityoffice.model.Notification;
import com.rodionov.cityoffice.model.NotificationSchema;

@Repository
public interface NotificationSchemaRepository extends MongoRepository<NotificationSchema, String> {
	List<NotificationSchema> findByName(String name);
	List<NotificationSchema> findByNotificationsIn(Notification notification);
}
