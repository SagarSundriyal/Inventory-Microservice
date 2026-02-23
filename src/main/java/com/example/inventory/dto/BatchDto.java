package com.example.inventory.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class BatchDto {
    private Long batchId;
    private Integer quantity;
    private LocalDate expiryDate;
    
	public Long getBatchId() {
		return batchId;
	}
	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public LocalDate getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(LocalDate expiryDate) {
		this.expiryDate = expiryDate;
	}
	
	public BatchDto(Long batchId, Integer quantity, LocalDate expiryDate) {
		super();
		this.batchId = batchId;
		this.quantity = quantity;
		this.expiryDate = expiryDate;
	}
	
	public BatchDto() {}
    
}
