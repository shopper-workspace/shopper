package com.shopper.productservice.infrastructure.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.shopper.productservice.infrastructure.entity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
}
