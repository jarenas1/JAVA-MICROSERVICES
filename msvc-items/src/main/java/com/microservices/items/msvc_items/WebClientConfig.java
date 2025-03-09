package com.microservices.items.msvc_items;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
//
//    @Bean
//    @LoadBalanced
//    WebClient.Builder WebClient(){
//        return WebClient.builder();
//    }

    //PARA CONTINUAR CON EL CONTEXTO DE LA TRAZA
    @Bean
    WebClient WebClient(WebClient.Builder WCbuilder, ReactorLoadBalancerExchangeFilterFunction lbFunction) { //Pasamos un wc, el cual es creado por spring, por ende contiene todo el contexto de la traza

        return WCbuilder.baseUrl("direccion del msvc").build();
    }
}
