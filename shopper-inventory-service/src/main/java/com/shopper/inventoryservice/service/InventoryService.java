package com.shopper.inventoryservice.service;

import com.shopper.inventoryservice.dto.InventoryRequest;
import com.shopper.inventoryservice.dto.InventoryResponse;
import com.shopper.inventoryservice.model.Inventory;
import com.shopper.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional
    public void createInventory(InventoryRequest inventoryRequest) {
        Inventory inventory = new Inventory();
        inventory.setSkuCode(inventoryRequest.getSkuCode());
        inventory.setQuantity(inventoryRequest.getQuantity());

        inventoryRepository.save(inventory);
        log.info("Inventory {} created", inventory.getId());
    }

    public List<InventoryResponse> isInStock(List<String> skuCode) {
        return inventoryRepository
                .findBySkuCodeIn(skuCode)
                .stream()
                .map(inventory ->
                        InventoryResponse
                                .builder()
                                .skuCode(inventory.getSkuCode())
                                .isInStock(inventory.getQuantity() > 0)
                                .build()
                )
                .toList();
    }
}
