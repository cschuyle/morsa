package com.example.morsor.search;

import java.util.List;
import java.util.Map;

public record SearchResponse(long count, List<SearchResultWithScore> results, int page, int size, Map<String, Long> troveCounts, String warning) {
    public SearchResponse(long count, List<SearchResultWithScore> results, int page, int size, Map<String, Long> troveCounts) {
        this(count, results, page, size, troveCounts, null);
    }
}
