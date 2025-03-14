package com.microservices.items.msvc_items;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CircuitConfig {

    @Bean
    Customizer<Resilience4JCircuitBreakerFactory> customizerCircuitBreaker(){
        //en el parentesis se pasa el idetificador que se le dio a lo que entrara, en este caso fue el id en el controller
        return (factory)-> factory.configureDefault(id ->{
            return new Resilience4JConfigBuilder(id).circuitBreakerConfig(CircuitBreakerConfig
                    .custom()
                    .slidingWindowSize(10) //La cantidad de veces que se debe ejecutar para decidir a que parte del circuito entrar
                    .failureRateThreshold(50) //porcentaje de fallos
                    .waitDurationInOpenState(Duration.ofSeconds(10L))
                    .permittedNumberOfCallsInHalfOpenState(5) //llamas en estado semi-abierto
                    .slowCallDurationThreshold(Duration.ofSeconds(5L)) // Duración que debe tener para entrar en llamada lenta
                    .slowCallRateThreshold(50) // % de llamadas que deben ser lentas para abrir circuito
                    .build() //tiempo en estado abierto
                    )
                    .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(3)).build()) //tiempo que esperara la respuesta antes de lanzar error
                    .build();
        });
    }
}
