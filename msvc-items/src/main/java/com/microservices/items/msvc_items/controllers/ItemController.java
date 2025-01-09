package com.microservices.items.msvc_items.controllers;

import com.microservices.items.msvc_items.entities.ItemEntity;
import com.microservices.items.msvc_items.services.IItemService;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
public class ItemController {

    @Autowired
    private IItemService itemService;

    @GetMapping
    public List<ItemEntity>gettItems(@RequestParam(name ="name", required = false)String name,
                                     @RequestHeader(name ="token-request", required = false)String tokenRequest) {
        System.out.println(name);
        System.out.println(tokenRequest);
        return itemService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getItemById(@PathVariable Long id) {
        Optional<ItemEntity> optionalItem = itemService.findById(id);

        if (optionalItem.isPresent()) {
            return ResponseEntity.ok(optionalItem.get());
        } else {
            return ResponseEntity.status(400).body(Collections.singletonMap("message", "Te product cant be founded in the products service"));
        }
    }
}


