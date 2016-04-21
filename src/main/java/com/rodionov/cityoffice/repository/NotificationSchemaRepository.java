package com.rodionov.cityoffice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.rodionov.cityoffice.model.NotificationSchema;

@Repository
public interface NotificationSchemaRepository extends MongoRepository<NotificationSchema, String> {

}
