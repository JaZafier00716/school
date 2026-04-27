package com.example.cateringapp.repository;

import com.example.cateringapp.entity.Menu;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @EntityGraph(attributePaths = {"project", "createdBy"})
    Optional<Menu> findByMenuId(Long menuId);
}
