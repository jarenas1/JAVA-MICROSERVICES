package com.microservices.products.msvc_products.services;

import com.microservices.products.msvc_products.entities.ProductEntity;

import java.util.List;
import java.util.Optional;

public interface IProductSevice {

    List<ProductEntity> findAll();

    Optional<ProductEntity> findById(Long id);
}
