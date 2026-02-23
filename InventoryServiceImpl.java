package com.example.inventory.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.inventory.dto.BatchDto;
import com.example.inventory.dto.InventoryResponse;
import com.example.inventory.dto.UpdateInventoryRequest;
import com.example.inventory.entity.InventoryBatch;
import com.example.inventory.handler.InventoryHandlerFactory;
import com.example.inventory.repository.InventoryRepository;
import com.example.inventory.service.InventoryService;

import java.util.ArrayList;
import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryHandlerFactory handlerFactory;
    
    public InventoryServiceImpl(InventoryRepository inventoryRepository, InventoryHandlerFactory handlerFactory) {
		this.inventoryRepository = inventoryRepository;
		this.handlerFactory = handlerFactory;
	}

	@Override
    @Transactional(readOnly = true)
    public InventoryResponse getInventoryByProduct(Long productId) {
        List<InventoryBatch> batches = inventoryRepository.findByProductIdOrderByExpiryDateAsc(productId);
        
        if (batches == null || batches.isEmpty()) {
            throw new RuntimeException("No inventory found for Product ID: " + productId);
        }

        List<BatchDto> batchDtos = batches.stream()
                .map(batch -> {
                    BatchDto dto = new BatchDto();
                    dto.setBatchId(batch.getBatchId());
                    dto.setQuantity(batch.getQuantity());
                    dto.setExpiryDate(batch.getExpiryDate());
                    return dto;
                })
                .toList();

        String productName = batches.get(0).getProductName();
        return new InventoryResponse(productId, productName, batchDtos);
    }
    
    
    @Override
    @Transactional
    public List<Long> updateInventory(UpdateInventoryRequest request) {
        List<InventoryBatch> batches = inventoryRepository.findByProductIdOrderByExpiryDateAsc(request.getProductId());

        if (batches == null || batches.isEmpty()) {
            throw new RuntimeException("Product not found or out of stock!");
        }

        int requestedQty = request.getQuantity();
        List<Long> usedBatchIds = new ArrayList<>();

        for (InventoryBatch batch : batches) {
            if (requestedQty <= 0) break;

            int inventoryQty = batch.getQuantity();
            int take = Math.min(inventoryQty, requestedQty);

            batch.setQuantity(inventoryQty - take);
            requestedQty -= take;
            usedBatchIds.add(batch.getBatchId());
        }
        
        inventoryRepository.saveAll(batches);
        return usedBatchIds;
    }

}