package com.microservices.products.msvc_products.controllers;

import com.microservices.products.msvc_products.entities.ProductEntity;
import com.microservices.products.msvc_products.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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
    public ResponseEntity<ProductEntity> details (@PathVariable Long id) throws InterruptedException {
        //PROBANDP EL RESILENCE4J EN CASO DE ERROR
//        if (id.equals(10L)){
//            throw new IllegalStateException("error de resilence");
//        }

        //PROBANDO EL RESILENCE EN CASO DE TIEMPO DE ESPERA
//        if (id.equals(7L)){
//            TimeUnit.SECONDS.sleep(7L);
//        }
        Optional<ProductEntity>product = this.productService.findById(id);
        if (product.isPresent()){
            return ResponseEntity.ok(product.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    //METODOS CRUD
    @PostMapping()
    ResponseEntity<ProductEntity> save(@RequestBody ProductEntity productEntity){
        return ResponseEntity.status(201).body(this.productService.save(productEntity));
    }

    @PutMapping("/{id}")
    ResponseEntity<ProductEntity> update(@RequestBody ProductEntity productEntity, @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(this.productService.update(productEntity, id));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id){
        productService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
