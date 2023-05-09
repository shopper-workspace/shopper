package com.shopper.inventoryservice.infrastucture.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.shopper.inventoryservice.infrastucture.entity.InventoryEntity;

public interface InventoryRepository extends JpaRepository<InventoryEntity, Long> {

    InventoryEntity findBySkuCode(String skudeCode);

    List<InventoryEntity> findBySkuCodeIn(List<String> skuCode);
}
