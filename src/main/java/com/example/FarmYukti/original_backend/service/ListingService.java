package com.example.FarmYukti.original_backend.service;

import com.example.FarmYukti.original_backend.exception.BusinessRuleException;
import com.example.FarmYukti.original_backend.exception.ResourceNotFoundException;
import com.example.FarmYukti.original_backend.exception.UnauthorizedAccessException;
import com.example.FarmYukti.original_backend.model.CropMaster;
import com.example.FarmYukti.original_backend.model.LandParcel;
import com.example.FarmYukti.original_backend.model.Listing;
import com.example.FarmYukti.original_backend.model.User;
import com.example.FarmYukti.original_backend.model.enums.ListingStatus;
import com.example.FarmYukti.original_backend.repo.CropMasterRepository;
import com.example.FarmYukti.original_backend.repo.LandParcelRepository;
import com.example.FarmYukti.original_backend.repo.ListingRepository;
import com.example.FarmYukti.original_backend.responseDTO.CreateListingRequest;
import com.example.FarmYukti.original_backend.responseDTO.ListingSearchRequest;
import com.example.FarmYukti.original_backend.specification.ListingSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ListingService {

    private final ListingRepository listingRepository;
    private final CropMasterRepository cropMasterRepository;
    private final LandParcelRepository landParcelRepository;
    private final UserService userService;

    @Transactional
    public Listing createListing(UUID farmerId, Long cropId, Long landParcelId, Listing listingData) {
        User farmer = userService.getUserById(farmerId);

        CropMaster crop = cropMasterRepository.findById(cropId)
                .orElseThrow(() -> new ResourceNotFoundException("Crop not found with ID: " + cropId));

        if (landParcelId != null) {
            LandParcel land = landParcelRepository.findById(landParcelId)
                    .orElseThrow(() -> new ResourceNotFoundException("Land Parcel not found with ID: " + landParcelId));

            // Strict ownership check
            if (!land.getFarmer().getId().equals(farmerId)) {
                throw new UnauthorizedAccessException("You cannot create a listing for a land parcel you do not own.");
            }
            listingData.setLandParcel(land);
        }

        listingData.setFarmer(farmer);
        listingData.setCrop(crop);
        listingData.setStatus(ListingStatus.ACTIVE);

        return listingRepository.save(listingData);
    }

    public List<Listing> getAllActiveListings() {
        return listingRepository.findByStatus(ListingStatus.ACTIVE);
    }


    public Listing getListingById(Long listingId) {
        return listingRepository.findById(listingId)
                .orElseThrow(() -> new ResourceNotFoundException("Listing not found with ID: " + listingId));
    }

    @Transactional
    public Listing updateListing(UUID farmerId, Long listingId, CreateListingRequest request) {
        Listing existingListing = getListingById(listingId);

        // 1. Security Check: Ensure the person updating it is the owner
        if (!existingListing.getFarmer().getId().equals(farmerId)) {
            throw new UnauthorizedAccessException("You can only modify your own listings.");
        }

        // 2. Business Rule: Prevent updating if it's already sold
        if (existingListing.getStatus() == ListingStatus.SOLD || existingListing.getStatus() == ListingStatus.CANCELLED) {
            throw new BusinessRuleException("Cannot update a listing that is already " + existingListing.getStatus());
        }

        // 3. Update the allowable fields
        existingListing.setQuantityKg(request.quantityKg());
        existingListing.setAskPricePerKg(request.askPricePerKg());
        existingListing.setHarvestDate(request.harvestDate());

        // Note: If they want to change the Crop or Land Parcel, you would fetch those
        // from their respective repositories here and update them just like in createListing().

        return listingRepository.save(existingListing);
    }

    @Transactional
    public void cancelListing(UUID farmerId, Long listingId) {
        Listing existingListing = getListingById(listingId);

        // 1. Security Check
        if (!existingListing.getFarmer().getId().equals(farmerId)) {
            throw new UnauthorizedAccessException("You can only cancel your own listings.");
        }

        // 2. Business Rule
        if (existingListing.getStatus() == ListingStatus.SOLD) {
            throw new BusinessRuleException("Cannot cancel a listing that has already been sold.");
        }

        // 3. Soft Delete
        existingListing.setStatus(ListingStatus.CANCELLED);
        listingRepository.save(existingListing);
    }

    public Page<Listing> searchListings(ListingSearchRequest searchRequest, Pageable pageable) {

        Specification<Listing> spec = ListingSpecifications.withDynamicQuery(searchRequest);

        // The repository will automatically execute the dynamic SQL and handle the pagination!
        return listingRepository.findAll(spec, pageable);
    }

    public List<Listing> getListingsByFarmer(UUID farmerId) {
        // 1. Verify the farmer actually exists (Throws ResourceNotFoundException if not)
        userService.getUserById(farmerId);

        // 2. Fetch the listings.
        // Note: You already added 'findByFarmerId' to your ListingRepository in the earlier step!
        return listingRepository.findByFarmerId(farmerId);
    }
}
