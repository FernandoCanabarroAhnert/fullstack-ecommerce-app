package com.fernandocanabarro.fullstack_ecommerce_app.services.validations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.DiscountCouponCodeDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.exceptions.FieldMessage;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.DiscountCoupon;
import com.fernandocanabarro.fullstack_ecommerce_app.repositories.DiscountCouponRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DiscountCouponCodeDTOValidator implements ConstraintValidator<DiscountCouponCodeDTOValid, DiscountCouponCodeDTO> {

    @Autowired
    private DiscountCouponRepository discountCouponRepository;

    @Override
    public boolean isValid(DiscountCouponCodeDTO dto, ConstraintValidatorContext context) {
        List<FieldMessage> errors = new ArrayList<>();

        Optional<DiscountCoupon> coupon = discountCouponRepository.findByCode(dto.getCode());
        if (coupon.isEmpty()) {
            errors.add(new FieldMessage("code","Nenhum Cupom foi encontrado"));
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
