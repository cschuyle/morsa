package com.example.morsor.search;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Server-side cache for search/duplicates/uniques full result sets.
 * Key = query params (no page/size). Value = full list. Pagination = slice the cached list.
 * TTL 5 minutes; keeps cache size minimal and response payload one page.
 */
@Component
public class SearchCache {

    private static final long TTL_MS = 5 * 60 * 1000;

    private final ConcurrentHashMap<String, Entry<?>> cache = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public <T> List<T> getOrCompute(String key, java.util.function.Supplier<List<T>> supplier) {
        Entry<?> entry = cache.get(key);
        if (entry != null && System.currentTimeMillis() < entry.expiryAt) {
            return (List<T>) entry.data;
        }
        List<T> data = supplier.get();
        cache.put(key, new Entry<>(data, System.currentTimeMillis() + TTL_MS));
        return data;
    }

    private static class Entry<T> {
        final List<T> data;
        final long expiryAt;

        Entry(List<T> data, long expiryAt) {
            this.data = data;
            this.expiryAt = expiryAt;
        }
    }
}
