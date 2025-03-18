package com.microservices.products.msvc_products.services;




import com.juan.libs.msvc.commons.libs_msvc_commons.entities.ProductEntity;
import com.microservices.products.msvc_products.repositories.ProductRepository;
import jakarta.ws.rs.NotFoundException;
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

    @Override
    @Transactional
    public ProductEntity save(ProductEntity productEntity) {
        try{
            return this.productRepository.save(productEntity);
        }catch (Exception e){
            throw new IllegalStateException("The user cant be saved");
        }

    }

    @Override
    public ProductEntity update(ProductEntity productEntity, Long id) {
        ProductEntity product = this.productRepository.findById(id).orElseThrow(()-> new IllegalStateException("The product cant be updated"));

        product.setName(productEntity.getName());
        product.setPrice(productEntity.getPrice());

        try{
            return this.productRepository.save(product);
        }catch (Exception e){
            throw new IllegalStateException("The product cant be updated");
        }
    }

    @Override
    public void deleteById(Long id) {
        try{
            ProductEntity product = this.productRepository.findById(id).orElseThrow(()-> new NotFoundException("The product cant be deleted"));
            productRepository.delete(product);
        }catch (Exception e){
            throw new IllegalStateException("The product cant be deleted");
        }
    }
}
