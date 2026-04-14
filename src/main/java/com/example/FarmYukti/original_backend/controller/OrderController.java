package com.example.FarmYukti.original_backend.controller;


import com.example.FarmYukti.original_backend.model.Order;
import com.example.FarmYukti.original_backend.responseDTO.CreateOrderRequest;
import com.example.FarmYukti.original_backend.responseDTO.UpdateOrderStatusRequest;
import com.example.FarmYukti.original_backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/buyer/{buyerId}")
    public ResponseEntity<Order> createOrder(
            @PathVariable UUID buyerId, // Again, usually extracted from JWT
            @RequestBody CreateOrderRequest request) {

        Order order = orderService.createOrder(
                buyerId, request.listingId(), request.requestedQuantity());

        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<Order>> getOrdersForBuyer(@PathVariable UUID buyerId) {
        // Fetch all orders where buyer_id matches
        return ResponseEntity.ok(orderService.getOrdersForBuyer(buyerId));
    }

    // 2. Farmer's "My Sales" Dashboard
    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<List<Order>> getOrdersForFarmer(@PathVariable UUID farmerId) {
        // Fetch all orders where farmer_id matches
        return ResponseEntity.ok(orderService.getOrdersForFarmer(farmerId));
    }

    // 3. View Single Order Details (For tracking/receipts)
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody UpdateOrderStatusRequest request) {

        // Inside your service, you must enforce rules here!
        // Example: Only a Farmer can change status to SHIPPED.
        // Only a Buyer can change status to DELIVERED.
        Order updatedOrder = orderService.updateOrderStatus(orderId, request.newStatus());
        return ResponseEntity.ok(updatedOrder);
    }
}
