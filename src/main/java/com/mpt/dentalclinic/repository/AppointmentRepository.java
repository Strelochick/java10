package com.mpt.dentalclinic.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mpt.dentalclinic.entity.AppointmentEntity;

@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Integer> {
    List<AppointmentEntity> findByStatusOrderId(Integer statusOrderId);
    List<AppointmentEntity> findByOrderDate(LocalDateTime orderDate);
    List<AppointmentEntity> findByOrderDateBetween(LocalDateTime start, LocalDateTime end);
    List<AppointmentEntity> findByStatusOrderIdAndOrderDateBetween(Integer statusOrderId, LocalDateTime start, LocalDateTime end);
    List<AppointmentEntity> findByUserId(Integer userId);
    List<AppointmentEntity> findByServicesId(Integer servicesId);
}