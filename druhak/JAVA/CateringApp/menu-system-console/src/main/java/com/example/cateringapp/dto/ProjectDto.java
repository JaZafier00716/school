package com.example.cateringapp.dto;

/**
 * Data Transfer Object for Project.
 */
public class ProjectDto {
    private long projectId;
    private String name;
    private String status;
    private Long activeVersionId;

    public ProjectDto() {
    }

    public ProjectDto(long projectId, String name, String status, Long activeVersionId) {
        this.projectId = projectId;
        this.name = name;
        this.status = status;
        this.activeVersionId = activeVersionId;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getActiveVersionId() {
        return activeVersionId;
    }

    public void setActiveVersionId(Long activeVersionId) {
        this.activeVersionId = activeVersionId;
    }

    @Override
    public String toString() {
        return "ProjectDto{" +
                "projectId=" + projectId +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", activeVersionId=" + activeVersionId +
                '}';
    }
}

