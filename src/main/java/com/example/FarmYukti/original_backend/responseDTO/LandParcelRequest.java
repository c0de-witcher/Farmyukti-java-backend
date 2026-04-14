package com.example.FarmYukti.original_backend.responseDTO;


import java.math.BigDecimal;
import java.util.Map;

public record LandParcelRequest(
        String agristackLandId,
        Double latitude,
        Double longitude,
        BigDecimal sizeInAcres,
        Map<String, Object> soilData
) {}