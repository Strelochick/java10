package com.mpt.dentalclinic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mpt.dentalclinic.entity.RoleEntity;
import com.mpt.dentalclinic.service.RoleService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/roleList")
public class RoleController {
    private static final int PAGE_SIZE = 10;

    @Autowired
    private RoleService roleService;

    @GetMapping
    public String getAllRoles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String name,
            Model model) {
        
        Page<RoleEntity> rolePage = roleService.searchRoles(name, PageRequest.of(page, PAGE_SIZE));
        model.addAttribute("roles", rolePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", rolePage.getTotalPages());
        model.addAttribute("roleName", name);
        return "roleList";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        model.addAttribute("roleToEdit", roleService.findById(id).orElseThrow());
        return "roleList";
    }

    @PostMapping("/add")
    public String addRole(@RequestParam String roleName, RedirectAttributes redirectAttributes) {
        try {
            RoleEntity role = new RoleEntity();
            role.setRoleName(roleName);
            roleService.createRole(role);
            redirectAttributes.addFlashAttribute("success", "Роль успешно добавлена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
        }
        return "redirect:/roleList";
    }

    @PostMapping("/update")
    public String updateRole(
            @RequestParam Integer roleId,
            @RequestParam String roleName,
            RedirectAttributes redirectAttributes) {
        try {
            RoleEntity role = roleService.findById(roleId).orElseThrow();
            role.setRoleName(roleName);
            roleService.updateRole(role);
            redirectAttributes.addFlashAttribute("success", "Роль успешно обновлена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при обновлении: " + e.getMessage());
        }
        return "redirect:/roleList";
    }

    @PostMapping("/delete/{id}")
    public String deleteRole(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            roleService.deleteRole(id);
            redirectAttributes.addFlashAttribute("success", "Роль успешно удалена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении: " + e.getMessage());
        }
        return "redirect:/roleList";
    }
}