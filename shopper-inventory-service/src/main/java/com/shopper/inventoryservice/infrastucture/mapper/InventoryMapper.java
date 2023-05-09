package com.shopper.inventoryservice.infrastucture.mapper;

import org.springframework.stereotype.Component;
import com.shopper.inventoryservice.domain.dto.Inventory;
import com.shopper.inventoryservice.infrastucture.entity.InventoryEntity;

@Component
public class InventoryMapper extends BaseMapper<InventoryEntity, Inventory> {

    @Override
    public InventoryEntity toEntity(Inventory inventoryDto) {

        InventoryEntity inventoryEntity = InventoryEntity.builder()
                .skuCode(inventoryDto.getSkuCode())
                .quantity(inventoryDto.getQuantity())
                .reservedQuantity(inventoryDto.getQuantity())
                .build();

        return inventoryEntity;
    }

    @Override
    public Inventory toDto(InventoryEntity inventoryEntity) {

        Inventory inventoryDto = Inventory.builder()
                .skuCode(inventoryEntity.getSkuCode())
                .quantity(inventoryEntity.getQuantity())
                .reservedQuantity(inventoryEntity.getReservedQuantity())
                .build();

        return inventoryDto;
    }
}
