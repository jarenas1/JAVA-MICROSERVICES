package com.juan.springcloud_msvcOauth.msvc_oauth.service;

import com.juan.springcloud_msvcOauth.msvc_oauth.models.User;
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
    private WebClient.Builder client;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        try{
            //Treaemos el usuario del controlador de usuarios
            User user = client.build().get().uri("/username/{username}", params)
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
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), roles);
        }catch(WebClientResponseException e){
            throw new UsernameNotFoundException("we cant found the user: "+username);
        }
    }
}
