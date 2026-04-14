package com.example.FarmYukti.original_backend.repo;

import com.example.FarmYukti.original_backend.model.*;
import com.example.FarmYukti.original_backend.model.enums.ListingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FarmerProfileRepository extends JpaRepository<FarmerProfile, UUID> {
}

