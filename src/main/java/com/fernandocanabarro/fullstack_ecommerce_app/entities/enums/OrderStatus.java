package com.fernandocanabarro.fullstack_ecommerce_app.entities.enums;

public enum OrderStatus {

    WAITING_PAYMENT("Waiting payment"),
    PAID("Paid"),
    SHIPPED("Shipped"),
    DELIVERED("Delivered"),
    CANCELED("Canceled");

    private String description;

    private OrderStatus(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }
}
