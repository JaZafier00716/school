package com.example.cateringapp.service;

import com.example.cateringapp.dto.TemplateDto;
import com.example.cateringapp.repository.TemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Service
public class TemplateService {

    private TemplateRepository templateRepository;
    private DtoMapper mapper;

    @Autowired
    public TemplateService(TemplateRepository templateRepository, DtoMapper mapper) {
        this.templateRepository = templateRepository;
        this.mapper = mapper;
    }

    public List<TemplateDto> getAllTemplates() {
        return templateRepository.findAll().stream().map(mapper::toDto).toList();
    }
}
