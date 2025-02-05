package com.fernandocanabarro.fullstack_ecommerce_app.dtos;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import com.fernandocanabarro.fullstack_ecommerce_app.entities.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    
    private Long id;
    private String fullName;
    private String email;
    private String cpf;
    private String birthDate;
    private String roles;
    private Boolean activated;

    public UserResponseDTO(User entity) {
        id = entity.getId();
        fullName = entity.getFullName();
        email = entity.getEmail();
        cpf = entity.getCpf();
        birthDate = entity.getBirthDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        roles = entity.getRoles().stream().map(role -> {
            String roleName = role.getAuthority().equals("ROLE_USER")
                ? "USUÁRIO" : "ADMIN";
            return roleName;
        })
        .collect(Collectors.joining(",")).toString();
        activated = entity.getActivated();
    }
}
