package com.shopper.inventoryservice.api.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.shopper.inventoryservice.domain.dto.Inventory;
import com.shopper.inventoryservice.domain.dto.InventoryStock;
import com.shopper.inventoryservice.domain.service.InventoryService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createInventory(@RequestBody Inventory inventoryRequest) {
        inventoryService.createInventory(inventoryRequest);
        return "Inventory created successfully";
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryStock> getInventory(@RequestParam List<String> skuCodes) {
        return inventoryService.getInventory(skuCodes);
    }
}
