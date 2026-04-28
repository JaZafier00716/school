package com.example.cateringapp.dto;

import jakarta.validation.constraints.NotNull;

public record CloneVersionRequest(@NotNull Long userId) {
}
