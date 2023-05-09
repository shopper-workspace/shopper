package com.shopper.orderservice.domain.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public final class OrderEvent {

    public record OrderItem(
            UUID id,
            String skuCode,
            BigDecimal unitPrice,
            Integer quantity) {
    }

    public record Order(
            UUID id,
            OrderStatus orderStatus,
            String description,
            List<OrderItem> orderItems) {
    }

    public enum OrderStatus {
        CREATED, FAILED, REJECTED, RESERVED, WAITFORSHIP
    }
}
