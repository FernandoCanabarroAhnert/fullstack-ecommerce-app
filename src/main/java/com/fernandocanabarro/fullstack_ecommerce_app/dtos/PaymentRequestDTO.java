package com.fernandocanabarro.fullstack_ecommerce_app.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDTO {

    @NotNull(message = "Campo Obrigatório")
    private String paymentType;
    private Long creditCardId;
    private Integer installmentQuantity;
}
