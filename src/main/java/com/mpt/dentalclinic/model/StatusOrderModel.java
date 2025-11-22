package com.mpt.dentalclinic.model;


public class StatusOrderModel {
    private int statusOrderId;
    private String statusName;

    public StatusOrderModel(int statusOrderId, String statusName) {
        this.statusOrderId = statusOrderId;
        this.statusName = statusName;
    }

    public int getStatusOrderId() { return statusOrderId; }
    public void setStatusOrderId(int statusOrderId) { this.statusOrderId = statusOrderId; }

    public String getStatusName() { return statusName; }
    public void setStatusName(String statusName) { this.statusName = statusName; }
}

