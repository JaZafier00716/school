package com.example.cateringapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Project.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto {
    private long projectId;
    private String name;
    private String status;
    private Long activeVersionId;
}

