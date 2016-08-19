package com.rodionov.cityoffice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.rodionov.cityoffice.model.UserProject;

public interface UserProjectRepository extends MongoRepository<UserProject, String>,
										   	   QueryDslPredicateExecutor<UserProject> {

	
}
