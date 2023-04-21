package com.shopper.orderservice.service;

import com.shopper.orderservice.dto.InventoryResponse;
import com.shopper.orderservice.dto.OrderLineItemDto;
import com.shopper.orderservice.dto.OrderRequest;
import com.shopper.orderservice.event.OrderPlacedEvent;
import com.shopper.orderservice.model.Order;
import com.shopper.orderservice.model.OrderLineItem;
import com.shopper.orderservice.repository.OrderRepository;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OrderService {

    private final WebClient.Builder webClient;
    private final ObservationRegistry observationRegistry;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final OrderRepository orderRepository;

    @Transactional
    public String placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItem> orderLineItems = orderRequest
                .getOrderLineItemDtoList()
                .stream()
                .map(this::mapToOrderLineItem)
                .toList();

        order.setOrderLineItemList(orderLineItems);

        // Check inventory stock for each item
        List<String> skuCodes = order
                .getOrderLineItemList()
                .stream()
                .map(OrderLineItem::getSkuCode)
                .toList();

        Observation inventoryServiceObservation = Observation
                .createNotStarted("inventory-service-lookup", this.observationRegistry)
                .lowCardinalityKeyValue("call", "inventory-service");
        return inventoryServiceObservation.observe(() -> {
            InventoryResponse[] inventoryResponses = webClient
                    .build()
                    .get()
                    .uri("http://inventory-service/api/inventory", uriBuilder ->
                            uriBuilder
                                    .queryParam("skuCode", skuCodes)
                                    .build())
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block();

            boolean allItemsInStock = Arrays.stream(inventoryResponses).allMatch(InventoryResponse::isInStock);
            if (allItemsInStock) {
                orderRepository.save(order);
                log.info("Order {} created", order.getId());

                applicationEventPublisher.publishEvent(new OrderPlacedEvent(this, order.getOrderNumber()));

                return "Order placed successfully";
            } else {
                throw new IllegalArgumentException("Product not in stock");
            }
        });
    }

    private OrderLineItem mapToOrderLineItem(OrderLineItemDto orderLineItemDto) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSkuCode(orderLineItemDto.getSkuCode());
        orderLineItem.setPrice(orderLineItemDto.getPrice());
        orderLineItem.setQuantity(orderLineItemDto.getQuantity());
        return orderLineItem;
    }
}
