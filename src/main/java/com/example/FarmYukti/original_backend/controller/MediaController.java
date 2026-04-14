package com.example.FarmYukti.original_backend.controller;

import com.example.FarmYukti.original_backend.model.ListingMedia;
import com.example.FarmYukti.original_backend.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/media")
@RequiredArgsConstructor
public class MediaController {

    // You will need a service that uploads files to AWS S3, Google Cloud Storage, or locally.
    private final FileStorageService fileStorageService;

    @PostMapping(value = "/listing/{listingId}", consumes = "multipart/form-data")
    public ResponseEntity<ListingMedia> uploadListingImage(
            @PathVariable Long listingId,
            @RequestParam("file") MultipartFile file) {

        // 1. Validate the file is an image (JPEG/PNG)
        // 2. Upload to S3/Cloud Storage and get the URL back
        // 3. Save the URL to the ListingMedia table in the database

        ListingMedia media = fileStorageService.uploadListingPhoto(listingId, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(media);
    }
}
