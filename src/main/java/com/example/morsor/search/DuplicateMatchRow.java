package com.example.morsor.search;

import java.util.List;

public record DuplicateMatchRow(SearchResult primary, List<ScoredSearchResult> matches) {}
