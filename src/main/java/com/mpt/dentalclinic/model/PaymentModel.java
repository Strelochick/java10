package com.mpt.dentalclinic.model;

import java.time.LocalDateTime;

public class PaymentModel {
    private int paymentId;
    private int appointmentId;
    private int paymentMethodId;
    private String paymentStatus;
    private LocalDateTime paymentDate;

    public PaymentModel(int paymentId, int appointmentId, int paymentMethodId, String paymentStatus, LocalDateTime paymentDate) {
        this.paymentId = paymentId;
        this.appointmentId = appointmentId;
        this.paymentMethodId = paymentMethodId;
        this.paymentStatus = paymentStatus;
        this.paymentDate = paymentDate;
    }

    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }

    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }

    public int getPaymentMethodId() { return paymentMethodId; }
    public void setPaymentMethodId(int paymentMethodId) { this.paymentMethodId = paymentMethodId; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }
}
