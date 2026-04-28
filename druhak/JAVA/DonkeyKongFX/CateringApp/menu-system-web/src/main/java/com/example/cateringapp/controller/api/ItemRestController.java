package com.example.cateringapp.controller.api;

import com.example.cateringapp.dto.CreateItemRequest;
import com.example.cateringapp.dto.ItemDto;
import com.example.cateringapp.dto.UpdateItemRequest;
import com.example.cateringapp.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ItemRestController {

    private final ItemService itemService;

    public ItemRestController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/items")
    public List<ItemDto> searchItems(@RequestParam(name = "name", required = false) String name) {
        return itemService.searchByName(name);
    }

    @PostMapping("/items")
    public ItemDto createItem(@RequestBody @Valid CreateItemRequest request) {
        return itemService.create(request);
    }

    @PutMapping("/items/{id}")
    public ItemDto updateItem(@PathVariable("id") Long itemId, @RequestBody @Valid UpdateItemRequest request) {
        return itemService.update(itemId, request);
    }
}
