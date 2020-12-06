package com.generic.dbcache.dao.mongo;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.generic.dbcache.dao.IGenericDBCacheDAO;
import com.generic.dbcache.value.AbstractValue;
import com.generic.dbcache.value.GenericCollection;

public class MongoRepositoryCacheDAOImpl<V extends AbstractValue> extends AbstractMongoDBCacheDAO<V> implements IGenericDBCacheDAO<String,V> {

	private MongoRepository<GenericCollection<V>,String> repository;
	
	public MongoRepositoryCacheDAOImpl(MongoRepository<GenericCollection<V>,String> repository) {
		this.repository = repository;
	}
	
	@Override
	public V insertIntoCache(String key, V value) {
		return repository.insert(genericCollection(key, value)).getValue();
	}

	@Override
	public V updateWithinCache(String key, V value) {
		Optional<GenericCollection<V>> optionalGenericCollection = repository.findById(key);
		if(optionalGenericCollection.isPresent()) {
			GenericCollection<V> genericCollection = optionalGenericCollection.get();
			genericCollection.value(value);
			return repository.save(genericCollection).getValue();
		}
		return genericCollection(key, value).getValue();
	}

	@Override
	public boolean deleteFromCache(String key) {
		Optional<GenericCollection<V>> optionalGenericCollection = repository.findById(key);
		if(optionalGenericCollection.isPresent()) {
			repository.deleteById(key);
			return true;
		}
		return false;
	}

	@Override
	public V getFromCache(String key) {
		Optional<GenericCollection<V>> optionalGenericCollection = repository.findById(key);
		if(optionalGenericCollection.isPresent()) {
			return optionalGenericCollection.get().getValue();
		}
		return null;
	}

	@Override
	public boolean existsInsideCache(String key) {
		return repository.existsById(key);
	}
}
