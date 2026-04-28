package com.example.cateringapp.dto;

import java.time.Instant;

public record TemplateDto(
        Long templateId,
        String name,
        String font,
        String backgroundImage,
        String styleJson,
        Instant createdAt
) {
}
