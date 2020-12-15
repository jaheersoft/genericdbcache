package com.generic.dbcache.dao.mongo;

import java.lang.reflect.ParameterizedType;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import com.generic.dbcache.dao.IGenericDBCacheDAO;
import com.generic.dbcache.exception.CacheException;
import com.generic.dbcache.util.MongoDBUtility;
import com.generic.dbcache.value.AbstractValue;
import com.generic.dbcache.value.GenericCollection;
import com.mongodb.annotations.ThreadSafe;

@ThreadSafe
public final class MongoTemplateCacheDAOImpl<V extends AbstractValue> extends AbstractMongoDBCacheDAO<V, MongoTemplate>
		implements IGenericDBCacheDAO<String, V> {

	private final BiFunction<String, V, V> INSERT_INTO_CACHE_USING_MONGOTEMPLATE = (key,
			value) -> getCurrentMongoDBCaller().insert(newGenericCollection(key, value)).getValue();

	private final BiFunction<String, V, V> UPDATE_WITHIN_CACHE_USING_MONGOTEMPLATE = (key, value) -> {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(key));
		Update update = MongoDBUtility.buildBaseUpdate(value);
		return getCurrentMongoDBCaller().findAndModify(query, update, entityClass()).getValue();
	};

	private final Predicate<String> DELETE_FROM_CACHE_USING_MONGOTEMPLATE = (key) -> {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(key));
		return getCurrentMongoDBCaller().remove(query).wasAcknowledged();
	};

	private final Function<String, V> GET_FROM_CACHE_USING_MONGOTEMPLATE = (key) -> {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(key));
		return getCurrentMongoDBCaller().findById(query, entityClass()).getValue();
	};

	private final Predicate<String> EXISTS_INSIDE_CACHE_USING_MONGOTEMPLATE = (key) -> {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(key));
		return getCurrentMongoDBCaller().exists(query, entityClass());
	};

	public MongoTemplateCacheDAOImpl(MongoTemplate template) {
		super(template);
	}

	@SuppressWarnings("unchecked")
	private Class<GenericCollection<V>> entityClass() {
		return ((Class<GenericCollection<V>>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0]);
	}

	@Override
	public final V insertIntoCache(String key, V value) throws CacheException {
		return decorateInsertIntoCache(key, value, INSERT_INTO_CACHE_USING_MONGOTEMPLATE);
	}

	@Override
	public final V updateWithinCache(String key, V value) throws CacheException {
		return decorateUpdateWithinCache(key, value, UPDATE_WITHIN_CACHE_USING_MONGOTEMPLATE);
	}

	@Override
	public final boolean deleteFromCache(String key) throws CacheException {
		return decorateDeleteFromCache(key, DELETE_FROM_CACHE_USING_MONGOTEMPLATE);
	}

	@Override
	public final V getFromCache(String key) throws CacheException {
		return decorateGetFromCache(key, GET_FROM_CACHE_USING_MONGOTEMPLATE);
	}

	@Override
	public final boolean existsInsideCache(String key) throws CacheException {
		return decorateExistsInsideCache(key, EXISTS_INSIDE_CACHE_USING_MONGOTEMPLATE);
	}
}
