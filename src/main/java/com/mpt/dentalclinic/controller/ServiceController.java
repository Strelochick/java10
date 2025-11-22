package com.mpt.dentalclinic.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.mpt.dentalclinic.entity.CategoryEntity;
import com.mpt.dentalclinic.entity.ServiceEntity;
import com.mpt.dentalclinic.service.CategoryService;
import com.mpt.dentalclinic.service.ServiceService;


@Controller
@RequestMapping("/serviceList")
public class ServiceController {

    @Autowired
    private ServiceService serviceService;
    
    @Autowired
    private CategoryService categoryService;

@GetMapping
public String getAllServices(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(required = false) String name,
    @RequestParam(required = false) Integer categoryServicesId,
    Model model) {
    
    // Инициализируем все необходимые атрибуты значениями по умолчанию
    model.addAttribute("services", java.util.Collections.emptyList());
    model.addAttribute("categories", java.util.Collections.emptyList());
    model.addAttribute("currentPage", 0);
    model.addAttribute("totalPages", 0);
    model.addAttribute("name", name != null ? name : "");
    model.addAttribute("categoryServicesId", categoryServicesId);
    
    try {
        int pageSize = 10;
        
        // Загружаем категории
        try {
            java.util.List<CategoryEntity> categories = categoryService.findAll();
            if (categories != null) {
                model.addAttribute("categories", categories);
            }
        } catch (Exception e) {
            System.err.println("Ошибка при загрузке категорий: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Загружаем услуги
        try {
            Page<ServiceEntity> servicePage = null;
            
            if (StringUtils.hasText(name) || categoryServicesId != null) {
                // Поиск с фильтрами
                servicePage = serviceService.searchServicesWithPagination(
                    name, 
                    categoryServicesId, 
                    PageRequest.of(page, pageSize)
                );
            } else {
                // Получение всех услуг
                servicePage = serviceService.findAllWithPagination(page, pageSize);
            }
            
            if (servicePage != null) {
                model.addAttribute("services", servicePage.getContent());
                model.addAttribute("currentPage", page);
                model.addAttribute("totalPages", Math.max(1, servicePage.getTotalPages()));
            }
        } catch (Exception e) {
            System.err.println("Ошибка при загрузке услуг: " + e.getMessage());
            e.printStackTrace();
            // Пробуем загрузить без пагинации
            try {
                java.util.List<ServiceEntity> allServices = serviceService.findAll();
                if (allServices != null && !allServices.isEmpty()) {
                    model.addAttribute("services", allServices);
                    model.addAttribute("currentPage", 0);
                    model.addAttribute("totalPages", 1);
                }
            } catch (Exception e2) {
                System.err.println("Ошибка при загрузке всех услуг: " + e2.getMessage());
                e2.printStackTrace();
            }
        }
        
    } catch (Exception e) {
        System.err.println("Критическая ошибка в контроллере: " + e.getMessage());
        e.printStackTrace();
        model.addAttribute("error", "Ошибка при загрузке данных. Проверьте подключение к базе данных.");
    }
    
    return "serviceList";
}
    @GetMapping("/edit/{id}")
    public String editServiceForm(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer categoryServicesId,
            Model model) {
        
        try {
            model.addAttribute("serviceToEdit", serviceService.findById(id).orElse(null));
            
            try {
                model.addAttribute("categories", categoryService.findAll());
            } catch (Exception e) {
                System.err.println("Ошибка при загрузке категорий в edit: " + e.getMessage());
                model.addAttribute("categories", java.util.Collections.emptyList());
            }
            
            // Загружаем список услуг для отображения
            try {
                Page<ServiceEntity> servicePage;
                int pageSize = 10;
                
                if (StringUtils.hasText(name) || categoryServicesId != null) {
                    servicePage = serviceService.searchServicesWithPagination(
                        name, 
                        categoryServicesId, 
                        PageRequest.of(page, pageSize)
                    );
                } else {
                    servicePage = serviceService.findAllWithPagination(page, pageSize);
                }
                
                if (servicePage != null) {
                    model.addAttribute("services", servicePage.getContent());
                    model.addAttribute("currentPage", page);
                    model.addAttribute("totalPages", Math.max(1, servicePage.getTotalPages()));
                } else {
                    model.addAttribute("services", java.util.Collections.emptyList());
                    model.addAttribute("currentPage", 0);
                    model.addAttribute("totalPages", 0);
                }
            } catch (Exception e) {
                System.err.println("Ошибка при загрузке услуг в edit: " + e.getMessage());
                e.printStackTrace();
                model.addAttribute("services", java.util.Collections.emptyList());
                model.addAttribute("currentPage", 0);
                model.addAttribute("totalPages", 0);
            }
            
            model.addAttribute("name", name != null ? name : "");
            model.addAttribute("categoryServicesId", categoryServicesId);
        } catch (Exception e) {
            System.err.println("Критическая ошибка в editServiceForm: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("serviceToEdit", null);
            model.addAttribute("categories", java.util.Collections.emptyList());
            model.addAttribute("services", java.util.Collections.emptyList());
            model.addAttribute("currentPage", 0);
            model.addAttribute("totalPages", 0);
            model.addAttribute("name", "");
            model.addAttribute("categoryServicesId", null);
        }
        
        return "serviceList";
    }

    @PostMapping("/add")
public String addService(@RequestParam String name,
                        @RequestParam String description,
                        @RequestParam BigDecimal price,
                        @RequestParam Integer categoryServicesId,
                        Model model) {
    
    CategoryEntity category = categoryService.findById(categoryServicesId)
        .orElseThrow(() -> new IllegalArgumentException("Неверный ID категории"));
    
    ServiceEntity service = new ServiceEntity();
    service.setName(name);
    service.setDescription(description);
    service.setPrice(price);
    service.setCategory(category);
    
    serviceService.createService(service);
    return "redirect:/serviceList?success=Service+added+successfully";
}

   @PostMapping("/update")
public String updateService(@RequestParam Integer servicesId,
                          @RequestParam String name,
                          @RequestParam String description,
                          @RequestParam BigDecimal price,
                          @RequestParam Integer categoryServicesId) {
    
    ServiceEntity service = serviceService.findById(servicesId)
        .orElseThrow(() -> new IllegalArgumentException("Неверный ID услуги"));
    
    CategoryEntity category = categoryService.findById(categoryServicesId)
        .orElseThrow(() -> new IllegalArgumentException("Неверный ID категории"));
    
    service.setName(name);
    service.setDescription(description);
    service.setPrice(price);
    service.setCategory(category);
    
    serviceService.updateService(service);
    return "redirect:/serviceList?success=Услуга+успешно+обновлена";
}

    @PostMapping("/delete/{id}")
public String deleteService(
    @PathVariable Integer id,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(required = false) String name,
    @RequestParam(required = false) Integer categoryServicesId) {
    
    serviceService.deleteService(id);
    
    String redirectUrl = String.format("/serviceList?page=%d", page);
    if (StringUtils.hasText(name)) {
        redirectUrl += "&name=" + name;
    }
    if (categoryServicesId != null) {
        redirectUrl += "&categoryServicesId=" + categoryServicesId;
    }
    
    return "redirect:" + redirectUrl + "&success=Услуга+успешно+удалена";
}
}