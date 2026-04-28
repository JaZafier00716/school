package com.example.cateringapp.dto;

import java.time.Instant;

public record MenuVersionDto(
        Long versionId,
        Long menuId,
        Long templateId,
        int versionNumber,
        boolean withPrices,
        Instant createdAt
) {
}
