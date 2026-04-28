package com.example.cateringapp.controller.web;

import com.example.cateringapp.dto.*;
import com.example.cateringapp.service.exception.NotFoundException;
import com.example.cateringapp.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/ui")
public class WebController {

    private final LookupService lookupService;
    private final MenuService menuService;
    private final SectionService sectionService;
    private final ItemService itemService;
    private final ProjectService projectService;

    public WebController(LookupService lookupService, MenuService menuService, SectionService sectionService, ItemService itemService,
                         ProjectService projectService) {
        this.lookupService = lookupService;
        this.menuService = menuService;
        this.sectionService = sectionService;
        this.itemService = itemService;
        this.projectService = projectService;
    }

    @GetMapping
    public String rootUi() {
        return "redirect:/ui/menus/create";
    }

    @GetMapping("/menus/create")
    public String createMenuScreen(Model model) {
        model.addAttribute("request", new MenuCreateForm());
        model.addAttribute("projects", lookupService.getProjects());
        model.addAttribute("users", lookupService.getUsers());
        model.addAttribute("templates", lookupService.getTemplates());
        return "menus/create-menu";
    }

    @PostMapping("/menus/create")
    public String createMenuSubmit(@ModelAttribute("request") MenuCreateForm request,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {
        if (request.getProjectId() == null || request.getCreatedBy() == null || request.getName() == null || request.getName().isBlank()) {
            model.addAttribute("projects", lookupService.getProjects());
            model.addAttribute("users", lookupService.getUsers());
            model.addAttribute("templates", lookupService.getTemplates());
            return "menus/create-menu";
        }

        MenuDto created = menuService.createMenu(new MenuCreateRequest(
                request.getProjectId(),
                request.getCreatedBy(),
                request.getName(),
                request.getTemplateId(),
                request.isWithPrices()
        ));
        redirectAttributes.addFlashAttribute("message", "Menu created: " + created.menuId());
        return "redirect:/ui/menus/" + created.menuId() + "/edit";
    }

    @GetMapping("/menus/{id}/edit")
    public String editMenuScreen(@PathVariable("id") Long menuId,
                                 @RequestParam(name = "versionId", required = false) Long versionId,
                                 Model model) {
        MenuDto menu = menuService.getMenu(menuId);
        var versions = menuService.getMenuVersions(menuId);
        Long selectedVersionId = versionId != null
                ? versionId
                : versions.stream().reduce((first, second) -> second).map(MenuVersionDto::versionId).orElse(null);

        model.addAttribute("menu", menu);
        model.addAttribute("menuId", menuId);
        model.addAttribute("selectedVersionId", selectedVersionId);
        model.addAttribute("sections", selectedVersionId != null ? sectionService.getSectionsByVersion(selectedVersionId) : java.util.List.of());
        model.addAttribute("versions", versions);
        model.addAttribute("cloneRequest", new CloneVersionForm());
        ProjectActiveVersionForm activeVersionForm = new ProjectActiveVersionForm();
        activeVersionForm.setVersionId(selectedVersionId);
        model.addAttribute("activeVersionRequest", activeVersionForm);
        return "menus/edit-menu";
    }

    @PostMapping("/menus/{id}/versions")
    public String cloneVersionSubmit(@PathVariable("id") Long menuId,
                                     @ModelAttribute("cloneRequest") CloneVersionForm request,
                                     RedirectAttributes redirectAttributes,
                                     Model model) {
        if (request.getUserId() == null) {
            model.addAttribute("menuId", menuId);
            model.addAttribute("versions", menuService.getMenuVersions(menuId));
            return "menus/edit-menu";
        }

        MenuVersionDto created = menuService.createNewMenuVersion(menuId, request.getUserId());
        redirectAttributes.addFlashAttribute("message", "Created version " + created.versionNumber());
        return "redirect:/ui/menus/" + menuId + "/edit?versionId=" + created.versionId();
    }

    @PostMapping("/projects/{id}/active-version")
    public String switchActiveVersionSubmit(@PathVariable("id") Long projectId,
                                            @ModelAttribute("activeVersionRequest") ProjectActiveVersionForm request,
                                            @RequestParam("menuId") Long menuId,
                                            RedirectAttributes redirectAttributes) {
        if (request.getVersionId() == null) {
            return "redirect:/ui/menus/" + menuId + "/edit";
        }
        projectService.updateActiveVersion(projectId, request.getVersionId());
        redirectAttributes.addFlashAttribute("message", "Active version switched");
        return "redirect:/ui/menus/" + menuId + "/edit?versionId=" + request.getVersionId();
    }

    @GetMapping("/sections/create")
    public String createSectionScreen(@RequestParam("versionId") Long versionId, Model model) {
        CreateSectionForm form = new CreateSectionForm();
        form.setVersionId(versionId);
        model.addAttribute("request", form);
        model.addAttribute("versionId", versionId);
        return "sections/create-section";
    }

    @PostMapping("/sections/create")
    public String createSectionSubmit(@ModelAttribute("request") CreateSectionForm request,
                                      Model model,
                                      RedirectAttributes redirectAttributes) {
        if (request.getVersionId() == null || request.getName() == null || request.getName().isBlank()) {
            model.addAttribute("versionId", request.getVersionId());
            return "sections/create-section";
        }

        SectionDto created = sectionService.createSection(
                new CreateSectionRequest(request.getVersionId(), request.getName(), request.getDisplayOrder())
        );
        MenuVersionDto version = menuService.getVersion(created.versionId());
        redirectAttributes.addFlashAttribute("message", "Section created: " + created.sectionId());
        return "redirect:/ui/menus/" + version.menuId() + "/edit";
    }

    @GetMapping("/sections/{id}/edit")
    public String editSectionScreen(@PathVariable("id") Long sectionId, Model model) {
        SectionDto section = sectionService.getSection(sectionId);
        model.addAttribute("sectionId", section.sectionId());
        model.addAttribute("versionId", section.versionId());
        UpdateSectionForm form = new UpdateSectionForm();
        form.setName(section.name());
        form.setDisplayOrder(section.displayOrder());
        model.addAttribute("request", form);
        return "sections/edit-section";
    }

    @PostMapping("/sections/{id}/edit")
    public String editSectionSubmit(@PathVariable("id") Long sectionId,
                                    @ModelAttribute("request") UpdateSectionForm request,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        if (request.getName() == null || request.getName().isBlank()) {
            SectionDto section = sectionService.getSection(sectionId);
            model.addAttribute("sectionId", section.sectionId());
            model.addAttribute("versionId", section.versionId());
            return "sections/edit-section";
        }

        SectionDto updated = sectionService.updateSection(
                sectionId,
                new UpdateSectionRequest(request.getName(), request.getDisplayOrder())
        );
        MenuVersionDto version = menuService.getVersion(updated.versionId());
        redirectAttributes.addFlashAttribute("message", "Section updated");
        return "redirect:/ui/menus/" + version.menuId() + "/edit";
    }

    @GetMapping("/items/create")
    public String createItemScreen(Model model) {
        model.addAttribute("request", new CreateItemForm());
        return "items/create-item";
    }

    @PostMapping("/items/create")
    public String createItemSubmit(@ModelAttribute("request") CreateItemForm request,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        if (request.getName() == null || request.getName().isBlank()) {
            return "items/create-item";
        }

        ItemDto created = itemService.create(new CreateItemRequest(
                request.getName(),
                request.getDescription(),
                request.getCategory(),
                request.getPrice(),
                request.getDph(),
                request.getAllergens()
        ));
        redirectAttributes.addFlashAttribute("message", "Item created: " + created.itemId());
        return "redirect:/ui/items/" + created.itemId() + "/edit";
    }

    @GetMapping("/items/{id}/edit")
    public String editItemScreen(@PathVariable("id") Long itemId, Model model) {
        ItemDto item = itemService.getById(itemId);
        model.addAttribute("itemId", item.itemId());
        UpdateItemForm form = new UpdateItemForm();
        form.setName(item.name());
        form.setDescription(item.description());
        form.setCategory(item.category());
        form.setPrice(item.price());
        form.setDph(item.dph());
        form.setAllergens(item.allergens());
        model.addAttribute("request", form);
        return "items/edit-item";
    }

    @PostMapping("/items/{id}/edit")
    public String editItemSubmit(@PathVariable("id") Long itemId,
                                 @ModelAttribute("request") UpdateItemForm request,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if (request.getName() == null || request.getName().isBlank()) {
            model.addAttribute("itemId", itemId);
            return "items/edit-item";
        }

        itemService.update(itemId, new UpdateItemRequest(
                request.getName(),
                request.getDescription(),
                request.getCategory(),
                request.getPrice(),
                request.getDph(),
                request.getAllergens()
        ));
        redirectAttributes.addFlashAttribute("message", "Item updated");
        return "redirect:/ui/items/" + itemId + "/edit";
    }

    @ExceptionHandler(NotFoundException.class)
    public String handleNotFound(NotFoundException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }
}
