package com.example.cateringapp.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "templates")
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_id")
    private Long templateId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "font")
    private String font;

    @Column(name = "background_image")
    private String backgroundImage;

    @Lob
    @Column(name = "style_json", columnDefinition = "TEXT")
    private String styleJson;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private Instant createdAt;

    public Long getTemplateId() {
        return templateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public String getStyleJson() {
        return styleJson;
    }

    public void setStyleJson(String styleJson) {
        this.styleJson = styleJson;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
