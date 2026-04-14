package com.example.FarmYukti.original_backend.service;


import com.example.FarmYukti.original_backend.model.LandParcel;
import com.example.FarmYukti.original_backend.model.User;
import com.example.FarmYukti.original_backend.repo.LandParcelRepository;
import com.example.FarmYukti.original_backend.repo.UserRepository;
import com.example.FarmYukti.original_backend.responseDTO.LandParcelRequest;
import com.example.FarmYukti.original_backend.responseDTO.LandParcelResponse;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LandParcelService {

    private final LandParcelRepository landParcelRepository;
    private final UserRepository userRepository;

    // GeometryFactory for SRID 4326 (WGS84 GPS coordinates)
    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    public void addLandParcel(String userPhoneNumber, LandParcelRequest request) {
        User farmer = userRepository.findByPhoneNumber(userPhoneNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Convert Lat/Lon to JTS Point (Note: Coordinate is X, Y -> Longitude, Latitude)
        Point location = geometryFactory.createPoint(new Coordinate(request.longitude(), request.latitude()));

        LandParcel parcel = new LandParcel();
        parcel.setFarmer(farmer);
        parcel.setAgristackLandId(request.agristackLandId());
        parcel.setLocation(location);
        parcel.setSizeInAcres(request.sizeInAcres());
        parcel.setSoilData(request.soilData());

        landParcelRepository.save(parcel);
    }

    public List<LandParcelResponse> getMyParcels(String userPhoneNumber) {
        // 1. Get the farmer entity using the phone number from JWT
        User farmer = userRepository.findByPhoneNumber(userPhoneNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Fetch all parcels for this farmer and map to Response DTO
        return landParcelRepository.findByFarmer(farmer)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Helper to convert Entity to DTO
    private LandParcelResponse mapToResponse(LandParcel parcel) {
        return new LandParcelResponse(
                parcel.getId(),
                parcel.getAgristackLandId(),
                parcel.getLocation().getY(), // Latitude (Y)
                parcel.getLocation().getX(), // Longitude (X)
                parcel.getSizeInAcres(),
                parcel.getSoilData()
        );
    }
}