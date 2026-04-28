package com.example.cateringapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateSectionRequest(
        @NotNull Long versionId,
        @NotBlank String name,
        int displayOrder
) {
}
