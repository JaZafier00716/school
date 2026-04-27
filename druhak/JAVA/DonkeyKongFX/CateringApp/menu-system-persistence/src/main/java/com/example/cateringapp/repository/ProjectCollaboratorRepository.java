package com.example.cateringapp.repository;

import com.example.cateringapp.entity.ProjectCollaborator;
import com.example.cateringapp.entity.ProjectCollaboratorId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectCollaboratorRepository extends JpaRepository<ProjectCollaborator, ProjectCollaboratorId> {

    boolean existsByProjectProjectIdAndUserUserId(Long projectId, Long userId);
}
