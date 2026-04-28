package com.example.cateringapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MenuCreateRequest(
        @NotNull Long projectId,
        @NotNull Long createdBy,
        @NotBlank String name,
        Long templateId,
        boolean withPrices
) {
}
