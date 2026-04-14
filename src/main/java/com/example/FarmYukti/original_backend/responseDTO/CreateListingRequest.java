package com.example.FarmYukti.original_backend.responseDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

// 2. Listing DTOs
public record CreateListingRequest(
        Long cropId,
        Long landParcelId, // Optional
        BigDecimal quantityKg,
        BigDecimal askPricePerKg,
        LocalDate harvestDate
) {}
