package com.example.cateringapp.controller.api;

import com.example.cateringapp.dto.SectionDto;
import com.example.cateringapp.service.SectionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VersionRestController {

    private final SectionService sectionService;

    public VersionRestController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @GetMapping("/versions/{id}/sections")
    public List<SectionDto> getSectionsByVersion(@PathVariable("id") Long versionId) {
        return sectionService.getSectionsByVersion(versionId);
    }
}
