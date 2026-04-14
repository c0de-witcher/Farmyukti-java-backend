package com.example.FarmYukti.original_backend.repo;

import com.example.FarmYukti.original_backend.model.AdvisoryRecord;
import com.example.FarmYukti.original_backend.model.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    Optional<Shipment> findByOrderId(Long orderId);
    List<Shipment> findByTransporterId(UUID transporterId);
}


