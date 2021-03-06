package com.rodionov.cityoffice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.rodionov.cityoffice.model.Project;

@Repository
public interface ProjectRepository extends MongoRepository<Project, String>,
											QueryDslPredicateExecutor<Project>	{
	
	List<Project> findByName(String name);
	
	List<Project> findByIdIn(List<String> ids);
}
