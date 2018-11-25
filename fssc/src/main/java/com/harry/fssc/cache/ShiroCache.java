package com.harry.fssc.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.harry.fssc.util.Const;

@SuppressWarnings("unchecked")
public class ShiroCache<K, V> implements Cache<K, V> {

	private static Logger logger = LoggerFactory.getLogger(ShiroCache.class);
	
	private String cacheKey;
	private RedisTemplate<K, V> redisTemplate;

	@SuppressWarnings("rawtypes")
	public ShiroCache(String name, RedisTemplate client) {
		this.cacheKey = Const.REDIS_SHIRO_CACHE + name + ":";
		this.redisTemplate = client;
	}

	@Override
	public V get(K key) throws CacheException {
		logger.debug("ShiroCache#get:"+key);
		redisTemplate.boundValueOps(getCacheKey(key)).expire(Const.GLOBAL_SESSION_TIMEOUT, TimeUnit.MILLISECONDS);
		return redisTemplate.boundValueOps(getCacheKey(key)).get();
	}

	@Override
	public V put(K key, V value) throws CacheException {
		logger.debug("ShiroCache#put:"+key);
		V old = get(key);
		redisTemplate.boundValueOps(getCacheKey(key)).set(value);
		return old;
	}

	@Override
	public V remove(K key) throws CacheException {
		logger.debug("ShiroCache#remove:"+key);
		V old = get(key);
		redisTemplate.delete(getCacheKey(key));
		return old;
	}

	@Override
	public void clear() throws CacheException {
		logger.debug("ShiroCache#clear");
		redisTemplate.delete(keys());
	}

	@Override
	public int size() {
		logger.debug("ShiroCache#size");
		return keys().size();
	}

	@Override
	public Set<K> keys() {
		logger.debug("ShiroCache#keys");
		return redisTemplate.keys(getCacheKey("*"));
	}

	@Override
	public Collection<V> values() {
		Set<K> set = keys();
		List<V> list = new ArrayList<>();
		for (K s : set) {
			list.add(redisTemplate.boundValueOps(s).get());
		}
		return list;
	}

	private K getCacheKey(Object k) {
		return (K) (this.cacheKey + k);
	}
}
