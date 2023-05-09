package com.shopper.inventoryservice.domain.dto;

import java.math.BigDecimal;
import java.util.UUID;

public final class ProductEvent {

    public record Product(
            UUID id,
            String skuCode,
            String name,
            String description,
            BigDecimal price) {
    }
}
