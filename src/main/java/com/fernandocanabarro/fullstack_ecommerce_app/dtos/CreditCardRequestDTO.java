package com.fernandocanabarro.fullstack_ecommerce_app.dtos;

import org.springframework.format.annotation.DateTimeFormat;

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
public class CreditCardRequestDTO {

    @NotBlank(message = "Campo Requerido")
    private String holderName;
    @NotBlank(message = "Campo Requerido")
    @Pattern(
        regexp = "^[0-9]{16}$",
        message = "O número do cartão deve conter exatamente 16 dígitos."
    )
    private String cardNumber;
    @NotBlank(message = "Campo Requerido")
    @Pattern(
        regexp = "^[0-9]{3}$",
        message = "O código CVV deve conter 3 dígitos numéricos."
    )
    private String cvv;
    @NotBlank(message = "Campo Requerido")
    @DateTimeFormat(pattern = "yyyy-MM")
    private String expirationDate;
    @NotBlank(message = "Campo Requerido")
    private String brand;
    
}
