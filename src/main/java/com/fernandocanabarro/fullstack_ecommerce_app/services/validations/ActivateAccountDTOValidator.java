package com.fernandocanabarro.fullstack_ecommerce_app.services.validations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.ActivateAccountDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.exceptions.FieldMessage;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.ActivationCode;
import com.fernandocanabarro.fullstack_ecommerce_app.repositories.ActivationCodeRepository;
import com.fernandocanabarro.fullstack_ecommerce_app.services.AuthService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ActivateAccountDTOValidator implements ConstraintValidator<ActivateAccountDTOValid, ActivateAccountDTO>{

    private final ActivationCodeRepository activationCodeRepository;
    private final AuthService authService;

    @Override
    public void initialize(ActivateAccountDTOValid ann){}

    @Override
    public boolean isValid(ActivateAccountDTO dto, ConstraintValidatorContext context) {

        List<FieldMessage> errors = new ArrayList<>();

        Optional<ActivationCode> activationCode = activationCodeRepository.findByCode(dto.getCode());
        
        if (!activationCode.isPresent()) {
            errors.add(new FieldMessage("code", "Código não encontrado"));
        }
        if (activationCode.isPresent()) {
            if (!activationCode.get().isValid()) {
                errors.add(new FieldMessage("code", "Código já expirou. Um novo e-mail de confirmação será enviado para "
                 + activationCode.get().getUser().getEmail()));
                authService.sendConfirmationEmail(activationCode.get().getUser());
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
