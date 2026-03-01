package com.example.morsor;

import com.example.morsor.search.DuplicateMatchRow;
import com.example.morsor.search.DuplicatesResponse;
import com.example.morsor.search.SearchResponse;
import com.example.morsor.search.SearchResult;
import com.example.morsor.search.ScoredSearchResult;
import com.example.morsor.search.TroveOption;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class SearchControllerTest {

    @LocalServerPort
    int port;

    final RestTemplate restTemplate = new RestTemplate();

    @Test
    void trovesReturnsListOfTroveOptions() {
        ResponseEntity<List<TroveOption>> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/troves",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TroveOption>>() {}
        );
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).as("Backend should return trove options from loaded data").isNotEmpty();
        assertThat(response.getBody().stream().map(TroveOption::id).toList()).contains("little-prince");
        assertThat(response.getBody().stream().map(TroveOption::name).toList()).contains("Little Prince");
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
        String url = "http://localhost:" + port + "/api/search?trove=little-prince&query=Greek";
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

    @Test
    void duplicatesExcludeSelfMatchWhenSameTroveInPrimaryAndCompare() {
        String url = "http://localhost:" + port + "/api/search/duplicates?primaryTrove=little-prince&compareTrove=little-prince&query=*";
        ResponseEntity<DuplicatesResponse> response = restTemplate.getForEntity(url, DuplicatesResponse.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        DuplicatesResponse body = response.getBody();
        assertThat(body).isNotNull();

        for (DuplicateMatchRow row : body.rows()) {
            String primaryId = row.primary() != null ? row.primary().id() : null;
            if (primaryId == null) continue;
            for (ScoredSearchResult match : row.matches() != null ? row.matches() : List.<ScoredSearchResult>of()) {
                String matchId = match.result() != null ? match.result().id() : null;
                assertThat(matchId)
                        .as("No match must be the same item as the primary (primary id=%s)", primaryId)
                        .isNotEqualTo(primaryId);
            }
        }
    }
}
