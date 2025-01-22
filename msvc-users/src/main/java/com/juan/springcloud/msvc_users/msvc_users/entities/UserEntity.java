package com.juan.springcloud.msvc_users.msvc_users.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
@Table(name = "users")
public class UserEntity {

     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

     @NotBlank
     @Column(unique = true)
    private String username;

     @NotBlank
    private String password;

     private boolean enabled;

     @Email
    @NotBlank
     @Column(unique = true)
    private String email;
}
