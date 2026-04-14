package com.example.FarmYukti.original_backend.responseDTO;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

// 1. Profile DTOs
public record FarmerProfileRequest(String addressLine1, String city, String state, String pincode) {}

