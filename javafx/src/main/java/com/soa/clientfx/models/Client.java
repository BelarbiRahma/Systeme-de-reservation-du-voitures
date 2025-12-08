package com.soa.clientfx.models;

public record Client(String nom, String email) {
    public boolean isValid() {
        return nom != null && !nom.isBlank() &&
                email != null && email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }
}