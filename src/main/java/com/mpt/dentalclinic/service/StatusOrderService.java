package com.mpt.dentalclinic.service;

import com.mpt.dentalclinic.entity.StatusOrderEntity;
import com.mpt.dentalclinic.repository.StatusOrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StatusOrderService {
    private final StatusOrderRepository statusOrderRepository;

    public StatusOrderService(StatusOrderRepository statusOrderRepository) {
        this.statusOrderRepository = statusOrderRepository;
    }

    public StatusOrderEntity createStatus(StatusOrderEntity status) {
        if (statusOrderRepository.existsByStatusNameIgnoreCase(status.getStatusName())) {
            throw new IllegalArgumentException("Статус с таким названием уже существует!");
        }
        return statusOrderRepository.save(status);
    }

    @Transactional(readOnly = true)
    public Optional<StatusOrderEntity> findById(Integer id) {
        return statusOrderRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<StatusOrderEntity> findAll() {
        return statusOrderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<StatusOrderEntity> findAllWithPagination(int page, int size) {
        return statusOrderRepository.findAll(PageRequest.of(page, size));
    }

    @Transactional(readOnly = true)
    public Page<StatusOrderEntity> searchStatuses(String name, Pageable pageable) {
        if (name == null || name.isEmpty()) {
            return statusOrderRepository.findAll(pageable);
        }
        return statusOrderRepository.findByStatusNameContainingIgnoreCase(name, pageable);
    }

    public StatusOrderEntity updateStatus(StatusOrderEntity status) {
        Optional<StatusOrderEntity> existingStatus = statusOrderRepository.findByStatusNameIgnoreCase(status.getStatusName());
        if (existingStatus.isPresent() && !existingStatus.get().getStatusOrderId().equals(status.getStatusOrderId())) {
            throw new IllegalArgumentException("Статус с таким названием уже существует!");
        }
        return statusOrderRepository.save(status);
    }

    public void deleteStatus(Integer id) {
        if (!statusOrderRepository.existsById(id)) {
            throw new IllegalArgumentException("Статус с ID " + id + " не найден");
        }
        statusOrderRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean existsByStatusName(String statusName) {
        return statusOrderRepository.existsByStatusNameIgnoreCase(statusName);
    }

    @Transactional(readOnly = true)
    public List<StatusOrderEntity> getAllStatusOrders() {
        return statusOrderRepository.findAll();
    }
}