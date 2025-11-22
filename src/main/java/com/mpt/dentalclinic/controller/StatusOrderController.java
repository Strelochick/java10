package com.mpt.dentalclinic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mpt.dentalclinic.entity.StatusOrderEntity;
import com.mpt.dentalclinic.service.StatusOrderService;

@Controller
@RequestMapping("/statusOrderList")
public class StatusOrderController {
    private static final int PAGE_SIZE = 10;

    @Autowired
    private StatusOrderService statusOrderService;

    @GetMapping
    public String getAllStatuses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String search,
            Model model) {
        
        Page<StatusOrderEntity> statusPage = statusOrderService.searchStatuses(search, PageRequest.of(page, PAGE_SIZE));
        model.addAttribute("statuses", statusPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", statusPage.getTotalPages());
        model.addAttribute("search", search);
        return "statusOrderList";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        model.addAttribute("statusToEdit", statusOrderService.findById(id).orElseThrow());
        return "statusOrderList";
    }

    @PostMapping("/add")
    public String addStatus(
            @RequestParam String statusName,
            RedirectAttributes redirectAttributes) {
        try {
            StatusOrderEntity status = new StatusOrderEntity();
            status.setStatusName(statusName);
            statusOrderService.createStatus(status);
            redirectAttributes.addFlashAttribute("success", "Статус успешно добавлен");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
        }
        return "redirect:/statusOrderList";
    }

    @PostMapping("/update")
    public String updateStatus(
            @RequestParam Integer statusOrderId,
            @RequestParam String statusName,
            RedirectAttributes redirectAttributes) {
        try {
            StatusOrderEntity status = statusOrderService.findById(statusOrderId).orElseThrow();
            status.setStatusName(statusName);
            statusOrderService.updateStatus(status);
            redirectAttributes.addFlashAttribute("success", "Статус успешно обновлен");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при обновлении: " + e.getMessage());
        }
        return "redirect:/statusOrderList";
    }

    @PostMapping("/delete/{id}")
    public String deleteStatus(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            statusOrderService.deleteStatus(id);
            redirectAttributes.addFlashAttribute("success", "Статус успешно удален");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении: " + e.getMessage());
        }
        return "redirect:/statusOrderList";
    }
}