package com.mpt.dentalclinic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mpt.dentalclinic.entity.UserEntity;
import com.mpt.dentalclinic.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Controller
@RequestMapping("/userList")
public class UserListController {
    private static final int PAGE_SIZE = 10;

    @Autowired
    private UserService userService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer userId,
            Model model) {
        
        try {
            Page<UserEntity> userPage;
            List<UserEntity> userList;
            
            if (userId != null || name != null || email != null) {
                // Поиск пользователей
                userList = userService.searchUsers(
                    name != null ? name.trim() : null,
                    email != null ? email.trim() : null,
                    userId
                );
                // Создаем страницу из результатов поиска
                int start = (int) PageRequest.of(page, PAGE_SIZE).getOffset();
                int end = Math.min((start + PAGE_SIZE), userList.size());
                List<UserEntity> pageContent = userList.subList(start, Math.min(end, userList.size()));
                userPage = new PageImpl<>(pageContent, PageRequest.of(page, PAGE_SIZE), userList.size());
            } else {
                // Получаем всех пользователей с пагинацией
                userPage = userService.findAllWithPagination(page, PAGE_SIZE);
            }
            
            model.addAttribute("users", userPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", userPage.getTotalPages());
            model.addAttribute("name", name);
            model.addAttribute("email", email);
            model.addAttribute("userId", userId);
            
            return "userList";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Ошибка при загрузке данных: " + e.getMessage());
            return "userList";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer userId,
            Model model) {
        
        UserEntity userToEdit = userService.findById(id).orElse(null);
        model.addAttribute("userToEdit", userToEdit);
        
        // Сохраняем параметры поиска
        model.addAttribute("currentPage", page);
        model.addAttribute("name", name);
        model.addAttribute("email", email);
        model.addAttribute("userId", userId);
        
        // Загружаем список пользователей для отображения
        Page<UserEntity> userPage = userService.findAllWithPagination(page, PAGE_SIZE);
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("totalPages", userPage.getTotalPages());
        
        return "userList";
    }

    @PostMapping("/add")
    public String addUser(
            @RequestParam String name,
            @RequestParam String surname,
            @RequestParam(required = false) String lastname,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String medicalCardNumber,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Проверяем, существует ли пользователь с таким email
            if (userService.findByEmail(email).isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Пользователь с таким email уже существует");
                return "redirect:/userList";
            }
            
            UserEntity user = new UserEntity();
            user.setName(name);
            user.setSurname(surname != null ? surname : "");
            user.setLastname(lastname != null ? lastname : "");
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setPhone(phone);
            user.setAddress(address);
            user.setMedicalCardNumber(medicalCardNumber);
            user.setActive(true);
            
            userService.createUser(user);
            redirectAttributes.addFlashAttribute("success", "Клиент успешно добавлен");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Ошибка при добавлении: " + e.getMessage());
        }
        
        return "redirect:/userList";
    }

    @PostMapping("/update")
    public String updateUser(
            @RequestParam Integer userId,
            @RequestParam String name,
            @RequestParam String surname,
            @RequestParam(required = false) String lastname,
            @RequestParam String email,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String medicalCardNumber,
            RedirectAttributes redirectAttributes) {
        
        try {
            UserEntity existingUser = userService.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
            
            // Проверяем email, если он изменился
            if (!existingUser.getEmail().equals(email)) {
                if (userService.findByEmail(email).isPresent()) {
                    redirectAttributes.addFlashAttribute("error", "Пользователь с таким email уже существует");
                    return "redirect:/userList/edit/" + userId;
                }
            }
            
            existingUser.setName(name);
            existingUser.setSurname(surname != null ? surname : "");
            existingUser.setLastname(lastname != null ? lastname : "");
            existingUser.setEmail(email);
            if (password != null && !password.trim().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(password));
            }
            existingUser.setPhone(phone);
            existingUser.setAddress(address);
            existingUser.setMedicalCardNumber(medicalCardNumber);
            
            userService.updateUser(existingUser);
            redirectAttributes.addFlashAttribute("success", "Клиент успешно обновлен");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Ошибка при обновлении: " + e.getMessage());
        }
        
        return "redirect:/userList";
    }

    @PostMapping("/delete")
    public String deleteUser(
            @RequestParam Integer userId,
            RedirectAttributes redirectAttributes) {
        
        try {
            userService.deleteUser(userId);
            redirectAttributes.addFlashAttribute("success", "Клиент успешно удален");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении: " + e.getMessage());
        }
        
        return "redirect:/userList";
    }
}

