package com.example.cateringapp.dto;

import jakarta.validation.constraints.NotNull;

public record ProjectActiveVersionUpdateRequest(@NotNull Long versionId) {
}
