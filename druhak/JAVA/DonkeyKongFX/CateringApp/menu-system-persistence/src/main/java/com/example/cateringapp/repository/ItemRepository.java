package com.example.cateringapp.repository;

import com.example.cateringapp.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByNameContainingIgnoreCaseOrderByNameAsc(String name);
}
