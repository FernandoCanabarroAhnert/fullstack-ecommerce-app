package com.fernandocanabarro.fullstack_ecommerce_app.services.validations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.EmailDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.PasswordRecoverDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.exceptions.FieldMessage;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.PasswordRecover;
import com.fernandocanabarro.fullstack_ecommerce_app.repositories.PasswordRecoverRepository;
import com.fernandocanabarro.fullstack_ecommerce_app.services.AuthService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PasswordRecoverDTOValidator implements ConstraintValidator<PasswordRecoverDTOValid, PasswordRecoverDTO>{

    private final PasswordRecoverRepository passwordRecoverRepository;
    private final AuthService authService;

    @Override
    public void initialize(PasswordRecoverDTOValid ann){}

    @Override
    public boolean isValid(PasswordRecoverDTO dto, ConstraintValidatorContext context) {

        List<FieldMessage> errors = new ArrayList<>();

        Optional<PasswordRecover> passwordRecover = passwordRecoverRepository.findByCode(dto.getCode());
        if (!passwordRecover.isPresent()) {
            errors.add(new FieldMessage("code", "Código não encontrado"));
        }

        if (passwordRecover.isPresent()) {
            if (!passwordRecover.get().isValid()) {
                errors.add(new FieldMessage("code", "Código já expirou. Um novo e-mail de confirmação será enviado para "
                 + passwordRecover.get().getUser().getEmail()));
                authService.createPasswordRecover(new EmailDTO(passwordRecover.get().getUser().getEmail()));
            }
        }

        String newPassword = dto.getNewPassword();

        if (!Pattern.matches(".*[A-Z].*", newPassword)) {
            errors.add(new FieldMessage("newPassword", "Senha deve conter pelo menos 1 letra maiúscula"));
        }
        if (!Pattern.matches(".*[a-z].*", newPassword)) {
            errors.add(new FieldMessage("newPassword", "Senha deve conter pelo menos 1 letra minúscula"));
        }
        if (!Pattern.matches(".*[0-9].*", newPassword)) {
            errors.add(new FieldMessage("newPassword", "Senha deve conter pelo menos 1 número"));
        }
        if (!Pattern.matches(".*[\\W].*", newPassword)) {
            errors.add(new FieldMessage("newPassword", "Senha deve conter pelo menos 1 caractere especial"));
        }

        if (!newPassword.equals(dto.getNewPasswordAck())) {
            errors.add(new FieldMessage("newPasswordAck", "As senhas devem ser iguais"));
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
