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

import com.mpt.dentalclinic.entity.PaymentEntity;
import com.mpt.dentalclinic.repository.PaymentRepository;

@Service
@Transactional
public class PaymentService {
    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public PaymentEntity createPayment(PaymentEntity payment) {
        return paymentRepository.save(payment);
    }

    @Transactional(readOnly = true)
    public Optional<PaymentEntity> findById(Integer id) {
        return paymentRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<PaymentEntity> findAll() {
        return paymentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<PaymentEntity> findAllWithPagination(int page, int size) {
        return paymentRepository.findAll(PageRequest.of(page, size));
    }

    @Transactional(readOnly = true)
public List<PaymentEntity> searchPayments(String status, LocalDateTime startDate, LocalDateTime endDate) {
    List<PaymentEntity> payments = paymentRepository.findAll();
    
    if (status != null) {
        payments = payments.stream()
                .filter(p -> status.equals(p.getPaymentStatus()))
                .collect(Collectors.toList());
    }
    
    if (startDate != null && endDate != null) {
        payments = payments.stream()
                .filter(p -> p.getPaymentDate() != null)
                .filter(p -> !p.getPaymentDate().isBefore(startDate))
                .filter(p -> !p.getPaymentDate().isAfter(endDate))
                .collect(Collectors.toList());
    }
    
    return payments;
}

    public PaymentEntity updatePayment(PaymentEntity payment) {
        return paymentRepository.save(payment);
    }

    public void deletePayment(Integer id) {
        paymentRepository.deleteById(id);
    }
}