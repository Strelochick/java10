package com.mpt.dentalclinic.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "payments")
@Getter @Setter @NoArgsConstructor
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer paymentId;

    @NotBlank @Size(max = 20)
    private String paymentStatus;

    @PastOrPresent
    private LocalDateTime paymentDate;

    @ManyToOne
    @JoinColumn(name = "appointment_id")
    private AppointmentEntity appointment;

    @ManyToOne
    @JoinColumn(name = "payment_method_id")
    private PaymentMethodEntity paymentMethod;

    public LocalDateTime getPaymentDate() {
       return paymentDate;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public void setAppointment(AppointmentEntity appointment) {
        this.appointment = appointment;
    }

    public void setPaymentMethod(PaymentMethodEntity paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public AppointmentEntity getAppointment() {
       return appointment;
    }

    public PaymentMethodEntity getPaymentMethod() {
       return paymentMethod;
    }

    public String getPaymentStatus() {
       return paymentStatus;
    }

    
}