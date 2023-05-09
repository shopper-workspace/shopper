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
public class Inventory {

    private String skuCode;

    private Integer quantity;

    private Integer reservedQuantity;
}
