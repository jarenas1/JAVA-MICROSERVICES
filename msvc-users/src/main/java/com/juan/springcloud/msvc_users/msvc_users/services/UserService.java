package com.juan.springcloud.msvc_users.msvc_users.services;

import com.juan.springcloud.msvc_users.msvc_users.entities.UserEntity;
import com.juan.springcloud.msvc_users.msvc_users.repositories.UserRepository;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
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

        UserEntity userdb = UserEntity.builder()
                .email(userEntity.getEmail())
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .enabled(userEntity.isEnabled())
        .build();
        return userRepository.save(userdb);
    }
}
