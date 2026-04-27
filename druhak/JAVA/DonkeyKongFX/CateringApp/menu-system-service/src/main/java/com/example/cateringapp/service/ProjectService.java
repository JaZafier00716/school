package com.example.cateringapp.service;

import com.example.cateringapp.entity.MenuVersion;
import com.example.cateringapp.entity.Project;
import com.example.cateringapp.service.exception.BadRequestException;
import com.example.cateringapp.service.exception.NotFoundException;
import com.example.cateringapp.repository.MenuVersionRepository;
import com.example.cateringapp.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Service
public class ProjectService {

    private ProjectRepository projectRepository;
    private MenuVersionRepository menuVersionRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, MenuVersionRepository menuVersionRepository) {
        this.projectRepository = projectRepository;
        this.menuVersionRepository = menuVersionRepository;
    }

    @Transactional
    public void updateActiveVersion(Long projectId, Long versionId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found: " + projectId));
        MenuVersion version = menuVersionRepository.findById(versionId)
                .orElseThrow(() -> new NotFoundException("Menu version not found: " + versionId));

        boolean belongsToProject = version.getMenu().getProject().getProjectId().equals(projectId);
        if (!belongsToProject) {
            throw new BadRequestException("Version does not belong to project " + projectId);
        }

        project.setActiveVersion(version);
        projectRepository.save(project);
    }
}
