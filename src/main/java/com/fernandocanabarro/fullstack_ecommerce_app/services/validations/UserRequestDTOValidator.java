package com.fernandocanabarro.fullstack_ecommerce_app.services.validations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.UserRequestDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.exceptions.FieldMessage;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.User;
import com.fernandocanabarro.fullstack_ecommerce_app.repositories.UserRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserRequestDTOValidator implements ConstraintValidator<UserRequestDTOValid, UserRequestDTO>{

    private final UserRepository userRepository;

    @Override
    public void initialize(UserRequestDTOValid ann){}

    @Override
    public boolean isValid(UserRequestDTO dto, ConstraintValidatorContext context) {

        List<FieldMessage> errors = new ArrayList<>();

        Optional<User> user = userRepository.findByEmail(dto.getEmail());
        if (user.isPresent()) {
            errors.add(new FieldMessage("email", "Este e-mail já está cadastrado"));
        }

        Optional<User> userCpf = userRepository.findByCpf(dto.getCpf());
        if (userCpf.isPresent()) {
            errors.add(new FieldMessage("cpf","Este CPF já está cadastrado"));
        }

        String password = dto.getPassword();

        if (!Pattern.matches(".*[A-Z].*", password)) {
            errors.add(new FieldMessage("password", "Senha deve conter pelo menos 1 letra maiúscula"));
        }
        if (!Pattern.matches(".*[a-z].*", password)) {
            errors.add(new FieldMessage("password", "Senha deve conter pelo menos 1 letra minúscula"));
        }
        if (!Pattern.matches(".*[0-9].*", password)) {
            errors.add(new FieldMessage("password", "Senha deve conter pelo menos 1 número"));
        }
        if (!Pattern.matches(".*[\\W].*", password)) {
            errors.add(new FieldMessage("password", "Senha deve conter pelo menos 1 caractere especial"));
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
