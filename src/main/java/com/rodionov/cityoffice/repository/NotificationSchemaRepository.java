package com.rodionov.cityoffice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.rodionov.cityoffice.model.NotificationSchema;

public interface NotificationSchemaRepository extends MongoRepository<NotificationSchema, String> {

}
