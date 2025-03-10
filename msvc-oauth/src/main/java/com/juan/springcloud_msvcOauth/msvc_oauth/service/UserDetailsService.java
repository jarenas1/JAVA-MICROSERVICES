package com.juan.springcloud_msvcOauth.msvc_oauth.service;

import com.juan.springcloud_msvcOauth.msvc_oauth.models.User;
import io.micrometer.tracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private WebClient client;

    @Autowired
    private Tracer tracer;

    private final Logger logger = LoggerFactory.getLogger(UserDetailsService.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Initialazing loggin process userDetailsService::loadByUsername() {}", username);
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        try{
            //Treaemos el usuario del controlador de usuarios
            User user = client.get().uri("/username/{username}", params)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(User.class)
                    .block();
            //traeremos los roles y cambiaremos su tipo al GrathedAuthorities
            List<GrantedAuthority> roles = user.getRoles().stream()
                    .map(role -> {
                 return new SimpleGrantedAuthority(role.getName());
            }).collect(Collectors.toList());
            //RETORNAREMOS UN USUARIO DE SPRING SECURITY
            logger.info("Succesfully get user with username:  {} in userDetailsService::LoadByUsername", username);
            tracer.currentSpan().tag("user", username);
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), roles);
        }catch(WebClientResponseException e){
            logger.error("cant found the user with username:  {} in userDetailsService::LoadByUsername", username);
            tracer.currentSpan().tag("Error message", e.getMessage());
            throw new UsernameNotFoundException("we cant found the user: "+username);
        }
    }
}
