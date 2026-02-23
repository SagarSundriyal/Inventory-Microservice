package com.example.inventory.handler;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.inventory.dto.BatchDto;
import com.example.inventory.dto.InventoryResponse;
import com.example.inventory.entity.InventoryBatch;
import com.example.inventory.repository.InventoryRepository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExpiryFIFOHandler implements InventoryHandler {

    private InventoryRepository repository;

	@Override
	public List<Long> deductStock(Long productId, Integer quantity) {
    List<InventoryBatch> batches = repository.findByProductIdOrderByExpiryDateAsc(productId);
        
        if (batches.isEmpty()) {
            throw new RuntimeException("Product not found or Out of Stock for ID: " + productId);
        }

        List<BatchDto> batchDtos = batches.stream()
                .map(batch -> new BatchDto(
                        batch.getBatchId(),
                        batch.getQuantity(),
                        batch.getExpiryDate()
                ))
                .toList();
        
        String name = batches.get(0).getProductName();
        return (List<Long>) new InventoryResponse(productId, name, batchDtos);
    }

	@Override
	public String getStrategyName() {
		return "EXPIRY_FIFO";
	}

}