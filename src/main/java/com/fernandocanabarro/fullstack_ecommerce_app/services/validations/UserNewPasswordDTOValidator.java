package com.fernandocanabarro.fullstack_ecommerce_app.services.validations;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.UserNewPasswordDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.exceptions.FieldMessage;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.User;
import com.fernandocanabarro.fullstack_ecommerce_app.services.AuthService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserNewPasswordDTOValidator implements ConstraintValidator<UserNewPasswordDTOValid, UserNewPasswordDTO>{

    @Autowired
    private AuthService authService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void initialize(UserNewPasswordDTOValid ann){}

    @Override
    public boolean isValid(UserNewPasswordDTO dto, ConstraintValidatorContext context) {

        List<FieldMessage> errors = new ArrayList<>();

        User user = authService.getConnectedUser();
        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            errors.add(new FieldMessage("currentPassword", "Senha atual incorreta"));
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
