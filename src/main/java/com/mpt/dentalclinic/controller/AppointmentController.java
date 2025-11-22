package com.mpt.dentalclinic.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.mpt.dentalclinic.entity.AppointmentEntity;
import com.mpt.dentalclinic.service.AppointmentService;
import com.mpt.dentalclinic.service.UserService;
import com.mpt.dentalclinic.service.ServiceService;

@Controller
@RequestMapping("/appointmentList")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ServiceService serviceService;

    @GetMapping
    public String getAllAppointments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer statusOrderId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) Integer servicesId,
            Model model) {
        
        Page<AppointmentEntity> appointmentsPage;
        if (statusOrderId != null || startDate != null || endDate != null || userId != null || servicesId != null) {
            List<AppointmentEntity> searchResults = appointmentService.searchAppointments(
                statusOrderId, startDate, endDate, userId, servicesId);
            appointmentsPage = new PageImpl<>(searchResults, PageRequest.of(page, 10), searchResults.size());
        } else {
            appointmentsPage = appointmentService.findAllWithPagination(page, 10);
        }
        
        model.addAttribute("appointments", appointmentsPage.getContent());
        model.addAttribute("users", userService.findAll());
        model.addAttribute("services", serviceService.findAll());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", appointmentsPage.getTotalPages());
        return "appointmentList";
    }

    @GetMapping("/edit/{id}")
public String editAppointmentForm(@PathVariable Integer id, Model model) {
    AppointmentEntity appointment = appointmentService.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Invalid appointment Id:" + id));
    model.addAttribute("appointmentToEdit", appointment);
    model.addAttribute("users", userService.findAll());
    model.addAttribute("services", serviceService.findAll());
    return "appointmentList";
}

    @PostMapping("/add")
    public String addAppointment(@ModelAttribute AppointmentEntity appointment) {
        appointmentService.createAppointment(appointment);
        return "redirect:/appointmentList?success=Запись+успешно+добавлена";
    }

    @PostMapping("/update")
    public String updateAppointment(@ModelAttribute AppointmentEntity appointment) {
        appointmentService.updateAppointment(appointment);
        return "redirect:/appointmentList?success=Запись+успешно+обновлена";
    }

    @PostMapping("/delete/{id}")
    public String deleteAppointment(@PathVariable Integer id) {
        appointmentService.deleteAppointment(id);
        return "redirect:/appointmentList?success=Запись+успешно+удалена";
    }
}