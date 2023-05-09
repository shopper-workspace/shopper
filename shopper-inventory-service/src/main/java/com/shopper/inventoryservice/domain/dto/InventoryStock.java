package com.shopper.inventoryservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryStock {

    private String skuCode;

    private boolean inStock;

    private Integer availableQuantity;
}
