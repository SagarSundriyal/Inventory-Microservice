package com.example.inventory.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryResponse {
 
	private Long productId;
    private String productName;
    private List<BatchDto> batches;
    
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public List<BatchDto> getBatches() {
		return batches;
	}
	public void setBatches(List<BatchDto> batches) {
		this.batches = batches;
	}
	
	public InventoryResponse(Long productId, String productName, List<BatchDto> batches) {
		// super();
		this.productId = productId;
		this.productName = productName;
		this.batches = batches;
	}
    
    
}
