package com.example.morsor;

import java.util.List;

public record DuplicateMatchRow(SearchResult primary, List<ScoredSearchResult> matches) {}
