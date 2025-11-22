package com.mpt.dentalclinic.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mpt.dentalclinic.entity.ReviewEntity;
import com.mpt.dentalclinic.repository.ReviewRepository;

@Service
@Transactional
public class ReviewService {
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

@Transactional
public ReviewEntity createReview(ReviewEntity review) {
    if (review.getRating() < 1 || review.getRating() > 5) {
        throw new IllegalArgumentException("Оценка должна быть от 1 до 5");
    }
    if (review.getReviewDate() == null) {
        review.setReviewDate(LocalDateTime.now());
    }
    return reviewRepository.save(review);
}

    @Transactional(readOnly = true)
    public Optional<ReviewEntity> findById(Integer id) {
        return reviewRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<ReviewEntity> findAll() {
        return reviewRepository.findAll();
    }

    @Transactional(readOnly = true)
public Page<ReviewEntity> findAllWithPagination(int page, int size) {
    return reviewRepository.findAllWithRelations(PageRequest.of(page, size));
}

   @Transactional(readOnly = true)
public Page<ReviewEntity> searchReviews(Integer userId, Integer rating, LocalDateTime startDate, LocalDateTime endDate, PageRequest pageRequest) {
    if (userId != null && rating != null && startDate != null && endDate != null) {
        return reviewRepository.findByUserUserIdAndRatingAndReviewDateBetween(userId, rating, startDate, endDate, pageRequest);
    } else if (userId != null && rating != null) {
        return reviewRepository.findByUserUserIdAndRating(userId, rating, pageRequest);
    } else if (userId != null) {
        return reviewRepository.findByUserUserId(userId, pageRequest);
    } else if (rating != null) {
        return reviewRepository.findByRating(rating, pageRequest);
    } else if (startDate != null && endDate != null) {
        return reviewRepository.findByReviewDateBetween(startDate, endDate, pageRequest);
    }
    return reviewRepository.findAll(pageRequest);
}

    @Transactional(readOnly = true)
    public List<ReviewEntity> findByRatingRange(Integer minRating, Integer maxRating) {
        return reviewRepository.findByRatingBetween(minRating, maxRating);
    }

    public ReviewEntity updateReview(ReviewEntity review) {
        return reviewRepository.save(review);
    }

    public void deleteReview(Integer id) {
        reviewRepository.deleteById(id);
    }
}