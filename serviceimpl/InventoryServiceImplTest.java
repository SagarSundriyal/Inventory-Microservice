package com.example.inventory.serviceimpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.inventory.dto.InventoryResponse;
import com.example.inventory.dto.UpdateInventoryRequest;
import com.example.inventory.entity.InventoryBatch;
import com.example.inventory.repository.InventoryRepository;
import com.example.inventory.service.impl.InventoryServiceImpl;

@ExtendWith(MockitoExtension.class)
class InventoryServiceImplTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private InventoryBatch mockBatch1; // Mocking the Entity

    @Mock
    private InventoryBatch mockBatch2; // Mocking another Entity

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    @Test
    @DisplayName("getInventoryByProduct - Should return mapped response")
    void testGetInventoryByProduct() {
        // Arrange
        Long productId = 101L;
        
        // Stubbing mocked entity behaviors
        when(mockBatch1.getBatchId()).thenReturn(500L);
        when(mockBatch1.getQuantity()).thenReturn(10);
        when(mockBatch1.getProductName()).thenReturn("Test Product");
        
        List<InventoryBatch> batches = List.of(mockBatch1);
        when(inventoryRepository.findByProductIdOrderByExpiryDateAsc(productId)).thenReturn(batches);

        // Act
        InventoryResponse response = inventoryService.getInventoryByProduct(productId);

        // Assert
        assertNotNull(response);
        assertEquals("Test Product", response.getProductName());
        assertEquals(1, response.getBatches().size());
        verify(inventoryRepository).findByProductIdOrderByExpiryDateAsc(productId);
    }

    @Test
    @DisplayName("updateInventory - Should reduce quantity across multiple batches")
    void testUpdateInventory_MultipleBatches() {
        // Arrange
        UpdateInventoryRequest request = new UpdateInventoryRequest(101L, 15);
        
        // Batch 1 has 10 items
        when(mockBatch1.getBatchId()).thenReturn(1L);
        when(mockBatch1.getQuantity()).thenReturn(10);
        
        // Batch 2 has 10 items
        when(mockBatch2.getBatchId()).thenReturn(2L);
        when(mockBatch2.getQuantity()).thenReturn(10);

        List<InventoryBatch> batches = Arrays.asList(mockBatch1, mockBatch2);
        when(inventoryRepository.findByProductIdOrderByExpiryDateAsc(101L)).thenReturn(batches);

        // Act
        List<Long> result = inventoryService.updateInventory(request);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(1L));
        assertTrue(result.contains(2L));

        // Verify that setQuantity was called to reduce inventory
        // Batch 1 should be 0 (10 - 10)
        verify(mockBatch1).setQuantity(0);
        // Batch 2 should be 5 (10 - 5)
        verify(mockBatch2).setQuantity(5);
        
        verify(inventoryRepository).saveAll(batches);
    }

    @Test
    @DisplayName("updateInventory - Should throw exception if no batches found")
    void testUpdateInventory_NotFound() {
        // Arrange
        UpdateInventoryRequest request = new UpdateInventoryRequest(999L, 5);
        when(inventoryRepository.findByProductIdOrderByExpiryDateAsc(999L)).thenReturn(new ArrayList<>());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            inventoryService.updateInventory(request);
        });

        assertEquals("Product not found or out of stock!", exception.getMessage());
        verify(inventoryRepository, never()).saveAll(any());
    }
}