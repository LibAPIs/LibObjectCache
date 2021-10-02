package com.mclarkdev.tools.libobjectcache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap.KeySetView;

import com.mclarkdev.tools.libmetrics.LibMetrics;

public class LibObjectCache {

	private static ConcurrentHashMap<String, LibObjectCache> caches = new ConcurrentHashMap<>();

	private final String name;
	private final LibMetrics stats;
	private final ConcurrentHashMap<String, LibObjectCacheCachedObject> cache;

	public LibObjectCache(String name) {

		this.name = name;
		this.stats = LibMetrics.instance();
		this.cache = new ConcurrentHashMap<>();
	}

	public String getName() {

		return name;
	}

	public void put(String key, LibObjectCacheCachedObject value) {

		stats.hitCounter("cache", name, "add");
		cache.put(key, value);
	}

	public int count() {

		return cache.size();
	}

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

	public LibObjectCacheCachedObject get(String key) {

		if (cache.containsKey(key)) {
			stats.hitCounter("cache", name, "hit");
		} else {
			stats.hitCounter("cache", name, "miss");
		}

		return cache.get(key);
	}

	public KeySetView<String, LibObjectCacheCachedObject> getKeys() {
		return cache.keySet();
	}

	public LibObjectCacheCachedObject remove(String key) {

		stats.hitCounter("cache", name, "remove");
		return cache.remove(key);
	}

	public void clearCache() {

		synchronized (cache) {

			double num = cache.size();
			stats.hitCounter(num, "cache", name, "clear");
			cache.clear();
		}
	}

	public static synchronized LibObjectCache getCache(String name) {

		if (!caches.containsKey(name)) {
			caches.put(name, new LibObjectCache(name));
		}

		return caches.get(name);
	}
}
