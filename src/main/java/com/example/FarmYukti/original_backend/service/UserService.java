package com.example.FarmYukti.original_backend.service;

import com.example.FarmYukti.original_backend.exception.BusinessRuleException;
import com.example.FarmYukti.original_backend.exception.ResourceNotFoundException;
import com.example.FarmYukti.original_backend.model.BuyerProfile;
import com.example.FarmYukti.original_backend.model.FarmerProfile;
import com.example.FarmYukti.original_backend.model.User;
import com.example.FarmYukti.original_backend.model.enums.Role;
import com.example.FarmYukti.original_backend.repo.BuyerProfileRepository;
import com.example.FarmYukti.original_backend.repo.FarmerProfileRepository;
import com.example.FarmYukti.original_backend.repo.UserRepository;
import com.example.FarmYukti.original_backend.responseDTO.BuyerProfileRequest;
import com.example.FarmYukti.original_backend.responseDTO.FarmerProfileRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FarmerProfileRepository farmerProfileRepository;
    private final BuyerProfileRepository buyerProfileRepository;

    public User getUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
    }

    @Transactional
    public FarmerProfile onboardFarmer(UUID userId, FarmerProfile profileData) {
        User user = getUserById(userId);

        if (user.getRole() != Role.FARMER) {
            throw new BusinessRuleException("Cannot attach farmer profile: User does not have the FARMER role.");
        }

        profileData.setUser(user);
        return farmerProfileRepository.save(profileData);
    }

    @Transactional
    public BuyerProfile onboardBuyer(UUID userId, BuyerProfile profileData) {
        User user = getUserById(userId);

        if (user.getRole() != Role.BUYER) {
            throw new BusinessRuleException("Cannot attach buyer profile: User does not have the BUYER role.");
        }

        profileData.setUser(user);
        return buyerProfileRepository.save(profileData);
    }

    public FarmerProfile getFarmerProfile(UUID userId) {
        // Since FarmerProfile shares the same ID as the User (via @MapsId)
        return farmerProfileRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Farmer profile not found. Please complete onboarding."));
    }

    public BuyerProfile getBuyerProfile(UUID userId) {
        return buyerProfileRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Buyer profile not found. Please complete onboarding."));
    }

    @Transactional
    public FarmerProfile updateFarmerProfile(UUID userId, FarmerProfileRequest request) {
        // Fetch the existing profile
        FarmerProfile existingProfile = getFarmerProfile(userId);

        // Update the fields
        existingProfile.setAddressLine1(request.addressLine1());
        existingProfile.setCity(request.city());
        existingProfile.setState(request.state());
        existingProfile.setPincode(request.pincode());

        // Save and return (Hibernate automatically runs an UPDATE query)
        return farmerProfileRepository.save(existingProfile);
    }

    @Transactional
    public BuyerProfile updateBuyerProfile(UUID userId, BuyerProfileRequest request) {
        BuyerProfile existingProfile = getBuyerProfile(userId);

        existingProfile.setCompanyName(request.companyName());
        existingProfile.setGstNumber(request.gstNumber());
        existingProfile.setShippingAddress(request.shippingAddress());

        return buyerProfileRepository.save(existingProfile);
    }


}