package com.rodionov.cityoffice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter  {
    
//	@Override
//    protected void configure(HttpSecurity http) throws Exception { 
//      http
//        .httpBasic()
//      .and()
//        .authorizeRequests()
//          .antMatchers("/index.html", "/home.html", "/login.html", "/").permitAll()
//          .antMatchers("/user").permitAll()
//          .anyRequest().authenticated();
//    }
    
    @Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.inMemoryAuthentication()
				.withUser("user").password("123").roles("USER");
	}
}