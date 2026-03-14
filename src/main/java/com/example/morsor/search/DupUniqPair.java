package com.example.morsor.search;

import java.util.List;

/** Result of one-pass duplicates + uniques computation for the same (primary, compare, query). */
public record DupUniqPair(List<DuplicateMatchRow> duplicates, List<UniqueResult> uniques) {}
