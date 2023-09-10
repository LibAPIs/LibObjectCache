package com.mclarkdev.tools.libobjectcache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap.KeySetView;

import com.mclarkdev.tools.libmetrics.LibMetrics;

/**
 * LibObjectCache // LibObjectCache
 * 
 * A simple java helper library for managing a cache of objects.
 */
public class LibObjectCache {

	private static ConcurrentHashMap<String, LibObjectCache> caches = new ConcurrentHashMap<>();

	private final String name;
	private final LibMetrics stats;
	private final ConcurrentHashMap<String, LibObjectCacheCachedObject> cache;

	/**
	 * Create a new basic object cache.
	 * 
	 * @param name the name of the cache
	 */
	private LibObjectCache(String name) {

		this.name = name;
		this.stats = LibMetrics.instance();
		this.cache = new ConcurrentHashMap<>();
	}

	/**
	 * Returns the name of the cache.
	 * 
	 * @return name of the cache
	 */
	public String getName() {

		return name;
	}

	/**
	 * Put an object in the cache.
	 * 
	 * @param key   the object key
	 * @param value the object value
	 */
	public void put(String key, LibObjectCacheCachedObject value) {

		stats.hitCounter("cache", name, "add");
		cache.put(key, value);
	}

	/**
	 * Returns the number of items in the cache.
	 * 
	 * @return number of items
	 */
	public int count() {

		return cache.size();
	}

	/**
	 * Removed expired items from the cache.
	 * 
	 * @return number removed
	 */
	public int expireOldObjects() {

		int removed = 0;
		for (Map.Entry<String, LibObjectCacheCachedObject> entry : cache.entrySet()) {

			// skip if not expired
			if (!entry.getValue().isExpired()) {
				continue;
			}

			// skip if not removed
			if (remove(entry.getKey()) == null) {
				continue;
			}

			removed++;
		}

		return removed;
	}

	/**
	 * Returns an object from the cache with the given key.
	 * 
	 * @param key the object key
	 * @return the object value
	 */
	public LibObjectCacheCachedObject get(String key) {

		if (cache.containsKey(key)) {
			stats.hitCounter("cache", name, "hit");
		} else {
			stats.hitCounter("cache", name, "miss");
		}

		return cache.get(key);
	}

	/**
	 * Returns a list of all cached objects.
	 * 
	 * @return
	 */
	public KeySetView<String, LibObjectCacheCachedObject> getKeys() {
		return cache.keySet();
	}

	/**
	 * Removes an object from the cache with the given key.
	 * 
	 * @param key the object key
	 * @return the object value
	 */
	public LibObjectCacheCachedObject remove(String key) {

		stats.hitCounter("cache", name, "remove");
		return cache.remove(key);
	}

	/**
	 * Removes all items from the cache.
	 */
	public void clearCache() {

		synchronized (cache) {

			double num = cache.size();
			stats.hitCounter(num, "cache", name, "clear");
			cache.clear();
		}
	}

	/**
	 * Returns an instance of the given cache.
	 * 
	 * @param name name of the cache
	 * @return the cache instance
	 */
	public static synchronized LibObjectCache getCache(String name) {

		if (!caches.containsKey(name)) {
			caches.put(name, new LibObjectCache(name));
		}

		return caches.get(name);
	}
}
