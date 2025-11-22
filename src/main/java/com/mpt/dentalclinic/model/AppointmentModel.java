package com.mpt.dentalclinic.model;

import java.time.LocalDateTime;

public class AppointmentModel {
    private Integer appointmentId;
    private Integer userId;
    private Integer servicesId;
    private LocalDateTime orderDate;
    private Integer quantity;
    private Integer statusOrderId;
    private String serviceName;
    private String statusName;

    public AppointmentModel() {}

    public AppointmentModel(Integer appointmentId, Integer userId, Integer servicesId, LocalDateTime orderDate, Integer quantity, Integer statusOrderId) {
        this.appointmentId = appointmentId;
        this.userId = userId;
        this.servicesId = servicesId;
        this.orderDate = orderDate;
        this.quantity = quantity;
        this.statusOrderId = statusOrderId;
    }

    public Integer getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Integer appointmentId) { this.appointmentId = appointmentId; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public Integer getServicesId() { return servicesId; }
    public void setServicesId(Integer servicesId) { this.servicesId = servicesId; }

    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Integer getStatusOrderId() { return statusOrderId; }
    public void setStatusOrderId(Integer statusOrderId) { this.statusOrderId = statusOrderId; }
    
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    
    public String getStatusName() { return statusName; }
    public void setStatusName(String statusName) { this.statusName = statusName; }
}
