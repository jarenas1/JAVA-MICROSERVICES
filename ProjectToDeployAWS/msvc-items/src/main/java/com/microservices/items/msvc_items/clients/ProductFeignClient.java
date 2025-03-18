package com.microservices.items.msvc_items.clients;

import com.microservices.items.msvc_items.entities.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//ACA VAMOS A MANEJAR LA COMUNCACION ENTRE NUESTROS 2 SERVICIOS, LA IDEA ES INYECTAR ESTA CLASE EN NUESTRA CLASE SERVICE

@FeignClient(name = "msvc-products")  //PONEMOS EL PUERTO DONDE SE LEVANTA EL NOMBRE DEL SERVICIO
public interface ProductFeignClient {

    @GetMapping()
    public List<ProductDto> findAll();

    @GetMapping("/{id}")
    public ProductDto details(@PathVariable Long id);

    @PostMapping
    public ProductDto create(@RequestBody ProductDto productDto);

    @PutMapping("/{id}")
    public ProductDto update(@PathVariable Long id, @RequestBody ProductDto productDto);

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id);
}

