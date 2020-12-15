package com.generic.dbcache.dao;

import com.generic.dbcache.exception.CacheException;

public interface IGenericDBCacheDAO<K, V> {

	V insertIntoCache(K key, V value) throws CacheException;

	V updateWithinCache(K key, V value) throws CacheException;

	boolean deleteFromCache(K key) throws CacheException;

	V getFromCache(K key) throws CacheException;

	boolean existsInsideCache(K key) throws CacheException;
}
