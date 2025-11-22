package com.mpt.dentalclinic.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import com.mpt.dentalclinic.entity.ServiceEntity;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Integer> {
    
    List<ServiceEntity> findByNameContainingIgnoreCase(String name);
     @Query("SELECT s FROM ServiceEntity s WHERE " +
           "(:name IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:categoryId IS NULL OR s.category.categoryServicesId = :categoryId)")
    List<ServiceEntity> searchServices(
            @Param("name") String name,
            @Param("categoryId") Integer categoryId);
    
    @Query("SELECT s FROM ServiceEntity s WHERE s.category.categoryServicesId = :categoryId")
    List<ServiceEntity> findByCategoryId(@Param("categoryId") Integer categoryId);
    
    @Query("SELECT s FROM ServiceEntity s WHERE " +
           "LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%')) AND " +
           "s.category.categoryServicesId = :categoryId")
    List<ServiceEntity> findByNameAndCategoryId(
            @Param("name") String name,
            @Param("categoryId") Integer categoryId);

    @Query(value = "SELECT s FROM ServiceEntity s WHERE " +
   "(:name IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
   "(:categoryId IS NULL OR s.category.categoryServicesId = :categoryId)",
   countQuery = "SELECT COUNT(s) FROM ServiceEntity s WHERE " +
   "(:name IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
   "(:categoryId IS NULL OR s.category.categoryServicesId = :categoryId)")
Page<ServiceEntity> searchServicesPageable(
    @Param("name") String name,
    @Param("categoryId") Integer categoryId,
    Pageable pageable);
}