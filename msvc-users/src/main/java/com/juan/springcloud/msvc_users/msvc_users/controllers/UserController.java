package com.juan.springcloud.msvc_users.msvc_users.controllers;

import com.juan.springcloud.msvc_users.msvc_users.entities.UserEntity;
import com.juan.springcloud.msvc_users.msvc_users.services.UserService;
import jakarta.validation.Valid;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class UserController {

    @Autowired
    private UserService userService;

    //PARA HASHEAR PASSWORD
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserEntity userEntity) {
        try {
            //Seteamos el password encriptado
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userEntity));

        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("THE USER CANNOT BE CREATED");
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        UserEntity userEntity = userService.findById(id).orElseThrow(()-> new NotFoundException("THE USER CANNOT BE FOUND"));
        return ResponseEntity.ok(userEntity);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<?> findByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.findByUsername(username));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@RequestBody UserEntity userEntity, @PathVariable Long id) {
        return ResponseEntity.ok().body(userService.update(id, userEntity));
    }
}
