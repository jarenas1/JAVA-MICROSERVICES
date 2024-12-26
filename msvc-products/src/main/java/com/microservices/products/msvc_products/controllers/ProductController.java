package com.microservices.products.msvc_products.controllers;

import com.microservices.products.msvc_products.entities.ProductEntity;
import com.microservices.products.msvc_products.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping
public class ProductController {

    final private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductEntity> list(){
        return this.productService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductEntity> details (@PathVariable Long id){
        Optional<ProductEntity>product = this.productService.findById(id);
        if (product.isPresent()){
            return ResponseEntity.ok(product.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }
}
