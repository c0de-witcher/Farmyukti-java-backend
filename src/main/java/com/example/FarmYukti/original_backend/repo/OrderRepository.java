package com.example.FarmYukti.original_backend.repo;

import com.example.FarmYukti.original_backend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // For the buyer's order history page
    List<Order> findByBuyerId(UUID buyerId);

    // For the farmer's sales dashboard
    List<Order> findByFarmerId(UUID farmerId);
}
