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

public class MongoTemplateCacheDAOImpl<V extends AbstractValue> extends AbstractMongoDBCacheDAO<V> implements IGenericDBCacheDAO<String,V> {
	
	private MongoTemplate template;
	
	public MongoTemplateCacheDAOImpl(MongoTemplate template) {
		this.template = template;
	}

	@SuppressWarnings("unchecked")
	protected Class<GenericCollection<V>> entityClass() {
		return ((Class<GenericCollection<V>>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
	}
	
	@Override
	public V insertIntoCache(String key, V value) {
		return template.insert(genericCollection(key, value)).getValue();
	}

	@Override
	public V updateWithinCache(String key, V value) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(key));
		Update update = MongoDBUtility.buildBaseUpdate(value);
		return template.findAndModify(query, update, entityClass()).getValue();
	}

	@Override
	public boolean deleteFromCache(String key) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(key));
		return template.remove(query).wasAcknowledged();
	}

	@Override
	public V getFromCache(String key) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(key));
		return template.findById(query, entityClass()).getValue();
	}

	@Override
	public boolean existsInsideCache(String key) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(key));
		return template.exists(query, entityClass());
	}
}
