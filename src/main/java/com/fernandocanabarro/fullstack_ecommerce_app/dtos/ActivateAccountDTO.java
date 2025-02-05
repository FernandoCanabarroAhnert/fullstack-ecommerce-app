package com.fernandocanabarro.fullstack_ecommerce_app.dtos;

import com.fernandocanabarro.fullstack_ecommerce_app.services.validations.ActivateAccountDTOValid;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ActivateAccountDTOValid
public class ActivateAccountDTO {

    @NotBlank(message = "Campo Requerido")
    private String code;
}
