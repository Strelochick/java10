package com.mpt.dentalclinic.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "services")
public class ServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer servicesId;

    @NotBlank @Size(max = 100)
    private String name;

    @Size(max = 500)
    private String description;

    @Positive @Digits(integer = 10, fraction = 2)
    private BigDecimal price;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_services_id", nullable = false)
    private CategoryEntity category;

    // Конструктор по умолчанию
    public ServiceEntity() {
    }

    // Геттеры
    public Integer getServicesId() {
        return servicesId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    // Сеттеры
    public void setServicesId(Integer servicesId) {
        this.servicesId = servicesId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }
}