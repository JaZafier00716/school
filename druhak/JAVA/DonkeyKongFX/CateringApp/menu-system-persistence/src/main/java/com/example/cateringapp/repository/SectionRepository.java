package com.example.cateringapp.repository;

import com.example.cateringapp.entity.Section;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long> {

    @EntityGraph(attributePaths = {"menuItems", "menuItems.item", "version"})
    List<Section> findByVersionVersionIdOrderByDisplayOrderAsc(Long versionId);
}
