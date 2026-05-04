package com.example.cateringapp.dto;

/**
 * Data Transfer Object for MenuVersion.
 */
public class MenuVersionDto {
    private long versionId;
    private long menuId;
    private int versionNumber;
    private boolean withPrices;
    private Long templateId;

    public MenuVersionDto() {
    }

    public MenuVersionDto(long versionId, long menuId, int versionNumber, boolean withPrices, Long templateId) {
        this.versionId = versionId;
        this.menuId = menuId;
        this.versionNumber = versionNumber;
        this.withPrices = withPrices;
        this.templateId = templateId;
    }

    public long getVersionId() {
        return versionId;
    }

    public void setVersionId(long versionId) {
        this.versionId = versionId;
    }

    public long getMenuId() {
        return menuId;
    }

    public void setMenuId(long menuId) {
        this.menuId = menuId;
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public boolean isWithPrices() {
        return withPrices;
    }

    public void setWithPrices(boolean withPrices) {
        this.withPrices = withPrices;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    @Override
    public String toString() {
        return "MenuVersionDto{" +
                "versionId=" + versionId +
                ", menuId=" + menuId +
                ", versionNumber=" + versionNumber +
                ", withPrices=" + withPrices +
                ", templateId=" + templateId +
                '}';
    }
}

