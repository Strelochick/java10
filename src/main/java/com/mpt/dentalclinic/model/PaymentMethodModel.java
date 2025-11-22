package com.mpt.dentalclinic.model;


public class PaymentMethodModel {
    private int paymentMethodId;
    private String methodName;

    public PaymentMethodModel(int paymentMethodId, String methodName) {
        this.paymentMethodId = paymentMethodId;
        this.methodName = methodName;
    }

    public int getPaymentMethodId() { return paymentMethodId; }
    public void setPaymentMethodId(int paymentMethodId) { this.paymentMethodId = paymentMethodId; }

    public String getMethodName() { return methodName; }
    public void setMethodName(String methodName) { this.methodName = methodName; }
}
