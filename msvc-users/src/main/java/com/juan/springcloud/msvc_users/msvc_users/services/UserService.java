package com.juan.springcloud.msvc_users.msvc_users.services;

import com.juan.springcloud.msvc_users.msvc_users.entities.RoleEntity;
import com.juan.springcloud.msvc_users.msvc_users.entities.UserEntity;
import com.juan.springcloud.msvc_users.msvc_users.repositories.RoleRepository;
import com.juan.springcloud.msvc_users.msvc_users.repositories.UserRepository;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    public UserEntity save(UserEntity userEntity) {
        if (userEntity.getRoles() == null || userEntity.getRoles().isEmpty()) {
            throw new IllegalArgumentException("User must have at least one role");
        }
        List<RoleEntity> roles = verifyRoles(userEntity);

        userEntity.setRoles(roles);
        return userRepository.save(userEntity);
    }

    @Transactional
    public List<RoleEntity> verifyRoles(UserEntity userEntity) {
        List<String> roleNames = userEntity.getRoles()
                .stream()
                .map(RoleEntity::getName)
                .toList();
        List<RoleEntity> roles = roleRepository.findByNameIn(roleNames);

        if (roles.isEmpty() || roles.size() != roleNames.size()) {
            //OBTENEMOS NOMBRES DE LOS ROLES DE LA LISTA DE ROLE ENTITY
            Set<String> foundRoleNames = roles.stream()
                    .map(RoleEntity::getName)
                    .collect(Collectors.toSet());

            //VERIFICAMOS QUE ROLES NO ESTAN EN LA LISTA
            List<String> missingRoles = roleNames.stream()
                    .filter(roleName -> !foundRoleNames.contains(roleName))
                    .toList();
            throw new NotFoundException("We can't find the roles " + missingRoles + " in the database");
        }
        return roles;
    }

    @Transactional(readOnly = true)
    public Optional<UserEntity> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("the user: " + username + " was not found in the database"));
    }

    @Transactional(readOnly = true)
    public List<UserEntity> findAll() {
        return (List<UserEntity>) userRepository.findAll();
    }

    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public UserEntity update(Long id, UserEntity userEntity) {
        UserEntity userDB = findById(id).orElseThrow(() -> new NotFoundException("the user: " + id + " was not found in the database"));

        if (userEntity.getRoles() == null || userEntity.getRoles().isEmpty()) {
            throw new IllegalArgumentException("User must have at least one role");
        }
        List<RoleEntity> roles = verifyRoles(userEntity);

        UserEntity userdb = UserEntity.builder()
                .id(id)
                .email(userEntity.getEmail())
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .enabled(userEntity.isEnabled())
                .roles(roles)
        .build();
        return userRepository.save(userdb);
    }
}
