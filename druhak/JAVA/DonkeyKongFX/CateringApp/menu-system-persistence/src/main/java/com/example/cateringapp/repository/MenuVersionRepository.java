package com.example.cateringapp.repository;

import com.example.cateringapp.entity.MenuVersion;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuVersionRepository extends JpaRepository<MenuVersion, Long> {

    List<MenuVersion> findByMenuMenuIdOrderByVersionNumberAsc(Long menuId);

    Optional<MenuVersion> findTopByMenuMenuIdOrderByVersionNumberDesc(Long menuId);

    @EntityGraph(attributePaths = {"menu", "template", "sections", "sections.menuItems", "sections.menuItems.item"})
    Optional<MenuVersion> findByVersionId(Long versionId);
}
