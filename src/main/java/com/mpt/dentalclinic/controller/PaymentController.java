package com.mpt.dentalclinic.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mpt.dentalclinic.entity.PaymentEntity;
import com.mpt.dentalclinic.service.AppointmentService;
import com.mpt.dentalclinic.service.PaymentMethodService;
import com.mpt.dentalclinic.service.PaymentService;

@Controller
@RequestMapping("/paymentList")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private AppointmentService appointmentService;
    
    @Autowired
    private PaymentMethodService paymentMethodService;

    @GetMapping
public String getAllPayments(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(required = false) Integer appointmentId,
        @RequestParam(required = false) Integer paymentMethodId,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
        Model model) {
    
    try {
        List<PaymentEntity> payments;
        
        if (status != null || startDate != null || endDate != null || appointmentId != null || paymentMethodId != null) {
            payments = paymentService.searchPayments(status, startDate, endDate);
            
            if (appointmentId != null) {
                payments = payments.stream()
                        .filter(p -> p.getAppointment() != null && p.getAppointment().getAppointmentId().equals(appointmentId))
                        .collect(Collectors.toList());
            }
            
            if (paymentMethodId != null) {
                payments = payments.stream()
                        .filter(p -> p.getPaymentMethod() != null && p.getPaymentMethod().getPaymentMethodId().equals(paymentMethodId))
                        .collect(Collectors.toList());
            }
            
            model.addAttribute("payments", payments);
        } else {
            Page<PaymentEntity> paymentPage = paymentService.findAllWithPagination(page, 10);
            model.addAttribute("payments", paymentPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", paymentPage.getTotalPages());
        }
        
        model.addAttribute("appointments", appointmentService.findAll());
        model.addAttribute("paymentMethods", paymentMethodService.findAll());
        
        return "paymentList";
    } catch (Exception e) {
        model.addAttribute("error", "Ошибка при загрузке данных: " + e.getMessage());
        return "paymentList";
    }
}

    @PostMapping("/add")
public String addPayment(
        @RequestParam Integer appointmentId,
        @RequestParam Integer paymentMethodId,
        @RequestParam String paymentStatus,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime paymentDate,
        RedirectAttributes redirectAttributes) {
    
    try {
        PaymentEntity payment = new PaymentEntity();
        payment.setPaymentStatus(paymentStatus);
        payment.setPaymentDate(paymentDate);
        
        if (!appointmentService.findById(appointmentId).isPresent()) {
            throw new IllegalArgumentException("Запись с ID " + appointmentId + " не найдена");
        }
        if (!paymentMethodService.findById(paymentMethodId).isPresent()) {
            throw new IllegalArgumentException("Метод оплаты с ID " + paymentMethodId + " не найден");
        }
        
        payment.setAppointment(appointmentService.findById(appointmentId).get());
        payment.setPaymentMethod(paymentMethodService.findById(paymentMethodId).get());
        
        paymentService.createPayment(payment);
        
        redirectAttributes.addFlashAttribute("success", "Платеж успешно добавлен");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "Ошибка при добавлении платежа: " + e.getMessage());
    }
    return "redirect:/paymentList";
}

    @GetMapping("/edit/{id}")
public String showEditForm(@PathVariable Integer id, Model model) {
    try {
        PaymentEntity payment = paymentService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Неверный ID платежа"));
         Page<PaymentEntity> paymentPage = paymentService.findAllWithPagination(0, 10);
        model.addAttribute("paymentToEdit", payment);
        model.addAttribute("appointments", appointmentService.findAll());
        model.addAttribute("paymentMethods", paymentMethodService.findAll());
        
        model.addAttribute("payments", paymentPage.getContent());
        model.addAttribute("currentPage", 0);
        model.addAttribute("totalPages", paymentPage.getTotalPages());
        
        return "paymentList";
    } catch (Exception e) {
        model.addAttribute("error", "Ошибка при загрузке формы редактирования: " + e.getMessage());
        return "redirect:/paymentList";
    }
}

    @PostMapping("/update/{id}")
    public String updatePayment(
            @PathVariable Integer id,
            @RequestParam Integer appointmentId,
            @RequestParam Integer paymentMethodId,
            @RequestParam String paymentStatus,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime paymentDate,
            RedirectAttributes redirectAttributes) {
        
        try {
            PaymentEntity payment = paymentService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Неверный ID платежа"));
            
            payment.setPaymentStatus(paymentStatus);
            payment.setPaymentDate(paymentDate);
            payment.setAppointment(appointmentService.findById(appointmentId)
                    .orElseThrow(() -> new IllegalArgumentException("Неверный ID записи")));
            payment.setPaymentMethod(paymentMethodService.findById(paymentMethodId)
                    .orElseThrow(() -> new IllegalArgumentException("Неверный ID метода оплаты")));
            
            paymentService.updatePayment(payment);
            
            redirectAttributes.addFlashAttribute("success", "Платеж успешно обновлен");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при обновлении платежа: " + e.getMessage());
        }
        return "redirect:/paymentList";
    }

    @PostMapping("/delete")
public String deletePayment(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
    try {
        paymentService.deletePayment(id);
        redirectAttributes.addFlashAttribute("success", "Платеж успешно удален");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "Ошибка при удалении платежа: " + e.getMessage());
    }
    return "redirect:/paymentList";
}
    @GetMapping("/search")
    public String searchPayments(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Model model) {
        
        try {
            List<PaymentEntity> payments = paymentService.searchPayments(status, startDate, endDate);
            model.addAttribute("payments", payments);
            model.addAttribute("appointments", appointmentService.findAll());
            model.addAttribute("paymentMethods", paymentMethodService.findAll());
            
            return "paymentList";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при поиске платежей: " + e.getMessage());
            return "paymentList";
        }
    }
}