package com.mpt.dentalclinic.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Table(name = "status_orders")
@Getter @Setter @NoArgsConstructor
public class StatusOrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer statusOrderId;

    @NotBlank @Size(max = 50)
    private String statusName;

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Integer getStatusOrderId() {
        return statusOrderId;
    }

    public String getStatusName() {
        return statusName;
    }
}