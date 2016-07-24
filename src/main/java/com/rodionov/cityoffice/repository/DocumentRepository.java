package com.rodionov.cityoffice.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.rodionov.cityoffice.model.Document;
import com.rodionov.cityoffice.model.DocumentStatus;


@Repository
public interface DocumentRepository extends MongoRepository<Document, String>, QueryDslPredicateExecutor<Document> {
	
	Page<Document> findAll(Pageable pageable);
	
	Page<Document> findByStatus(DocumentStatus status, Pageable pageable);
	
	List<Document> findByStatus(DocumentStatus status);
	
	List<Document> findByDeadlineGreaterThan(Date date);
	
	List<Document> findByProjectId(String projectId);
	
	Page<Document> findByProjectIdInAndStatus(List<String> projectIds, String status, Pageable pageable);
	
	List<Document> findByNotificationSchemaId(String notificationSchemaId);
	
}