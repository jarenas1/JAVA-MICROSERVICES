package com.microservices.items.msvc_items;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CircuitConfig {

    @Bean
    Customizer<Resilience4JCircuitBreakerFactory> customizerBreaker(){
        //en el parentesis se pasa el idetificador que se le dio a lo que entrara, en este caso fue el id en el controller
        return (factory)-> factory.configureDefault(id ->{
            return new Resilience4JConfigBuilder(id).circuitBreakerConfig(CircuitBreakerConfig
                    .custom()
                    .slidingWindowSize(10) //La cantidad de veces que se debe ejecutar para decidir a que parte del circuito entrar
                    .failureRateThreshold(50) //porcentaje de fallos
                                    .waitDurationInOpenState(Duration.ofSeconds(5L))
                    .build() //tiempo en estado abierto
                    )
                    .build();
        });
    }
}
