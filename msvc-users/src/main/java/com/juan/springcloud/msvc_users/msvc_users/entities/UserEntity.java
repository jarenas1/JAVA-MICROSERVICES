package com.juan.springcloud.msvc_users.msvc_users.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.List;

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

     //EVITARA QUE SE REPITAN MUCHAS VECES LO JSON
    @JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
     @ManyToMany
     @JoinTable(
             name = "users_roles",
             joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"),
             //permitira que no haya un registro que repita ambas llaves
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "role_id"})
     )
     List<RoleEntity> roles;
}
