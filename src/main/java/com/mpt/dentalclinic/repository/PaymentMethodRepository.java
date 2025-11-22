package com.mpt.dentalclinic.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mpt.dentalclinic.entity.PaymentMethodEntity;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethodEntity, Integer> {
    Page<PaymentMethodEntity> findByMethodNameContainingIgnoreCase(String name, Pageable pageable);
    boolean existsByMethodNameIgnoreCase(String methodName);
}