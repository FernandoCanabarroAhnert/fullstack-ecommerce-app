package com.fernandocanabarro.fullstack_ecommerce_app.dtos;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;

import com.fernandocanabarro.fullstack_ecommerce_app.entities.Role;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.User;
import com.fernandocanabarro.fullstack_ecommerce_app.services.validations.UserUpdateDTOValid;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@UserUpdateDTOValid
public class UserUpdateDTO {

    private Long id;
    @NotBlank(message = "Campo requerido")
    private String fullName;
    @Pattern(regexp = "^[A-Za-z0-9+._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "E-mail deve estar em formato válido")
    private String email;
    @CPF(message = "CPF deve estar em formato válido")
    private String cpf;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    private List<Long> roles;
    private Boolean activated;

    public UserUpdateDTO(User entity) {
        id = entity.getId();
        fullName = entity.getFullName();
        email = entity.getEmail();
        cpf = entity.getCpf();
        roles = entity.getRoles().stream().map(Role::getId).toList();
        activated =  entity.getActivated();
    }
}
