package com.mpt.dentalclinic.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mpt.dentalclinic.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    List<UserEntity> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email);
    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);
    Page<UserEntity> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email, Pageable pageable);
}