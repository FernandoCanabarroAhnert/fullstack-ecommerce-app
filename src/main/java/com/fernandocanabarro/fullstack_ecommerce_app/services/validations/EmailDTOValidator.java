package com.fernandocanabarro.fullstack_ecommerce_app.services.validations;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.EmailDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.exceptions.FieldMessage;
import com.fernandocanabarro.fullstack_ecommerce_app.repositories.UserRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmailDTOValidator implements ConstraintValidator<EmailDTOValid, EmailDTO>{

    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(EmailDTOValid ann){}

    @Override
    public boolean isValid(EmailDTO dto, ConstraintValidatorContext context) {

        List<FieldMessage> errors = new ArrayList<>();

        if (!userRepository.findByEmail(dto.getEmail()).isPresent()) {
            errors.add(new FieldMessage("email", "Usuário com o e-mail " + dto.getEmail() + " não encontrado"));
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
