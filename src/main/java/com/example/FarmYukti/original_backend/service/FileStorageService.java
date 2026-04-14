package com.example.FarmYukti.original_backend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.FarmYukti.original_backend.exception.BusinessRuleException;
import com.example.FarmYukti.original_backend.model.Listing;
import com.example.FarmYukti.original_backend.model.ListingMedia;
import com.example.FarmYukti.original_backend.model.enums.MediaType;
import com.example.FarmYukti.original_backend.repo.ListingMediaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final Cloudinary cloudinary;
    private final ListingService listingService; // To verify the listing exists
    private final ListingMediaRepository listingMediaRepository;

    // Allowed MIME types
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList("image/jpeg", "image/png", "image/webp");

    @Transactional
    public ListingMedia uploadListingPhoto(Long listingId, MultipartFile file) {
        // 1. Verify Listing Exists
        Listing listing = listingService.getListingById(listingId);

        // 2. Strict File Validation
        if (file.isEmpty()) {
            throw new BusinessRuleException("Cannot upload an empty file.");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType)) {
            throw new BusinessRuleException("Invalid file format. Only JPEG, PNG, and WEBP are allowed.");
        }

        try {
            // 3. Upload to Cloudinary
            // We use 'farmyukti/listings' as a folder to keep the cloud bucket organized
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", "farmyukti/listings",
                    "resource_type", "image"
            ));

            // Extract the secure HTTPS URL provided by Cloudinary
            String secureUrl = uploadResult.get("secure_url").toString();

            // 4. Save to Database
            ListingMedia media = new ListingMedia();
            media.setListing(listing);
            media.setMediaUrl(secureUrl);
            media.setMediaType(MediaType.IMAGE);
            media.setUsedForGrading(false); // Can be updated later if sent to AI

            return listingMediaRepository.save(media);

        } catch (IOException e) {
            // Catching IO exceptions specifically to differentiate from DB errors
            throw new RuntimeException("Failed to upload file to cloud storage.", e);
        }
    }
}
