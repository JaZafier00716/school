package com.example.cateringapp.dto;

import java.time.Instant;

public record MenuDto(
        Long menuId,
        Long projectId,
        String name,
        Long createdBy,
        Instant createdAt
) {
}
