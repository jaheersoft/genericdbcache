package com.generic.dbcache.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.generic.dbcache.dao.IGenericDBCacheDAO;
import com.generic.dbcache.dao.mongo.MongoCollectionCacheDAOImpl;
import com.generic.dbcache.dao.mongo.MongoRepositoryCacheDAOImpl;
import com.generic.dbcache.dao.mongo.MongoTemplateCacheDAOImpl;
import com.generic.dbcache.value.AbstractValue;
import com.generic.dbcache.value.GenericCollection;
import com.mongodb.client.MongoCollection;

@Configuration
public final class MongoDBConfiguration {
	
	/*@Value("${mongodb.connection.uri}")
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
	}*/
	
	@Bean(name = "MongoRepositoryCacheDAO")
	public <V extends AbstractValue> IGenericDBCacheDAO<String,V> mongoRepositoryCacheDAO(MongoRepository<GenericCollection<V>,String> repository) {
		return new MongoRepositoryCacheDAOImpl<V>(repository);
	}
	
	@Bean(name = "MongoTemplateCacheDAO")
	public <V extends AbstractValue> IGenericDBCacheDAO<String,V> mongoTemplateCacheDAO(MongoTemplate template) {
		return new MongoTemplateCacheDAOImpl<V>(template);
	}
	
	@Bean(name = "MongoTemplateCacheDAO")
	public <V extends AbstractValue> IGenericDBCacheDAO<String,V> mongoCollectionCacheDAO(MongoCollection<GenericCollection<V>> genericCollections) {
		return new MongoCollectionCacheDAOImpl<V>(genericCollections);
	}
}
