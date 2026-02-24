package com.example.morsor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CollectionToSearchResultMapperTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void mapsCollectionJsonToSearchResults() throws Exception {
        try (InputStream in = new ClassPathResource("search-data.json").getInputStream()) {
            JsonNode root = objectMapper.readTree(in);
            List<SearchResult> results = CollectionToSearchResultMapper.mapRootToSearchResults(root);
            assertThat(results).hasSize(2);
            assertThat(results.get(0).trove()).isEqualTo("Little Prince");
            assertThat(results.get(0).title()).isEqualTo("Princi i Vogël - The Little Prince in Albanian");
            assertThat(results.get(0).id()).isEqualTo("little-prince-0");
            assertThat(results.get(1).id()).isEqualTo("PP-4277");
            assertThat(results.get(1).title()).isEqualTo("The Little Prince, in Ancient Greek");
        }
    }
}
