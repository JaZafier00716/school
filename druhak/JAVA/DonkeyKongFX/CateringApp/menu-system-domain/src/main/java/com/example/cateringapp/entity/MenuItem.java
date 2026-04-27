package com.example.cateringapp.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "menu_items")
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_item_id")
    private Long menuItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(name = "servings_per_person", precision = 8, scale = 2)
    private BigDecimal servingsPerPerson;

    @Column(name = "price_at_version", precision = 12, scale = 2)
    private BigDecimal priceAtVersion;

    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    @Lob
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private Instant updatedAt;

    public Long getMenuItemId() {
        return menuItemId;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public BigDecimal getServingsPerPerson() {
        return servingsPerPerson;
    }

    public void setServingsPerPerson(BigDecimal servingsPerPerson) {
        this.servingsPerPerson = servingsPerPerson;
    }

    public BigDecimal getPriceAtVersion() {
        return priceAtVersion;
    }

    public void setPriceAtVersion(BigDecimal priceAtVersion) {
        this.priceAtVersion = priceAtVersion;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
