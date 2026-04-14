package com.example.FarmYukti.original_backend.repo;

import com.example.FarmYukti.original_backend.model.BuyerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BuyerProfileRepository extends JpaRepository<BuyerProfile, UUID> {
}
