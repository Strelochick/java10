package com.mpt.dentalclinic.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.PageRequest;

import com.mpt.dentalclinic.entity.ReviewEntity;
import com.mpt.dentalclinic.entity.ServiceEntity;
import com.mpt.dentalclinic.entity.UserEntity;
import com.mpt.dentalclinic.service.ReviewService;
import com.mpt.dentalclinic.service.ServiceService;
import com.mpt.dentalclinic.service.UserService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/reviewList")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private UserService userService;
    @Autowired
    private ServiceService servicesService;

    @GetMapping
    public String getAllReviews(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(required = false) Integer userId,
        @RequestParam(required = false) Integer rating,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reviewDate,
        Model model) {
        
        Page<ReviewEntity> reviewPage = reviewService.searchReviews(
            userId, 
            rating, 
            reviewDate != null ? reviewDate.atStartOfDay() : null,
            reviewDate != null ? reviewDate.plusDays(1).atStartOfDay() : null,
            PageRequest.of(page, 10)
        );
        
        model.addAttribute("reviews", reviewPage.getContent());
        model.addAttribute("users", userService.findAll());
        model.addAttribute("services", servicesService.findAll());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", reviewPage.getTotalPages());
        model.addAttribute("userId", userId);
        model.addAttribute("rating", rating);
        model.addAttribute("reviewDate", reviewDate);
        return "reviewList";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        ReviewEntity review = reviewService.findById(id).orElseThrow();
        model.addAttribute("reviewToEdit", review);
        model.addAttribute("users", userService.findAll());
        model.addAttribute("services", servicesService.findAll());
        return "reviewList";
    }

    @PostMapping("/add")
    public String addReview(
        @RequestParam Integer userId,
        @RequestParam Integer servicesId,
        @RequestParam Integer rating,
        @RequestParam String comment,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime reviewDate,
        RedirectAttributes redirectAttributes) {
        
        try {
            ReviewEntity review = new ReviewEntity();
            review.setUser(userService.findById(userId).orElseThrow());
            review.setService(servicesService.findById(servicesId).orElseThrow());
            review.setRating(rating);
            review.setComment(comment);
            review.setReviewDate(reviewDate != null ? reviewDate : LocalDateTime.now());
            
            reviewService.createReview(review);
            redirectAttributes.addFlashAttribute("success", "Отзыв успешно добавлен");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
        }
        return "redirect:/reviewList";
    }

    @PostMapping("/update/{id}")
    public String updateReview(@PathVariable Integer id,
                            @RequestParam Integer userId,
                            @RequestParam Integer servicesId,
                            @RequestParam Integer rating,
                            @RequestParam String comment,
                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime reviewDate,
                            RedirectAttributes redirectAttributes) {
        try {
            ReviewEntity review = reviewService.findById(id).orElseThrow();
            review.setUser(userService.findById(userId).orElseThrow());
            review.setService(servicesService.findById(servicesId).orElseThrow());
            review.setRating(rating);
            review.setComment(comment);
            review.setReviewDate(reviewDate);
            
            reviewService.updateReview(review);
            redirectAttributes.addFlashAttribute("success", "Отзыв успешно обновлен");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при обновлении отзыва: " + e.getMessage());
        }
        return "redirect:/reviewList";
    }

    @PostMapping("/delete/{id}")
    public String deleteReview(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            reviewService.deleteReview(id);
            redirectAttributes.addFlashAttribute("success", "Отзыв успешно удален");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении отзыва: " + e.getMessage());
        }
        return "redirect:/reviewList";
    }
}