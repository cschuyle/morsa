package com.example.morsor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Stream;

@Service
public class SearchDataService {

    private static final Logger log = LoggerFactory.getLogger(SearchDataService.class);

    private final Resource dataResource;
    private final ObjectMapper objectMapper;

    private List<SearchResult> allResults = List.of();

    public SearchDataService(
            @Value("classpath:search-data.json") Resource dataResource,
            ObjectMapper objectMapper) {
        this.dataResource = dataResource;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    void loadData() {
        try (InputStream in = dataResource.getInputStream()) {
            var root = objectMapper.readTree(in);
            allResults = CollectionToSearchResultMapper.mapRootToSearchResults(root);
            log.info("Loaded {} search results from {}", allResults.size(), dataResource.getDescription());
        } catch (Exception e) {
            log.error("Failed to load search data from {}: {}", dataResource.getDescription(), e.getMessage(), e);
            allResults = List.of();
        }
    }

    public List<SearchResult> search(String trove, String query) {
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
        return allResults.stream()
                .map(SearchResult::trove)
                .filter(t -> t != null && !t.isBlank())
                .distinct()
                .sorted()
                .toList();
    }
}
