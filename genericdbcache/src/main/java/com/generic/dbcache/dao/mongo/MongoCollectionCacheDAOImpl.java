package com.generic.dbcache.dao.mongo;

import org.bson.Document;
import static com.mongodb.client.model.Filters.eq;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import com.generic.dbcache.dao.IGenericDBCacheDAO;
import com.generic.dbcache.exception.CacheException;
import com.generic.dbcache.value.AbstractValue;
import com.generic.dbcache.value.GenericCollection;
import com.mongodb.annotations.ThreadSafe;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;

@ThreadSafe
public final class MongoCollectionCacheDAOImpl<V extends AbstractValue> extends
		AbstractMongoDBCacheDAO<V, MongoCollection<GenericCollection<V>>> implements IGenericDBCacheDAO<String, V> {

	private final BiFunction<String, V, V> INSERT_INTO_CACHE_USING_MONGOCOLLECTION = (key, value) -> {
		if (getCurrentMongoDBCaller().insertOne(newGenericCollection(key, value)).wasAcknowledged()) {
			return getCurrentMongoDBCaller().find(eq("id", key)).first().getValue();
		}
		return value;
	};

	private final BiFunction<String, V, V> UPDATE_WITHIN_CACHE_USING_MONGOCOLLECTION = (key, value) -> {
		Document filterById = new Document("_id", key);
		FindOneAndReplaceOptions returnDocAfterReplace = new FindOneAndReplaceOptions()
				.returnDocument(ReturnDocument.AFTER);
		GenericCollection<V> genericCollection = getCurrentMongoDBCaller().find(eq("id", key)).first();
		genericCollection.value(value);
		return getCurrentMongoDBCaller().findOneAndReplace(filterById, genericCollection, returnDocAfterReplace)
				.getValue();
	};

	private final Predicate<String> DELETE_FROM_CACHE_USING_MONGOCOLLECTION = (key) -> {
		Document filterById = new Document("_id", key);
		return getCurrentMongoDBCaller().deleteOne(filterById).wasAcknowledged();
	};

	private final Function<String, V> GET_FROM_CACHE_USING_MONGOCOLLECTION = (key) -> getCurrentMongoDBCaller()
			.find(eq("id", key)).first().getValue();

	private final Predicate<String> EXISTS_INSIDE_CACHE_USING_MONGOCOLLECTION = (key) -> {
		FindIterable<GenericCollection<V>> findIterable = getCurrentMongoDBCaller().find(eq("id", key));
		if (findIterable != null) {
			GenericCollection<V> genericCollection = findIterable.first();
			if (genericCollection != null && genericCollection.getValue() != null) {
				if (key.equals(genericCollection.getCollectionId()) && genericCollection.getValue() != null) {
					return true;
				}
			}
		}
		return false;
	};

	public MongoCollectionCacheDAOImpl(MongoCollection<GenericCollection<V>> genericCollections) {
		super(genericCollections);
	}

	@Override
	public final V insertIntoCache(String key, V value) throws CacheException {
		return decorateInsertIntoCache(key, value, INSERT_INTO_CACHE_USING_MONGOCOLLECTION);
	}

	@Override
	public final V updateWithinCache(String key, V value) throws CacheException {
		return decorateUpdateWithinCache(key, value, UPDATE_WITHIN_CACHE_USING_MONGOCOLLECTION);
	}

	@Override
	public final boolean deleteFromCache(String key) throws CacheException {
		return decorateDeleteFromCache(key, DELETE_FROM_CACHE_USING_MONGOCOLLECTION);
	}

	@Override
	public final V getFromCache(String key) throws CacheException {
		return decorateGetFromCache(key, GET_FROM_CACHE_USING_MONGOCOLLECTION);
	}

	@Override
	public final boolean existsInsideCache(String key) throws CacheException {
		return decorateExistsInsideCache(key, EXISTS_INSIDE_CACHE_USING_MONGOCOLLECTION);
	}
}
