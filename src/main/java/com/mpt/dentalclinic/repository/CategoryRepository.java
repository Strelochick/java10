package com.mpt.dentalclinic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mpt.dentalclinic.entity.CategoryEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {
    List<CategoryEntity> findByCategoryNameContainingIgnoreCase(String name);
}