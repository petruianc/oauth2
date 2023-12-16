package com.petru.inventoryservice.service;

import com.petru.inventoryservice.dto.InventoryResponse;
import com.petru.inventoryservice.repository.InventoryRepo;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepo inventoryRepo;
    @Transactional(readOnly = true)
    @SneakyThrows
    public List<InventoryResponse> inStock(List<String> skuCode){
        log.info("Wait started!");
        Thread.sleep(10000);
        log.info("Wait ended");
        return inventoryRepo.findBySkuCodeIn(skuCode).stream()
                .map(inventory ->
                        InventoryResponse.builder()
                                .skuCode(inventory.getSkuCode())
                                .isInStock(inventory.getQuantity()>0).build()).toList();
    }
}
