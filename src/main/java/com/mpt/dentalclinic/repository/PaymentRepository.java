package com.mpt.dentalclinic.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mpt.dentalclinic.entity.PaymentEntity;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Integer> {
    List<PaymentEntity> findByPaymentStatus(String status);
    List<PaymentEntity> findByPaymentDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}