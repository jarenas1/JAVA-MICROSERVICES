package com.microservices.products.msvc_products.services;



import com.juan.libs.msvc.commons.libs_msvc_commons.entities.ProductEntity;

import java.util.List;
import java.util.Optional;

public interface IProductSevice {

    List<ProductEntity> findAll();

    Optional<ProductEntity> findById(Long id);

    //CRUD

    ProductEntity save(ProductEntity productEntity);

    ProductEntity update(ProductEntity productEntity, Long id);

    void deleteById(Long id);
}
