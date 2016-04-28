package com.rodionov.cityoffice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.rodionov.cityoffice.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
	User findByUsername(String username);
	User findByEmail(String email);
	List<User> findByProjectIdsIn(String projectId);
}
