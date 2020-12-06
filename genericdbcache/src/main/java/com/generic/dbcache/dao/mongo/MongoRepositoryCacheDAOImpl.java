package com.generic.dbcache.dao.mongo;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.generic.dbcache.dao.IGenericDBCacheDAO;
import com.generic.dbcache.value.AbstractValue;
import com.generic.dbcache.value.GenericCollection;
import com.mongodb.annotations.ThreadSafe;

@ThreadSafe
public final class MongoRepositoryCacheDAOImpl<V extends AbstractValue> extends AbstractMongoDBCacheDAO<V,MongoRepository<GenericCollection<V>,String>> implements IGenericDBCacheDAO<String,V> {

	public MongoRepositoryCacheDAOImpl(MongoRepository<GenericCollection<V>,String> repository) {
		super(repository);
	}
	
	@Override
	public final V insertIntoCache(String key, V value) {
		return getCaller().insert(genericCollection(key, value)).getValue();
	}

	@Override
	public final V updateWithinCache(String key, V value) {
		Optional<GenericCollection<V>> optionalGenericCollection = getCaller().findById(key);
		if(optionalGenericCollection.isPresent()) {
			GenericCollection<V> genericCollection = optionalGenericCollection.get();
			genericCollection.value(value);
			return getCaller().save(genericCollection).getValue();
		}
		return genericCollection(key, value).getValue();
	}

	@Override
	public final boolean deleteFromCache(String key) {
		Optional<GenericCollection<V>> optionalGenericCollection = getCaller().findById(key);
		if(optionalGenericCollection.isPresent()) {
			getCaller().deleteById(key);
			return true;
		}
		return false;
	}

	@Override
	public final V getFromCache(String key) {
		Optional<GenericCollection<V>> optionalGenericCollection = getCaller().findById(key);
		if(optionalGenericCollection.isPresent()) {
			return optionalGenericCollection.get().getValue();
		}
		return null;
	}

	@Override
	public final boolean existsInsideCache(String key) {
		return getCaller().existsById(key);
	}
}
