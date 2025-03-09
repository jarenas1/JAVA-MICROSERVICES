package com.juan.springcloud_msvcOauth.msvc_oauth;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    @LoadBalanced
//    WebClient.Builder webClientBuilder() {
//        return WebClient.builder().baseUrl("http://msvc-users");
//    }
    @Bean //COMPATIBLE CON LAS TRAZAS
    WebClient WebClient(WebClient.Builder WCbuilder, ReactorLoadBalancerExchangeFilterFunction lbFunction) { //Pasamos un wc, el cual es creado por spring, por ende contiene todo el contexto de la traza
        return WCbuilder.baseUrl("http://msvc-users").build();
    }

}
