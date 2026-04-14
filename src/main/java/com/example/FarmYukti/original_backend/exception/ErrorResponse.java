package com.example.FarmYukti.original_backend.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    String timestamp;
    int status;
    String error;
    String message;
}
