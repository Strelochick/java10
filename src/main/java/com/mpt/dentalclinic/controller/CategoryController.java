package com.mpt.dentalclinic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.mpt.dentalclinic.entity.CategoryEntity;
import com.mpt.dentalclinic.service.CategoryService;

@Controller
@RequestMapping("/categoryList")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String getAllCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String search,
            Model model) {
        
        Page<CategoryEntity> categoryPage = categoryService.findAllWithPagination(page, 10);
        
        if (search != null && !search.isEmpty()) {
            model.addAttribute("categories", categoryService.searchCategories(search));
            model.addAttribute("search", search);
        } else {
            model.addAttribute("categories", categoryPage.getContent());
        }
        
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", categoryPage.getTotalPages());
        return "categoryList";
    }

    @GetMapping("/edit/{id}")
    public String editCategoryForm(@PathVariable Integer id, Model model) {
        model.addAttribute("categoryToEdit", categoryService.findById(id).orElse(null));
        return "categoryList";
    }

    @PostMapping("/add")
    public String addCategory(@RequestParam String categoryName) {
        CategoryEntity category = new CategoryEntity();
        category.setCategoryName(categoryName);
        categoryService.createCategory(category);
        return "redirect:/categoryList?success=Category+added+successfully";
    }

    @PostMapping("/update")
    public String updateCategory(
            @RequestParam Integer categoryServicesId,
            @RequestParam String categoryName) {
        CategoryEntity category = categoryService.findById(categoryServicesId).orElse(null);
        if (category != null) {
            category.setCategoryName(categoryName);
            categoryService.updateCategory(category);
        }
        return "redirect:/categoryList?success=Category+updated+successfully";
    }

    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return "redirect:/categoryList?success=Category+deleted+successfully";
    }
}