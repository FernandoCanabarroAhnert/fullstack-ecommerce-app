package com.fernandocanabarro.fullstack_ecommerce_app.entities.enums;

public enum PaymentType {

    CARTAO("Cartao"),
    BOLETO("Boleto"),
    PIX("Pix");

    private String description;

    private PaymentType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
}
