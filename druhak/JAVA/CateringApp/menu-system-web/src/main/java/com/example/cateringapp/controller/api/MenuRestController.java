package com.example.cateringapp.controller.api;

import com.example.cateringapp.dto.*;
import com.example.cateringapp.service.MenuService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MenuRestController {

    private final MenuService menuService;

    public MenuRestController(MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping("/menus")
    public MenuDto createMenu(@RequestBody @Valid MenuCreateRequest request) {
        return menuService.createMenu(request);
    }

    @GetMapping("/menus/{id}/versions")
    public List<MenuVersionDto> getMenuVersions(@PathVariable("id") Long menuId) {
        return menuService.getMenuVersions(menuId);
    }

    @PostMapping("/menus/{id}/versions")
    public MenuVersionDto cloneMenuVersion(@PathVariable("id") Long menuId, @RequestBody @Valid CloneVersionRequest request) {
        return menuService.createNewMenuVersion(menuId, request.userId());
    }
}
