package com.example.morsor.search;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SearchResultWithScore(@JsonUnwrapped SearchResult result, Double score) {}
