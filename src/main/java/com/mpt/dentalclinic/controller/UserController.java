package com.mpt.dentalclinic.controller;

import com.mpt.dentalclinic.entity.AppointmentEntity;
import com.mpt.dentalclinic.entity.ServiceEntity;
import com.mpt.dentalclinic.entity.StatusOrderEntity;
import com.mpt.dentalclinic.entity.UserEntity;
import com.mpt.dentalclinic.model.AppointmentModel;
import com.mpt.dentalclinic.model.ServiceModel;
import com.mpt.dentalclinic.service.AppointmentService;
import com.mpt.dentalclinic.service.ServiceService;
import com.mpt.dentalclinic.service.StatusOrderService;
import com.mpt.dentalclinic.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private StatusOrderService statusOrderService;

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String userDashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getName();
        
        UserEntity user = userService.findByEmail(userEmail).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }

        List<ServiceModel> services = serviceService.getAllServices();
        
        List<AppointmentModel> userAppointments = appointmentService.getAppointmentsByUserId(user.getUserId());
        
        enrichAppointmentsWithDetails(userAppointments);

        model.addAttribute("user", user);
        model.addAttribute("services", services);
        model.addAttribute("appointments", userAppointments);
        
        return "user/dashboard";
    }

    @GetMapping("/appointments")
    public String userAppointments(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getName();
        
        UserEntity user = userService.findByEmail(userEmail).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }

        List<AppointmentModel> appointments = appointmentService.getAppointmentsByUserId(user.getUserId());

        enrichAppointmentsWithDetails(appointments);
        
        model.addAttribute("appointments", appointments);
        model.addAttribute("user", user);
        
        return "user/appointments";
    }

    @GetMapping("/book-appointment")
    public String bookAppointmentForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getName();
        
        UserEntity user = userService.findByEmail(userEmail).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }

        List<ServiceModel> services = serviceService.getAllServices();
        List<StatusOrderEntity> statuses = statusOrderService.getAllStatusOrders();
        
        model.addAttribute("user", user);
        model.addAttribute("services", services);
        model.addAttribute("statuses", statuses);
        model.addAttribute("appointment", new AppointmentModel());
        
        return "user/book-appointment";
    }

    @PostMapping("/book-appointment")
    public String bookAppointment(@ModelAttribute AppointmentModel appointmentModel) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getName();
        
        UserEntity user = userService.findByEmail(userEmail).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }

        try {
            appointmentModel.setUserId(user.getUserId());
            
            appointmentModel.setStatusOrderId(1);
            
            if (appointmentModel.getQuantity() == null) {
                appointmentModel.setQuantity(1);
            }
            
            appointmentService.createAppointment(appointmentModel);
            return "redirect:/user/dashboard?success=true";
        } catch (Exception e) {
            return "redirect:/user/book-appointment?error=true";
        }
    }

    @PostMapping("/cancel-appointment/{id}")
    public String cancelAppointment(@PathVariable Integer id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getName();
        
        UserEntity user = userService.findByEmail(userEmail).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }

        try {
            AppointmentModel appointment = appointmentService.getAppointmentById(id);
            if (appointment != null && appointment.getUserId().equals(user.getUserId())) {
                appointment.setStatusOrderId(3);
                appointmentService.updateAppointment(id, appointment);
            }
            return "redirect:/user/appointments?cancelled=true";
        } catch (Exception e) {
            return "redirect:/user/appointments?error=true";
        }
    }
    
    private void enrichAppointmentsWithDetails(List<AppointmentModel> appointments) {
        for (AppointmentModel appointment : appointments) {
            if (appointment.getServicesId() != null) {
                try {
                    ServiceModel service = serviceService.getServiceById(appointment.getServicesId());
                    if (service != null) {
                        appointment.setServiceName(service.getName());
                    }
                } catch (Exception e) {
                }
            }
            
            if (appointment.getStatusOrderId() != null) {
                try {
                    StatusOrderEntity status = statusOrderService.findById(appointment.getStatusOrderId()).orElse(null);
                    if (status != null) {
                        appointment.setStatusName(status.getStatusName());
                    }
                } catch (Exception e) {
                }
            }
        }
    }
}