package com.mpt.dentalclinic.service;

import com.mpt.dentalclinic.entity.CategoryEntity;
import com.mpt.dentalclinic.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryEntity createCategory(CategoryEntity category) {
        return categoryRepository.save(category);
    }

    @Transactional(readOnly = true)
    public Optional<CategoryEntity> findById(Integer id) {
        return categoryRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<CategoryEntity> findAll() {
        return categoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<CategoryEntity> findAllWithPagination(int page, int size) {
        return categoryRepository.findAll(PageRequest.of(page, size));
    }

    @Transactional(readOnly = true)
    public List<CategoryEntity> searchCategories(String name) {
        return categoryRepository.findByCategoryNameContainingIgnoreCase(name);
    }

    public CategoryEntity updateCategory(CategoryEntity category) {
        return categoryRepository.save(category);
    }

    public void deleteCategory(Integer id) {
        categoryRepository.deleteById(id);
    }
}