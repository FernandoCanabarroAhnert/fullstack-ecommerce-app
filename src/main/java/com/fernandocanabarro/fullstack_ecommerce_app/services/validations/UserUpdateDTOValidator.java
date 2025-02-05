package com.fernandocanabarro.fullstack_ecommerce_app.services.validations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.UserUpdateDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.exceptions.FieldMessage;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.User;
import com.fernandocanabarro.fullstack_ecommerce_app.repositories.UserRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserUpdateDTOValidator implements ConstraintValidator<UserUpdateDTOValid, UserUpdateDTO>{

    private final UserRepository userRepository;

    @Override
    public void initialize(UserUpdateDTOValid ann){}

    @Override
    public boolean isValid(UserUpdateDTO dto, ConstraintValidatorContext context) {

        List<FieldMessage> errors = new ArrayList<>();

        Optional<User> user = userRepository.findByEmail(dto.getEmail());
        if (user.isPresent()) {
            if (user.get().getId() != dto.getId()) {
                errors.add(new FieldMessage("email", "Este e-mail já está cadastrado"));
            }
        }

        Optional<User> userCpf = userRepository.findByCpf(dto.getCpf());
        if (userCpf.isPresent()) {
            if (user.get().getId() != dto.getId()) {
                errors.add(new FieldMessage("cpf","Este CPF já está cadastrado"));
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
