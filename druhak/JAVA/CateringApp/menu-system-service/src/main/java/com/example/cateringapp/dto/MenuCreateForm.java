package com.example.cateringapp.dto;

public class MenuCreateForm {
    private Long projectId;
    private Long createdBy;
    private String name;
    private Long templateId;
    private boolean withPrices;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public boolean isWithPrices() {
        return withPrices;
    }

    public void setWithPrices(boolean withPrices) {
        this.withPrices = withPrices;
    }
}
