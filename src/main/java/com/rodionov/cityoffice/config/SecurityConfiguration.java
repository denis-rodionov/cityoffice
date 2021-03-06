package com.rodionov.cityoffice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter  {
	
	@Autowired
	UserDetailsService userDetailsService;
    
	@Override
    protected void configure(HttpSecurity http) throws Exception { 
      http
        .authorizeRequests()
          .antMatchers("/index.html", "/home.html", "/login.html", "/").permitAll()
          .anyRequest().authenticated()
		.and()
		.httpBasic()
		.and()
		.logout()
			.deleteCookies("remove")
        	.invalidateHttpSession(true)
        	.logoutUrl("/logout")
        	.logoutSuccessUrl("/")
        .and().csrf().disable();
        
        //	.and();
		//.addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class)
		//.csrf().csrfTokenRepository(csrfTokenRepository());   
    }
    
    @Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//		auth
//			.inMemoryAuthentication()
//				.withUser("user").password("123").roles("USER");
		auth.userDetailsService(userDetailsService);
	}
    
    @SuppressWarnings("unused")
	private CsrfTokenRepository csrfTokenRepository() {
    	  HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
    	  repository.setHeaderName("X-XSRF-TOKEN");
    	  return repository;
    	}
}