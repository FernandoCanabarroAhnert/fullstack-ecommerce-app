package com.fernandocanabarro.fullstack_ecommerce_app.controllers.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fernandocanabarro.fullstack_ecommerce_app.services.exceptions.BadRequestException;
import com.fernandocanabarro.fullstack_ecommerce_app.services.exceptions.ResourceNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public String notFound(ResourceNotFoundException ex,Model model) {
        model.addAttribute("message", ex.getMessage());
        return "error/not-found";
    }

    @ExceptionHandler(BadRequestException.class)
    public String badRequest(BadRequestException ex,Model model) {
        model.addAttribute("message", ex.getMessage());
        return "error/bad-request";
    }
}
