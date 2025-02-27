package com.juan.springcloud.gateway.app.api_gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean //SE CREO CON WEBFLUX, POR ENDE, CAMBIA ALGUNAS COSAS
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeExchange(auth ->{
            auth.pathMatchers("/authorized", "/logout").permitAll()
                    //RUTAS DEL GATWAY PUBLICAS
                    .pathMatchers(HttpMethod.GET, "/api/items", "api/products", "/api/users").permitAll()
                    //protegidas
                    .pathMatchers(HttpMethod.GET, "/api/items/{id}", "/api/products/{id}", "/api/users/{id}").hasAnyRole("ADMIN", "USER")
                    //resto de rutas
                    .pathMatchers(HttpMethod.PUT,"/api/products/**", "/api/items/**","/api/users/**").hasAnyRole("ADMIN")
                    .pathMatchers(HttpMethod.POST,"/api/products/**", "/api/items/**","/api/users/**").hasAnyRole("ADMIN")
                    .pathMatchers(HttpMethod.DELETE,"/api/products/**", "/api/items/**","/api/users/**").hasAnyRole("ADMIN")
                    .anyExchange().authenticated();
        }).cors(csrf -> csrf.disable())
                .oauth2Login(withDefaults()) //RECORDAR QUE SE DEBE IMPORTAR static
                .oauth2Client(withDefaults())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
                .build();
    }
}
