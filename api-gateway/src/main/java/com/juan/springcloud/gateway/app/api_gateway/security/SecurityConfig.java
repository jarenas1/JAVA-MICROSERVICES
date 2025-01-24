package com.juan.springcloud.gateway.app.api_gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) throws Exception {
        return http.authorizeExchange(auth ->{
            auth.pathMatchers("/authorized", "/logout").permitAll()
                    //RUTAS DEL GATWAY PUBLICAS
                    .pathMatchers(HttpMethod.GET, "/api/items", "api/products", "/api/users").permitAll()
                    //protegidas
                    .pathMatchers(HttpMethod.GET, "/api/items/{id}", "/api/products/{id}", "/api/users/{id}").hasAnyRole("ADMIN", "USER")
                    //resto de rutas
                    .pathMatchers("/api/products/**", "/api/itmes/**","/api/users/**").
        });
    }
}
