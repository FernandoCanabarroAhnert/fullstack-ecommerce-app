package com.fernandocanabarro.fullstack_ecommerce_app.dtos;

import com.fernandocanabarro.fullstack_ecommerce_app.services.validations.UserNewPasswordDTOValid;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@UserNewPasswordDTOValid
public class UserNewPasswordDTO {

    private String currentPassword;
    @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
    private String newPassword;
    @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
    private String newPasswordAck;
}
