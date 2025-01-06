package com.microservices.items.msvc_items.services;

import com.microservices.items.msvc_items.entities.ItemEntity;
import com.microservices.items.msvc_items.entities.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Primary
@Service
public class ItemServiceWebClient implements IItemService {

    @Autowired
    private WebClient.Builder webClient;


    @Override
    public List<ItemEntity> findAll() {
        return webClient.build().get().uri("http://msvc-products") //EN VEZ DE UNA URL USAMOS LOS NOBRES DEL BALANCEO DE CARGA DEL PROPETIES
                .accept(MediaType.APPLICATION_JSON) //RECIBIMOS SOLO RESPUESTAS JSON
                .retrieve() //LO TRANFORMAMOS
                .bodyToFlux(ProductDto.class) //LE DAMOS EL TIPO DE DATO
                .map(product -> new ItemEntity(product, new Random().nextInt(10) + 1)) //Creamos una lista de items con los productos recibidos
                .collectList() //CREAMOS UNA LISTA
                .block(); //BLOQUEAMOS EL REQUEST YA QUE NO ES UNA APLICACION REACTIVA
    }

    @Override
    public Optional<ItemEntity> findById(Long id) {
        //PASAMOS LOS PARAMETROS DNECESARIOS PARA LA PETICION HTTP
        HashMap<String, Long> params = new HashMap<>();
        params.put("id", id);

        return Optional.ofNullable(webClient.build().get().uri("http://msvc-products/{id}", params)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ProductDto.class) //Usamos MONO YA QUESOLO SE RECIBIRA UN ELEMENTO
                .map(product -> new ItemEntity(product, new Random().nextInt(10) + 1))
                .block());
    }
}
