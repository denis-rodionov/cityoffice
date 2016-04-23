package com.rodionov.cityoffice.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.rodionov.cityoffice.model.Document;
import com.rodionov.cityoffice.model.DocumentStatus;


@Repository
public interface DocumentRepository extends MongoRepository<Document, String> {
	
	List<Document> findByStatus(DocumentStatus status);
	
	List<Document> findByDeadlineGreaterThan(Date date);
	
	List<Document> findByProjectId(String projectId);
	
	List<Document> findByNotificationSchemaId(String notificationSchemaId);
}