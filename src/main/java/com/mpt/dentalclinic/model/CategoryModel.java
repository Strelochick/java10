package com.mpt.dentalclinic.model;

public class CategoryModel {
    private int categoryServicesId;
    private String categoryName;

    public CategoryModel(int categoryServicesId, String categoryName) {
        this.categoryServicesId = categoryServicesId;
        this.categoryName = categoryName;
    }

    public int getCategoryServicesId() { return categoryServicesId; }
    public void setCategoryServicesId(int categoryServicesId) { this.categoryServicesId = categoryServicesId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
}
