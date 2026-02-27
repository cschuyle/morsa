package com.example.morsor;

import java.util.List;

public record DuplicatesResponse(long total, int page, int size, List<DuplicateMatchRow> rows) {}
