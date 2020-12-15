package com.generic.dbcache.function;

import static com.mongodb.client.model.Filters.eq;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.generic.dbcache.value.AbstractValue;
import com.generic.dbcache.value.GenericCollection;
import com.mongodb.annotations.ThreadSafe;
import com.mongodb.client.MongoCollection;

@ThreadSafe
public final class MongoDBCacheDAOFunctions {
	
	private MongoDBCacheDAOFunctions() {}
	
	private static final <V extends AbstractValue> GenericCollection<V> newGenericCollection(String key,V value) {
		return new GenericCollection<V>().collectionId(key).value(value);
	}
	
	@FunctionalInterface
	private interface CacheThroughDataBaseRespository<K,V,C> {
		V apply(K key,V value,C caller);
	}
	
	public static final CacheThroughDataBaseRespository<String,? extends AbstractValue,MongoRepository<GenericCollection<? extends AbstractValue>,String>> INSERT_INTO_CACHE_THROUGH_MONGOREPOSITORY 
														= (key,value,repository) -> repository.insert(newGenericCollection(key, value)).getValue();
														
	public static final CacheThroughDataBaseRespository<String,? extends AbstractValue,MongoTemplate> INSERT_INTO_CACHE_THROUGH_MONGOTEMPLATE 
														= (key,value,template) -> template.insert(newGenericCollection(key, value)).getValue();
														
	public static final CacheThroughDataBaseRespository<String,? extends AbstractValue,MongoCollection<GenericCollection<? extends AbstractValue>>> INSERT_INTO_CACHE_THROUGH_MONGOCOLLECTION
														= (key,value,genericCollections) -> {
															if(genericCollections.insertOne(newGenericCollection(key, value)).wasAcknowledged()) {
																return genericCollections.find(eq("id",key)).first().getValue();
															}
															return value;
														};
														
	/*public static final CacheThroughDataBaseRespository<String,? extends AbstractValue,MongoRepository<GenericCollection<? extends AbstractValue>,String>> UPDATE_WITHIN_CACHE_THROUGH_MONGOREPOSITORY 
																= (key,value,repository) -> {
																	if(repository.findById(key).isPresent()) {
																		repository.findById(key).get();
																		return repository.save(updateGenericCollection(value,repository.findById(key).get())).getValue();
																	}
																	return newGenericCollection(key, value).getValue();
																};*/
	
}
