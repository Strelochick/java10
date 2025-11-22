package com.mpt.dentalclinic.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "appointments")
@Getter @Setter @NoArgsConstructor
public class AppointmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer appointmentId;

    @NotNull
    private Integer userId;

    @NotNull
    private Integer servicesId;

    @FutureOrPresent
    private LocalDateTime orderDate;

    @Min(1) @Max(10)
    private Integer quantity;

    @NotNull
    private Integer statusOrderId;

    public Integer getAppointmentId() {
    return appointmentId;
}
}