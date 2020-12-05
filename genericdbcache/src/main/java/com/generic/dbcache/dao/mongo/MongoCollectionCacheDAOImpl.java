package com.generic.dbcache.dao.mongo;

import org.bson.Document;
import static com.mongodb.client.model.Filters.eq;
import com.generic.dbcache.dao.IGenericDBCacheDAO;
import com.generic.dbcache.value.AbstractValue;
import com.generic.dbcache.value.GenericCollection;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;

public class MongoCollectionCacheDAOImpl<V extends AbstractValue> extends AbstractMongoDBCacheDAO<V,MongoCollection<GenericCollection<V>>> implements IGenericDBCacheDAO<String,V>{

	private MongoCollection<GenericCollection<V>> genericCollections;
	
	public MongoCollectionCacheDAOImpl(MongoCollection<GenericCollection<V>> genericCollections) {
		this.genericCollections = genericCollections;
	}
	
	@Override
	public V insertIntoCache(String key, V value) {
		if(genericCollections.insertOne(genericCollection(key, value)).wasAcknowledged()) {
			return getFromCache(key);
		}
		return value;
	}

	@Override
	public V updateWithinCache(String key, V value) {
		Document filterById = new Document("_id",key);
		FindOneAndReplaceOptions returnDocAfterReplace = new FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER);
		GenericCollection<V> genericCollection = genericCollections.find(eq("id",key)).first();
		genericCollection.value(value);
		return genericCollections.findOneAndReplace(filterById, genericCollection,returnDocAfterReplace).getValue();
	}

	@Override
	public boolean deleteFromCache(String key) {
		Document filterById = new Document("_id",key);
		return genericCollections.deleteOne(filterById).wasAcknowledged();
	}

	@Override
	public V getFromCache(String key) {
		return genericCollections.find(eq("id",key)).first().getValue();
	}

	@Override
	public boolean existsInsideCache(String key) {
		FindIterable<GenericCollection<V>> findIterable = genericCollections.find(eq("id",key));
		if(findIterable != null) {
			GenericCollection<V> genericCollection = findIterable.first();
			if(genericCollection != null && genericCollection.getValue() != null) {
				if(key.equals(genericCollection.getCollectionId()) && genericCollection.getValue() != null) {
					return true;
				}
			}
		}
		return false;
	}

}
