package com.shopper.inventoryservice.domain.service;

import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.shopper.inventoryservice.domain.dto.Inventory;
import com.shopper.inventoryservice.domain.dto.InventoryStock;
import com.shopper.inventoryservice.domain.dto.OrderEvent;
import com.shopper.inventoryservice.domain.dto.OrderEvent.OrderItem;
import com.shopper.inventoryservice.domain.dto.ProductEvent;
import com.shopper.inventoryservice.infrastucture.entity.InventoryEntity;
import com.shopper.inventoryservice.infrastucture.mapper.InventoryMapper;
import com.shopper.inventoryservice.infrastucture.repository.InventoryRepository;
import com.shopper.shared.outbox.OutboxEvent;
import com.shopper.shared.outbox.OutboxEventService;
import com.shopper.shared.outbox.mapper.JsonMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private static final Logger logger = LoggerFactory.getLogger(InventoryService.class);

    private final InventoryMapper inventoryMapper;
    private final InventoryRepository inventoryRepository;
    private final OutboxEventService outboxEventService;

    @Transactional
    public String createInventory(Inventory inventoryDto) {

        InventoryEntity inventory = inventoryMapper.toEntity(inventoryDto);
        inventoryRepository.save(inventory);

        logger.info("Inventory created: {}", inventory.getId());

        return inventory.getId().toString();
    }

    @Transactional(readOnly = true)
    public List<InventoryStock> getInventory(List<String> skuCodes) {

        return inventoryRepository.findBySkuCodeIn(skuCodes)
                .stream()
                .map(inventory -> InventoryStock.builder()
                        .skuCode(inventory.getSkuCode())
                        .inStock(inventory.getQuantity() - inventory.getReservedQuantity() > 0)
                        .availableQuantity(inventory.getQuantity() - inventory.getReservedQuantity())
                        .build())
                .toList();
    }

    @Transactional
    public void addStock(String skuCode, Integer stockQuantity) {

        InventoryEntity inventory = inventoryRepository.findBySkuCode(skuCode);

        inventory.setQuantity(inventory.getQuantity() + stockQuantity);
        inventoryRepository.save(inventory);

        logger.info("Stock adjusted: {}: {} available item(s)", skuCode, stockQuantity);
    }

    @Transactional
    public void reserveStock(String skuCode, Integer reservedQuantity) {

        InventoryEntity inventory = inventoryRepository.findBySkuCode(skuCode);

        inventory.setReservedQuantity(inventory.getReservedQuantity() + reservedQuantity);
        inventoryRepository.save(inventory);

        logger.info("Stock reserved: {}: {} reserved item(s)", skuCode, reservedQuantity);
    }

    @Transactional
    public void processProductCreatedEvent(String messageId, String payload) {

        ProductEvent.Product productDto = JsonMapper.readFromJson(payload, ProductEvent.Product.class);
        outboxEventService.processEvent(UUID.fromString(messageId));

        Inventory inventory = Inventory.builder()
                .skuCode(productDto.skuCode())
                .quantity(0)
                .reservedQuantity(0)
                .build();
        createInventory(inventory);
    }

    @Transactional
    public void processOrderCreatedEvent(String messageId, String payload) {

        OrderEvent.Order orderDto = JsonMapper.readFromJson(payload, OrderEvent.Order.class);
        outboxEventService.processEvent(UUID.fromString(messageId));

        List<String> skuCodes = orderDto.orderItems()
                .stream()
                .map(OrderItem::skuCode)
                .toList();

        boolean allItemsInStock = getInventory(skuCodes)
                .stream()
                .allMatch(InventoryStock::isInStock);

        if (allItemsInStock) {
            orderDto.orderItems().forEach(oi -> reserveStock(oi.skuCode(), oi.quantity()));
            outboxEventService.publishEvent(new OutboxEvent("INVENTORY", orderDto.id(), "RESERVE_STOCK_SUCCCESS", payload));
        } else {
            outboxEventService.publishEvent(new OutboxEvent("INVENTORY", orderDto.id(), "RESERVE_STOCK_FAIL", payload));
        }
    }

    @Transactional
    public void processOrderStatusChangedEvent(String messageId, String payload) {

        OrderEvent.Order orderDto = JsonMapper.readFromJson(payload, OrderEvent.Order.class);
        outboxEventService.processEvent(UUID.fromString(messageId));

        if (orderDto.orderStatus() == OrderEvent.OrderStatus.FAILED) {
            orderDto.orderItems().forEach(oi -> reserveStock(oi.skuCode(), -1 * oi.quantity()));
        }
    }
}
