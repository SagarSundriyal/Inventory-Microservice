package com.example.inventory.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.inventory.dto.InventoryResponse;
import com.example.inventory.dto.UpdateInventoryRequest;
import com.example.inventory.service.InventoryService;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
		this.inventoryService = inventoryService;
	}

	/**
     * Requirement: GET /inventory/{productId}
     * Returns list of inventory batches sorted by expiry date.
     */
    @GetMapping("/{productId}")
    public ResponseEntity<InventoryResponse> getInventory(@PathVariable Long productId) {
        return ResponseEntity.ok(inventoryService.getInventoryByProduct(productId));
    }

    /**
     * Requirement: POST /inventory/update
     * Updates inventory after an order is placed using Factory Pattern logic.
     */
    @PostMapping("/update")
    public ResponseEntity<List<Long>> updateInventory(@RequestBody UpdateInventoryRequest request) {
    	System.out.println("Received request for Product: " + request.getProductId());
        List<Long> reservedBatchIds = inventoryService.updateInventory(request);
        return ResponseEntity.ok(reservedBatchIds);
    }
}