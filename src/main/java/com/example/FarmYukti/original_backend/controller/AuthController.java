package com.example.FarmYukti.original_backend.controller;


import com.example.FarmYukti.original_backend.responseDTO.AuthResponse;
import com.example.FarmYukti.original_backend.responseDTO.RegisterRequest;
import com.example.FarmYukti.original_backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth") // Postman hits this...
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register") // ...and this exact method!
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
}