package com.example.inventory.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.inventory.dto.InventoryResponse;
import com.example.inventory.dto.UpdateInventoryRequest;
import com.example.inventory.service.InventoryService;

@ExtendWith(MockitoExtension.class) // Required to initialize Mockito annotations
class InventoryControllerTest {

    @Mock
    private InventoryService inventoryService; // Create a mock of the service

    @InjectMocks
    private InventoryController inventoryController; // Inject the mock into the controller
    
    @Mock
    private InventoryResponse inventoryResponse;

    @Test
    @DisplayName("Should return mocked DTO when getInventory is called")
    void testGetInventoryWithMockedDto() {
        // 1. Arrange
        Long productId = 101L;
        
        when(inventoryResponse.getProductId()).thenReturn(productId);
        
        // Stub the service to return the MOCKED DTO
        when(inventoryService.getInventoryByProduct(productId)).thenReturn(inventoryResponse);

        // 2. Act
        ResponseEntity<InventoryResponse> result = inventoryController.getInventory(productId);

        // 3. Assert
        assertEquals(200, result.getStatusCode().value());
        
        // This works only because we stubbed getProductId() above
        assertEquals(productId, result.getBody().getProductId()); 
    }

    @Test
    @DisplayName("Should return list of batch IDs when updateInventory is called")
    void testUpdateInventory() {
        // Arrange
        UpdateInventoryRequest request = new UpdateInventoryRequest(101L, 10);
        List<Long> mockBatchIds = Arrays.asList(100L, 200L);
        
        when(inventoryService.updateInventory(any(UpdateInventoryRequest.class))).thenReturn(mockBatchIds);

        // Act
        ResponseEntity<List<Long>> response = inventoryController.updateInventory(request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals(100L, response.getBody().get(0));
        
        verify(inventoryService, times(1)).updateInventory(request);
    }
}