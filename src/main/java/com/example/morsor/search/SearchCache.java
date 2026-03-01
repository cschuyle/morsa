package com.example.morsor.search;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Server-side cache for search/duplicates/uniques full result sets.
 * Key = query params (no page/size). Value = full list. Pagination = slice the cached list.
 * TTL 5 minutes. Memory limited to 1GB; when exceeded, new results are not cached.
 */
@Component
public class SearchCache {

    private static final long TTL_MS = 5 * 60 * 1000;
    private static final long MAX_BYTES = 1024L * 1024 * 1024; // 1GB
    /** Rough estimate per item (search result, duplicate row, or unique result) for memory cap. */
    private static final long ESTIMATED_BYTES_PER_ITEM = 1024;

    private final ConcurrentHashMap<String, Entry<?>> cache = new ConcurrentHashMap<>();
    private long totalBytes;

    /**
     * @return result with data and whether it was cached (false if cache memory limit would be exceeded)
     */
    public <T> CacheResult<T> getOrCompute(String key, java.util.function.Supplier<List<T>> supplier) {
        long now = System.currentTimeMillis();
        Entry<?> entry = cache.get(key);
        if (entry != null && now < entry.expiryAt) {
            @SuppressWarnings("unchecked")
            List<T> data = (List<T>) entry.data;
            return new CacheResult<>(data, true);
        }
        List<T> data = supplier.get();
        long estimatedBytes = estimateSize(data);
        synchronized (this) {
            evictExpired(now);
            if (totalBytes + estimatedBytes > MAX_BYTES) {
                return new CacheResult<>(data, false);
            }
            cache.put(key, new Entry<>(data, now + TTL_MS, estimatedBytes));
            totalBytes += estimatedBytes;
        }
        return new CacheResult<>(data, true);
    }

    private void evictExpired(long now) {
        cache.entrySet().removeIf(e -> {
            Entry<?> ent = e.getValue();
            if (now >= ent.expiryAt) {
                totalBytes -= ent.estimatedBytes;
                return true;
            }
            return false;
        });
    }

    private static long estimateSize(List<?> list) {
        if (list == null) return 0;
        return (long) list.size() * ESTIMATED_BYTES_PER_ITEM;
    }

    public record CacheResult<T>(List<T> data, boolean cached) {}

    private static class Entry<T> {
        final List<T> data;
        final long expiryAt;
        final long estimatedBytes;

        Entry(List<T> data, long expiryAt, long estimatedBytes) {
            this.data = data;
            this.expiryAt = expiryAt;
            this.estimatedBytes = estimatedBytes;
        }
    }
}
