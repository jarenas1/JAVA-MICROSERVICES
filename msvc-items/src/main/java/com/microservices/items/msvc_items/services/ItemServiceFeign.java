package com.microservices.items.msvc_items.services;

import com.microservices.items.msvc_items.clients.ProductFeignClient;
import com.microservices.items.msvc_items.entities.ItemEntity;
import com.microservices.items.msvc_items.entities.ProductDto;
import feign.FeignException;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;


@Service
public class ItemServiceFeign implements IItemService {

    private final ProductFeignClient productFeignClient;

    public ItemServiceFeign(ProductFeignClient productFeignClient) {
        this.productFeignClient = productFeignClient;
    }

    @Override
    public List<ItemEntity> findAll() {
        return this.productFeignClient.findAll().stream().map( product ->{ //Pasamos de product a item list
            Random cuantity = new Random(); //generamos la cantidad random
            return new ItemEntity(product, cuantity.nextInt(10) + 1);  //se suma 1 ya que genera de 0 a 9
        }).collect(Collectors.toList()); //Transformamos a una lista que se devolvera
    }

    @Override
    public Optional<ItemEntity> findById(Long id) {
//        try{
            return Optional.of( new ItemEntity(this.productFeignClient.details(id),10));
//        } catch(FeignException e){
//            return Optional.empty();
//        }
    }

    @Override
    public ProductDto save(ProductDto product) {
        return null;
    }

    @Override
    public ProductDto update(ProductDto product, Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
