package com.hackathon.fiap.msautenticacao.entity.user.model.enums;

public enum UserRole {
    ADMIN("admin"),
    INTEGRATION("integration");

    private String role;

    UserRole(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }
}