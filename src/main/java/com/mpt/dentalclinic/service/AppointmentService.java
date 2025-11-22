package com.mpt.dentalclinic.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mpt.dentalclinic.entity.AppointmentEntity;
import com.mpt.dentalclinic.model.AppointmentModel;
import com.mpt.dentalclinic.repository.AppointmentRepository;

@Service
@Transactional
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public AppointmentEntity createAppointment(AppointmentEntity appointment) {
        return appointmentRepository.save(appointment);
    }

    @Transactional(readOnly = true)
    public Optional<AppointmentEntity> findById(Integer id) {
        return appointmentRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<AppointmentEntity> findAll() {
        return appointmentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<AppointmentEntity> findAllWithPagination(int page, int size) {
        return appointmentRepository.findAll(PageRequest.of(page, size));
    }

    @Transactional(readOnly = true)
public List<AppointmentEntity> searchAppointments(Integer statusOrderId, LocalDateTime startDate, LocalDateTime endDate, 
                                                Integer userId, Integer servicesId) {
    if (statusOrderId != null && startDate != null && endDate != null) {
        return appointmentRepository.findByStatusOrderIdAndOrderDateBetween(statusOrderId, startDate, endDate);
    } else if (statusOrderId != null) {
        return appointmentRepository.findByStatusOrderId(statusOrderId);
    } else if (startDate != null && endDate != null) {
        return appointmentRepository.findByOrderDateBetween(startDate, endDate);
    } else if (userId != null) {
        return appointmentRepository.findByUserId(userId);
    } else if (servicesId != null) {
        return appointmentRepository.findByServicesId(servicesId);
    }
    return appointmentRepository.findAll();
}

    public AppointmentEntity updateAppointment(AppointmentEntity appointment) {
        return appointmentRepository.save(appointment);
    }

    public void deleteAppointment(Integer id) {
        appointmentRepository.deleteById(id);
    }

    // Методы для работы с моделями
    public AppointmentModel createAppointment(AppointmentModel appointmentModel) {
        AppointmentEntity entity = convertToEntity(appointmentModel);
        AppointmentEntity savedEntity = appointmentRepository.save(entity);
        return convertToModel(savedEntity);
    }

    public AppointmentModel updateAppointment(Integer id, AppointmentModel appointmentModel) {
        AppointmentEntity existingEntity = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));
        
        existingEntity.setUserId(appointmentModel.getUserId());
        existingEntity.setServicesId(appointmentModel.getServicesId());
        existingEntity.setOrderDate(appointmentModel.getOrderDate());
        existingEntity.setQuantity(appointmentModel.getQuantity());
        existingEntity.setStatusOrderId(appointmentModel.getStatusOrderId());
        
        AppointmentEntity savedEntity = appointmentRepository.save(existingEntity);
        return convertToModel(savedEntity);
    }

    public AppointmentModel getAppointmentById(Integer id) {
        return appointmentRepository.findById(id)
                .map(this::convertToModel)
                .orElse(null);
    }

    public List<AppointmentModel> getAppointmentsByUserId(Integer userId) {
        return appointmentRepository.findByUserId(userId)
                .stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    private AppointmentEntity convertToEntity(AppointmentModel model) {
        AppointmentEntity entity = new AppointmentEntity();
        entity.setUserId(model.getUserId());
        entity.setServicesId(model.getServicesId());
        entity.setOrderDate(model.getOrderDate());
        entity.setQuantity(model.getQuantity());
        entity.setStatusOrderId(model.getStatusOrderId());
        return entity;
    }

    private AppointmentModel convertToModel(AppointmentEntity entity) {
        return new AppointmentModel(
                entity.getAppointmentId(),
                entity.getUserId(),
                entity.getServicesId(),
                entity.getOrderDate(),
                entity.getQuantity(),
                entity.getStatusOrderId()
        );
    }
}