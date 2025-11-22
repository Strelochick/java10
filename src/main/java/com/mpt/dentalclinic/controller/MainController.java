package com.mpt.dentalclinic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {
    @GetMapping({"/", "/index"})
    public String home() {
        return "index";  
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }

    @GetMapping("/user")
    public String redirectToUserDashboard() {
        return "redirect:/user/dashboard";
    }

    @PostMapping("/")
    public String handlePost() {
        return "redirect:/";
    }
}
