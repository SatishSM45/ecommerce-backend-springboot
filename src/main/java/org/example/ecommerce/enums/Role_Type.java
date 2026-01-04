package org.example.ecommerce.enums;

public enum Role_Type {
    ADMIN("ADMIN"),
    USER("USER"),
    SUPER_ADMIN("SUPER_ADMIN");

    private String role;

    Role_Type(String role) {
        this.role = role;
    }
}
