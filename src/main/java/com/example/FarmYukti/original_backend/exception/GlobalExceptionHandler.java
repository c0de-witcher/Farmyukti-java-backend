package com.example.FarmYukti.original_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex){
        ErrorResponse error = new ErrorResponse(OffsetDateTime.now().toString(),404,"Not Found", ex.getMessage());
        return new ResponseEntity<ErrorResponse>(error,HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(UnauthorizedAccessException.class)
    public  ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedAccessException ex) {
        var error = new ErrorResponse(OffsetDateTime.now().toString(), 403, "Forbidden", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErrorResponse> handleBusinessRule(BusinessRuleException ex) {
        var error = new ErrorResponse(OffsetDateTime.now().toString(), 400, "Bad Request", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        // Catch-all for unexpected server errors (500)
        var error = new ErrorResponse(OffsetDateTime.now().toString(), 500, "Internal Server Error", "An unexpected error occurred.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

}
