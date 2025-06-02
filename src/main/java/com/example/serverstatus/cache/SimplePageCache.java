package com.example.serverstatus.cache;

import java.util.concurrent.ConcurrentHashMap;

public class SimplePageCache {
    private static class CacheEntry {
        final String value;
        final long expiry;
        CacheEntry(String value, long expiry) {
            this.value = value;
            this.expiry = expiry;
        }
    }

    private static final ConcurrentHashMap<String, CacheEntry> cache = new ConcurrentHashMap<>();
    private static final long DEFAULT_TTL_MS = 2 * 60 * 1000; // 2 minutes

    public static String get(String key) {
        CacheEntry entry = cache.get(key);
        if (entry == null || System.currentTimeMillis() > entry.expiry) {
            cache.remove(key);
            return null;
        }
        return entry.value;
    }

    public static void put(String key, String value) {
        cache.put(key, new CacheEntry(value, System.currentTimeMillis() + DEFAULT_TTL_MS));
    }

    public static void put(String key, String value, long ttlMs) {
        cache.put(key, new CacheEntry(value, System.currentTimeMillis() + ttlMs));
    }

    public static void clear() {
        cache.clear();
    }
}
