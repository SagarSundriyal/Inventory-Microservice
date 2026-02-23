package com.example.inventory.handler;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InventoryHandlerFactory {
    private List<InventoryHandler> handlers;

    public InventoryHandler getHandler(String strategyName) {
        return handlers.stream()
                .filter(h -> h.getStrategyName().equalsIgnoreCase(strategyName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown Strategy"));
    }
}