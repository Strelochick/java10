package com.mpt.dentalclinic.model;

public class UserModel {
    private int userId;
    private String name;
    private String surname;
    private String lastname;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String medicalCardNumber;
    private int roleId;

    public UserModel(int userId, String name, String surname, String lastname, String email, 
                     String password, String phone, String address, 
                     String medicalCardNumber, int roleId) {
        this.userId = userId;
        this.name = name;
        this.surname = surname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.medicalCardNumber = medicalCardNumber;
        this.roleId = roleId;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getMedicalCardNumber() { return medicalCardNumber; }
    public void setMedicalCardNumber(String medicalCardNumber) { this.medicalCardNumber = medicalCardNumber; }

    public int getRoleId() { return roleId; }
    public void setRoleId(int roleId) { this.roleId = roleId; }
}