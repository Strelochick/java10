package com.mpt.dentalclinic.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @NotBlank @Size(max = 50)
    private String name;

    @Size(max = 50)
    private String surname;

    @Size(max = 50)
    private String lastname;

    @NotBlank @Email @Size(max = 100)
    @Column(unique = true)
    private String email;

    @NotBlank @Size(min = 8)
    private String password;

    @Pattern(regexp = "\\+?[0-9]{10,15}")
    private String phone;

    @Size(max = 200)
    private String address;

    @Size(max = 20)
    private String medicalCardNumber;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean active = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roles = new HashSet<>();

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
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
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public Set<RoleEntity> getRoles() { return roles; }
    public void setRoles(Set<RoleEntity> roles) { this.roles = roles; }
    
    public boolean hasRole(String roleName) {
        return this.roles.stream()
                .anyMatch(role -> role.getRoleName().equals("ROLE_" + roleName));
    }
}