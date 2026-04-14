package com.example.FarmYukti.original_backend.repo;

import com.example.FarmYukti.original_backend.model.FarmerProfile;
import com.example.FarmYukti.original_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    // Crucial for Spring Security login and checking for duplicates during registration
    Optional<User> findByPhoneNumber(String phoneNumber);

    // Check if an agristack ID is already registered
    Optional<User> findByAgristackId(String agristackId);
}

