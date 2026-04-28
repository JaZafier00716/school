package com.example.cateringapp.service;

import com.example.cateringapp.dto.CreateSectionRequest;
import com.example.cateringapp.dto.SectionDto;
import com.example.cateringapp.dto.UpdateSectionRequest;
import com.example.cateringapp.entity.MenuVersion;
import com.example.cateringapp.entity.Section;
import com.example.cateringapp.service.exception.NotFoundException;
import com.example.cateringapp.repository.MenuVersionRepository;
import com.example.cateringapp.repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Service
public class SectionService {

    private SectionRepository sectionRepository;
    private MenuVersionRepository menuVersionRepository;
    private DtoMapper mapper;

    @Autowired
    public SectionService(SectionRepository sectionRepository, MenuVersionRepository menuVersionRepository, DtoMapper mapper) {
        this.sectionRepository = sectionRepository;
        this.menuVersionRepository = menuVersionRepository;
        this.mapper = mapper;
    }

    public List<SectionDto> getSectionsByVersion(Long versionId) {
        return sectionRepository.findByVersionVersionIdOrderByDisplayOrderAsc(versionId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Transactional
    public SectionDto createSection(CreateSectionRequest request) {
        MenuVersion version = menuVersionRepository.findById(request.versionId())
                .orElseThrow(() -> new NotFoundException("Menu version not found: " + request.versionId()));

        Section section = new Section();
        section.setVersion(version);
        section.setName(request.name());
        section.setDisplayOrder(request.displayOrder());

        return mapper.toDto(sectionRepository.save(section));
    }

    @Transactional
    public SectionDto updateSection(Long sectionId, UpdateSectionRequest request) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new NotFoundException("Section not found: " + sectionId));

        section.setName(request.name());
        section.setDisplayOrder(request.displayOrder());

        return mapper.toDto(sectionRepository.save(section));
    }

    public SectionDto getSection(Long sectionId) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new NotFoundException("Section not found: " + sectionId));
        return mapper.toDto(section);
    }
}
