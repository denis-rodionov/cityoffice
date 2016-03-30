package com.rodionov.cityoffice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.rodionov.cityoffice.model.Document;

public interface DocumentRepository extends MongoRepository<Document, String> {
	
}