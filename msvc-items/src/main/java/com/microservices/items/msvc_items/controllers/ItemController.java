package com.microservices.items.msvc_items.controllers;

import com.microservices.items.msvc_items.entities.ItemEntity;
import com.microservices.items.msvc_items.services.IItemService;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController

public class ItemController {

    @Autowired
    private IItemService itemService;

    @GetMapping
    public List<ItemEntity>gettItems() {
        return itemService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemEntity> getItemById(@PathVariable Long id) {
        Optional<ItemEntity> optionalItem = itemService.findById(id);

        if (optionalItem.isPresent()) {
            return ResponseEntity.ok(optionalItem.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}


