package com.example.FarmYukti.original_backend.controller;


import com.example.FarmYukti.original_backend.responseDTO.LandParcelRequest;
import com.example.FarmYukti.original_backend.responseDTO.LandParcelResponse;
import com.example.FarmYukti.original_backend.service.LandParcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/land-parcels")
@RequiredArgsConstructor
public class LandParcelController {

    private final LandParcelService landParcelService;

    /**
     * Creates a new Land Parcel for the authenticated Farmer.
     * The farmer's identity is extracted from the JWT token.
     */
    @PostMapping
    public ResponseEntity<Void> addLandParcel(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody LandParcelRequest request) {

        // userDetails.getUsername() retrieves the phone number from the JWT
        landParcelService.addLandParcel(userDetails.getUsername(), request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Retrieves all land parcels belonging to the logged-in farmer.
     */
    @GetMapping("/my-farms")
    public ResponseEntity<List<LandParcelResponse>> getMyFarms(
            @AuthenticationPrincipal UserDetails userDetails) {

        List<LandParcelResponse> farms = landParcelService.getMyParcels(userDetails.getUsername());
        return ResponseEntity.ok(farms);
    }
}