package com.example.FarmYukti.original_backend.responseDTO;

import java.math.BigDecimal;

// 3. Order DTOs
public record CreateOrderRequest(
        Long listingId,
        BigDecimal requestedQuantity
) {}
