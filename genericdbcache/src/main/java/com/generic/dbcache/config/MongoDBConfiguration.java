package com.generic.dbcache.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

@Configuration
public final class MongoDBConfiguration {
	
	@Value("${mongodb.connection.uri}")
	private String mongodbConnectionURI;
	
	@Value("${database.name}")
	private String dataBaseName;
	
	@Primary
	@Bean(name = "genericMongoClient")
	public MongoClient genericMongoClient() {
		final ConnectionString connectionString = new ConnectionString(mongodbConnectionURI);
		final MongoClientSettings mongoClientSettings = MongoClientSettings.builder().applyConnectionString(connectionString).build();
		return MongoClients.create(mongoClientSettings);
	}

	@Primary
	@Bean(name = "genericMongoTemplate")
	public MongoTemplate genericMongoTemplate() {
		return new MongoTemplate(genericMongoClient(),dataBaseName);
	}
	
	@Primary
	@Bean(name = "genericMongoDatabase")
	public MongoDatabase genericMongoDatabase() {
		return genericMongoClient().getDatabase(dataBaseName);
	}
}
