package com.microservices.items.msvc_items.services;

import com.microservices.items.msvc_items.entities.ItemEntity;
import com.microservices.items.msvc_items.entities.ProductDto;

import java.util.List;
import java.util.Optional;

public interface IItemService {

    List<ItemEntity> findAll();

    Optional<ItemEntity> findById(Long id);

    ProductDto save(ProductDto product);

    ProductDto update(ProductDto product, Long id);

    void delete(Long id);
}
