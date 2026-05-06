package com.example.cateringapp.dto;

import lombok.*;

/**
 * Data Transfer Object for MenuVersion.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuVersionDto {
    private long versionId;
    private long menuId;
    private int versionNumber;
    private boolean withPrices;
    private Long templateId;
}

