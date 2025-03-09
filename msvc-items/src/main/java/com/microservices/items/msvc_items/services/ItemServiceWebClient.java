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

//    @Autowired
//    private WebClient.Builder webClient;

    //SE MANEJA ASI AHORA POR LA CONFIGURACION PARA MANEJAR TRAZAS
    @Autowired
    private WebClient webClient;


    //BORRAREMOS DE TODOS LOS METODOS EL .BUILD()
    @Override
    public List<ItemEntity> findAll() {
        return webClient.get().uri("http://msvc-products") //EN VEZ DE UNA URL USAMOS LOS NOBRES DEL BALANCEO DE CARGA DEL PROPETIES
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

//        return Optional.ofNullable(webClient.build().get().uri("http://msvc-products/{id}", params)
        //BORRAMOS EL .BUILD()
        return Optional.ofNullable(webClient.get().uri("http://msvc-products/{id}", params)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ProductDto.class) //Usamos MONO YA QUESOLO SE RECIBIRA UN ELEMENTO
                .map(product -> new ItemEntity(product, new Random().nextInt(10) + 1))
                .block());
    }

    @Override
    public ProductDto save(ProductDto product) {
        return webClient.post().uri("http://msvc-products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(product)//cuerpo de peticion
                .retrieve() //se hace el envio de peticion
                .bodyToMono(ProductDto.class)//Pasamos la respuesta a mono
                .block(); //bloqueamos para devolver
    }

    @Override
    public ProductDto update(ProductDto product, Long id) {
        HashMap<String, Long> params = new HashMap<>();
        params.put("id", id);
        return webClient.put().uri("http://msvc-products/{id}", params)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(product)
                .retrieve()//envia peticion
                .bodyToMono(ProductDto.class)//Recibe respuesta
                .block(); //obtiene respuesta
    }

    @Override
    public void delete(Long id) {
        HashMap<String, Long> params = new HashMap<>();
        params.put("id", id);
        webClient.delete().uri("http://msvc-products/{id}", params) //NO RETORNA NADA
        .retrieve()
        .toBodilessEntity() // Indica que no esperas cuerpo
        .block();
    }
}
