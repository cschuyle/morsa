package com.example.morsor.search;

import java.util.List;

public record UniquesResponse(long total, int page, int size, List<UniqueResult> results) {}
