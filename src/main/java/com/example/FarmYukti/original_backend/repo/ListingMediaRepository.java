package com.example.FarmYukti.original_backend.repo;

import com.example.FarmYukti.original_backend.model.EscrowTransaction;
import com.example.FarmYukti.original_backend.model.ListingMedia;
import com.example.FarmYukti.original_backend.model.Order;
import com.example.FarmYukti.original_backend.model.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ListingMediaRepository extends JpaRepository<ListingMedia, Long> {
    List<ListingMedia> findByListingId(Long listingId);
}


