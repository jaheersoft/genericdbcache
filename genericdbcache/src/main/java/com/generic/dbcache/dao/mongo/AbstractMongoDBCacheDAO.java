package com.generic.dbcache.dao.mongo;

import java.util.Date;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import com.generic.dbcache.exception.CacheException;
import com.generic.dbcache.exception.CacheException.CacheOperations;
import com.generic.dbcache.exception.CacheException.Database;
import com.generic.dbcache.value.AbstractValue;
import com.generic.dbcache.value.GenericCollection;

public abstract class AbstractMongoDBCacheDAO<V extends AbstractValue, C> {

	private C currentMongoDBCaller;

	public AbstractMongoDBCacheDAO(C currentMongoDBCaller) {
		this.currentMongoDBCaller = currentMongoDBCaller;
	}

	public C getCurrentMongoDBCaller() {
		return currentMongoDBCaller;
	}

	public final GenericCollection<V> newGenericCollection(String key, V value) {
		value.valueId(key + "-" + value.getClass().getName());
		return new GenericCollection<V>().collectionId(key).value(value).createdAt(new Date()).createdBy("CLIENT");
	}

	public final V decorateInsertIntoCache(String key, V value, BiFunction<String, V, V> insertIntoCache)
			throws CacheException {
		try {
			return insertIntoCache.apply(key, value);
		} catch (Exception e) {
			throw new CacheException.Builder(e).inDatabase(Database.MONGODB).whilePerforming(CacheOperations.INSERT)
					.forKey(key).withCollectionValues(value.toString()).build();
		}
	}

	public final V decorateUpdateWithinCache(String key, V value, BiFunction<String, V, V> updateWithinCache)
			throws CacheException {
		try {
			return updateWithinCache.apply(key, value);
		} catch (Exception e) {
			throw new CacheException.Builder(e).inDatabase(Database.MONGODB).whilePerforming(CacheOperations.UPDATE)
					.forKey(key).withCollectionValues(value.toString()).build();
		}
	}

	public final boolean decorateDeleteFromCache(String key, Predicate<String> deleteFromCache) throws CacheException {
		try {
			return deleteFromCache.test(key);
		} catch (Exception e) {
			throw new CacheException.Builder(e).inDatabase(Database.MONGODB).whilePerforming(CacheOperations.DELETE)
					.forKey(key).build();
		}
	}

	public final V decorateGetFromCache(String key, Function<String, V> getFromCache) throws CacheException {
		try {
			return getFromCache.apply(key);
		} catch (Exception e) {
			throw new CacheException.Builder(e).inDatabase(Database.MONGODB).whilePerforming(CacheOperations.RETRIEVE)
					.forKey(key).build();
		}
	}

	public final boolean decorateExistsInsideCache(String key, Predicate<String> existsInsideCache)
			throws CacheException {
		try {
			return existsInsideCache.test(key);
		} catch (Exception e) {
			throw new CacheException.Builder(e).inDatabase(Database.MONGODB).whilePerforming(CacheOperations.EXISTS)
					.forKey(key).build();
		}
	}
}
