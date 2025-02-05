package com.fernandocanabarro.fullstack_ecommerce_app.entities.enums;

public enum CreditCardBrand {

    VISA("Visa"),
    MASTERCARD("MasterCard");

    private String description;

    private CreditCardBrand(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    
}
