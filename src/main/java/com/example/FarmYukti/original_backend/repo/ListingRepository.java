package com.example.FarmYukti.original_backend.repo;

import com.example.FarmYukti.original_backend.model.Listing;
import com.example.FarmYukti.original_backend.model.enums.ListingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Long>, JpaSpecificationExecutor<Listing> {
    // Find all active listings for buyers to see
    List<Listing> findByStatus(ListingStatus status);

    // Find all listings created by a specific farmer
    List<Listing> findByFarmerId(UUID farmerId);

    // Find listings by crop (e.g., "Show me all Wheat listings")
    List<Listing> findByCropIdAndStatus(Long cropId, ListingStatus status);
}
