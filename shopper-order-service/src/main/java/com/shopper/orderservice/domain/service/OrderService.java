package com.shopper.orderservice.domain.service;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.shopper.orderservice.domain.dto.Order;
import com.shopper.orderservice.domain.dto.OrderEvent;
import com.shopper.orderservice.domain.dto.OrderStatus;
import com.shopper.orderservice.infrastructure.entity.OrderEntity;
import com.shopper.orderservice.infrastructure.mapper.OrderMapper;
import com.shopper.orderservice.infrastructure.repository.OrderRepository;
import com.shopper.shared.outbox.OutboxEvent;
import com.shopper.shared.outbox.OutboxEventService;
import com.shopper.shared.outbox.mapper.JsonMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final OutboxEventService outboxEventService;

    @Transactional
    public String createOrder(Order orderDto) {

        OrderEntity order = orderMapper.toEntity(orderDto);

        orderRepository.save(order);
        outboxEventService.publishEvent(new OutboxEvent("ORDER", order.getId(), "ORDER_CREATED", JsonMapper.writeToJson(order)));

        logger.info("Order created: {}", order.getId());

        return order.getId().toString();
    }

    @Transactional
    public void updateOrderStatus(UUID orderId, OrderStatus status) {

        OrderEntity order = orderRepository.getReferenceById(orderId);

        order.setOrderStatus(status);

        orderRepository.save(order);
        outboxEventService.publishEvent(new OutboxEvent("ORDER", order.getId(), "ORDER_STATUS_CHANGED", JsonMapper.writeToJson(order)));

        logger.info("Order status changed: {}: {}", orderId, status);
    }

    @Transactional
    public void processStockReserveSuccessEvent(String messageId, String payload) {

        OrderEvent.Order orderDto = JsonMapper.readFromJson(payload, OrderEvent.Order.class);
        outboxEventService.processEvent(UUID.fromString(messageId));

        updateOrderStatus(orderDto.id(), OrderStatus.RESERVED);
    }

    @Transactional
    public void processStockReserveFailEvent(String messageId, String payload) {

        OrderEvent.Order orderDto = JsonMapper.readFromJson(payload, OrderEvent.Order.class);
        outboxEventService.processEvent(UUID.fromString(messageId));

        updateOrderStatus(orderDto.id(), OrderStatus.REJECTED);
    }

    @Transactional
    public void processPaymentSuccessEvent(String messageId, String payload) {

        OrderEvent.Order orderDto = JsonMapper.readFromJson(payload, OrderEvent.Order.class);
        outboxEventService.processEvent(UUID.fromString(messageId));

        updateOrderStatus(orderDto.id(), OrderStatus.WAITFORSHIP);
    }

    @Transactional
    public void processPaymentFailEvent(String messageId, String payload) {

        OrderEvent.Order orderDto = JsonMapper.readFromJson(payload, OrderEvent.Order.class);
        outboxEventService.processEvent(UUID.fromString(messageId));

        updateOrderStatus(orderDto.id(), OrderStatus.FAILED);
    }
}
