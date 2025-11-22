package com.mpt.dentalclinic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mpt.dentalclinic.entity.PaymentMethodEntity;
import com.mpt.dentalclinic.service.PaymentMethodService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/paymentMethodList")
public class PaymentMethodController {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private PaymentMethodService paymentMethodService;

    @GetMapping
    public String getAllPaymentMethods(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(required = false) String search,
                                     Model model) {
        Page<PaymentMethodEntity> methodsPage = paymentMethodService.searchPaymentMethods(search, PageRequest.of(page, PAGE_SIZE));
        model.addAttribute("paymentMethods", methodsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", methodsPage.getTotalPages());
        model.addAttribute("search", search);
        return "paymentMethodList";
    }

    @PostMapping("/add")
public String addPaymentMethod(@Valid PaymentMethodEntity method, 
                              BindingResult result,
                              RedirectAttributes redirectAttributes) {
    if (result.hasErrors()) {
        return "paymentMethodList";
    }
    
    if (paymentMethodService.existsByMethodName(method.getMethodName())) {
        redirectAttributes.addFlashAttribute("error", "Метод оплаты с таким названием уже существует");
        return "redirect:/paymentMethodList";
    }
    
    paymentMethodService.createPaymentMethod(method);
    redirectAttributes.addFlashAttribute("success", "Метод оплаты успешно добавлен");
    return "redirect:/paymentMethodList";
}

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        PaymentMethodEntity method = paymentMethodService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid payment method id: " + id));
        
        model.addAttribute("methodToEdit", method);
        return "paymentMethodList";
    }

    @PostMapping("/update/{id}")
public String updatePaymentMethod(@PathVariable Integer id,
                                @RequestParam String methodName,
                                RedirectAttributes redirectAttributes) {
    try {
        PaymentMethodEntity method = paymentMethodService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid payment method id: " + id));
        
        if (!method.getMethodName().equalsIgnoreCase(methodName) && 
            paymentMethodService.existsByMethodName(methodName)) {
            redirectAttributes.addFlashAttribute("error", "Метод оплаты с таким названием уже существует");
            return "redirect:/paymentMethodList/edit/" + id;
        }
        
        method.setMethodName(methodName);
        paymentMethodService.updatePaymentMethod(method);
        
        redirectAttributes.addFlashAttribute("success", "Метод оплаты успешно обновлен");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "Ошибка при обновлении: " + e.getMessage());
    }   
    return "redirect:/paymentMethodList";
}

    @PostMapping("/delete")
    public String deletePaymentMethod(@RequestParam Integer id) {
        paymentMethodService.deletePaymentMethod(id);
        return "redirect:/paymentMethodList";
    }
}