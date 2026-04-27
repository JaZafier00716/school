package com.example.cateringapp;

import com.example.cateringapp.entity.Item;
import com.example.cateringapp.repository.ItemRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackageClasses = Item.class)
@EnableJpaRepositories(basePackageClasses = ItemRepository.class)
public class CateringAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(CateringAppApplication.class, args);
    }
}
