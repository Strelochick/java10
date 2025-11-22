package com.mpt.dentalclinic.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mpt.dentalclinic.entity.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    List<RoleEntity> findByRoleNameContainingIgnoreCase(String name);
    Page<RoleEntity> findByRoleNameContainingIgnoreCase(String name, Pageable pageable);
    Optional<RoleEntity> findByRoleName(String roleName);
    boolean existsByRoleName(String roleName);
}