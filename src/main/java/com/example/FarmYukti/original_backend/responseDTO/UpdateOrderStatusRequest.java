package com.example.FarmYukti.original_backend.responseDTO;

import com.example.FarmYukti.original_backend.model.enums.OrderStatus;

public record UpdateOrderStatusRequest(OrderStatus newStatus) { }
