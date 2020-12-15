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

//@Configuration
public final class MongoDBConfiguration {

	/*@Bean(name = "mongoRepositoryCacheDAO")
	public <V extends AbstractValue> IGenericDBCacheDAO<String, V> mongoRepositoryCacheDAO(
			MongoRepository<GenericCollection<V>, String> repository) {
		return new MongoRepositoryCacheDAOImpl<V>(repository);
	}

	@Bean(name = "mongoTemplateCacheDAO")
	public <V extends AbstractValue> IGenericDBCacheDAO<String, V> mongoTemplateCacheDAO(MongoTemplate template) {
		return new MongoTemplateCacheDAOImpl<V>(template);
	}

	@Bean(name = "mongoCollectionCacheDAO")
	public <V extends AbstractValue> IGenericDBCacheDAO<String, V> mongoCollectionCacheDAO(
			MongoCollection<GenericCollection<V>> genericCollections) {
		return new MongoCollectionCacheDAOImpl<V>(genericCollections);
	}*/
}
