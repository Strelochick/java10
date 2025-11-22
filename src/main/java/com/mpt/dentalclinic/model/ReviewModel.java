package com.mpt.dentalclinic.model;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ReviewModel {
    private Integer reviewId;
    @NotNull(message = "Выберите пациента")
private Integer userId;

@NotNull(message = "Выберите услугу")  
private Integer servicesId;

@NotNull(message = "Укажите оценку")
@Min(1) @Max(5)
private Integer rating;

@NotBlank(message = "Введите комментарий")
private String comment;

@NotNull(message = "Укажите дату")
private LocalDateTime reviewDate;

    public ReviewModel() {}

    public ReviewModel(Integer reviewId, Integer userId, Integer servicesId, Integer rating, String comment, LocalDateTime reviewDate) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.servicesId = servicesId;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate;
    }

    public Integer getReviewId() { return reviewId; }
    public void setReviewId(Integer reviewId) { this.reviewId = reviewId; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public Integer getServicesId() { return servicesId; }
    public void setServicesId(Integer servicesId) { this.servicesId = servicesId; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getReviewDate() { return reviewDate; }
    public void setReviewDate(LocalDateTime reviewDate) { this.reviewDate = reviewDate; }
}

