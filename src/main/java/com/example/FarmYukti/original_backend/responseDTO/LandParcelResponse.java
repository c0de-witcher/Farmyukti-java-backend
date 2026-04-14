package com.example.FarmYukti.original_backend.responseDTO;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public record LandParcelResponse(
        Long id,
        String agristackLandId,
        Double latitude,
        Double longitude,
        BigDecimal sizeInAcres,
        Map<String, Object> soilData
) {}