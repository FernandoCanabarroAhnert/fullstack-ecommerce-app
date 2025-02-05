package com.fernandocanabarro.fullstack_ecommerce_app.entities.enums;

public enum PaymentStatus {

    PENDING("Pending"),
    PAID("Paid"),
    CANCELED("Canceled");

    private String description;

    private PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
