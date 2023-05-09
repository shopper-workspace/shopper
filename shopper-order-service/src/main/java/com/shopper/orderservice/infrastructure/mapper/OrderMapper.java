package com.shopper.orderservice.infrastructure.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.shopper.orderservice.domain.dto.Order;
import com.shopper.orderservice.domain.dto.OrderItem;
import com.shopper.orderservice.domain.dto.OrderStatus;
import com.shopper.orderservice.infrastructure.entity.OrderEntity;
import com.shopper.orderservice.infrastructure.entity.OrderItemEntity;

@Component
public class OrderMapper extends BaseMapper<OrderEntity, Order> {

    @Override
    public OrderEntity toEntity(Order orderDto) {

        OrderEntity orderEntity = OrderEntity.builder()
                .orderStatus(OrderStatus.CREATED)
                .description(orderDto.getDescription())
                .build();

        Set<OrderItemEntity> orderItemEntities = orderDto.getOrderItems()
                .stream()
                .map(orderItemDto -> toOrderItemEntity(orderItemDto, orderEntity))
                .collect(Collectors.toSet());

        orderEntity.setOrderItems(orderItemEntities);

        return orderEntity;
    }

    @Override
    public Order toDto(OrderEntity orderEntity) {

        List<OrderItem> orderItemDtos = orderEntity.getOrderItems()
                .stream()
                .map(this::toOrderItemDto)
                .collect(Collectors.toList());

        Order orderDto = Order.builder()
                .description(orderEntity.getDescription())
                .orderItems(orderItemDtos)
                .build();

        return orderDto;
    }

    private OrderItem toOrderItemDto(OrderItemEntity orderItemEntity) {

        OrderItem orderItemDto = OrderItem.builder()
                .skuCode(orderItemEntity.getSkuCode())
                .unitPrice(orderItemEntity.getUnitPrice())
                .quantity(orderItemEntity.getQuantity())
                .build();

        return orderItemDto;
    }

    private OrderItemEntity toOrderItemEntity(OrderItem orderItemDto, OrderEntity orderEntity) {

        OrderItemEntity orderItemEntity = OrderItemEntity.builder()
                .skuCode(orderItemDto.getSkuCode())
                .unitPrice(orderItemDto.getUnitPrice())
                .quantity(orderItemDto.getQuantity())
                .order(orderEntity)
                .build();

        return orderItemEntity;
    }
}
