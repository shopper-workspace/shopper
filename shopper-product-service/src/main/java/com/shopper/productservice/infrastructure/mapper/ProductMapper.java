package com.shopper.productservice.infrastructure.mapper;

import org.springframework.stereotype.Component;
import com.shopper.productservice.domain.dto.Product;
import com.shopper.productservice.infrastructure.entity.ProductEntity;

@Component
public class ProductMapper extends BaseMapper<ProductEntity, Product> {

    @Override
    public ProductEntity toEntity(Product productDto) {

        ProductEntity productEntity = ProductEntity.builder()
                .skuCode(productDto.getSkuCode())
                .name(productDto.getName())
                .description(productDto.getDescription())
                .price(productDto.getPrice())
                .build();

        return productEntity;
    }

    @Override
    public Product toDto(ProductEntity productEntity) {

        Product productDto = Product.builder()
                .skuCode(productEntity.getSkuCode())
                .name(productEntity.getName())
                .description(productEntity.getDescription())
                .price(productEntity.getPrice())
                .build();

        return productDto;
    }
}
