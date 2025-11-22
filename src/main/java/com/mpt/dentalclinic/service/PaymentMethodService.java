package com.mpt.dentalclinic.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mpt.dentalclinic.entity.PaymentMethodEntity;
import com.mpt.dentalclinic.repository.PaymentMethodRepository;

@Service
@Transactional
public class PaymentMethodService {
    private final PaymentMethodRepository paymentMethodRepository;

    public PaymentMethodService(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }

    public PaymentMethodEntity createPaymentMethod(PaymentMethodEntity method) {
        return paymentMethodRepository.save(method);
    }

    @Transactional(readOnly = true)
    public Optional<PaymentMethodEntity> findById(Integer id) {
        return paymentMethodRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<PaymentMethodEntity> findAll() {
        return paymentMethodRepository.findAll();
    }

    public PaymentMethodEntity updatePaymentMethod(PaymentMethodEntity method) {
        return paymentMethodRepository.save(method);
    }

    @Transactional(readOnly = true)
public boolean existsByMethodName(String methodName) {
    return paymentMethodRepository.existsByMethodNameIgnoreCase(methodName);
}

    public void deletePaymentMethod(Integer id) {
        paymentMethodRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<PaymentMethodEntity> searchPaymentMethods(String name, Pageable pageable) {
        if (name == null || name.isEmpty()) {
            return paymentMethodRepository.findAll(pageable);
        }
        return paymentMethodRepository.findByMethodNameContainingIgnoreCase(name, pageable);
    }
}