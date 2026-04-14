package com.example.FarmYukti.original_backend.repo;

import com.example.FarmYukti.original_backend.model.CropMaster;
import com.example.FarmYukti.original_backend.model.LandParcel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CropMasterRepository extends JpaRepository<CropMaster, Long> {
    // Search crops by name (useful for dropdowns/search bars)
    List<CropMaster> findByNameContainingIgnoreCase(String name);
}


