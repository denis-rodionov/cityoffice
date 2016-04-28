package com.rodionov.cityoffice.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.authority.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class MUserDetails implements UserDetails {
	
	private static final long serialVersionUID = 8493307231603178227L;

	private final String DEFAULT_ROLE = "USER";
	
	private User user;
	
	public MUserDetails(User user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		
		authorities.add(new SimpleGrantedAuthority(DEFAULT_ROLE));
		
		return authorities;
	}

	@Override
	public String getPassword() {
		return "123";
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
