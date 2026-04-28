package com.example.cateringapp.service;

import com.example.cateringapp.dto.MenuCreateRequest;
import com.example.cateringapp.dto.MenuDto;
import com.example.cateringapp.dto.MenuVersionDto;
import com.example.cateringapp.entity.*;
import com.example.cateringapp.service.exception.BadRequestException;
import com.example.cateringapp.service.exception.ForbiddenOperationException;
import com.example.cateringapp.service.exception.NotFoundException;
import com.example.cateringapp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Service
public class MenuService {

    private MenuRepository menuRepository;
    private MenuVersionRepository menuVersionRepository;
    private SectionRepository sectionRepository;
    private MenuItemRepository menuItemRepository;
    private ProjectRepository projectRepository;
    private UserRepository userRepository;
    private TemplateRepository templateRepository;
    private ProjectCollaboratorRepository projectCollaboratorRepository;
    private DtoMapper mapper;

    @Autowired
    public MenuService(
            MenuRepository menuRepository,
            MenuVersionRepository menuVersionRepository,
            SectionRepository sectionRepository,
            MenuItemRepository menuItemRepository,
            ProjectRepository projectRepository,
            UserRepository userRepository,
            TemplateRepository templateRepository,
            ProjectCollaboratorRepository projectCollaboratorRepository,
            DtoMapper mapper
    ) {
        this.menuRepository = menuRepository;
        this.menuVersionRepository = menuVersionRepository;
        this.sectionRepository = sectionRepository;
        this.menuItemRepository = menuItemRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.templateRepository = templateRepository;
        this.projectCollaboratorRepository = projectCollaboratorRepository;
        this.mapper = mapper;
    }

    @Transactional
    public MenuDto createMenu(MenuCreateRequest request) {
        Project project = projectRepository.findById(request.projectId())
                .orElseThrow(() -> new NotFoundException("Project not found: " + request.projectId()));
        User creator = userRepository.findById(request.createdBy())
                .orElseThrow(() -> new NotFoundException("User not found: " + request.createdBy()));

        Menu menu = new Menu();
        menu.setProject(project);
        menu.setName(request.name());
        menu.setCreatedBy(creator);
        Menu savedMenu = menuRepository.save(menu);

        MenuVersion firstVersion = new MenuVersion();
        firstVersion.setMenu(savedMenu);
        firstVersion.setVersionNumber(1);
        firstVersion.setWithPrices(request.withPrices());

        if (request.templateId() != null) {
            Template template = templateRepository.findById(request.templateId())
                    .orElseThrow(() -> new NotFoundException("Template not found: " + request.templateId()));
            firstVersion.setTemplate(template);
        }

        MenuVersion savedVersion = menuVersionRepository.save(firstVersion);
        project.setActiveVersion(savedVersion);
        projectRepository.save(project);

        return new MenuDto(savedMenu.getMenuId(), project.getProjectId(), savedMenu.getName(), creator.getUserId(), null);
    }

    public List<MenuVersionDto> getMenuVersions(Long menuId) {
        if (!menuRepository.existsById(menuId)) {
            throw new NotFoundException("Menu not found: " + menuId);
        }
        return menuVersionRepository.findByMenuMenuIdOrderByVersionNumberAsc(menuId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public MenuDto getMenu(Long menuId) {
        Menu menu = menuRepository.findByMenuId(menuId)
                .orElseThrow(() -> new NotFoundException("Menu not found: " + menuId));
        return mapper.toDto(menu);
    }

    public MenuVersionDto getVersion(Long versionId) {
        MenuVersion version = menuVersionRepository.findById(versionId)
                .orElseThrow(() -> new NotFoundException("Menu version not found: " + versionId));
        return mapper.toDto(version);
    }

    @Transactional
    public MenuVersionDto createNewMenuVersion(Long menuId, Long userId) {
        Menu menu = menuRepository.findByMenuId(menuId)
                .orElseThrow(() -> new NotFoundException("Menu not found: " + menuId));

        Long projectId = menu.getProject().getProjectId();
        boolean collaborator = projectCollaboratorRepository.existsByProjectProjectIdAndUserUserId(projectId, userId);
        if (!collaborator) {
            throw new ForbiddenOperationException("User is not a collaborator for project " + projectId);
        }

        MenuVersion activeVersion = resolveActiveVersion(menu);

        int nextVersion = menuVersionRepository.findTopByMenuMenuIdOrderByVersionNumberDesc(menuId)
                .map(MenuVersion::getVersionNumber)
                .orElse(0) + 1;

        MenuVersion newVersion = new MenuVersion();
        newVersion.setMenu(menu);
        newVersion.setTemplate(activeVersion.getTemplate());
        newVersion.setWithPrices(activeVersion.isWithPrices());
        newVersion.setVersionNumber(nextVersion);
        MenuVersion savedVersion = menuVersionRepository.save(newVersion);

        MenuVersion sourceDetailed = menuVersionRepository.findByVersionId(activeVersion.getVersionId())
                .orElseThrow(() -> new NotFoundException("Active version not found: " + activeVersion.getVersionId()));

        Map<Long, Section> sectionMapping = new HashMap<>();

        for (Section oldSection : sourceDetailed.getSections()) {
            Section clonedSection = new Section();
            clonedSection.setVersion(savedVersion);
            clonedSection.setName(oldSection.getName());
            clonedSection.setDisplayOrder(oldSection.getDisplayOrder());
            Section persistedSection = sectionRepository.save(clonedSection);
            sectionMapping.put(oldSection.getSectionId(), persistedSection);
        }

        for (Section oldSection : sourceDetailed.getSections()) {
            Section targetSection = sectionMapping.get(oldSection.getSectionId());
            for (MenuItem oldMenuItem : oldSection.getMenuItems()) {
                MenuItem clonedMenuItem = new MenuItem();
                clonedMenuItem.setSection(targetSection);
                clonedMenuItem.setItem(oldMenuItem.getItem());
                clonedMenuItem.setServingsPerPerson(oldMenuItem.getServingsPerPerson());
                clonedMenuItem.setPriceAtVersion(oldMenuItem.getPriceAtVersion());
                clonedMenuItem.setDisplayOrder(oldMenuItem.getDisplayOrder());
                clonedMenuItem.setNotes(oldMenuItem.getNotes());
                menuItemRepository.save(clonedMenuItem);
            }
        }

        menu.getProject().setActiveVersion(savedVersion);
        projectRepository.save(menu.getProject());

        return mapper.toDto(savedVersion);
    }

    private MenuVersion resolveActiveVersion(Menu menu) {
        MenuVersion projectActive = menu.getProject().getActiveVersion();
        if (projectActive != null) {
            MenuVersion loadedActive = menuVersionRepository.findById(projectActive.getVersionId())
                    .orElseThrow(() -> new NotFoundException("Project active version not found: " + projectActive.getVersionId()));
            if (loadedActive.getMenu().getMenuId().equals(menu.getMenuId())) {
                return loadedActive;
            }
        }

        return menuVersionRepository.findTopByMenuMenuIdOrderByVersionNumberDesc(menu.getMenuId())
                .orElseThrow(() -> new BadRequestException("Menu has no versions to clone"));
    }
}
