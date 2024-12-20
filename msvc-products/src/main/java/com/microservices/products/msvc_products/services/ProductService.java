package com.microservices.products.msvc_products.services;

import com.microservices.products.msvc_products.entities.ProductEntity;
import com.microservices.products.msvc_products.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductSevice{

//    @Autowired
//    private ProductRepository productRepository;

    //MEJPR PRACTICA
    final private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProductEntity> findAll() {
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductEntity> findById(Long id) {
        return Optional.empty();
    }
}
