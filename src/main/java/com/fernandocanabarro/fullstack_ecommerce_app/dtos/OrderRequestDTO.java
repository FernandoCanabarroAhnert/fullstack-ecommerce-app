package com.fernandocanabarro.fullstack_ecommerce_app.dtos;

import com.fernandocanabarro.fullstack_ecommerce_app.services.validations.OrderRequestDTOValid;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@OrderRequestDTOValid
public class OrderRequestDTO {

    @NotNull(message = "Campo Obrigatório")
    private Long addressId;
    private Long discountCouponId;
    @Valid
    private PaymentRequestDTO payment;
}
