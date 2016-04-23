package com.rodionov.cityoffice.model;

import java.util.List;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class User {
	
	//private static final Logger logger = Logger.getLogger(User.class);
	
	@Id
	private String id;
	private String username;
	private String email;
	private List<String> projectIds;
	
	public User() { }
	
	public User(String id, String username, String email) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
	}

	public User(String username, String email) {
		super();
		this.username = username;
		this.email = email;
	}
	
	public User(String id, String username, String email, List<String> projectIds) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.projectIds = projectIds;
	}
	
	public List<String> getProjectIds() {
		return projectIds;
	}

	public void setProjectIds(List<String> projectIds) {
		this.projectIds = projectIds;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", email=" + email + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
