package com.juan.springcloud.msvc_users.msvc_users.repositories;

import com.juan.springcloud.msvc_users.msvc_users.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    List<RoleEntity> findByRoleNameIn(List roles);
}
