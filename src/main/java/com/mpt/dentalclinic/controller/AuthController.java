package com.mpt.dentalclinic.controller;

import com.mpt.dentalclinic.entity.RoleEntity;
import com.mpt.dentalclinic.entity.RoleEnum;
import com.mpt.dentalclinic.entity.UserEntity;
import com.mpt.dentalclinic.repository.RoleRepository;
import com.mpt.dentalclinic.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("roles", roleRepository.findAll());
        return "registration";
    }

    @PostMapping("/registration")
public String registerUser(
        @RequestParam String email,
        @RequestParam String password,
        @RequestParam String name,
        @RequestParam Integer roleId,
        Model model) {

    if (userRepository.findByEmail(email).isPresent()) {
        model.addAttribute("message", "Email уже существует");
        return "registration";
    }

    UserEntity user = new UserEntity();
    user.setEmail(email);
    user.setPassword(passwordEncoder.encode(password));
    user.setName(name); 
    user.setActive(true);

    RoleEntity role = roleRepository.findById(roleId)
            .orElseThrow(() -> new IllegalArgumentException("Неверный ID роли"));
    user.getRoles().add(role); 

    userRepository.save(user);

    return "redirect:/login?registered";
}
}