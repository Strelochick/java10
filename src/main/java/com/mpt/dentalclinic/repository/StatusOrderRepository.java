package com.mpt.dentalclinic.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mpt.dentalclinic.entity.StatusOrderEntity;

public interface StatusOrderRepository extends JpaRepository<StatusOrderEntity, Integer> {
    List<StatusOrderEntity> findByStatusNameContainingIgnoreCase(String name);
    Page<StatusOrderEntity> findByStatusNameContainingIgnoreCase(String name, Pageable pageable);
    Optional<StatusOrderEntity> findByStatusNameIgnoreCase(String name);
    boolean existsByStatusNameIgnoreCase(String name);
}