package com.mpt.dentalclinic.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reviews")
@Getter @Setter @NoArgsConstructor
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reviewId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "services_id")
    private ServiceEntity service;

    @Min(1) @Max(5)
    private Integer rating;

    @Size(max = 1000)
    private String comment;

    @PastOrPresent
    private LocalDateTime reviewDate;

    public void setUser(UserEntity user) {
        this.user = user;    
    }

    public void setService(ServiceEntity service) {
        this.service = service;    
    }

    public void setRating(Integer rating) {
        this.rating = rating;    
    }

    public void setComment(String comment) {
        this.comment = comment;    
    }

    public void setReviewDate(LocalDateTime reviewDate) {
        this.reviewDate = reviewDate;    
    }

    public Integer getRating() {
    return rating;
    }

    public LocalDateTime getReviewDate() {
    return reviewDate;
    }

    public Integer getReviewId() {
        return reviewId;
    }

    public UserEntity getUser() {
        return user;
    }

    public ServiceEntity getService() {
        return service;
    }
}