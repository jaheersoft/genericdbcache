package com.generic.dbcache.dao.mongo;

import java.util.Date;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.generic.dbcache.dao.IGenericDBCacheDAO;
import com.generic.dbcache.exception.CacheException;
import com.generic.dbcache.value.AbstractValue;
import com.generic.dbcache.value.GenericCollection;
import com.mongodb.annotations.ThreadSafe;

@ThreadSafe
public final class MongoRepositoryCacheDAOImpl<V extends AbstractValue>
		extends AbstractMongoDBCacheDAO<V, MongoRepository<GenericCollection<V>, String>>
		implements IGenericDBCacheDAO<String, V> {

	private final BiFunction<String, V, V> INSERT_INTO_CACHE_USING_MONGOREPOSITORY = (key,
			value) -> getCurrentMongoDBCaller().insert(newGenericCollection(key, value)).getValue();

	private final BiFunction<String, V, V> UPDATE_WITHIN_CACHE_USING_MONGOREPOSITORY = (key, value) -> {
		Optional<GenericCollection<V>> optionalGenericCollection = getCurrentMongoDBCaller().findById(key);
		if (optionalGenericCollection.isPresent()) {
			GenericCollection<V> genericCollection = optionalGenericCollection.get();
			genericCollection.value(value).updateAt(new Date()).updatedBy("CLIENT");
			return getCurrentMongoDBCaller().save(genericCollection).getValue();
		}
		return newGenericCollection(key, value).getValue();
	};

	private final Predicate<String> DELETE_FROM_CACHE_USING_MONGOREPOSITORY = (key) -> {
		Optional<GenericCollection<V>> optionalGenericCollection = getCurrentMongoDBCaller().findById(key);
		if (optionalGenericCollection.isPresent()) {
			getCurrentMongoDBCaller().deleteById(key);
			return true;
		}
		return false;
	};

	private final Function<String, V> GET_FROM_CACHE_USING_MONGOREPOSITORY = (key) -> {
		Optional<GenericCollection<V>> optionalGenericCollection = getCurrentMongoDBCaller().findById(key);
		if (optionalGenericCollection.isPresent()) {
			return optionalGenericCollection.get().getValue();
		}
		return null;
	};

	private final Predicate<String> EXISTS_INSIDE_CACHE_USING_MONGOREPOSITORY = (key) -> getCurrentMongoDBCaller()
			.existsById(key);

	public MongoRepositoryCacheDAOImpl(MongoRepository<GenericCollection<V>, String> repository) {
		super(repository);
	}

	@Override
	public final V insertIntoCache(String key, V value) throws CacheException {
		return decorateInsertIntoCache(key, value, INSERT_INTO_CACHE_USING_MONGOREPOSITORY);
	}

	@Override
	public final V updateWithinCache(String key, V value) throws CacheException {
		return decorateUpdateWithinCache(key, value, UPDATE_WITHIN_CACHE_USING_MONGOREPOSITORY);
	}

	@Override
	public final boolean deleteFromCache(String key) throws CacheException {
		return decorateDeleteFromCache(key, DELETE_FROM_CACHE_USING_MONGOREPOSITORY);
	}

	@Override
	public final V getFromCache(String key) throws CacheException {
		return decorateGetFromCache(key, GET_FROM_CACHE_USING_MONGOREPOSITORY);
	}

	@Override
	public final boolean existsInsideCache(String key) throws CacheException {
		return decorateExistsInsideCache(key, EXISTS_INSIDE_CACHE_USING_MONGOREPOSITORY);
	}
}
