package com.shopper.inventoryservice.domain.consumer;

import java.nio.charset.StandardCharsets;
import java.util.stream.StreamSupport;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import com.shopper.inventoryservice.domain.service.InventoryService;
import com.shopper.shared.outbox.DuplicateEventException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InventoryConsumer {

    private static final Logger logger = LoggerFactory.getLogger(InventoryConsumer.class);

    private final InventoryService inventoryService;

    @KafkaListener(groupId = "inventory-consumer", topics = {"ORDER.events", "PRODUCT.events"}, containerFactory = "kafkaListenerContainerFactory")
    public void consume(ConsumerRecord<String, String> cr, @Payload String payload) {

        String key = cr.key();
        String eventType = getHeaderValue(cr.headers(), "eventType");
        String messageId = getHeaderValue(cr.headers(), "id");

        logger.debug("Consumed message -> messageId: {}, key {}: Type [{}] | Payload: {}", messageId, key, eventType, payload);

        try {

            switch (eventType) {
                case "PRODUCT_CREATED" -> inventoryService.processProductCreatedEvent(messageId, payload);
                case "ORDER_CREATED" -> inventoryService.processOrderCreatedEvent(messageId, payload);
                case "ORDER_STATUS_CHANGED" -> inventoryService.processOrderStatusChangedEvent(messageId, payload);
                default -> logger.debug("Unknown event: {}", messageId);
            }

        } catch (DuplicateEventException e) {
            logger.debug("Duplicate message received: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error consuming message: " + e.getMessage());
        }
    }

    private static String getHeaderValue(Headers headers, String key) {
        return StreamSupport.stream(headers.spliterator(), false)
                .filter(header -> header.key().equals(key))
                .findFirst()
                .map(header -> new String(header.value(), StandardCharsets.UTF_8))
                .orElse("N/A");
    }
}
