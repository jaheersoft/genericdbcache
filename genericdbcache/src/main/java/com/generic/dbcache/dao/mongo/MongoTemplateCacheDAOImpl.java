package com.generic.dbcache.dao.mongo;

import java.lang.reflect.ParameterizedType;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import com.generic.dbcache.dao.IGenericDBCacheDAO;
import com.generic.dbcache.util.MongoDBUtility;
import com.generic.dbcache.value.AbstractValue;
import com.generic.dbcache.value.GenericCollection;
import com.mongodb.annotations.ThreadSafe;

@ThreadSafe
public final class MongoTemplateCacheDAOImpl<V extends AbstractValue> extends AbstractMongoDBCacheDAO<V,MongoTemplate> implements IGenericDBCacheDAO<String,V> {
	
	public MongoTemplateCacheDAOImpl(MongoTemplate template) {
		super(template);
	}

	@SuppressWarnings("unchecked")
	private Class<GenericCollection<V>> entityClass() {
		return ((Class<GenericCollection<V>>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
	}
	
	@Override
	public final V insertIntoCache(String key, V value) {
		return getCaller().insert(genericCollection(key, value)).getValue();
	}

	@Override
	public final V updateWithinCache(String key, V value) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(key));
		Update update = MongoDBUtility.buildBaseUpdate(value);
		return getCaller().findAndModify(query, update, entityClass()).getValue();
	}

	@Override
	public final boolean deleteFromCache(String key) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(key));
		return getCaller().remove(query).wasAcknowledged();
	}

	@Override
	public final V getFromCache(String key) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(key));
		return getCaller().findById(query, entityClass()).getValue();
	}

	@Override
	public final boolean existsInsideCache(String key) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(key));
		return getCaller().exists(query, entityClass());
	}
}
