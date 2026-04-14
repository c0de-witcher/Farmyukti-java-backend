package com.example.FarmYukti.original_backend.responseDTO;

import com.example.FarmYukti.original_backend.model.enums.Role;

public record RegisterRequest(
        String phoneNumber,
        String password,
        String fullName,
        Role role // FARMER, BUYER, or TRANSPORTER
) {}
