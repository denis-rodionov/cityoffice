package com.rodionov.cityoffice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.rodionov.cityoffice.model.UserVacation;

public interface UserVacationRepository extends MongoRepository<UserVacation, String>,
   QueryDslPredicateExecutor<UserVacation> {


}