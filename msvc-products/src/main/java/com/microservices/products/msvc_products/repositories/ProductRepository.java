package com.microservices.products.msvc_products.repositories;

import com.juan.libs.msvc.commons.libs_msvc_commons.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
