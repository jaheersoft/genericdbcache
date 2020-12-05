package com.generic.dbcache.dao;

public interface IGenericDBCacheDAO<K,V> {
	
	V insertIntoCache(K key,V value);
	
	V updateWithinCache(K key,V value);
	
	boolean deleteFromCache(K key);
	
	V getFromCache(K key);
	
	boolean existsInsideCache(K key);
}
