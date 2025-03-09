package com.juan.springcloud.msvc_users.msvc_users.controllers;

import com.juan.springcloud.msvc_users.msvc_users.entities.UserEntity;
import com.juan.springcloud.msvc_users.msvc_users.services.UserService;
import jakarta.validation.Valid;
import jakarta.ws.rs.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserEntity userEntity) {
        logger.info("Creating user in UserController::createUser() {}", userEntity);
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
        logger.info("Getting user from UserController::GetAll() ");
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        logger.info("Finding user by  id: {} inUserController::findById()", id);
        UserEntity userEntity = userService.findById(id).orElseThrow(()-> new NotFoundException("THE USER CANNOT BE FOUND"));
        return ResponseEntity.ok(userEntity);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<?> findByUsername(@PathVariable String username) {
        logger.info("Finding user by  username LOGIN: {} inUserController::findByUsername()", username);
        return ResponseEntity.ok(userService.findByUsername(username));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        logger.info("deleting user by  id: {} inUserController::delete()", id);
        userService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@RequestBody UserEntity userEntity, @PathVariable Long id) {
        logger.info("Updating user in UserController::updateUser() {}", userEntity);
        return ResponseEntity.ok().body(userService.update(id, userEntity));
    }
}
