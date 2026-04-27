package com.example.cateringapp.dto;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record CreateItemRequest(
        @NotBlank String name,
        String description,
        String category,
        BigDecimal price,
        BigDecimal dph,
        String allergens
) {
}
