package com.example.cateringapp.dto;

import java.math.BigDecimal;

public class UpdateItemForm {
    private String name;
    private String description;
    private String category;
    private BigDecimal price;
    private BigDecimal dph;
    private String allergens;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDph() {
        return dph;
    }

    public void setDph(BigDecimal dph) {
        this.dph = dph;
    }

    public String getAllergens() {
        return allergens;
    }

    public void setAllergens(String allergens) {
        this.allergens = allergens;
    }
}
