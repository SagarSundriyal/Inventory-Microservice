package com.example.inventory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.inventory.dto.BatchDto;
import com.example.inventory.dto.InventoryResponse;
import com.example.inventory.dto.UpdateInventoryRequest;

import java.util.List;

public interface InventoryService {
    InventoryResponse getInventoryByProduct(Long productId);
    List<Long> updateInventory(UpdateInventoryRequest request);
}