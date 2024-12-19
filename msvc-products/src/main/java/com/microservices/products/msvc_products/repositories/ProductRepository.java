package com.microservices.products.msvc_products.repositories;

import com.microservices.products.msvc_products.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
