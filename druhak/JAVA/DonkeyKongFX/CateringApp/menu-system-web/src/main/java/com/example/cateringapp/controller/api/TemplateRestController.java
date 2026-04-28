package com.example.cateringapp.controller.api;

import com.example.cateringapp.dto.TemplateDto;
import com.example.cateringapp.service.TemplateService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TemplateRestController {

    private final TemplateService templateService;

    public TemplateRestController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @GetMapping("/templates")
    public List<TemplateDto> getTemplates() {
        return templateService.getAllTemplates();
    }
}
