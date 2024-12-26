package com.microservices.products.msvc_products.services;

import com.microservices.products.msvc_products.entities.ProductEntity;
import com.microservices.products.msvc_products.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService implements IProductSevice{

//    @Autowired
//    private ProductRepository productRepository;

    //MEJPR PRACTICA
    final private ProductRepository productRepository;

    final private Environment environment;

    public ProductService(ProductRepository productRepository, Environment enviroment){
        this.productRepository = productRepository;
        this.environment = enviroment;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProductEntity> findAll() {
        //SETEAMOS EL PUERTO
        return this.productRepository.findAll().stream().map(productEntity -> {
            productEntity.setPort(Integer.parseInt(environment.getProperty("local.server.port")));
            return productEntity;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductEntity> findById(Long id) {
        return this.productRepository.findById(id).map(productEntity -> {
            productEntity.setPort(Integer.parseInt(environment.getProperty("local.server.port")));
            return productEntity;
        });
    }
}
