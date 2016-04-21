package com.rodionov.cityoffice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.rodionov.cityoffice.model.SentNotification;

@Repository
public interface SentNotificationRepository extends MongoRepository<SentNotification, String> {
	List<SentNotification> findByDocumentId(String documentId);
}
