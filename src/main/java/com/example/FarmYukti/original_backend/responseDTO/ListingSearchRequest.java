package com.example.FarmYukti.original_backend.responseDTO;

import java.math.BigDecimal;

public record ListingSearchRequest(
        Long cropId,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        String qualityGrade
) {}