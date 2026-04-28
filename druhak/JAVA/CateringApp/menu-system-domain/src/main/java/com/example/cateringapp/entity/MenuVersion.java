package com.example.cateringapp.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "menu_versions")
public class MenuVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "version_id")
    private Long versionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    private Template template;

    @Column(name = "version_number", nullable = false)
    private int versionNumber;

    @Column(name = "with_prices", nullable = false)
    private boolean withPrices;

    @OneToMany(mappedBy = "version", fetch = FetchType.LAZY)
    @OrderBy("displayOrder ASC")
    private List<Section> sections = new ArrayList<>();

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private Instant createdAt;

    public Long getVersionId() {
        return versionId;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
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

    public List<Section> getSections() {
        return sections;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
