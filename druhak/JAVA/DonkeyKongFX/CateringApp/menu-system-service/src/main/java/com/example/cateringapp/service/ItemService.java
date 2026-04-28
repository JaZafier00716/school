package com.example.cateringapp.service;

import com.example.cateringapp.dto.CreateItemRequest;
import com.example.cateringapp.dto.ItemDto;
import com.example.cateringapp.dto.UpdateItemRequest;
import com.example.cateringapp.entity.Item;
import com.example.cateringapp.service.exception.NotFoundException;
import com.example.cateringapp.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Service
public class ItemService {

    private ItemRepository itemRepository;
    private DtoMapper mapper;

    @Autowired
    public ItemService(ItemRepository itemRepository, DtoMapper mapper) {
        this.itemRepository = itemRepository;
        this.mapper = mapper;
    }

    public List<ItemDto> searchByName(String name) {
        String filter = name == null ? "" : name;
        return itemRepository.findByNameContainingIgnoreCaseOrderByNameAsc(filter)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public ItemDto getById(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found: " + itemId));
        return mapper.toDto(item);
    }

    @Transactional
    public ItemDto create(CreateItemRequest request) {
        Item item = new Item();
        copy(item, request.name(), request.description(), request.category(), request.price(), request.dph(), request.allergens());
        return mapper.toDto(itemRepository.save(item));
    }

    @Transactional
    public ItemDto update(Long itemId, UpdateItemRequest request) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found: " + itemId));
        copy(item, request.name(), request.description(), request.category(), request.price(), request.dph(), request.allergens());
        return mapper.toDto(itemRepository.save(item));
    }

    private void copy(Item item, String name, String description, String category,
                      java.math.BigDecimal price, java.math.BigDecimal dph, String allergens) {
        item.setName(name);
        item.setDescription(description);
        item.setCategory(category);
        item.setPrice(price);
        item.setDph(dph);
        item.setAllergens(allergens);
    }
}
