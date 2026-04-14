package com.example.FarmYukti.original_backend.controller;

import com.example.FarmYukti.original_backend.model.Listing;
import com.example.FarmYukti.original_backend.responseDTO.CreateListingRequest;
import com.example.FarmYukti.original_backend.responseDTO.ListingSearchRequest;
import com.example.FarmYukti.original_backend.service.ListingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/listings")
@RequiredArgsConstructor
public class ListingController {

    private final ListingService listingService;

    @PostMapping("/farmer/{farmerId}")
    public ResponseEntity<Listing> createListing(
            @PathVariable UUID farmerId, // In a real app, extract this from the JWT Security Context, not the URL!
            @RequestBody CreateListingRequest request) {

        Listing listingData = Listing.builder()
                .quantityKg(request.quantityKg())
                .askPricePerKg(request.askPricePerKg())
                .harvestDate(request.harvestDate())
                .build();

        Listing savedListing = listingService.createListing(
                farmerId, request.cropId(), request.landParcelId(), listingData);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedListing);
    }

    @GetMapping("/active")
    public ResponseEntity<List<Listing>> getActiveListings() {
        return ResponseEntity.ok(listingService.getAllActiveListings());
    }

    @PutMapping("/{listingId}/farmer/{farmerId}")
    public ResponseEntity<Listing> updateListing(
            @PathVariable Long listingId,
            @PathVariable UUID farmerId, // Again, will be replaced by JWT context later
            @RequestBody CreateListingRequest request) {

        Listing updatedListing = listingService.updateListing(farmerId, listingId, request);
        return ResponseEntity.ok(updatedListing);
    }

    @DeleteMapping("/{listingId}/farmer/{farmerId}")
    public ResponseEntity<Void> cancelListing(
            @PathVariable Long listingId,
            @PathVariable UUID farmerId) {

        listingService.cancelListing(farmerId, listingId);

        // 204 No Content tells the frontend "Success, and there is no JSON body to return"
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/search")
    public ResponseEntity<Page<Listing>> searchListings(
            @RequestParam(required = false) Long cropId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String aiQualityGrade,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        // 1. Pack the parameters into our DTO
        ListingSearchRequest searchRequest = new ListingSearchRequest(
                cropId, minPrice, maxPrice, aiQualityGrade
        );

        // 2. Set up the pagination and sorting rules
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        // 3. Execute search
        Page<Listing> results = listingService.searchListings(searchRequest, pageable);

        return ResponseEntity.ok(results);
    }

    @GetMapping("/{listingId}")
    public ResponseEntity<Listing> getListingById(@PathVariable Long listingId) {
        return ResponseEntity.ok(listingService.getListingById(listingId));
    }

    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<List<Listing>> getListingsByFarmer(@PathVariable UUID farmerId) {
        // You'll need to add this method to your ListingRepository and Service
        return ResponseEntity.ok(listingService.getListingsByFarmer(farmerId));
    }
}