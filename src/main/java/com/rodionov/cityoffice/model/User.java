package com.rodionov.cityoffice.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import lombok.Data;

@Data
public class User {
	
	//private static final Logger logger = Logger.getLogger(User.class);
	
	@Id
	private String id;
	
	private String username;
	
	@Indexed(unique = true)
	private String email;	
	
	private List<String> projectIds;
	
	private String password;
	
	private String role;
	
	private List<UserProject> resourcePlanning;
	
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
	
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", email=" + email + ", projectIds=" + projectIds
				+ ", role=" + role + "]";
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> getProjectIds() {
		return projectIds;
	}

	public void setProjectIds(List<String> projectIds) {
		this.projectIds = projectIds;
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

	public List<UserProject> getResourcePlanning() {
		return resourcePlanning;
	}

	public void setResourcePlanning(List<UserProject> resourcePlanning) {
		this.resourcePlanning = resourcePlanning;
	}
	
	
}
