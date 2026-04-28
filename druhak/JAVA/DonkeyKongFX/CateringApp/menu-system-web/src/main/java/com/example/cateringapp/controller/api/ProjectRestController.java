package com.example.cateringapp.controller.api;

import com.example.cateringapp.dto.ProjectActiveVersionUpdateRequest;
import com.example.cateringapp.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProjectRestController {

    private final ProjectService projectService;

    public ProjectRestController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PutMapping("/projects/{id}/active-version")
    public ResponseEntity<Void> updateActiveVersion(@PathVariable("id") Long projectId,
                                                    @RequestBody @Valid ProjectActiveVersionUpdateRequest request) {
        projectService.updateActiveVersion(projectId, request.versionId());
        return ResponseEntity.noContent().build();
    }
}
