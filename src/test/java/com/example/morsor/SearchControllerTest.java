package com.example.morsor;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SearchControllerTest {

    @LocalServerPort
    int port;

    final RestTemplate restTemplate = new RestTemplate();

    @Test
    void trovesReturnsListOfTroveNames() {
        ResponseEntity<List<String>> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/troves",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<String>>() {}
        );
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).as("Backend should return trove names from loaded data").isNotEmpty();
        assertThat(response.getBody()).contains("Little Prince");
    }

    @Test
    void searchReturnsDataFromJson() {
        // With no filters we get all loaded results (verifies data is loaded from JSON)
        ResponseEntity<SearchResponse> allResponse = restTemplate.exchange(
                "http://localhost:" + port + "/api/search",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<SearchResponse>() {}
        );
        assertThat(allResponse.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(allResponse.getBody()).isNotNull();
        assertThat(allResponse.getBody().results()).as("Data should be loaded from JSON").isNotEmpty();

        // Filter by trove and query: should find the Ancient Greek item
        String url = "http://localhost:" + port + "/api/search?trove=Prince&query=Greek";
        ResponseEntity<SearchResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<SearchResponse>() {}
        );
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        SearchResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.results()).as("Filtered search should return the Ancient Greek item").isNotEmpty();
        SearchResult greekResult = body.results().stream()
                .filter(r -> "PP-4277".equals(r.id()))
                .findFirst()
                .orElse(null);
        assertThat(greekResult).isNotNull();
        assertThat(greekResult.trove()).isEqualTo("Little Prince");
        assertThat(greekResult.title()).isEqualTo("The Little Prince, in Ancient Greek");
    }
}
