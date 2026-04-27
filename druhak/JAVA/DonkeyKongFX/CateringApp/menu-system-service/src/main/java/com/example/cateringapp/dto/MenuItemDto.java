package com.example.cateringapp.dto;

import java.math.BigDecimal;

public record MenuItemDto(
        Long menuItemId,
        Long itemId,
        String itemName,
        BigDecimal servingsPerPerson,
        BigDecimal priceAtVersion,
        int displayOrder,
        String notes
) {
}
