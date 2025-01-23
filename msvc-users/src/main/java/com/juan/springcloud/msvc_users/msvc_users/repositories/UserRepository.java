package com.juan.springcloud.msvc_users.msvc_users.repositories;


import com.juan.springcloud.msvc_users.msvc_users.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Long>{
    public Optional<UserEntity> findByUsername(String username);
}
