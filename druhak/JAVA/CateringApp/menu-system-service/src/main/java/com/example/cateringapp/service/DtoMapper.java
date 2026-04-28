package com.example.cateringapp.service;

import com.example.cateringapp.dto.*;
import com.example.cateringapp.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DtoMapper {

    public TemplateDto toDto(Template template) {
        return new TemplateDto(
                template.getTemplateId(),
                template.getName(),
                template.getFont(),
                template.getBackgroundImage(),
                template.getStyleJson(),
                template.getCreatedAt()
        );
    }

    public MenuDto toDto(Menu menu) {
        return new MenuDto(
                menu.getMenuId(),
                menu.getProject().getProjectId(),
                menu.getName(),
                menu.getCreatedBy().getUserId(),
                menu.getCreatedAt()
        );
    }

    public MenuVersionDto toDto(MenuVersion version) {
        return new MenuVersionDto(
                version.getVersionId(),
                version.getMenu().getMenuId(),
                version.getTemplate() != null ? version.getTemplate().getTemplateId() : null,
                version.getVersionNumber(),
                version.isWithPrices(),
                version.getCreatedAt()
        );
    }

    public SectionDto toDto(Section section) {
        List<MenuItemDto> items = section.getMenuItems().stream()
                .map(this::toDto)
                .toList();
        return new SectionDto(
                section.getSectionId(),
                section.getVersion().getVersionId(),
                section.getName(),
                section.getDisplayOrder(),
                section.getCreatedAt(),
                items
        );
    }

    public MenuItemDto toDto(MenuItem menuItem) {
        return new MenuItemDto(
                menuItem.getMenuItemId(),
                menuItem.getItem().getItemId(),
                menuItem.getItem().getName(),
                menuItem.getServingsPerPerson(),
                menuItem.getPriceAtVersion(),
                menuItem.getDisplayOrder(),
                menuItem.getNotes()
        );
    }

    public ItemDto toDto(Item item) {
        return new ItemDto(
                item.getItemId(),
                item.getName(),
                item.getDescription(),
                item.getCategory(),
                item.getPrice(),
                item.getDph(),
                item.getAllergens()
        );
    }
}
