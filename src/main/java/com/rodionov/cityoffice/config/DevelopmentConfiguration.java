package com.rodionov.cityoffice.config;

import java.net.UnknownHostException;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

import com.mongodb.Mongo;

@Configuration
@Profile("development")
public class DevelopmentConfiguration extends AbstractMongoConfiguration {
	@Override
	protected String getDatabaseName() {
		return "cityoffice-db";
	}

	@SuppressWarnings("deprecation")
	@Override
	public Mongo mongo() {
		try {
			return new Mongo("mongodb://localhost/cityoffice");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected String getMappingBasePackage() {
		return "com.rodionov.cityoffice.repository";
	}
}
