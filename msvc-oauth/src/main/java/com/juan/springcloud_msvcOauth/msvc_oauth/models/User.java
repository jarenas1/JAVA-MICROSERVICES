package com.juan.springcloud_msvcOauth.msvc_oauth.models;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
public class User {

    private Long id;

    private String username;

    private String password;

     private boolean enabled;

    private String email;

     private List<Role> roles = new ArrayList<>();
}
