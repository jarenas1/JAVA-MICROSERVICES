package com.microservices.items.msvc_items.controllers;

import com.microservices.items.msvc_items.entities.ItemEntity;
import com.microservices.items.msvc_items.entities.ProductDto;
import com.microservices.items.msvc_items.services.IItemService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
public class ItemController {

    @Autowired
    private IItemService itemService;

    @Autowired
    private CircuitBreakerFactory  circuitBreakerFactory;

    @GetMapping
    public List<ItemEntity>gettItems(@RequestParam(name ="name", required = false)String name,
                                     @RequestHeader(name ="token-request", required = false)String tokenRequest) {
        System.out.println(name);
        System.out.println(tokenRequest);
        return itemService.findAll();
    }

    //SIN UNSAR LA ANOTACION Y USANDO LA CONFIGURACION DE BEAN

    @GetMapping("/{id}")
    public ResponseEntity<?> getItemById(@PathVariable Long id) {
        Optional<ItemEntity> itemOptional = circuitBreakerFactory.create("items").run(()->itemService.findById(id), e ->{
            //CODIGO EN CASO DE ERROR
            System.out.println(e.getMessage());
            ProductDto productDto = new ProductDto();
            productDto.setName("fallaConResilence");
            productDto.setId(1L);
            productDto.setPrice(100.0);
            productDto.setCreatedAt(LocalDate.now());
            return Optional.of(new ItemEntity(productDto, 5));
        });
        if (itemOptional.isPresent()) {
            return ResponseEntity.ok(itemOptional.get());
        } else {
            return ResponseEntity.status(404).body(Collections.singletonMap("message", "Te product cant be founded in the products service"));
        }
    }

    @CircuitBreaker(name = "items", fallbackMethod = "getFallBackMethosProduct") //Este nombre sera el identificador del controlador
    @GetMapping("/details/{id}")
    public ResponseEntity<?> getItemById2(@PathVariable Long id) {
        Optional<ItemEntity> itemOptional = itemService.findById(id);
        if (itemOptional.isPresent()) {
            return ResponseEntity.ok(itemOptional.get());
        } else {
            return ResponseEntity.status(404).body(Collections.singletonMap("message", "Te product cant be founded in the products service"));
        }
    }

    //EL METODO DEBE DEVOLVER LO MISMO QUE DEVUELVE EL METODO AL QUE ACOMPAÑARA
    public ResponseEntity<?> getFallBackMethosProduct(Throwable e){
        System.out.println(e.getMessage());
        ProductDto productDto = new ProductDto();
        productDto.setName("fallaConResilence");
        productDto.setId(1L);
        productDto.setPrice(100.0);
        productDto.setCreatedAt(LocalDate.now());
        return ResponseEntity.ok(new ItemEntity(productDto, 5));
    }
}


