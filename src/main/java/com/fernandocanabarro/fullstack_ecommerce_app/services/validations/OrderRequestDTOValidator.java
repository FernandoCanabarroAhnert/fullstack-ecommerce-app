package com.fernandocanabarro.fullstack_ecommerce_app.services.validations;

import java.util.ArrayList;
import java.util.List;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.OrderRequestDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.exceptions.FieldMessage;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OrderRequestDTOValidator implements ConstraintValidator<OrderRequestDTOValid, OrderRequestDTO>{

    @Override
    public void initialize(OrderRequestDTOValid ann) {}

    @Override
    public boolean isValid(OrderRequestDTO dto, ConstraintValidatorContext context) {
        List<FieldMessage> errors = new ArrayList<>();

        if (dto.getPayment().getPaymentType() != null) {
            if (dto.getPayment().getPaymentType().equals("CARTAO")){
                if (dto.getPayment().getCreditCardId() == null) {
                    errors.add(new FieldMessage("payment.creditCardId","Campo Requerido"));
                }
            }
        }

        errors.forEach(error -> {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(error.getMessage())
                .addPropertyNode(error.getFieldName())
                .addConstraintViolation();
        }); 

        return errors.isEmpty();
    }

}
