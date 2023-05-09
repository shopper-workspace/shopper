package com.shopper.orderservice.infrastructure.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.shopper.orderservice.infrastructure.entity.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
}
