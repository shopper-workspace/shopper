package com.shopper.paymentservice.domain.service;

import java.math.BigDecimal;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.shopper.paymentservice.domain.dto.OrderEvent;
import com.shopper.shared.outbox.OutboxEvent;
import com.shopper.shared.outbox.OutboxEventService;
import com.shopper.shared.outbox.mapper.JsonMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    private final OutboxEventService outboxEventService;

    @Transactional
    public void processOrderStatusChangedEvent(String messageId, String payload) {

        OrderEvent.Order orderDto = JsonMapper.readFromJson(payload, OrderEvent.Order.class);
        outboxEventService.processEvent(UUID.fromString(messageId));

        if (orderDto.orderStatus() == OrderEvent.OrderStatus.RESERVED) {

            if (Math.random() < 0.8) {
                BigDecimal totalPrice = orderDto.orderItems().stream().map(OrderEvent.OrderItem::unitPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

                logger.info("Payment successful: {}: {} paid", orderDto.id(), totalPrice);
                outboxEventService.publishEvent(new OutboxEvent("PAYMENT", orderDto.id(), "PAYMENT_SUCCESS", payload));
            } else {
                logger.info("Payment failed: {}", orderDto.id());
                outboxEventService.publishEvent(new OutboxEvent("PAYMENT", orderDto.id(), "PAYMENT_FAIL", payload));
            }
        }
    }
}
