package com.example.FarmYukti.original_backend.controller;



import com.example.FarmYukti.original_backend.model.BuyerProfile;
import com.example.FarmYukti.original_backend.model.FarmerProfile;
import com.example.FarmYukti.original_backend.responseDTO.BuyerProfileRequest;
import com.example.FarmYukti.original_backend.responseDTO.FarmerProfileRequest;
import com.example.FarmYukti.original_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/{userId}/farmer-profile")
    public ResponseEntity<FarmerProfile> onboardFarmer(
            @PathVariable UUID userId,
            @RequestBody FarmerProfileRequest request) {

        FarmerProfile profile = FarmerProfile.builder()
                .addressLine1(request.addressLine1())
                .city(request.city())
                .state(request.state())
                .pincode(request.pincode())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.onboardFarmer(userId, profile));
    }


    @GetMapping("/me")
    public ResponseEntity<String> testAuthentication(@AuthenticationPrincipal UserDetails userDetails) {
        // If the token is valid, Spring Security automatically injects the user details here!
        return ResponseEntity.ok("Success! You have passed the security filter. You are logged in as: " + userDetails.getUsername());
    }

    @PostMapping("/{userId}/buyer-profile")
    public ResponseEntity<BuyerProfile> onboardBuyer(
            @PathVariable UUID userId,
            @RequestBody BuyerProfileRequest request) {

        BuyerProfile profile = BuyerProfile.builder()
                .companyName(request.companyName())
                .gstNumber(request.gstNumber())
                .shippingAddress(request.shippingAddress())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.onboardBuyer(userId, profile));
    }

    @GetMapping("/{userId}/farmer-profile")
    public ResponseEntity<FarmerProfile> getFarmerProfile(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.getFarmerProfile(userId));
    }

    @PutMapping("/{userId}/farmer-profile")
    public ResponseEntity<FarmerProfile> updateFarmerProfile(
            @PathVariable UUID userId,
            @RequestBody FarmerProfileRequest request) {

        FarmerProfile updatedProfile = userService.updateFarmerProfile(userId, request);
        return ResponseEntity.ok(updatedProfile); // 200 OK for updates
    }


    @GetMapping("/{userId}/buyer-profile")
    public ResponseEntity<BuyerProfile> getBuyerProfile(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.getBuyerProfile(userId));
    }

    @PutMapping("/{userId}/buyer-profile")
    public ResponseEntity<BuyerProfile> updateBuyerProfile(
            @PathVariable UUID userId,
            @RequestBody BuyerProfileRequest request) {

        BuyerProfile updatedProfile = userService.updateBuyerProfile(userId, request);
        return ResponseEntity.ok(updatedProfile);
    }
}
