package com.generic.dbcache.util;

import java.lang.reflect.Field;

import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.annotations.ThreadSafe;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@ThreadSafe
public final class MongoDBUtility {

	private MongoDBUtility() {}
	
	public static <T> MongoCollection<T> genericMongoCollection(MongoDatabase mongoDatabase,String collectionName,Class<T> genericClass) {
		return mongoDatabase.getCollection(collectionName,genericClass);
	}
	
	public static <T> Update buildBaseUpdate(T t) {
		Update update = new Update();
		Field[] fields = t.getClass().getDeclaredFields();
		for(Field field : fields) {
			field.setAccessible(true);
			try {
				Object value = field.get(t);
				if(value != null) {
					update.set(field.getName(), value);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return update;
	}
}
