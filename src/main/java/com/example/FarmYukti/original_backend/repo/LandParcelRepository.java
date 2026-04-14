package com.example.FarmYukti.original_backend.repo;

import com.example.FarmYukti.original_backend.model.LandParcel;
import com.example.FarmYukti.original_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LandParcelRepository extends JpaRepository<LandParcel, Long> {
    // Find all parcels belonging to a specific farmer
    List<LandParcel> findByFarmerId(UUID farmerId);

    List<LandParcel> findByFarmer(User farmer);

    // Example of a custom PostGIS query: Find land parcels within X meters of a given Long/Lat
    @Query(value = "SELECT * FROM land_parcels WHERE ST_DWithin(location, ST_SetSRID(ST_MakePoint(:lon, :lat), 4326), :distanceMeters)", nativeQuery = true)
    List<LandParcel> findParcelsNearLocation(@Param("lon") double lon, @Param("lat") double lat, @Param("distanceMeters") double distanceMeters);
}
