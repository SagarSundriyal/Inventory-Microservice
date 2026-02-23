package com.example.inventory.handler;

import java.util.List;

public interface InventoryHandler {
    List<Long> deductStock(Long productId, Integer quantity);
    String getStrategyName();
}