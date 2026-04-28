package com.example.cateringapp.controller.api;

import com.example.cateringapp.dto.CreateSectionRequest;
import com.example.cateringapp.dto.SectionDto;
import com.example.cateringapp.dto.UpdateSectionRequest;
import com.example.cateringapp.service.SectionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
public class SectionRestController {

    private final SectionService sectionService;

    public SectionRestController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/sections")
    public SectionDto createSection(@RequestBody @Valid CreateSectionRequest request) {
        return sectionService.createSection(request);
    }

    @PutMapping("/sections/{id}")
    public SectionDto updateSection(@PathVariable("id") Long sectionId,
                                    @RequestBody @Valid UpdateSectionRequest request) {
        return sectionService.updateSection(sectionId, request);
    }
}
