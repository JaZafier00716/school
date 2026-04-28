package com.example.cateringapp.dto;

import java.time.Instant;
import java.util.List;

public record SectionDto(
        Long sectionId,
        Long versionId,
        String name,
        int displayOrder,
        Instant createdAt,
        List<MenuItemDto> menuItems
) {
}
