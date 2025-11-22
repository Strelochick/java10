package com.mpt.dentalclinic.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
        
        model.addAttribute("services", servicePage.getContent());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", servicePage.getTotalPages());
        model.addAttribute("name", name);
        model.addAttribute("categoryServicesId", categoryServicesId);
        
        return "serviceList";
    } catch (Exception e) {
        e.printStackTrace();
        model.addAttribute("error", "Ошибка при загрузке данных: " + e.getMessage());
        return "serviceList";
    }
}
    @GetMapping("/edit/{id}")
    public String editServiceForm(@PathVariable Integer id, Model model) {
        model.addAttribute("serviceToEdit", serviceService.findById(id).orElse(null));
        model.addAttribute("categories", categoryService.findAll());
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