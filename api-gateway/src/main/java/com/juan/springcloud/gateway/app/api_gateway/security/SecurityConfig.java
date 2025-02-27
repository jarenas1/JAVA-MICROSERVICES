package com.juan.springcloud.gateway.app.api_gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.stream.Collectors;

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
                //ELIMINA EL MANEJO DE SESIONES (ONLY SPRING WEBFLUX NO WB"
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .oauth2Login(withDefaults()) //RECORDAR QUE SE DEBE IMPORTAR static
                .oauth2Client(withDefaults())
                //CON LOS PERMISOS DEL CLIENTE DEL SERVIDOR DE AUTORIZACIOJN
//                .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
                //con permisos del token puestos por nosotros
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtSpec -> {
                    jwtSpec.jwtAuthenticationConverter(new Converter<Jwt, Mono<AbstractAuthenticationToken>>() {
                        @Override
                        public Mono<AbstractAuthenticationToken> convert(Jwt source) {
                            //OBTENEMOS LOS ROLES
                            Collection<String> rolesString = source.getClaimAsStringList("roles");
                            //CASTEO A GRATHEDAUTH..
                            Collection<GrantedAuthority> authorities = rolesString.stream().map(role ->
                                    new SimpleGrantedAuthority(role))
                                    .collect(Collectors.toList());
                            return Mono.just(new JwtAuthenticationToken(source, authorities));
                        }
                    });
                }))
                .build();
    }
}
