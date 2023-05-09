package com.shopper.productservice.domain.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.shopper.productservice.domain.dto.Product;
import com.shopper.productservice.infrastructure.entity.ProductEntity;
import com.shopper.productservice.infrastructure.mapper.ProductMapper;
import com.shopper.productservice.infrastructure.repository.ProductRepository;
import com.shopper.shared.outbox.OutboxEvent;
import com.shopper.shared.outbox.OutboxEventService;
import com.shopper.shared.outbox.mapper.JsonMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final OutboxEventService outboxEventService;

    @Transactional
    public String createProduct(Product productDto) {

        ProductEntity product = productMapper.toEntity(productDto);

        productRepository.save(product);
        outboxEventService.publishEvent(new OutboxEvent("PRODUCT", product.getId(), "PRODUCT_CREATED", JsonMapper.writeToJson(product)));

        logger.info("Product created: {}", product.getId());

        return product.getId().toString();
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {

        List<ProductEntity> productEntities = productRepository.findAll();

        return productMapper.toDtoList(productEntities);
    }
}
