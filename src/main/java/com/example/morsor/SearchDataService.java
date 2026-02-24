package com.example.morsor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Stream;

@Service
public class SearchDataService {

    private final Resource dataResource;
    private final ObjectMapper objectMapper;

    private List<SearchResult> allResults;

    public SearchDataService(
            @Value("classpath:search-data.json") Resource dataResource,
            ObjectMapper objectMapper) {
        this.dataResource = dataResource;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    void loadData() throws IOException {
        try (InputStream in = dataResource.getInputStream()) {
            JsonNode root = objectMapper.readTree(in);
            allResults = CollectionToSearchResultMapper.mapRootToSearchResults(root);
        }
    }

    public List<SearchResult> search(String trove, String query) {
        if (allResults == null) {
            return List.of();
        }
        String troveLower = trove == null ? "" : trove.trim().toLowerCase();
        String queryLower = query == null ? "" : query.trim().toLowerCase();
        Stream<SearchResult> stream = allResults.stream();
        if (!troveLower.isEmpty()) {
            stream = stream.filter(r -> r.trove() != null && r.trove().toLowerCase().contains(troveLower));
        }
        if (!queryLower.isEmpty()) {
            stream = stream.filter(r ->
                    (r.title() != null && r.title().toLowerCase().contains(queryLower))
                            || (r.snippet() != null && r.snippet().toLowerCase().contains(queryLower)));
        }
        return stream.toList();
    }

    public List<String> getTroveNames() {
        if (allResults == null) {
            return List.of();
        }
        return allResults.stream()
                .map(SearchResult::trove)
                .filter(t -> t != null && !t.isBlank())
                .distinct()
                .sorted()
                .toList();
    }
}
