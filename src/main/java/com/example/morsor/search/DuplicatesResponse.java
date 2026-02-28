package com.example.morsor.search;

import java.util.List;

public record DuplicatesResponse(long total, int page, int size, List<DuplicateMatchRow> rows) {}
