package com.mpt.dentalclinic.entity;

import org.springframework.security.core.GrantedAuthority;

public enum RoleEnum implements GrantedAuthority {
    ADMIN, DOCTOR, PATIENT;
    
    @Override
    public String getAuthority() {
        return name();
    }
}