package com.generic.dbcache.dao.mongo;

import com.generic.dbcache.value.AbstractValue;
import com.generic.dbcache.value.GenericCollection;

public abstract class AbstractMongoDBCacheDAO<V extends AbstractValue> {

	protected GenericCollection<V> genericCollection(String key,V value) {
		return new GenericCollection<V>().collectionId(key).value(value);
	}
}
