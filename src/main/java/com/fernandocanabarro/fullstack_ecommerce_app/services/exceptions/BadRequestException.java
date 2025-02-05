package com.fernandocanabarro.fullstack_ecommerce_app.services.exceptions;

public class BadRequestException extends RuntimeException{

    public BadRequestException(String msg){
        super(msg);
    }
}
