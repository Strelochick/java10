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
@Table(name = "payment_methods")
@Getter @Setter @NoArgsConstructor
public class PaymentMethodEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer paymentMethodId;

    @NotBlank @Size(max = 50)
    private String methodName;

    public Integer getPaymentMethodId() {
    return paymentMethodId;
}

    public void setMethodName(String methodName) {
        this.methodName = methodName;    
    }

    public String getMethodName() {
        return methodName;
    }
}