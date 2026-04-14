package com.example.FarmYukti.original_backend.service;

import com.example.FarmYukti.original_backend.exception.BusinessRuleException;
import com.example.FarmYukti.original_backend.exception.ResourceNotFoundException;
import com.example.FarmYukti.original_backend.model.EscrowTransaction;
import com.example.FarmYukti.original_backend.model.Listing;
import com.example.FarmYukti.original_backend.model.Order;
import com.example.FarmYukti.original_backend.model.User;
import com.example.FarmYukti.original_backend.model.enums.EscrowStatus;
import com.example.FarmYukti.original_backend.model.enums.ListingStatus;
import com.example.FarmYukti.original_backend.model.enums.OrderStatus;
import com.example.FarmYukti.original_backend.repo.EscrowTransactionRepository;
import com.example.FarmYukti.original_backend.repo.ListingRepository;
import com.example.FarmYukti.original_backend.repo.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ListingRepository listingRepository;
    private final EscrowTransactionRepository escrowTransactionRepository;
    private final UserService userService;

    @Transactional
    public Order createOrder(UUID buyerId, Long listingId, BigDecimal requestedQuantity) {
        User buyer = userService.getUserById(buyerId);

        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new ResourceNotFoundException("Listing not found with ID: " + listingId));

        // Business Rule Validations
        if (listing.getStatus() != ListingStatus.ACTIVE) {
            throw new BusinessRuleException("Cannot purchase: This listing is no longer active.");
        }
        if (requestedQuantity.compareTo(listing.getQuantityKg()) > 0) {
            throw new BusinessRuleException("Insufficient stock: Requested " + requestedQuantity + "kg, but only " + listing.getQuantityKg() + "kg available.");
        }

        // Financial Calculation
        BigDecimal finalPricePerKg = listing.getAskPricePerKg();
        BigDecimal totalAmount = finalPricePerKg.multiply(requestedQuantity);

        // Create Order
        Order order = Order.builder()
                .listing(listing)
                .buyer(buyer)
                .farmer(listing.getFarmer())
                .finalPricePerKg(finalPricePerKg)
                .finalQuantityKg(requestedQuantity)
                .totalAmount(totalAmount)
                .status(OrderStatus.PENDING_PAYMENT)
                .build();

        Order savedOrder = orderRepository.save(order);

        // Initialize Escrow
        EscrowTransaction escrow = new EscrowTransaction();
        escrow.setOrder(savedOrder);
        escrow.setAmount(totalAmount);
        escrow.setStatus(EscrowStatus.FUNDS_COMMITTED);
        escrow.setProviderTxnId("PENDING_GATEWAY");

        escrowTransactionRepository.save(escrow);

        // Update Listing Stock/Status
        if (requestedQuantity.compareTo(listing.getQuantityKg()) == 0) {
            listing.setStatus(ListingStatus.SOLD);
        } else {
            listing.setQuantityKg(listing.getQuantityKg().subtract(requestedQuantity));
        }
        listingRepository.save(listing);

        return savedOrder;
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
    }

    public List<Order> getOrdersForBuyer(UUID buyerId) {
        userService.getUserById(buyerId); // Verify buyer exists
        return orderRepository.findByBuyerId(buyerId);
    }

    public List<Order> getOrdersForFarmer(UUID farmerId) {
        userService.getUserById(farmerId); // Verify farmer exists
        return orderRepository.findByFarmerId(farmerId);
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order existingOrder = getOrderById(orderId);

        // Basic State Machine Rules (Preventing illegal status jumps)
        if (existingOrder.getStatus() == OrderStatus.COMPLETED || existingOrder.getStatus() == OrderStatus.CANCELLED) {
            throw new BusinessRuleException("Cannot change status of an order that is already " + existingOrder.getStatus());
        }

        // Apply the new status
        existingOrder.setStatus(newStatus);

        // If the status changes to DELIVERED, you would typically also trigger logic
        // to update the EscrowTransaction to 'FUNDS_RELEASED' here.

        return orderRepository.save(existingOrder);
    }
}