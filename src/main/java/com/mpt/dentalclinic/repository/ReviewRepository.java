package com.mpt.dentalclinic.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mpt.dentalclinic.entity.ReviewEntity;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Integer> {
    Page<ReviewEntity> findByRating(Integer rating, Pageable pageable);
    
    Page<ReviewEntity> findByReviewDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    Page<ReviewEntity> findByUserUserId(Integer userId, Pageable pageable);
    
    Page<ReviewEntity> findByUserUserIdAndRating(Integer userId, Integer rating, Pageable pageable);
    
    Page<ReviewEntity> findByUserUserIdAndRatingAndReviewDateBetween(
        Integer userId, Integer rating, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    List<ReviewEntity> findByRatingBetween(Integer minRating, Integer maxRating);

    @Query("SELECT r FROM ReviewEntity r JOIN FETCH r.user JOIN FETCH r.service LEFT JOIN FETCH r.service.category")
Page<ReviewEntity> findAllWithRelations(Pageable pageable);
}