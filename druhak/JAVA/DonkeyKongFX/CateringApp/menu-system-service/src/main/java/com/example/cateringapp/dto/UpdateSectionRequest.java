package com.example.cateringapp.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateSectionRequest(
        @NotBlank String name,
        int displayOrder
) {
}
