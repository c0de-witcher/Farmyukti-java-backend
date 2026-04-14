package com.example.FarmYukti.original_backend.service;


import com.example.FarmYukti.original_backend.model.User;
import com.example.FarmYukti.original_backend.repo.UserRepository;
import com.example.FarmYukti.original_backend.responseDTO.AuthResponse;
import com.example.FarmYukti.original_backend.responseDTO.RegisterRequest;
import com.example.FarmYukti.original_backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {
        // 1. Check if user already exists
        if (userRepository.findByPhoneNumber(request.phoneNumber()).isPresent()) {
            throw new RuntimeException("User with this phone number already exists.");
        }

        // 2. Create the new user entity
        User user = new User();
        user.setPhoneNumber(request.phoneNumber());
        user.setPasswordHash(passwordEncoder.encode(request.password())); // Hash the password!
        user.setRole(request.role());

        // 3. Save to database
        User savedUser = userRepository.save(user);

        // 4. Generate the JWT token
        String jwtToken = jwtService.generateToken(savedUser);

        return new AuthResponse(jwtToken, "User registered successfully");
    }
}