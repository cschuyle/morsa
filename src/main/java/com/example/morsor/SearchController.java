package com.example.morsor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SearchController {

    private final SearchDataService searchDataService;

    public SearchController(SearchDataService searchDataService) {
        this.searchDataService = searchDataService;
    }

    @GetMapping("/troves")
    public List<String> troves() {
        return searchDataService.getTroveNames();
    }

    @GetMapping("/search")
    public SearchResponse search(
            @RequestParam(required = false, defaultValue = "") String trove,
            @RequestParam(required = false, defaultValue = "") String query) {
        List<SearchResult> results = searchDataService.search(trove, query);
        return new SearchResponse(results.size(), results);
    }
}
