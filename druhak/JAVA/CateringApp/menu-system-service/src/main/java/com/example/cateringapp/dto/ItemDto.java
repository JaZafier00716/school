package com.example.cateringapp.dto;

import java.math.BigDecimal;

public record ItemDto(
        Long itemId,
        String name,
        String description,
        String category,
        BigDecimal price,
        BigDecimal dph,
        String allergens
) {
}
