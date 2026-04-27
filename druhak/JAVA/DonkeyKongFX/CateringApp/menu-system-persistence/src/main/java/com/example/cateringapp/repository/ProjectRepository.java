package com.example.cateringapp.repository;

import com.example.cateringapp.entity.Project;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @EntityGraph(attributePaths = {"activeVersion"})
    Optional<Project> findByProjectId(Long projectId);
}
